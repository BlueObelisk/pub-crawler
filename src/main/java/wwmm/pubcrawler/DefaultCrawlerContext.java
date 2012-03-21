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

import com.mongodb.DB;
import com.mongodb.Mongo;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import uk.ac.cam.ch.wwmm.httpcrawler.DefaultHttpFetcher;
import uk.ac.cam.ch.wwmm.httpcrawler.HttpFetcher;
import wwmm.pubcrawler.crawlers.AbstractCrawlerFactory;
import wwmm.pubcrawler.data.mongo.MongoStore;

import java.io.IOException;

/**
 * @author Sam Adams
 */
public class DefaultCrawlerContext extends CrawlerContext {

    public DefaultCrawlerContext(AbstractCrawlerFactory crawlerFactory) throws IOException {
        super(createDataStore(), createCrawler(), crawlerFactory);
    }

    private static HttpFetcher createCrawler() throws IOException {
        int connectionTimeoutMillis = 20000;
        int socketTimeoutMillis = 20000;

        HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), connectionTimeoutMillis);
        HttpConnectionParams.setSoTimeout(client.getParams(), socketTimeoutMillis);
        HttpProtocolParams.setUserAgent(client.getParams(), "pubcrawler/0.3");

        return new DefaultHttpFetcher(client, null);
    }

    private static MongoStore createDataStore() throws IOException {
        Mongo mongo = new Mongo();
        DB db = mongo.getDB("crawler");
        MongoStore store = new MongoStore(db);
        return store;
    }

}
