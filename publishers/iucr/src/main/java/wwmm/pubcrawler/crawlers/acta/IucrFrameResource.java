package wwmm.pubcrawler.crawlers.acta;

import nu.xom.Document;

import java.net.URI;

/**
 * @author Sam Adams
 */
public class IucrFrameResource {

    private final URI baseUrl;
    private final Document body;
    private final Document head;

    public IucrFrameResource(final URI baseUrl, final Document body, final Document head) {
        this.baseUrl = baseUrl;
        this.body = body;
        this.head = head;
    }

    public URI getBaseUrl() {
        return baseUrl;
    }

    public Document getBody() {
        return body;
    }

    public Document getHead() {
        return head;
    }
}
