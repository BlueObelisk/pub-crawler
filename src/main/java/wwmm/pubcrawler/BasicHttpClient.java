package wwmm.pubcrawler;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.print.URIException;

import nu.xom.Builder;
import nu.xom.Document;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;


/**
 * <p>
 * The <code>BasicHttpClient</code> class provides convenience methods for
 * performing common HTTP methods and retrieving the results in various forms.
 * </p>
 * 
 * @author Nick Day
 * @version 1.1
 * 
 */
public class BasicHttpClient {

	private static final Logger LOG = Logger.getLogger(BasicHttpClient.class);

	private HttpClient client;

	public BasicHttpClient() {
		client = new DefaultHttpClient();
	}

	public BasicHttpClient(HttpClient client) {
		this.client = client;
	}
	
	/**
	 * <p>
	 * Returns the HTTP client that this class wraps.
	 * </p>
	 * 
	 * @return The <code>HttpClient</code> that this class wraps.
	 */
	public HttpClient getClient() {
		return client;
	}

	/**
	 * <p>
	 * Executes a HTTP GET on the resource at the provided <code>URI</code>.  The 
	 * resource contents are returned in an <code>InputStream</code>.
	 * </p>
	 * 
	 * @param uri - the resource to retrieve.
	 * 
	 * @return InputStream containing the contents of the resource at the
	 * provided <code>URI</code>.
	 * 
	 */
	private InputStream getResourceStream(URI uri) {
		InputStream in = null;
		HttpResponse resp = executeGET(uri);
		try {
			in = resp.getEntity().getContent();
		} catch (IOException e) {
			throw new RuntimeException("Exception getting response stream for: "+uri);
		}
		return in;
	}

	/**
	 * <p>
	 * Executes a HTTP GET on the resource at the provided <code>URI</code>.  The 
	 * resource contents are returned in a String.
	 * </p>
	 * 
	 * @param uri - the resource to retrieve.
	 * 
	 * @return String containing the contents of the resource at the provided 
	 * <code>URI</code>.
	 * 
	 */
	public String getResourceString(URI uri) {
		InputStream in = getResourceStream(uri);
		String html = null;
		try {
			html = IOUtils.toString(in);
		} catch (IOException e) {
			throw new RuntimeException("Exception converting webpage stream to string: "+uri, e);
		} finally {
			IOUtils.closeQuietly(in);
		}
		return html;
	}

