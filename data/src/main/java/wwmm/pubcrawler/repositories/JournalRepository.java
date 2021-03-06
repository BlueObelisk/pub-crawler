package wwmm.pubcrawler.repositories;


import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.model.id.PublisherId;

import java.util.List;

/**
 * @author Sam Adams
 */
public interface JournalRepository {

    Journal getJournal(PublisherId publisher, String id);

    List<Journal> getJournalsForPublisher(String publisher);
    
    void addIssues(Journal journal, String... issues);

    void updateJournal(PublisherId acs, Journal journal);

    void saveOrUpdateJournal(Journal journal);

    long getNextSequence();
}
