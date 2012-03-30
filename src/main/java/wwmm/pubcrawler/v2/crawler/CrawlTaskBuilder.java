package wwmm.pubcrawler.v2.crawler;

import org.joda.time.Duration;

import java.util.Collections;
import java.util.Map;

/**
 * @author Sam Adams
 */
public class CrawlTaskBuilder {

    private String id;
    private Duration maxAge;
    private Class<? extends CrawlRunner> jobClass;
    private Map<String,String> data;

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

    public CrawlTaskBuilder withMaxAge(final Duration maxAge) {
        this.maxAge = maxAge;
        return this;
    }

    public CrawlTaskBuilder withData(final Map<String, String> data) {
        this.data = data;
        return this;
    }

    public CrawlTask build() {
        return new DefaultCrawlTask(id, maxAge,  new TaskData(data != null ? data : Collections.<String,String>emptyMap()), jobClass);
    }

    private static class DefaultCrawlTask implements CrawlTask {

        private final String id;
        private final Duration maxAge;
        private final TaskData data;
        private final Class<? extends CrawlRunner> jobClass;

        public DefaultCrawlTask(final String id, final Duration maxAge, final TaskData data, final Class<? extends CrawlRunner> jobClass) {
            this.id = id;
            this.maxAge = maxAge;
            this.data = data;
            this.jobClass = jobClass;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public Duration getMaxAge() {
            return maxAge;
        }

        @Override
        public Class<? extends CrawlRunner> getTaskClass() {
            return jobClass;
        }

        @Override
        public TaskData getData() {
            return data;
        }

    }
}
