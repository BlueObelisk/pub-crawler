/*
 * Copyright 2010-2011 Nick Day, Sam Adams
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wwmm.pubcrawler.crawlers.acs;

import nu.xom.Document;
import org.apache.log4j.Logger;
import org.joda.time.Duration;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.crawlers.AbstractCrawler;
import wwmm.pubcrawler.crawlers.IssueCrawler;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.Journal;

import java.io.IOException;

/**
 * <p>The <code>AcsIssueCrawler</code> class provides a method for obtaining
 * information about all articles from a particular issue of a journal
 * published by the American Chemical Society.</p>
 *
 * @author Nick Day
 * @author Sam Adams
 * @version 2.0
 */
public class AcsIssueCrawler extends AbstractCrawler implements IssueCrawler {

    private static final Logger LOG = Logger.getLogger(AcsIssueCrawler.class);

    private final Journal journal;
    private final Issue issueRef;

    private final Document document;
    
    /**
	 * <p>Creates an instance of the AcsIssueCrawler class and
	 * specifies the issue to be crawled.</p>
	 *
	 * @param issue the issue to be crawled.
	 */
    public AcsIssueCrawler(Issue issue, Journal journal, CrawlerContext context) throws IOException {
        super(context);
        this.journal = journal;
        this.issueRef = issue;
        
        document = fetchHtml(issue);
    }

    @Override
    protected Logger log() {
        return LOG;
    }

    protected Document fetchHtml(Issue issue) throws IOException {
        Duration maxAge;
        if (issue.isCurrent()) {
            maxAge = AGE_1DAY;
        } else {
            maxAge = AGE_MAX;
        }
        return readHtml(issue.getUrl(), issue.getId(), "toc.html", maxAge);
    }

    @Override
    public Issue toIssue() {
        final AcsIssueParser parser = new AcsIssueParser(issueRef, document, journal);
        return parser.toIssue();
    }
    
}
