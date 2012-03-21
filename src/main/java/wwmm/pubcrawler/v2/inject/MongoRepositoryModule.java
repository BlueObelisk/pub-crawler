package wwmm.pubcrawler.v2.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import wwmm.pubcrawler.v2.repositories.mongo.MongoTaskRepository;
import wwmm.pubcrawler.v2.repositories.TaskRepository;
import wwmm.pubcrawler.v2.repositories.ArticleRepository;
import wwmm.pubcrawler.v2.repositories.IssueRepository;
import wwmm.pubcrawler.v2.repositories.JournalRepository;
import wwmm.pubcrawler.v2.repositories.mongo.MongoArticleRepository;
import wwmm.pubcrawler.v2.repositories.mongo.MongoIssueRepository;
import wwmm.pubcrawler.v2.repositories.mongo.MongoJournalRepository;

import javax.inject.Singleton;
import java.net.UnknownHostException;

/**
 * @author Sam Adams
 */
public class MongoRepositoryModule extends AbstractModule {

    private final DB database;

    public MongoRepositoryModule(final DB database) {
        this.database = database;
    }

    @Override
    protected void configure() {
        bind(JournalRepository.class).to(MongoJournalRepository.class);
        bind(IssueRepository.class).to(MongoIssueRepository.class);
        bind(ArticleRepository.class).to(MongoArticleRepository.class);
        bind(TaskRepository.class).to(MongoTaskRepository.class);
    }

    @Provides @Singleton @Journals
    public DBCollection getJournalCollection() {
        return database.getCollection("journals");
    }

    @Provides @Singleton @Issues
    public DBCollection getIssueCollection() {
        return database.getCollection("issues");
    }

    @Provides @Singleton @Articles
    public DBCollection getArticleCollection() {
        return database.getCollection("articles");
    }

    @Provides @Singleton @Tasks
    public DBCollection getTaskCollection() {
        return database.getCollection("tasks");
    }

}
