package wwmm.pubcrawler;

import static wwmm.pubcrawler.CrawlerConstants.X_XHTML;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;
import nu.xom.ParsingException;
import nu.xom.Serializer;
import nu.xom.ValidityException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * <p>
 * Utility methods used throughout the crawler project.
 * </p>
 * 
 * @author Nick Day
 * @version 1.1
 */
public class Utils {

	private static final Logger LOG = Logger.getLogger(Utils.class);

	/**
	 * <p>
	 * The <code>sleep</code> method is called by all methods in this class which 
	 * call a method in the superclass.  A random number between 0 and 
	 * <code>maxSleep</code> is used to determine how long a period the process 
	 * should sleep for before continuing.
	 * </p>
	 */
	public static void sleep(int maxSleep) {
		int maxTime = Integer.valueOf(maxSleep);
		try {
			Thread.sleep(((int) (maxTime * Math.random())));
		} catch (InterruptedException e) {
			LOG.debug("Sleep interrupted.");
		}
	}

	/**
	 * <p>
	 * Convenience method for doing an XPath query and receiving 
	 * back a list of <code>Node</code> rather than the <code>Nodes</code>
	 * returned in the default in XOM.
	 * </p>
	 * 
	 * @param doc you want to query
	 * @param xpath - the XPath String you want to use in the query.
	 * 
	 * @return list of <code>Node</code> that represent the parts of
	 * the queried XML document that matched the provided XPath query.
	 */
	public static List<Node> queryHTML(Document doc, String xpath) {
		Node node = doc.getRootElement();
		return queryHTML(node, xpath);
	}

	/**
	 * <p>
	 * Convenience method for doing an XPath query and receiving 
	 * back a list of <code>Node</code> rather than the <code>Nodes</code>
	 * returned in the default in XOM.
	 * </p>
	 * 
	 * @param node you want to query
	 * @param xpath - the XPath String you want to use in the query.
	 * 
	 * @return list of <code>Node</code> that represent the parts of
	 * the queried XML document that matched the provided XPath query.
	 */
	public static List<Node> queryHTML(Node node, String xpath) {
		Nodes nodes = node.query(xpath, X_XHTML);
		return getNodeListFromNodes(nodes);
	}

	/**
	 * <p>
	 * Convenience method for getting a list of <code>Node</code>
	 * from <code>Nodes</code>.
	 * </p>
	 * 
	 * @param nodes you want converted to a list.
	 * 
	 * @return list of <Node> containing the same XML elemenst as 
	 * the provided <code>Nodes</code>.
	 */
	public static List<Node> getNodeListFromNodes(Nodes nodes) {
		List<Node> nodeList = new ArrayList<Node>(nodes.size());
		for (int i = 0; i < nodes.size(); i++) {
			nodeList.add(nodes.get(i));
		}
		return nodeList;
	}

	/**
	 * <p>
	 * Parses the contents of an <code>InputStream</code> into an
	 * XML document.
	 * </p>
	 * 
	 * @param in - the InputStream you want converted to XML.
	 * 
	 * @return XML document representing the contents of the 
	 * provided InputStream.
	 */
	public static Document parseXml(InputStream in) {
		return parseXml(new Builder(), in);
	}

	/**
	 * <p>
	 * Parses the contents of an <code>InputStream</code> into an
	 * XML document using a specified XML builder.
	 * </p>
	 * 
	 * @param builder - the XML builder you want the parser to use
	 * in creating the XML document.
	 * @param in - the InputStream you want converted to XML.
	 * 
	 * @return XML document representing the contents of the 
	 * provided InputStream.
	 */
	public static Document parseXml(Builder builder, InputStream in) {
		Document doc;
		try {
			doc = builder.build(in);
		} catch (ValidityException e) {
			throw new RuntimeException("Invalid XML", e);
		} catch (ParsingException e) {
			throw new RuntimeException("Could not parse XML", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unsupported encoding", e);
		} catch (IOException e) {
			throw new RuntimeException("Input exception", e);
		}
		return doc;
	}

	/**
	 * <p>
	 * Parses the contents of a <code>Reader</code> into an
	 * XML document.
	 * </p>
	 * 
	 * @param reader - the Reader you want converted to XML.
	 * 
	 * @return XML document representing the contents of the 
	 * provided Reader.
	 */
	public static Document parseXml(Reader reader) {
		return Utils.parseXml(new Builder(), reader);
	}

	/**
	 * <p>
	 * Parses the contents of a <code>Reader</code> into an
	 * XML document using a specified XML builder.
	 * </p>
	 * 
	 * @param builder - the XML builder you want the parser to use
	 * in creating the XML document.
	 * @param reader - the Reader you want converted to XML.
	 * 
	 * @return XML document representing the contents of the 
	 * provided Reader.
	 */
	public static Document parseXml(Builder builder, Reader reader) {
		Document doc;
		try {
			doc = builder.build(reader);
		} catch (ValidityException e) {
			throw new RuntimeException("Invalid XML", e);
		} catch (ParsingException e) {
			throw new RuntimeException("Could not parse XML", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unsupported encoding", e);
		} catch (IOException e) {
			throw new RuntimeException("Input exception", e);
		}
		return doc;
	}

	/**
	 * <p>
	 * Parses the contents of the provided <code>File</code> 
	 * into an XML document.
	 * </p>
	 * 
	 * @param file you want to be parsed.
	 * 
	 * @return XML document containing the contents of the file.
	 */
	public static Document parseXml(File file) {
		try {
			return Utils.parseXml(new FileReader(file));
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Could not find file "+file.getAbsolutePath(), e);
		}
	}

}