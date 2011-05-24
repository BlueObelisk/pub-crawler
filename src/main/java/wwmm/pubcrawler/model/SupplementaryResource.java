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

import java.net.URI;

/**
 * @author Sam Adams
 */
public class SupplementaryResource extends MongoDBObject {


    public URI getUrl() {
        String s = getString("url");
        return s == null ? null : URI.create(s);
    }

    public void setUrl(URI url) {
        String s = url == null ? null : url.toString();
        put("url", s);
    }


    public String getContentType() {
        return getString("contentType");
    }

    public void setContentType(String contentType) {
        put("contentType", contentType);
    }


    public String getLinkText() {
        return getString("linkText");
    }

    public void setLinkText(String linkText) {
        put("linkText", linkText);
    }


    public String getDescription() {
        return getString("description");
    }

    public void setDescription(String description) {
        put("description", description);
    }


    public Long getLength() {
        return getLong("length");
    }

    public void setLength(Long length) {
        put("length", length);
    }


    public String getFilePath() {
        return getString("filepath");
    }

    public void setFilePath(String filePath) {
        put("filepath", filePath);
    }


    public String toString() {
        return getUrl()+"\n"+getLinkText()+"\n"+getContentType()+"\n";
    }

}
