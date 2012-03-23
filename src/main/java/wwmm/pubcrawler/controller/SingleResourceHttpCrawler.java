package wwmm.pubcrawler.controller;

import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class SingleResourceHttpCrawler extends Crawler<URITask,CrawlerResponse> {

    @Inject
    public SingleResourceHttpCrawler(final BasicHttpFetcher fetcher, final Processor<CrawlerResponse> resourceProcessor) {
        super(fetcher, resourceProcessor);
    }

}
