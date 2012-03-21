package wwmm.pubcrawler.v2.repositories.mongo;

import com.mongodb.DBCollection;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.model.id.PublisherId;
import wwmm.pubcrawler.v2.inject.Journals;
import wwmm.pubcrawler.v2.repositories.JournalRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * @author Sam Adams
 */
@Singleton
public class MongoJournalRepository implements JournalRepository {

    private final DBCollection collection;

    @Inject
    public MongoJournalRepository(@Journals final DBCollection collection) {
        this.collection = collection;
    }

    @Override
    public Journal getJournal(final PublisherId publisher, final String id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Journal> getJournalsForPublisher(final PublisherId publisher) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addIssues(final Journal journal, final String... issues) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateJournal(final PublisherId acs, final Journal journal) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
