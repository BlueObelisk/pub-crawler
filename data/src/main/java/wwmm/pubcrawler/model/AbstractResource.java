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

import wwmm.pubcrawler.model.id.ResourceId;

import java.net.URI;

/**
 * @author Sam Adams
 */
public abstract class AbstractResource extends PubcrawlerObject<ResourceId> {

    private String linkText;
    private String contentType;
    private String description;
    private Long length;
    private String filePath;

    protected AbstractResource() { }

    protected AbstractResource(final ResourceId id, final URI url) {
        setId(id);
        setUrl(url);
    }

    public String getLinkText() {
        return linkText;
    }

    public void setLinkText(final String linkText) {
        this.linkText = linkText;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(final String contentType) {
        this.contentType = contentType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(final Long length) {
        this.length = length;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(final String filePath) {
        this.filePath = filePath;
    }
}
