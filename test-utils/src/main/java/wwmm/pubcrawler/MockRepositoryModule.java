package wwmm.pubcrawler;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.mockito.Mockito;
import wwmm.pubcrawler.archivers.ArticleArchiver;
import wwmm.pubcrawler.archivers.IssueArchiver;
import wwmm.pubcrawler.archivers.JournalArchiver;

/**
 * @author Sam Adams
 */
public class MockRepositoryModule extends AbstractModule {

    @Override
    protected void configure() {
    }
    
    @Provides
    public JournalArchiver provideJournalArchiver() {
        return Mockito.mock(JournalArchiver.class);
    }

    @Provides
    public IssueArchiver provideIssueArchiver() {
        return Mockito.mock(IssueArchiver.class);
    }

    @Provides
    public ArticleArchiver providesArticleArchiver() {
        return Mockito.mock(ArticleArchiver.class);
    }

}
