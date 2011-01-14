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

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.log4j.Logger;
import org.joda.time.Duration;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.data.DataStore;
import wwmm.pubcrawler.httpcrawler.CrawlerGetRequest;
import wwmm.pubcrawler.httpcrawler.CrawlerRequest;
import wwmm.pubcrawler.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.httpcrawler.HttpCrawler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.concurrent.TimeUnit;

/**
 * @author Sam Adams
 */
public abstract class AbstractCrawler {

    public static final Duration AGE_0 = new Duration(0);
    public static final Duration AGE_1DAY = new Duration(TimeUnit.DAYS.toMillis(1));
    public static final Duration AGE_MAX = new Duration(Long.MAX_VALUE);

    private CrawlerContext context;

    protected AbstractCrawler(CrawlerContext context) {
        this.context = context;
    }


    protected abstract Logger log();


    protected CrawlerContext getContext() {
        return context;
    }

    protected DataStore getDataStore() {
        return context.getDataStore();
    }

    protected HttpCrawler getHttpCrawler() {
        return context.getHttpCrawler();
    }

    protected AbstractCrawlerFactory getFactory() {
        return context.getCrawlerFactory();
    }


    protected String readString(URI url, String id, Duration maxage) throws IOException {
        CrawlerGetRequest request = new CrawlerGetRequest(url, id, maxage);
        return readString(request);
    }

    protected String readString(CrawlerRequest request) throws IOException {
        CrawlerResponse response = getHttpCrawler().execute(request);
        try {
            return readString(response);
        } finally {
            response.close();
        }
    }

    private String readString(CrawlerResponse response) throws IOException {
        String encoding = getEntityCharset(response);
        if (encoding != null) {
            return IOUtils.toString(response.getContent(), encoding);
        } else {
            return IOUtils.toString(response.getContent());
        }
    }

    protected Document readDocument(URI url, String id, Duration maxage) throws IOException {
        CrawlerGetRequest request = new CrawlerGetRequest(url, id, maxage);
        return readDocument(request);
    }

    protected Document readDocument(CrawlerRequest request) throws IOException {
        CrawlerResponse response = getHttpCrawler().execute(request);
        try {
            String encoding = getEntityCharset(response);
            if (encoding == null) {
                return readDocument(response, new Builder());
            } else {
                return readDocument(response, new Builder(), encoding);
            }
        } finally {
            response.close();
        }
    }

    private Document readDocument(CrawlerResponse response, Builder builder) throws IOException {
        try {
            Document doc = builder.build(response.getContent());
            doc.setBaseURI(response.getUrl().toString());
            return doc;
        } catch (ParsingException e) {
            throw new IOException("Error reading XML", e);
        }
    }

    private Document readDocument(CrawlerResponse response, Builder builder, String encoding) throws IOException {
        try {
            InputStreamReader isr = new InputStreamReader(response.getContent(), encoding);
            Document doc = builder.build(isr);
            doc.setBaseURI(response.getUrl().toString());
            return doc;
        } catch (ParsingException e) {
            throw new IOException("Error reading XML", e);
        }
    }

    protected  Document readHtml(URI url, String id, Duration maxage) throws IOException {
        CrawlerGetRequest request = new CrawlerGetRequest(url, id, maxage);
        return readHtml(request);
    }

    protected Document readHtml(CrawlerRequest request) throws IOException {
        CrawlerResponse response = getHttpCrawler().execute(request);
        try {
            String encoding = getEntityCharset(response);
            if (encoding == null) {
                return readDocument(response, newTagSoupBuilder());
            } else {
                return readDocument(response, newTagSoupBuilder(), encoding);
            }
        } finally {
            response.close();
        }
    }


    protected static Builder newTagSoupBuilder() {
        XMLReader tagSoupReader = newTagSoupReader();
		return new Builder(tagSoupReader);
	}

    protected static XMLReader newTagSoupReader() {
        XMLReader reader;
        try {
            reader = XMLReaderFactory.createXMLReader("org.ccil.cowan.tagsoup.Parser");
        } catch (SAXException e) {
            throw new RuntimeException("Exception whilst creating XMLReader from org.ccil.cowan.tagsoup.Parser", e);
        }
        return reader;
    }


    private static String getEntityCharset(CrawlerResponse response) {
        Header contentType = response.getContentType();
        if (contentType != null) {
            // e.g. text/html; charset=utf-8
            String value = contentType.getValue();
            for (String s : value.split("; ")) {
                if (s.toLowerCase().startsWith("charset=")) {
                    return s.substring(8);
                }
            }
        }
        return null;
    }

}