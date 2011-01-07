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
package wwmm.pubcrawler.crawlers.chemsocjapan;

import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.crawlers.AbstractArticleCrawler;
import wwmm.pubcrawler.crawlers.AbstractCrawlerFactory;
import wwmm.pubcrawler.crawlers.AbstractIssueCrawler;
import wwmm.pubcrawler.crawlers.AbstractJournalCrawler;

import java.io.IOException;

/**
 * @author Sam Adams
 */
public class ChemSocJapanCrawlerFactory extends AbstractCrawlerFactory {

    @Override
    public AbstractJournalCrawler createJournalCrawler(Journal journal, CrawlerContext context) throws IOException {
        return new ChemSocJapanJournalCrawler(journal, context);
    }

    @Override
    public AbstractIssueCrawler createIssueCrawler(Issue issue, Journal journal, CrawlerContext context) throws IOException {
        return new ChemSocJapanIssueCrawler(issue, context);
    }

    @Override
    public AbstractArticleCrawler createArticleCrawler(Article article, CrawlerContext context) throws IOException {
        return new ChemSocJapanArticleCrawler(article, context);
    }
    
}
