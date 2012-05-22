package wwmm.pubcrawler.crawler;

import org.joda.time.Duration;
import wwmm.pubcrawler.tasks.TaskSpecification;

/**
 * @author Sam Adams
 */
public class TaskBuilder<T> {

    private final TaskSpecification<T> taskSpecification;
    private String id;
    private Duration interval = Duration.standardDays(999);
    private T data;

    public static <T> TaskBuilder<T> newTask(final TaskSpecification<T> taskSpecification) {
        return new TaskBuilder(taskSpecification);
    }

    private TaskBuilder(final TaskSpecification<T> taskSpecification) {
        this.taskSpecification = taskSpecification;
    }

    public TaskBuilder<T> withId(final String id) {
        this.id = id;
        return this;
    }

    public TaskBuilder<T> withInterval(final Duration interval) {
        this.interval = interval;
        return this;
    }

    public TaskBuilder<T> withData(final T data) {
        this.data = data;
        return this;
    }

    public Task<T> build() {
        return new DefaultTask(id, interval, data, taskSpecification);
    }

    private static class DefaultTask<T> implements Task {

        private final String id;
        private final Duration interval;
        private final TaskSpecification<T> taskSpecification;
        private final T data;

        public DefaultTask(final String id, final Duration interval, final T data, final TaskSpecification<T> taskSpecification) {
            this.id = id;
            this.interval = interval;
            this.data = data;
            this.taskSpecification = taskSpecification;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public Duration getInterval() {
            return interval;
        }

        @Override
        public TaskSpecification<T> getTaskSpecification() {
            return taskSpecification;
        }

        @Override
        public T getData() {
            return data;
        }

        @Override
        public String toString() {
            return "DefaultTask{" +
                    "id='" + id + '\'' +
                    ", interval=" + interval +
                    ", taskSpecification=" + taskSpecification +
                    ", data=" + data +
                    '}';
        }
    }
}
