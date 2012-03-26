package wwmm.pubcrawler.utils;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Sam Adams
 */
public class HtmlUtils {

    public static Document readHtmlDocument(CrawlerResponse response) throws IOException {
        String encoding = response.getCharacterEncoding();
        if (encoding == null) {
            return readDocument(response, newTagSoupBuilder());
        } else {
            return readDocument(response, newTagSoupBuilder(), encoding);
        }
    }

    public static Document readXmlDocument(CrawlerResponse response) throws IOException {
        String encoding = response.getCharacterEncoding();
        if (encoding == null) {
            return readDocument(response, new Builder());
        } else {
            return readDocument(response, new Builder(), encoding);
        }
    }

    private static Document readDocument(CrawlerResponse response, Builder builder) throws IOException {
        try {
            Document doc = builder.build(response.getContent());
            setDocBaseUrl(response, doc);
            return doc;
        } catch (ParsingException e) {
            throw new IOException("Error reading XML", e);
        }
    }

    private static Document readDocument(CrawlerResponse response, Builder builder, String encoding) throws IOException {
        try {
            InputStreamReader isr = new InputStreamReader(response.getContent(), encoding);
            Document doc = builder.build(isr);
            setDocBaseUrl(response, doc);
            return doc;
        } catch (ParsingException e) {
            throw new IOException("Error reading XML", e);
        }
    }

    private static void setDocBaseUrl(CrawlerResponse response, Document doc) {
        String url = response.getUrl().toString();
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
