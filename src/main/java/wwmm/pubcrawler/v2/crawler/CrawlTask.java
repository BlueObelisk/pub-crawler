package wwmm.pubcrawler.v2.crawler;

import java.util.Map;

/**
 * @author Sam Adams
 */
public interface CrawlTask {

    Class<? extends CrawlRunner> getTaskClass();
    
    String getId();
    
    TaskData getData();

}
