package wwmm.pubcrawler.crawlers.elsevier;

import wwmm.pubcrawler.model.id.PublisherId;

import java.net.URI;

/**
 * @author Sam Adams
 */
public class Elsevier {

    public static final URI OPML_URL = URI.create("http://feeds.sciencedirect.com/opml.xml");

    public static final PublisherId PUBLISHER_ID = new PublisherId("elsevier");

}
