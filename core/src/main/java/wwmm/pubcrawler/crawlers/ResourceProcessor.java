package wwmm.pubcrawler.crawlers;

/**
 * @author Sam Adams
 */
public interface ResourceProcessor<Resource, Task> {

    void process(String taskId, Task task, Resource resource);

}
