package wwmm.pubcrawler;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.mockito.Mockito;
import wwmm.pubcrawler.archivers.Archiver;
import wwmm.pubcrawler.archivers.ArticleArchiver;
import wwmm.pubcrawler.archivers.IssueArchiver;
import wwmm.pubcrawler.archivers.JournalArchiver;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.Journal;

/**
 * @author Sam Adams
 */
public class MockRepositoryModule extends AbstractModule {

    @Override
    protected void configure() {
    }
    
    @Provides
    public Archiver<Journal> provideJournalArchiver() {
        return Mockito.mock(JournalArchiver.class);
    }

    @Provides
    public Archiver<Issue> provideIssueArchiver() {
        return Mockito.mock(IssueArchiver.class);
    }

    @Provides
    public Archiver<Article> providesArticleArchiver() {
        return Mockito.mock(ArticleArchiver.class);
    }

}
