package wwmm.pubcrawler.crawlers.acta.tasks;

import org.joda.time.Duration;
import wwmm.pubcrawler.crawler.CrawlRunner;
import wwmm.pubcrawler.crawler.TaskData;
import wwmm.pubcrawler.crawlers.acta.IucrFrameResource;
import wwmm.pubcrawler.crawlers.acta.IucrFrameTask;
import wwmm.pubcrawler.http.Fetcher;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.model.id.PublisherId;
import wwmm.pubcrawler.processors.IssueTocProcessor;

import javax.inject.Inject;
import java.net.URI;

/**
 * @author Sam Adams
 */
public class IucrIssueTocCrawlTask implements CrawlRunner {

    private final Fetcher<IucrFrameTask, IucrFrameResource> fetcher;
    private final IssueTocProcessor<IucrFrameResource> processor;

    @Inject
    public IucrIssueTocCrawlTask(final Fetcher<IucrFrameTask, IucrFrameResource> fetcher, final IssueTocProcessor<IucrFrameResource> processor) {
        this.fetcher = fetcher;
        this.processor = processor;
    }

    @Override
    public void run(final String id, final TaskData data) throws Exception {
        final Duration maxAge = data.containsKey("maxAge") ? new Duration(Long.valueOf(data.getString("maxAge"))) : null;
        final URI url = URI.create(data.getString("url"));

        final PublisherId publisherId = new PublisherId(data.getString("publisher"));
        final JournalId journalId = new JournalId(publisherId, data.getString("journal"));

        final URI bodyUrl = url.resolve("isscontsbdy.html");
        final URI headUrl = url.resolve("isscontshdr.html");
        
        final IucrFrameTask task = new IucrFrameTask(id, bodyUrl, headUrl, maxAge, null);
        final IucrFrameResource resource = fetcher.fetch(task);
        processor.process(id, journalId, resource);
    }
}
