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

package wwmm.pubcrawler.crawlers;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import uk.ac.cam.ch.wwmm.httpcrawler.HttpCrawler;
import wwmm.pubcrawler.utils.ResourceUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

/**
 * @author Sam Adams
 */
public abstract class AbstractCrawlerTest {

    protected static HttpCrawler getHttpCrawler() {
        return new HttpCrawler(getHttpClient());
    }

    private static HttpClient getHttpClient() {
        HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 30000);
        HttpConnectionParams.setSoTimeout(client.getParams(), 30000);
        return client;
    }

    protected CrawlerResponse prepareResponse(String path, URI url) throws IOException {
        byte[] bytes = ResourceUtil.readBytes(getClass(), path);
        InputStream content = new ByteArrayInputStream(bytes);
        List<BasicHeader> headers = Arrays.asList(new BasicHeader("Content-type", "text/html; charset=UTF-8"));
        CrawlerResponse response = new CrawlerResponse(url, headers, content, false, false);
        return response;
    }

}
