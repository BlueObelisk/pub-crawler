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
public class SupplementaryResource extends AbstractResource {

    public SupplementaryResource() { }

    public SupplementaryResource(ResourceId id, URI url, String path) {
        super(id, url);
        setFilePath(path);
    }

    public String toString() {
        return getUrl()+"\n"+getLinkText()+"\n"+getContentType()+"\n";
    }

}
