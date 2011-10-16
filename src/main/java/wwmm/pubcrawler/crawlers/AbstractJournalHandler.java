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
package wwmm.pubcrawler.crawlers;

import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.Journal;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * @author Sam Adams
 */
public abstract class AbstractJournalHandler extends AbstractCrawler implements JournalHandler {

    private final Journal journal;

    protected AbstractJournalHandler(Journal journal, CrawlerContext context) {
        super(context);
        this.journal = journal;
    }

    protected Journal getJournal() {
        return journal;
    }

    public Issue fetchIssue(Issue issue) throws IOException {
        AbstractIssueCrawler crawler = getFactory().createIssueCrawler(issue, getJournal(), getContext());
        Issue result = crawler.toIssue();
        return result;
    }

    public Article fetchArticle(Article article) throws IOException {
        AbstractArticleCrawler crawler = getFactory().createArticleCrawler(article, getContext());
        if (crawler != null) {
            return crawler.toArticle();
        }
        return article;
    }

    public List<Issue> fetchIssueList() throws IOException {
        return Collections.emptyList();
    }

}
