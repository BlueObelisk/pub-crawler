package wwmm.pubcrawler.crawler;

import org.joda.time.Duration;

/**
 * @author Sam Adams
 */
public interface CrawlTask {

    Class<? extends CrawlRunner> getTaskClass();
    
    String getId();

    Duration getMaxAge();
    
    TaskData getData();

}
