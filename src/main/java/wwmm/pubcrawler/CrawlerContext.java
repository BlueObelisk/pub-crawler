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
package wwmm.pubcrawler;

import uk.ac.cam.ch.wwmm.httpcrawler.HttpCrawler;
import wwmm.pubcrawler.crawlers.AbstractCrawlerFactory;
import wwmm.pubcrawler.data.mongo.MongoStore;

/**
 * @author Sam Adams
 */
public class CrawlerContext {

    private final MongoStore dataStore;
    private final HttpCrawler httpCrawler;
    private final AbstractCrawlerFactory crawlerFactory;

    public CrawlerContext(MongoStore dataStore, HttpCrawler httpCrawler, AbstractCrawlerFactory crawlerFactory) {
        this.dataStore = dataStore;
        this.httpCrawler = httpCrawler;
        this.crawlerFactory = crawlerFactory;
    }

    public MongoStore getDataStore() {
        return dataStore;
    }

    public HttpCrawler getHttpCrawler() {
        return httpCrawler;
    }

    public AbstractCrawlerFactory getCrawlerFactory() {
        return crawlerFactory;
    }
    
}
