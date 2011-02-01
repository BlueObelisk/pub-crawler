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
package wwmm.pubcrawler.crawlers.rsc;

import nu.xom.ParsingException;
import org.apache.log4j.Logger;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.DefaultCrawlerContext;
import wwmm.pubcrawler.crawlers.AbstractJournalCrawler;
import wwmm.pubcrawler.journals.RscJournalIndex;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.Journal;

import java.io.IOException;
import java.net.URI;

/**
 * @author Sam Adams
 */
public class RscJournalCrawler extends AbstractJournalCrawler {

    private static final Logger LOG = Logger.getLogger(RscJournalCrawler.class);

    private static final String CURRENT_ISSUE_IDENTIFIER = "Latest";

    public RscJournalCrawler(Journal journal, CrawlerContext context) {
        super(journal, context);
    }

    @Override
    protected Logger log() {
        return LOG;
    }

    @Override
    public Issue fetchCurrentIssue() throws IOException {
        log().debug("Fetching current issue of " + getJournal().getFullTitle());
        Issue issue = new Issue();
        issue.setId("rsc/"+getJournal().getAbbreviation()+"/latest");
        issue.setCurrent(true);
        issue.setUrl(URI.create(CURRENT_ISSUE_IDENTIFIER));
        return fetchIssue(issue);
    }

    public static void main(String[] args) throws IOException, ParsingException {

        for (Journal journal : RscJournalIndex.getIndex().values()) {
            CrawlerContext context = new DefaultCrawlerContext(new RscCrawlerFactory());
            RscJournalCrawler crawler = new RscJournalCrawler(journal, context);
            crawler.crawlJournal();
        }

    }

}
