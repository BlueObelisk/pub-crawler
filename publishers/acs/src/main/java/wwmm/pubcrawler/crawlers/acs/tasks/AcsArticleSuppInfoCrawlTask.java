package wwmm.pubcrawler.crawlers.acs.tasks;

import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.http.Fetcher;
import wwmm.pubcrawler.http.URITask;
import wwmm.pubcrawler.crawlers.BasicHttpCrawlTask;
import wwmm.pubcrawler.crawler.TaskData;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class AcsArticleSuppInfoCrawlTask extends BasicHttpCrawlTask {

    @Inject
    public AcsArticleSuppInfoCrawlTask(final Fetcher<URITask, CrawlerResponse> fetcher) {
        super(fetcher);
    }

    @Override
    protected void handleResponse(final String id, final TaskData data, final CrawlerResponse response) throws Exception {
        throw new UnsupportedOperationException("NYI");
    }

}
