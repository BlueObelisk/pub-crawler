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

import nu.xom.Document;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.CrawlerRuntimeException;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.FullTextResource;
import wwmm.pubcrawler.model.Reference;
import wwmm.pubcrawler.model.SupplementaryResource;
import wwmm.pubcrawler.model.id.ArticleId;
import wwmm.pubcrawler.types.Doi;

import java.io.IOException;
import java.net.URI;
import java.util.List;

/**
 * <p>
 * The abstract <code>ArticleCrawler</code> class provides a base implementation
 * for crawling the webpages of published articles.  It is assumed that all 
 * articles have a DOI, which can be used to find all the necessary details.  Hence,
 * this class has a single constructor which takes a DOI parameter.
 * </p>
 *
 * @author Nick Day
 * @author Sam Adams
 * @version 2
 *
 */
public abstract class AbstractArticleCrawler extends AbstractCrawler {

    private final Article articleRef;

    private final Document html;
    private final URI url;

    protected AbstractArticleCrawler(Article article, CrawlerContext context) throws IOException {
        super(context);
        this.articleRef = article;
        this.html = fetchHtml(article);
        if (this.html != null) {
            this.url = URI.create(html.getBaseURI());
        } else {
            this.url = null;
        }
    }

    public Article getArticleRef() {
        return articleRef;
    }

    public Document fetchHtml(Article article) throws IOException {
        Doi doi = article.getDoi();
        if (doi == null) {
            throw new CrawlerRuntimeException("Article missing DOI: " + article);
        }
        return readHtml(doi.getUrl(), article.getId(), "abstract.html", AGE_MAX);
    }


    protected Document getHtml() {
        return html;
    }

    protected URI getUrl() {
        return url;
    }

    protected ArticleId getArticleId() {
        return getArticleRef().getId();
    }

    public Doi getDoi() {
        return getArticleRef().getDoi();
    }

    public Article toArticle() {
        Article article = new Article();
        article.setId(getArticleId());
        article.setDoi(getDoi());
        article.setUrl(getUrl());
        article.setReference(getReference());
        List<SupplementaryResource> supplementaryResources = getSupplementaryResources();
        checkSupplementaryResources(supplementaryResources);
        article.setSupplementaryResources(supplementaryResources);
        article.setFullTextResources(getFullTextResources());
        return article;
    }

    private void checkSupplementaryResources(List<SupplementaryResource> supplementaryResources) {
        if (supplementaryResources != null) {
            for (SupplementaryResource resource : supplementaryResources) {
                if (resource.getUrl() == null) {
                    throw new CrawlerRuntimeException("Supplementary resource missing URL");
                }
                if (resource.getFilePath() == null) {
                    throw new CrawlerRuntimeException("Supplementary resource missing file path");
                }
            }
        }
    }

    /**
	 * <p>
	 * Creates the article bibliographic reference from information found
	 * on the abstract webpage.
	 * </p>
	 *
	 * @return the article bibliographic reference.
	 *
	 */
    public abstract Reference getReference();

    /**
	 * <p>
	 * Gets the details of any supplementary files provided alongside
	 * the published article.
	 * </p>
	 *
	 * @return a list where each item describes a separate supplementary
	 * data file (as a <code>SupplementaryResource</code> object).
	 */
    public abstract List<SupplementaryResource> getSupplementaryResources();

    /**
	 * <p>
	 * Gets the details about the full-text resources for this article.
	 * </p>
	 *
	 * @return details about the full-text resources for this article.
	 */
    public abstract List<FullTextResource> getFullTextResources();

    /**
	 * <p>
	 * Gets a authors of the article from the abstract webpage.
	 * </p>
	 *
	 * @return String containing the article authors.
	 *
	 */
    public abstract List<String> getAuthors();

//    /**
//	 * <p>
//	 * Gets the article title from the abstract webpage.
//	 * </p>
//	 *
//	 * @return the article title.
//	 *
//	 */
//    protected abstract String getTitle();
    
}
