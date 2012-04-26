package wwmm.pubcrawler.crawlers.acta;

import nu.xom.Document;

/**
 * @author Sam Adams
 */
public class IucrFrameResource {

    private final Document body;
    private final Document head;

    public IucrFrameResource(final Document body, final Document head) {
        this.body = body;
        this.head = head;
    }

    public Document getBody() {
        return body;
    }

    public Document getHead() {
        return head;
    }
}
