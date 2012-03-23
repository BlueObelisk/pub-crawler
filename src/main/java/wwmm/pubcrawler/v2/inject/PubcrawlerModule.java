package wwmm.pubcrawler.v2.inject;

import com.google.inject.AbstractModule;
import wwmm.pubcrawler.v2.crawler.DefaultTaskQueue;
import wwmm.pubcrawler.v2.crawler.TaskQueue;

/**
 * @author Sam Adams
 */
public class PubcrawlerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(TaskQueue.class).to(DefaultTaskQueue.class);
    }
    
}
