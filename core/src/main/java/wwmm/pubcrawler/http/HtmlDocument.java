package wwmm.pubcrawler.http;

import nu.xom.Document;

import java.net.URI;

/**
 * @author Sam Adams
 */
public class HtmlDocument {

    private final URI url;
    private final Document document;

    public HtmlDocument(final URI url, final Document document) {
        this.url = url;
        this.document = document;
    }

    public URI getUrl() {
        return url;
    }

    public Document getDocument() {
        return document;
    }

    @Override
    public String toString() {
        return "HtmlDocument{" +
                "url=" + url +
                '}';
    }
}