	/**
	 * <p>
	 * Writes the resource at the provided URI to the provided
	 * file.
	 * </p>
	 * 
	 * @param uri of the resource you wish written to file.
	 * @param file that the resource will be written to.
	 * 
	 * @return true if the resource is successfully written
	 * to file, false if not. 
	 */
	public boolean writeResourceToFile(URI uri, File file) {
		file.getParentFile().mkdirs();
		InputStream in = null;
		OutputStream out = null;
		try {
			in = getResourceStream(uri);
			out = new BufferedOutputStream(new FileOutputStream(file));
			IOUtils.copy(in, out);
		} catch (IOException e) {
			LOG.info("Could not write URI ("+uri.toString()+") to file ("+file+")\n"+
					e.getMessage());
			return false;
		} finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}
		return true;
	}

	/**
	 * <p>
	 * Executes a HTTP GET on the resource at the provided <code>URI</code>.  The 
	 * resource contents are parsed by Tagsoup and returned as a XOM 
	 * <code>Document</code>.
	 * </p>
	 * 
	 * @param uri - the resource to retrieve.
	 * 
	 * @return XML <code>Document</code> containing the contents of the resource 
	 * at the provided <code>URI</code> after they have been parsed using Tagsoup.
	 * 
	 */
	public Document getResourceHTML(URI uri) {
		InputStream in = getResourceStream(uri);
		Document doc = null;
		try {
			Builder builder = getTagsoupBuilder();
			doc = Utils.parseXml(builder, in);
		} finally {
			IOUtils.closeQuietly(in);
		}
		return doc;
	}

	/**
	 * <p>
	 * Executes a HTTP GET on the resource at the provided <code>URI</code>.  
	 * The resource contents are first obtained as a <code>String</code> so that
	 * a horrible hack can be performed to remove any comments before the HTML is 
	 * passed to Tagsoup.  This is someimtes necessary to remove anything that might
	 * cause Tagsoup or XOM to fail when parsing the HTML (such as including -- in 
	 * middle of a comment).  After the comments have been removed, the HTML is tidied
	 * and parsed by Tagsoup into a XOM <code>Document</code>.
	 * </p>
	 * 
	 * @param uri of the resource for which you want to obtain the HTML.
	 * 
	 * @return XML <code>Document</code> containined the parsed HTML.
	 */
	public Document getResourceHTMLMinusComments(URI uri) {
		String html = getResourceString(uri);

		String patternStr = "<!--(.*)?-->";
		String replacementStr = "";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(html);
		html = matcher.replaceAll(replacementStr);
		patternStr = "<!-->";
		replacementStr = "";
		pattern = Pattern.compile(patternStr);
		matcher = pattern.matcher(html);
		html = matcher.replaceAll(replacementStr);

		StringReader sr = new StringReader(html);
		BufferedReader br = new BufferedReader(sr);
		Document doc = null;
		try {
			Builder builder = getTagsoupBuilder();
			doc = Utils.parseXml(builder, br);
		} finally {
			IOUtils.closeQuietly(br);
			IOUtils.closeQuietly(sr);
		}
		return doc;
	}

	/**
	 * <p>
	 * Executes a HTTP GET on the resource at the provided <code>URI</code>.  The 
	 * resource contents are parsed using the default XOM <code>Builder</code> and 
	 * returned as a XOM <code>Document</code>.
	 * </p>
	 * 
	 * @param uri - the resource to retrieve.
	 * 
	 * @return XML <code>Document</code> containing the contents of the resource at 
	 * the provided <code>URI</code>.
	 * 
	 */
	public Document getResourceXML(URI uri) {
		InputStream in = getResourceStream(uri);
		Document doc = null;
		try {
			doc = Utils.parseXml(in);
		} finally {
			IOUtils.closeQuietly(in);
		}
		return doc;
	}	

	/**
	 * <p>
	 * Executes a HTTP POST using the provided <code>postMethod</code> (which will 
	 * contain the details of the POST URI).  The POST results retrieved are 
	 * returned in an <code>InputStream</code>.
	 * </p>
	 * 
	 * @param postMethod - POST method to execute.
	 * 
	 * @return InputStream containing the results of the POST method.
	 * 
	 */
	private InputStream getPostResultStream(HttpPost postMethod) {
		HttpPost method = postMethod;
		HttpResponse resp = executeMethod(method);
		InputStream in = null;
		try {
			in = resp.getEntity().getContent();
		} catch (IOException e) {
			throw new RuntimeException("Problem getting POST response stream.", e);
		}
		return in;
	}

	/**
	 * <p>
	 * Executes a HTTP POST using the provided <code>postMethod</code> (which will 
	 * contain the details of the POST URI).  The POST results retrieved are 
	 * returned in a <code>String</code>.
	 * </p>
	 * 
	 * @param postMethod - POST method to execute.
	 * 
	 * @return String containing the results of the POST method.
	 * 
	 */
	public String getPostResultString(HttpPost postMethod) {
		InputStream in = getPostResultStream(postMethod);
		String result = null;
		try {
			result = IOUtils.toString(in);
		} catch (IOException e) {
			throw new RuntimeException("Problem converting POST result stream to string.", e);
		} finally {
			IOUtils.closeQuietly(in);
		}
		return result;
	}

	/**
	 * <p>
	 * Executes a HTTP POST using the provided <code>postMethod</code> (which will 
	 * contain the details of the POST URI).  The POST results retrieved are returned 
	 * in a XOM <code>Document</code>.
	 * </p>
	 * 
	 * @param postMethod - POST method to execute.
	 * 
	 * @return XML <code>Document</code> containing the results of the POST method.
	 * 
	 */
	public Document getPostResultXML(HttpPost postMethod) {
		InputStream in = getPostResultStream(postMethod);
		Document doc = null;
		try {
			Builder builder = getTagsoupBuilder();
			doc = Utils.parseXml(builder, in);
		} finally {
			IOUtils.closeQuietly(in);
		}
		return doc;
	}

	/**
	 * <p>
	 * Executes a HTTP HEAD on the resource at the provided URI.  All of the
	 * resource headers are retrieved and returned.
	 * </p>
	 * 
	 * @param uri - the resource for which to retrieve the headers.
	 * 
	 * @return array containing all of the HTTP headers for the resource at
	 * the provided <code>URI</code>.
	 * 
	 */
	public Header[] getHeaders(URI uri) {
		HttpResponse resp = executeHEAD(uri);
		return resp.getAllHeaders();
	}

	/**
	 * <p>
	 * Constructs a XOM <code>Builder</code> using the Tagsoup HTML parser.  The 
	 * <code>Builder</code> is used for all parsing and tidying of HTML in this 
	 * class.
	 * </p> 
	 * 
	 * @return XOM <code>Builder</code> created with the Tagsoup HTML parser.
	 * 
	 */
	public static Builder getTagsoupBuilder() {
		XMLReader tagsoup = null;
		try {
			tagsoup = XMLReaderFactory.createXMLReader("org.ccil.cowan.tagsoup.Parser");
		} catch (SAXException e) {
			throw new RuntimeException("Exception whilst creating XMLReader from org.ccil.cowan.tagsoup.Parser");
		}
		return new Builder(tagsoup);
	}

	/**
	 * <p>
	 * Simply executes the HTTP method provided as a parameter.
	 * </p>
	 * 
	 * @param method - HTTP method to execute
	 * 
	 */
	private HttpResponse executeMethod(HttpRequestBase method) {
		URI uri = null;
		HttpResponse resp = null;
		try {
			uri = method.getURI();
			resp = client.execute(method);
			int statusCode = resp.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				throw new RuntimeException("Problems executing "+method.getClass()
						+" method on "+uri+". Returned status code = "+statusCode);
			}
		} catch (IOException e) {
			throw new RuntimeException("IOException executing "+method.getClass()
					+" method on "+uri, e);
		}
		return resp;
	}

	/**
	 * <p>
	 * Executes a HTTP GET on the provided <code>URI</code>.
	 * </p>
	 * 
	 * @param uri - resource to GET
	 * 
	 * @return Apache <code>HTTPClient</code> wrapper containing the GET method 
	 * details and results.
	 * 
	 */
	public HttpResponse executeGET(URI uri) {
		HttpGet method = new HttpGet();
		method.setURI(uri);
		return executeMethod(method);
	}

	/**
	 * <p>
	 * Executes a HTTP HEAD on the provided <code>URI</code>.
	 * </p>
	 * 
	 * @param uri - resource to HEAD
	 * 
	 * @return Apache <code>HTTPClient</code> wrapper containing the HEAD method 
	 * details and results.
	 * 
	 */
	public HttpResponse executeHEAD(URI uri) {
		HttpHead method = new HttpHead();
		method.setURI(uri);
		return executeMethod(method);
	}

	/**
	 * <p>
	 * Performs a HTTP HEAD on the provided <code>URI</code> and returns the value 
	 * of the Content-Type header.
	 * </p>
	 * 
	 * @param uri - resource for which to retrieve the Content-Type
	 * 
	 * @return If the header exists, the String value of the resource Content-Type 
	 * is returned.  If the header does not exist, then <code>null</code> is 
	 * returned.
	 * 
	 */
	public String getContentType(URI uri) {
		Header[] headers = this.getHeaders(uri);
		String contentType = null;
		for (Header header : headers) {
			String name = header.getName();
			if ("Content-Type".equals(name) ||
					"Content-type".equals(name)) {
				contentType = header.getValue();
			}
		}
		return contentType;
	}

	/**
	 * <p>
	 * Main method only for demonstration of class use. Does not require
	 * any arguments.
	 * </p>
	 * 
	 * @param args
	 * 
	 * @throws URIException
	 * @throws NullPointerException
	 * 
	 */
	public static void main(String[] args) throws URISyntaxException, NullPointerException {
		BasicHttpClient bhc = new BasicHttpClient();
		Header[] headers = bhc.getHeaders(new URI("http://pubs.rsc.org/suppdata/CC/b8/b811528a/b811528a.pdf"));
		for (Header h : headers) {
			System.out.println(h.getName()+" = "+h.getValue());
		}
	}

}
