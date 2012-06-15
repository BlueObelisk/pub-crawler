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

    private IssueId issueRef;
    private String title;
    private String titleHtml;
    private List<String> authors;
    private Reference reference;
    private String abstractText;
    private Doi doi;
    private String abstractHtml;
    private List<SupplementaryResource> supplementaryResources;
    private List<FullTextResource> fullTextResources;
    private URI supplementaryResourceUrl;
    private long sequence;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }


    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(final List<String> authors) {
        this.authors = authors;
    }


    public Reference getReference() {
        return reference;
    }

    public void setReference(final Reference reference) {
        this.reference = reference;
    }

    public String getAbstractText() {
        return abstractText;
    }

    public void setAbstractText(final String abstractText) {
        this.abstractText = abstractText;
    }


    public Doi getDoi() {
        return doi;
    }

    public void setDoi(final Doi doi) {
        this.doi = doi;
    }


    public String getTitleHtml() {
        return titleHtml;
    }

    public void setTitleHtml(final String titleHtml) {
        this.titleHtml = titleHtml;
    }


    public String getAbstractHtml() {
        return abstractHtml;
    }

    public void setAbstractHtml(final String abstractHtml) {
        this.abstractHtml = abstractHtml;
    }


    public List<SupplementaryResource> getSupplementaryResources() {
        return supplementaryResources;
    }

    public void setSupplementaryResources(final List<SupplementaryResource> supplementaryResources) {
        this.supplementaryResources = supplementaryResources;
    }


    public List<FullTextResource> getFullTextResources() {
        return fullTextResources;
    }

    public void setFullTextResources(final List<FullTextResource> fullTextResources) {
        this.fullTextResources = fullTextResources;
    }

    public URI getSupplementaryResourceUrl() {
        return supplementaryResourceUrl;
    }

    public void setSupplementaryResourceUrl(final URI url) {
        this.supplementaryResourceUrl = url;
    }

    public IssueId getIssueRef() {
        return issueRef;
    }

    public void setIssueRef(final IssueId issueRef) {
        this.issueRef = issueRef;
    }

    public long getSequence() {
        return sequence;
    }

    public void setSequence(final long sequence) {
        this.sequence = sequence;
    }
}
