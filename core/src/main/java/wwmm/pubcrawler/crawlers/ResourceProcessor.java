package wwmm.pubcrawler.crawlers;

import wwmm.pubcrawler.crawler.Task;

/**
 * @author Sam Adams
 */
public interface ResourceProcessor<Resource, Task> {

    void process(String taskId, Task task, Resource resource);

}
