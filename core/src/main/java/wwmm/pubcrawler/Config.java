package wwmm.pubcrawler;

import org.joda.time.Duration;

/**
 * @author Sam Adams
 */
public class Config {

    public static final Duration PUBLICATION_LIST_CACHE_MAX_AGE = Duration.standardDays(7);
    public static final Duration ISSUE_LIST_CACHE_MAX_AGE = Duration.standardDays(1);
    public static final Duration ISSUE_TOC_CACHE_MAX_AGE = Duration.standardDays(180);
    public static final Duration ARTICLE_CACHE_MAX_AGE = Duration.standardDays(180);

    public static final Duration PUBLICATION_LIST_CRAWL_INTERVAL = Duration.standardDays(180);
    public static final Duration ISSUE_LIST_CRAWL_INTERVAL = Duration.standardDays(180);
    public static final Duration ISSUE_TOC_CRAWL_INTERVAL = Duration.standardDays(180);
    public static final Duration ARTICLE_CRAWL_INTERVAL = Duration.standardDays(180);

}
