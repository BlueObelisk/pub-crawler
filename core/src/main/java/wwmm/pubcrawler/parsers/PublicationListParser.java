package wwmm.pubcrawler.parsers;

import wwmm.pubcrawler.model.Journal;

import java.util.List;

/**
 * @author Sam Adams
 */
public interface PublicationListParser {

    List<Journal> findJournals();

}
