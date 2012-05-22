package wwmm.pubcrawler.controller;

import com.google.inject.Injector;
import wwmm.pubcrawler.crawler.Task;
import wwmm.pubcrawler.tasks.TaskRunner;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class TaskFactory {
    
    private final Injector injector;

    @Inject
    public TaskFactory(final Injector injector) {
        this.injector = injector;
    }

    public <T> TaskRunner<T> createCrawler(final Task<T> task) {
        return injector.getInstance(task.getTaskSpecification().getRunnerClass());
    }
}
