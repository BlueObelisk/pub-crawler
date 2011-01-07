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

import wwmm.pubcrawler.types.Doi;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sam Adams
 */
public class Article {

    private String id;
    private URI url;
    private String title;
    private List<String> authors;

    private Reference reference;
    
    private String abstractText;
    private Doi doi;

    private String titleHtml;
    private String abstractHtml;

    private List<FullTextResource> fullTextResources;
    private List<SupplementaryResource> supplementaryResources;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public Reference getReference() {
        return reference;
    }

    public void setReference(Reference reference) {
        this.reference = reference;
    }
    

    public String getAbstractText() {
        return abstractText;
    }

    public void setAbstractText(String abstractText) {
        this.abstractText = abstractText;
    }


    public Doi getDoi() {
        return doi;
    }

    public void setDoi(Doi doi) {
        this.doi = doi;
    }


    public URI getUrl() {
        return url;
    }

    public void setUrl(URI url) {
        this.url = url;
    }


    public String getTitleHtml() {
        return titleHtml;
    }

    public void setTitleHtml(String titleHtml) {
        this.titleHtml = titleHtml;
    }

    public String getAbstractHtml() {
        return abstractHtml;
    }

    public void setAbstractHtml(String abstractHtml) {
        this.abstractHtml = abstractHtml;
    }


    public List<SupplementaryResource> getSupplementaryResources() {
        if (supplementaryResources == null) {
            supplementaryResources = new ArrayList<SupplementaryResource>();
        }
        return supplementaryResources;
    }

    public void setSupplementaryResources(List<SupplementaryResource> supplementaryResources) {
        this.supplementaryResources = supplementaryResources;
    }


    public List<FullTextResource> getFullTextResources() {
        if (fullTextResources == null) {
            fullTextResources = new ArrayList<FullTextResource>();
        }
        return fullTextResources;
    }

    public void setFullTextResources(List<FullTextResource> fullTextResources) {
        this.fullTextResources = fullTextResources;
    }
    
}
