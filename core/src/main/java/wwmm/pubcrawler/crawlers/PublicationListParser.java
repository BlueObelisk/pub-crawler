package wwmm.pubcrawler.crawlers;

import wwmm.pubcrawler.model.Journal;

import java.util.List;

/**
 * @author Sam Adams
 */
public interface PublicationListParser {

    List<Journal> findJournals();

}
