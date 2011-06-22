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
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerGetRequest;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerRequest;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import uk.ac.cam.ch.wwmm.httpcrawler.HttpCrawler;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.data.mongo.MongoStore;
import wwmm.pubcrawler.model.id.Id;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.concurrent.TimeUnit;

/**
 * @author Sam Adams
 */
public abstract class AbstractCrawler {

    public static final Duration AGE_0 = new Duration(0);
    public static final Duration AGE_30MINS = new Duration(TimeUnit.MINUTES.toMillis(30));
    public static final Duration AGE_1DAY = new Duration(TimeUnit.DAYS.toMillis(7));
    public static final Duration AGE_28DAYS = new Duration(TimeUnit.DAYS.toMillis(28));
    public static final Duration AGE_MAX = new Duration(Long.MAX_VALUE);

    private CrawlerContext context;

    protected AbstractCrawler(CrawlerContext context) {
        this.context = context;
    }


    protected abstract Logger log();


    protected CrawlerContext getContext() {
        return context;
    }

    protected MongoStore getDataStore() {
        return context.getDataStore();
    }

    protected HttpCrawler getHttpCrawler() {
        return context.getHttpCrawler();
    }

    protected AbstractCrawlerFactory getFactory() {
        return context.getCrawlerFactory();
    }


    protected String readString(URI url, Id id, String qualifier, Duration maxage) throws IOException {
        CrawlerGetRequest request = new CrawlerGetRequest(url, id.getValue()+"_"+qualifier, maxage);
        return readString(request);
    }

    protected String readString(CrawlerRequest request) throws IOException {
        CrawlerResponse response = getHttpCrawler().execute(request);
        try {
            return readString(response);
        } finally {
            response.closeQuietly();
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
            response.closeQuietly();
        }
    }

    private Document readDocument(CrawlerResponse response, Builder builder) throws IOException {
        try {
            Document doc = builder.build(response.getContent());
            setDocBaseUrl(response, doc);
            return doc;
        } catch (ParsingException e) {
            throw new IOException("Error reading XML", e);
        }
    }

    private Document readDocument(CrawlerResponse response, Builder builder, String encoding) throws IOException {
        try {
            InputStreamReader isr = new InputStreamReader(response.getContent(), encoding);
            Document doc = builder.build(isr);
            setDocBaseUrl(response, doc);
            return doc;
        } catch (ParsingException e) {
            throw new IOException("Error reading XML", e);
        }
    }

    protected void setDocBaseUrl(CrawlerResponse response, Document doc) {
        String url = response.getUrl().toString();
        if (url.indexOf('#') != -1) {
            url = url.substring(0, url.indexOf('#'));
        }
        doc.setBaseURI(url);
    }

    protected  Document readHtml(URI url, Id id, Duration maxage) throws IOException {
        CrawlerGetRequest request = new CrawlerGetRequest(url, id.getValue()+".html", maxage);
        return readHtml(request);
    }

    protected  Document readHtml(URI url, Id id, String qualifier, Duration maxage) throws IOException {
        CrawlerGetRequest request = new CrawlerGetRequest(url, id.getValue()+"_"+qualifier+".html", maxage);
        return readHtml(request);
    }

    protected Document readHtml(CrawlerRequest request) throws IOException {
        CrawlerResponse response = getHttpCrawler().execute(request);
        try {
            if (response.isFromCache()) {
                log().debug("Retrieved "+response.getUrl()+" from cache");
            } else {
                log().info("Downloaded "+response.getUrl());
            }

            String encoding = getEntityCharset(response);
            if (encoding == null) {
                return readDocument(response, newTagSoupBuilder());
            } else {
                return readDocument(response, newTagSoupBuilder(), encoding);
            }
        } finally {
            response.closeQuietly();
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


    protected static String getEntityCharset(CrawlerResponse response) {
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
