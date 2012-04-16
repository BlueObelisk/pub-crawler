package wwmm.pubcrawler.crawlers.rsc.parsers;

import nu.xom.Document;
import wwmm.pubcrawler.crawlers.PublicationListParser;
import wwmm.pubcrawler.model.Journal;

import java.util.List;

/**
 * @author Sam Adams
 */
public class RscPublicationListParser implements PublicationListParser {

    private final Document document;

    public RscPublicationListParser(final Document document) {
        this.document = document;
    }

    @Override
    public List<Journal> findJournals() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

}
