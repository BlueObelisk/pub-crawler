package wwmm.pubcrawler.crawlers.wiley;

import wwmm.pubcrawler.model.id.PublisherId;

import java.net.URI;

/**
 * @author Sam Adams
 */
public class Wiley {

    public static final PublisherId PUBLISHER_ID = new PublisherId("wiley");

    public static final URI PUBLICATION_LIST_URL = URI.create("http://onlinelibrary.wiley.com/browse/publications?type=journal&&start=1&resultsPerPage=10000");

}
