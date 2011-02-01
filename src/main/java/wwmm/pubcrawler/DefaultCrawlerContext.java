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

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import uk.ac.cam.ch.wwmm.httpcrawler.httpcrawler.HttpCrawler;
import uk.ac.cam.ch.wwmm.httpcrawler.httpcrawler.cache.HttpCache;
import uk.ac.cam.ch.wwmm.httpcrawler.httpcrawler.cache.file.FileSystemCache;
import wwmm.pubcrawler.crawlers.AbstractCrawlerFactory;
import wwmm.pubcrawler.data.DataStore;

import java.io.File;
import java.io.IOException;

/**
 * @author Sam Adams
 */
public class DefaultCrawlerContext extends CrawlerContext {

    public DefaultCrawlerContext(AbstractCrawlerFactory crawlerFactory) throws IOException {
        super(createDataStore(), createCrawler(), crawlerFactory);
    }

    private static HttpCrawler createCrawler() throws IOException {
        int connectionTimeoutMillis = 20000;
        int socketTimeoutMillis = 20000;

        HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), connectionTimeoutMillis);
        HttpConnectionParams.setSoTimeout(client.getParams(), socketTimeoutMillis);
        HttpProtocolParams.setUserAgent(client.getParams(), "pubcrawler/0.3");

        HttpCache cache = new FileSystemCache(new File("../cache/"));
        return new HttpCrawler(client, cache);
    }

    private static DataStore createDataStore() throws IOException {
        DataStore store = new DataStore(new File("../data/"));
        return store;
    }

}
