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
import nu.xom.Element;
import nu.xom.Serializer;
import org.apache.log4j.Logger;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.CrawlerRuntimeException;
import wwmm.pubcrawler.crawlers.AbstractCrawler;
import wwmm.pubcrawler.crawlers.ArticleCrawler;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.types.Doi;
import wwmm.pubcrawler.utils.XPathUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;

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
public class AcsArticleCrawler extends AbstractCrawler implements ArticleCrawler {

    private static final Logger LOG = Logger.getLogger(AcsArticleCrawler.class);

    private final Document html;
    private final URI url;

    private Article articleRef;

    public AcsArticleCrawler(Article article, CrawlerContext context) throws IOException {
        super(context);
        this.articleRef = article;
        this.html = fetchHtml();
        this.url = URI.create(html.getBaseURI());
    }

    public Document fetchHtml() throws IOException {
        Doi doi = articleRef.getDoi();
        if (doi == null) {
            throw new CrawlerRuntimeException("Article missing DOI: " + articleRef);
        }
        return readHtml(doi.getUrl(), articleRef.getId(), "abstract.html", AGE_MAX);
    }



    @Override
    protected Logger log() {
        return LOG;
    }

    /**
	 * <p>
	 * Crawls the abstract webpage to find a link to a webpage listing the
	 * article supplementary files.
	 * </p>
	 *
	 * @return URL of the webpage listing the article supplementary files.
	 */
    private URI getSupportingInfoUrl() {
        String href = XPathUtils.getString(html, ".//x:a[@title='View Supporting Information']/@href");
        log().trace("Supporting Information URL: "+href);
        return url == null ? null : url.resolve(href);
    }



    /**
	 * <p>
	 * Gets the ID of the supplementary file at the publisher's site from
	 * the supplementary file URL.
	 * </p>
	 *
	 * @param href - the URL from which to obtain the filename.
	 *
	 * @return the filename of the supplementary file.
	 */
    private String getSuppFilePath(String href) {
        int i = href.indexOf("/suppl_file/");
        String filepath = href.substring(i+12);
        return filepath;
    }


    @Override
    public Article toArticle() {
        AcsArticleParser parser = new AcsArticleParser(articleRef, html, url);
        return parser.toArticle();
    }

}
