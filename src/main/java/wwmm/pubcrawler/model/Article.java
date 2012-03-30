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
package wwmm.pubcrawler.model;

import wwmm.pubcrawler.model.id.ArticleId;
import wwmm.pubcrawler.types.Doi;

import java.net.URI;
import java.util.List;

/**
 * @author Sam Adams
 */
public class Article extends PubcrawlerObject<ArticleId> {

    private String issueRef;

    public Article() {
    }

    public Article(final ArticleId articleId, final String title, final List<String> authors, final Reference reference, final URI url, final Doi doi) {
        setId(articleId);
        setTitle(title);
        setAuthors(authors);
        setReference(reference);
        if (url != null) {
            setUrl(url);
        }
        if (doi != null) {
            setDoi(doi);
        }
    }

    @Override
    protected ArticleId createId(final String id) {
        return new ArticleId(id);
    }

    public String getTitle() {
        return getString("title");
    }

    public void setTitle(final String title) {
        put("title", title);
    }


    public List<String> getAuthors() {
        return (List<String>) get("authors");
    }

    public void setAuthors(final List<String> authors) {
        put("authors", authors);
    }


    public Reference getReference() {
        return (Reference) get("reference");
    }

    public void setReference(final Reference reference) {
        put("reference", reference);
    }



    public String getAbstractText() {
        return getString("abstract");
    }

    public void setAbstractText(final String abstractText) {
        put("abstract", abstractText);
    }


    public Doi getDoi() {
        final String s = getString("doi");
        return s == null ? null : new Doi(s);
    }

    public void setDoi(final Doi doi) {
        put("doi", doi == null ? null : doi.getValue());
    }


    public String getTitleHtml() {
        return getString("title-html");
    }

    public void setTitleHtml(final String titleHtml) {
        put("title-html", titleHtml);
    }


    public String getAbstractHtml() {
        return getString("abstact-html");
    }

    public void setAbstractHtml(final String abstractHtml) {
        put("abstract-html", abstractHtml);
    }


    public List<SupplementaryResource> getSupplementaryResources() {
        return (List<SupplementaryResource>) get("suppResources");
    }

    public void setSupplementaryResources(final List<SupplementaryResource> supplementaryResources) {
        put("suppResources", supplementaryResources);
    }


    public List<FullTextResource> getFullTextResources() {
        return (List<FullTextResource>) get("fullText");
    }

    public void setFullTextResources(final List<FullTextResource> fullTextResources) {
        put("fullText", fullTextResources);
    }

    public URI getSupplementaryResourceUrl() {
        final String s = getString("suppUrl");
        return s == null ? null : URI.create(s);
    }

    public void setSupplementaryResourceUrl(final URI url) {
        final String s = url == null ? null : url.toString();
        put("suppUrl", s);
    }

    public String getIssueRef() {
        return issueRef;
    }

    public void setIssueRef(final String issueRef) {
        this.issueRef = issueRef;
    }
}
