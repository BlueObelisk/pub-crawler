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
import wwmm.pubcrawler.model.id.IssueId;
import wwmm.pubcrawler.types.Doi;

import java.net.URI;
import java.util.List;

/**
 * @author Sam Adams
 */
public class Article extends PubcrawlerObject<ArticleId> {

    private static final String TITLE = "title";
    private static final String AUTHORS = "authors";
    private static final String REFERENCE = "reference";
    private static final String ABSTRACT = "abstract";
    private static final String DOI = "doi";
    private static final String TITLE_HTML = "title-html";
    private static final String ABSTRACT_HTML = "abstract-html";
    private static final String SUPP_RESOURCES = "suppResources";
    private static final String FULL_TEXT = "fullText";
    private static final String SUPP_URL = "suppUrl";

    private IssueId issueRef;

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
        return getString(TITLE);
    }

    public void setTitle(final String title) {
        put(TITLE, title);
    }


    public List<String> getAuthors() {
        return (List<String>) get(AUTHORS);
    }

    public void setAuthors(final List<String> authors) {
        put(AUTHORS, authors);
    }


    public Reference getReference() {
        return (Reference) get(REFERENCE);
    }

    public void setReference(final Reference reference) {
        put(REFERENCE, reference);
    }



    public String getAbstractText() {
        return getString(ABSTRACT);
    }

    public void setAbstractText(final String abstractText) {
        put(ABSTRACT, abstractText);
    }


    public Doi getDoi() {
        final String s = getString(DOI);
        return s == null ? null : new Doi(s);
    }

    public void setDoi(final Doi doi) {
        put(DOI, doi == null ? null : doi.getValue());
    }


    public String getTitleHtml() {
        return getString(TITLE_HTML);
    }

    public void setTitleHtml(final String titleHtml) {
        put(TITLE_HTML, titleHtml);
    }


    public String getAbstractHtml() {
        return getString(ABSTRACT_HTML);
    }

    public void setAbstractHtml(final String abstractHtml) {
        put(ABSTRACT_HTML, abstractHtml);
    }


    public List<SupplementaryResource> getSupplementaryResources() {
        return (List<SupplementaryResource>) get(SUPP_RESOURCES);
    }

    public void setSupplementaryResources(final List<SupplementaryResource> supplementaryResources) {
        put(SUPP_RESOURCES, supplementaryResources);
    }


    public List<FullTextResource> getFullTextResources() {
        return (List<FullTextResource>) get(FULL_TEXT);
    }

    public void setFullTextResources(final List<FullTextResource> fullTextResources) {
        put(FULL_TEXT, fullTextResources);
    }

    public URI getSupplementaryResourceUrl() {
        final String s = getString(SUPP_URL);
        return s == null ? null : URI.create(s);
    }

    public void setSupplementaryResourceUrl(final URI url) {
        final String s = url == null ? null : url.toString();
        put(SUPP_URL, s);
    }

    public IssueId getIssueRef() {
        return issueRef;
    }

    public void setIssueRef(final IssueId issueRef) {
        this.issueRef = issueRef;
    }
}
