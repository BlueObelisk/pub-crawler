package wwmm.pubcrawler.utils;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;
import org.apache.commons.io.IOUtils;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;

import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.DOTALL;

/**
 * @author Sam Adams
 */
public class HtmlUtils {

    private static final Pattern DOCTYPE = Pattern.compile("<!DOCTYPE.*?>", CASE_INSENSITIVE | DOTALL);

    public static Document readHtmlDocument(final CrawlerResponse response) throws IOException {
        final String encoding = response.getCharacterEncoding();
        if (encoding == null) {
            return readDocument(response, newTagSoupBuilder());
        } else {
            return readDocument(response, newTagSoupBuilder(), encoding);
        }
    }

    public static Document readXmlDocument(final CrawlerResponse response) throws IOException {
        final String encoding = response.getCharacterEncoding();
        if (encoding == null) {
            return readDocument(response, new Builder());
        } else {
            return readDocument(response, new Builder(), encoding);
        }
    }

    private static Document readDocument(final CrawlerResponse response, final Builder builder) throws IOException {
        final String text = IOUtils.toString(response.getContent());
        return readDocument(text, builder, response);
    }

    private static Document readDocument(final CrawlerResponse response, final Builder builder, final String encoding) throws IOException {
        final String text = IOUtils.toString(response.getContent(), encoding);
        return readDocument(text, builder, response);
    }

    private static Document readDocument(final String text, final Builder builder, final CrawlerResponse response) throws IOException {
        try {
            final Document doc = builder.build(new StringReader(removeDocType(text)));
            setDocBaseUrl(response, doc);
            return doc;
        } catch (ParsingException e) {
            throw new IOException("Error reading XML", e);
        }
    }

    private static String removeDocType(final String text) {
        return DOCTYPE.matcher(text).replaceFirst("");
    }

    private static void setDocBaseUrl(final CrawlerResponse response, final Document doc) {
        final String url = response.getUrl().toString();
        doc.setBaseURI(removeFragment(url));
    }

    private static String removeFragment(final String url) {
        final int index = url.indexOf('#');
        return (index == -1) ? url : url.substring(0, index);
    }

    private static Builder newTagSoupBuilder() {
        return new Builder(new org.ccil.cowan.tagsoup.Parser());
    }

}
