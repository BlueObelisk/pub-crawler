package wwmm.pubcrawler.crawler;

import org.joda.time.Duration;

/**
 * @author Sam Adams
 */
public interface CrawlTask {

    String getId();

    Class<? extends CrawlRunner> getTaskClass();

    Duration getInterval();
    
    TaskData getData();

}
