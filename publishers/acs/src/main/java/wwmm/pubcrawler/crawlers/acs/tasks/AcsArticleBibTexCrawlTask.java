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
package wwmm.pubcrawler.crawlers.acs.tasks;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.joda.time.Duration;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.http.Fetcher;
import wwmm.pubcrawler.http.URITask;
import wwmm.pubcrawler.crawler.CrawlRunner;
import wwmm.pubcrawler.crawler.TaskData;
import wwmm.pubcrawler.model.id.ArticleId;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.model.id.PublisherId;
import wwmm.pubcrawler.types.Doi;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * The <code>AcsArticleCrawler</code> class uses a provided DOI to get
 * information about an article that is published in a journal of the
 * American Chemical Society.
 * </p>
 *
 * @author Nick Day
 * @author Sam Adams
 * @version 2
 */
public class AcsArticleBibTexCrawlTask implements CrawlRunner {

    private static final Logger LOG = Logger.getLogger(AcsArticleBibTexCrawlTask.class);
    private static final Duration MAX_AGE = Duration.standardDays(9999);
    private final Fetcher<URITask, CrawlerResponse> fetcher;

    public AcsArticleBibTexCrawlTask(final Fetcher<URITask, CrawlerResponse> fetcher) {
        this.fetcher = fetcher;
    }

    @Override
    public void run(final String id, final TaskData data) throws Exception {

        final PublisherId publisherId = new PublisherId(data.getString("publisher"));
        final JournalId journalId = new JournalId(publisherId, data.getString("journal"));
        final ArticleId articleId = new ArticleId(journalId, data.getString("article"));
        final Doi doi = new Doi(data.getString("doi"));

        fetchBibtex(articleId, doi);
    }

    private void fetchBibtex(final ArticleId articleId, final Doi doi) throws Exception {
        // This is necessary for correct cookies to be set...
        touchDownloadPage(articleId, doi);

        final List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("doi", doi.getValue()));
        params.add(new BasicNameValuePair("downloadFileName", "bibtex"));
        params.add(new BasicNameValuePair("direct", "true"));
        params.add(new BasicNameValuePair("format", "bibtex"));
        params.add(new BasicNameValuePair("include", "cit"));
        params.add(new BasicNameValuePair("submit", "Download Citation(s)"));

        final URI url = URI.create("http://pubs.acs.org/action/downloadCitation");
        final String id = articleId.getPublisherPart() + ":bibtex:" + articleId.getJournalPart() + "/" + articleId.getArticlePart() + "/bibtex.txt";
        fetcher.fetch(new URITask(url, id, MAX_AGE, null));
    }

    private void touchDownloadPage(final ArticleId articleId, final Doi doi) throws Exception {
        final URI url = URI.create("http://pubs.acs.org/action/showCitFormats?doi=" + doi.getValue());
        final String id = articleId.getPublisherPart() + ":bibtex:" + articleId.getJournalPart() + "/" + articleId.getArticlePart() + "/download.html";
        fetcher.fetch(new URITask(url, id, MAX_AGE, null));
    }

}
