/*
 * Copyright 2010-2011 Nick Day, Sam Adams
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package wwmm.pubcrawler.crawlers.springer;

import org.apache.log4j.Logger;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.crawlers.AbstractJournalCrawler;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.model.id.JournalId;

import java.io.IOException;
import java.net.URI;
import java.util.List;

/**
 * @author Sam Adams
 */
public class SpringerJournalCrawler extends AbstractJournalCrawler {

    private static final Logger LOG = Logger.getLogger(SpringerJournalCrawler.class);

    public SpringerJournalCrawler(Journal journal, CrawlerContext context) {
        super(journal, context);
    }

    @Override
    public Issue fetchCurrentIssue() throws IOException {
        return null;
    }

    @Override
    public List<Issue> fetchIssueList() throws IOException {
        JournalId id = new JournalId("springer/"+getJournal().getAbbreviation());
        URI url = getJournalIndexUrl();
        SpringerJournalIndexCrawler crawler = new SpringerJournalIndexCrawler(getContext(), url, id);
        return crawler.listIssues();
    }

    private URI getJournalIndexUrl() {
        return URI.create("http://www.springerlink.com/content/"+getJournal().getAbbreviation()+"/");
    }

    @Override
    protected Logger log() {
        return LOG;
    }

}
