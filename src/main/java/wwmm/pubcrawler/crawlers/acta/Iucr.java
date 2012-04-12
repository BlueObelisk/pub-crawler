package wwmm.pubcrawler.crawlers.acta;

import wwmm.pubcrawler.model.id.PublisherId;

import java.net.URI;

/**
 * @author Sam Adams
 */
public class Iucr {

    public static final PublisherId PUBLISHER_ID = new PublisherId("acta");

    public static final URI JOURNALS_URL = URI.create("http://journals.iucr.org/");
    
}
