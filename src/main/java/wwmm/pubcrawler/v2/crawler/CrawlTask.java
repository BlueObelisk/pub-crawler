package wwmm.pubcrawler.v2.crawler;

import org.joda.time.Duration;

import java.util.Map;

/**
 * @author Sam Adams
 */
public interface CrawlTask {

    Class<? extends CrawlRunner> getTaskClass();
    
    String getId();

    Duration getMaxAge();
    
    TaskData getData();

}
