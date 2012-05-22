package wwmm.pubcrawler.crawler;

import org.joda.time.Duration;
import wwmm.pubcrawler.tasks.TaskSpecification;

/**
 * @author Sam Adams
 */
public interface Task<T> {

    String getId();

    TaskSpecification<T> getTaskSpecification();

    Duration getInterval();
    
    T getData();

}
