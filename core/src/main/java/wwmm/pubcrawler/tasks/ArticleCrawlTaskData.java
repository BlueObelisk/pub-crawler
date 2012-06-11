package wwmm.pubcrawler.tasks;

import org.joda.time.Duration;
import wwmm.pubcrawler.model.id.ArticleId;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.model.id.PublisherId;

import java.net.URI;

/**
 * @author Sam Adams
 */
public class ArticleCrawlTaskData extends HttpCrawlTaskData {

    private final ArticleId articleRef;

    public ArticleCrawlTaskData(final URI url, final String fileId, final Duration maxAge, final ArticleId articleRef) {
        super(url, fileId, maxAge, url);
        this.articleRef = articleRef;
    }

    public ArticleId getArticleRef() {
        return articleRef;
    }

}
