package wwmm.pubcrawler.crawler;

import org.joda.time.Duration;

import java.util.Collections;
import java.util.Map;

/**
 * @author Sam Adams
 */
public class CrawlTaskBuilder {

    private String id;
    private Duration interval = Duration.standardDays(999);
    private Class<? extends CrawlRunner> jobClass;
    private Map<String,String> data = Collections.emptyMap();

    public static CrawlTaskBuilder newJob() {
        return new CrawlTaskBuilder();
    }

    public static CrawlTaskBuilder newJob(final Class <? extends CrawlRunner> jobClass) {
        return newJob().ofType(jobClass);
    }

    public CrawlTaskBuilder ofType(final Class<? extends CrawlRunner> jobClass) {
        this.jobClass = jobClass;
        return this;
    }
    
    public CrawlTaskBuilder withId(final String id) {
        this.id = id;
        return this;
    }

    public CrawlTaskBuilder withInterval(final Duration interval) {
        this.interval = interval;
        return this;
    }

    public CrawlTaskBuilder withData(final Map<String, String> data) {
        this.data = data;
        return this;
    }

    public CrawlTask build() {
        return new DefaultCrawlTask(id, interval,  new TaskData(data != null ? data : Collections.<String,String>emptyMap()), jobClass);
    }

    private static class DefaultCrawlTask implements CrawlTask {

        private final String id;
        private final Duration interval;
        private final TaskData data;
        private final Class<? extends CrawlRunner> jobClass;

        public DefaultCrawlTask(final String id, final Duration interval, final TaskData data, final Class<? extends CrawlRunner> jobClass) {
            this.id = id;
            this.interval = interval;
            this.data = data;
            this.jobClass = jobClass;
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
        public Class<? extends CrawlRunner> getTaskClass() {
            return jobClass;
        }

        @Override
        public TaskData getData() {
            return data;
        }

        @Override
        public String toString() {
            return "DefaultCrawlTask{" +
                    "id='" + id + '\'' +
                    ", jobClass=" + jobClass +
                    ", interval=" + interval +
                    '}';
        }
    }
}
