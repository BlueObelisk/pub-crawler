package wwmm.pubcrawler.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import wwmm.pubcrawler.repositories.*;
import wwmm.pubcrawler.repositories.mongo.*;
import wwmm.pubcrawler.tasks.repository.*;

import javax.inject.Singleton;

/**
 * @author Sam Adams
 */
public class MongoRepositoryModule extends AbstractModule {

    private final DB bibDb;
    private final DB taskDb;

    public MongoRepositoryModule(final DB bibDb, final DB taskDb) {
        this.bibDb = bibDb;
        this.taskDb = taskDb;
    }

    @Override
    protected void configure() {
        bind(JournalRepository.class).to(MongoJournalRepository.class);
        bind(IssueRepository.class).to(MongoIssueRepository.class);
        bind(ArticleRepository.class).to(MongoArticleRepository.class);
        bind(TaskRepository.class).to(MongoTaskRepository.class);
        bind(TaskSpecificationFactory.class).to(GuiceTaskSpecificationFactory.class);
    }

    @Provides @Singleton @Journals
    public DBCollection getJournalCollection() {
        return bibDb.getCollection("journals");
    }

    @Provides @Singleton @Issues
    public DBCollection getIssueCollection() {
        return bibDb.getCollection("issues");
    }

    @Provides @Singleton @Articles
    public DBCollection getArticleCollection() {
        return bibDb.getCollection("articles");
    }

    @Provides @Singleton @Tasks
    public DBCollection getTaskCollection() {
        return taskDb.getCollection("tasks");
    }

}
