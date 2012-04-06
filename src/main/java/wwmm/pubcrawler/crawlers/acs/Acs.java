package wwmm.pubcrawler.crawlers.acs;

import wwmm.pubcrawler.model.id.PublisherId;

import java.net.URI;

/**
 * @author Sam Adams
 */
public class Acs {
    
    public static final PublisherId PUBLISHER_ID = new PublisherId("acs");

    public static final URI JOURNAL_LIST_URL = URI.create("http://pubs.acs.org/");

}
