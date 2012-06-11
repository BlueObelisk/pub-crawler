package wwmm.pubcrawler.tasks;

import org.joda.time.Duration;
import wwmm.pubcrawler.model.id.ArticleId;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.model.id.PublisherId;

import java.net.URI;

/**
 * @author Sam Adams
 */
public class ArticleCrawlTaskDataMarshaller extends AbstractCrawlTaskDataMarshaller implements Marshaller<ArticleCrawlTaskData> {

    private static final String PUBLISHER = "publisher";
    private static final String JOURNAL = "journal";
    private static final String ARTICLE = "article";

    @Override
    public void marshall(final ArticleCrawlTaskData data, final DataSink target) {
        marshallCommonFields(data, target);
        final ArticleId articleRef = data.getArticleRef();
        target.writeString(PUBLISHER, articleRef.getPublisherPart());
        target.writeString(JOURNAL, articleRef.getJournalPart());
        target.writeString(ARTICLE, articleRef.getArticlePart());
    }

    @Override
    public ArticleCrawlTaskData unmarshall(final DataSource source) {
        final URI url = source.readUri(AbstractCrawlTaskDataMarshaller.URL);
        final String fileId = source.readString(AbstractCrawlTaskDataMarshaller.FILE_ID);
        final Duration maxAge = source.readDuration(AbstractCrawlTaskDataMarshaller.MAX_AGE);
        final String publisher = source.readString(PUBLISHER);
        final String journal = source.readString(JOURNAL);
        final String article = source.readString(ARTICLE);
        final ArticleId articleRef = new ArticleId(new JournalId(new PublisherId(publisher), journal), article);
        return new ArticleCrawlTaskData(url, fileId, maxAge, articleRef);
    }
}
