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

    private static final String LINK_TEXT = "linkText";
    private static final String CONTENT_TYPE = "contentType";
    private static final String DESCRIPTION = "description";
    private static final String LENGTH = "length";
    private static final String FILEPATH = "filepath";

    protected AbstractResource() { }

    protected AbstractResource(final ResourceId id, final URI url) {
        setId(id);
        setUrl(url);
    }


    @Override
    protected ResourceId createId(final String id) {
        return new ResourceId(id);
    }


    public String getLinkText() {
        return getString(LINK_TEXT);
    }

    public void setLinkText(final String linkText) {
        put(LINK_TEXT, linkText);
    }


    public String getContentType() {
        return getString(CONTENT_TYPE);
    }

    public void setContentType(final String contentType) {
        put(CONTENT_TYPE, contentType);
    }


    public String getDescription() {
        return getString(DESCRIPTION);
    }

    public void setDescription(final String description) {
        put(DESCRIPTION, description);
    }


    public Long getLength() {
        return getLong(LENGTH);
    }

    public void setLength(final Long length) {
        put(LENGTH, length);
    }


    public String getFilePath() {
        return getString(FILEPATH);
    }

    public void setFilePath(final String filePath) {
        put(FILEPATH, filePath);
    }

    
}
