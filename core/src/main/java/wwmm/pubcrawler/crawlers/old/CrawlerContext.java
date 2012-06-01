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
package wwmm.pubcrawler.crawlers.old;

import uk.ac.cam.ch.wwmm.httpcrawler.HttpFetcher;
import wwmm.pubcrawler.data.mongo.MongoStore;

/**
 * @author Sam Adams
 */
public class CrawlerContext {

    private final MongoStore dataStore;
    private final HttpFetcher httpCrawler;

    public CrawlerContext(final MongoStore dataStore, final HttpFetcher httpCrawler) {
        this.dataStore = dataStore;
        this.httpCrawler = httpCrawler;
    }

    public MongoStore getDataStore() {
        return dataStore;
    }

    public HttpFetcher getHttpCrawler() {
        return httpCrawler;
    }

}
