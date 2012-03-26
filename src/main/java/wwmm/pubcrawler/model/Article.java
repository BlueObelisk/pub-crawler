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

    @Override
    protected ArticleId createId(String id) {
        return new ArticleId(id);
    }

    public String getTitle() {
        return getString("title");
    }

    public void setTitle(String title) {
        put("title", title);
    }


    public List<String> getAuthors() {
        return (List<String>) get("authors");
    }

    public void setAuthors(List<String> authors) {
        put("authors", authors);
    }


    public Reference getReference() {
        return (Reference) get("reference");
    }

    public void setReference(Reference reference) {
        put("reference", reference);
    }



    public String getAbstractText() {
        return getString("abstract");
    }

    public void setAbstractText(String abstractText) {
        put("abstract", abstractText);
    }


    public Doi getDoi() {
        String s = getString("doi");
        return s == null ? null : new Doi(s);
    }

    public void setDoi(Doi doi) {
        put("doi", doi == null ? null : doi.getValue());
    }


    public String getTitleHtml() {
        return getString("title-html");
    }

    public void setTitleHtml(String titleHtml) {
        put("title-html", titleHtml);
    }


    public String getAbstractHtml() {
        return getString("abstact-html");
    }

    public void setAbstractHtml(String abstractHtml) {
        put("abstract-html", abstractHtml);
    }


    public List<SupplementaryResource> getSupplementaryResources() {
        return (List<SupplementaryResource>) get("suppResources");
    }

    public void setSupplementaryResources(List<SupplementaryResource> supplementaryResources) {
        put("suppResources", supplementaryResources);
    }


    public List<FullTextResource> getFullTextResources() {
        return (List<FullTextResource>) get("fullText");
    }

    public void setFullTextResources(List<FullTextResource> fullTextResources) {
        put("fullText", fullTextResources);
    }

    public URI getSupplementaryResourceUrl() {
        String s = getString("suppUrl");
        return s == null ? null : URI.create(s);
    }

    public void setSupplementaryResourceUrl(URI url) {
        String s = url == null ? null : url.toString();
        put("suppUrl", s);
    }

    public String getIssueRef() {
        return issueRef;
    }

    public void setIssueRef(final String issueRef) {
        this.issueRef = issueRef;
    }
}
