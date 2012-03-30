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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerGetRequest;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.crawlers.AbstractCrawler;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.types.Doi;
import wwmm.pubcrawler.utils.BibtexTool;

import java.io.IOException;
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
public class AcsArticleBibTexCrawler extends AbstractCrawler {

    private static final Logger LOG = Logger.getLogger(AcsArticleBibTexCrawler.class);

    private final BibtexTool bibtex;

    private Article articleRef;

    public AcsArticleBibTexCrawler(final Article article, final CrawlerContext context) throws IOException {
        super(context);
        this.articleRef = article;
        this.bibtex = fetchBibtex();

    }

    private BibtexTool fetchBibtex() throws IOException {
        final Doi doi = articleRef.getDoi();

        // This is necessary for correct cookies to be set...
        touchDownloadPage(doi);

        final List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("doi", doi.getValue()));
        params.add(new BasicNameValuePair("downloadFileName", "bibtex"));
        params.add(new BasicNameValuePair("direct", "true"));
        params.add(new BasicNameValuePair("format", "bibtex"));
        params.add(new BasicNameValuePair("include", "cit"));
        params.add(new BasicNameValuePair("submit", "Download Citation(s)"));

        final URI url = URI.create("http://pubs.acs.org/action/downloadCitation");
        final String bibtex = readStringPost(url, params, articleRef.getId(), "bibtex.txt", AGE_MAX);
        return new BibtexTool(bibtex);
    }
        
    private void touchDownloadPage(final Doi doi) throws IOException {
        final URI url = URI.create("http://pubs.acs.org/action/showCitFormats?doi="+doi.getValue());
        final CrawlerGetRequest request = new CrawlerGetRequest(url, getCacheId(articleRef.getId(), "_bib_touch"), AGE_MAX);
        touch(request);
    }

    @Override
    protected Logger log() {
        return LOG;
    }

}
