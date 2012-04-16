package wwmm.pubcrawler.crawlers.rsc;

import wwmm.pubcrawler.model.id.PublisherId;

import java.net.URI;

/**
 * @author Sam Adams
 */
public class Rsc {

    public static final PublisherId PUBLISHER_ID = new PublisherId("rsc");

    public static final URI JOURNAL_LIST = URI.create("http://pubs.rsc.org/en/journals/getatozresult?key=title&value=current");
}
