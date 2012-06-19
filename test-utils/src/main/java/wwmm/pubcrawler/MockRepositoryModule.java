package wwmm.pubcrawler;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.mockito.Mockito;
import wwmm.pubcrawler.archivers.*;

/**
 * @author Sam Adams
 */
public class MockRepositoryModule extends AbstractModule {

    @Override
    protected void configure() {
    }
    
    @Provides
    public JournalArchiver provideJournalArchiver() {
        return Mockito.mock(JournalRepositoryArchiver.class);
    }

    @Provides
    public IssueArchiver provideIssueArchiver() {
        return Mockito.mock(IssueRepositoryArchiver.class);
    }

    @Provides
    public ArticleArchiver providesArticleArchiver() {
        return Mockito.mock(ArticleRepositoryArchiver.class);
    }

}
