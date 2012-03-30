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
import org.apache.http.NameValuePair;
import org.apache.log4j.Logger;
import org.joda.time.Duration;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import uk.ac.cam.ch.wwmm.httpcrawler.*;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.data.mongo.MongoStore;
import wwmm.pubcrawler.model.id.Id;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;
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

    protected AbstractCrawler(final CrawlerContext context) {
        this.context = context;
    }


    protected abstract Logger log();


    protected CrawlerContext getContext() {
        return context;
    }

    protected MongoStore getDataStore() {
        return getContext().getDataStore();
    }

    protected HttpFetcher getHttpCrawler() {
        return getContext().getHttpCrawler();
    }

    protected AbstractCrawlerFactory getFactory() {
        return getContext().getCrawlerFactory();
    }


    protected String readString(final URI url, final Id<?> id, final String filename, final Duration maxage) throws IOException {
        final CrawlerGetRequest request = new CrawlerGetRequest(url, getCacheId(id, filename), maxage);
        return readString(request);
    }

    protected String readStringPost(final URI url, final List<? extends NameValuePair> params, final Id<?> id, final String filename, final Duration maxage) throws IOException {
        final CrawlerPostRequest request = new CrawlerPostRequest(url, params, getCacheId(id, filename), maxage);
        return readString(request);
    }

    private String readString(final CrawlerRequest request) throws IOException {
        final CrawlerResponse response = getHttpCrawler().execute(request);
        try {
            return readString(response);
        } finally {
            response.closeQuietly();
        }
    }

    private String readString(final CrawlerResponse response) throws IOException {
        final String encoding = getEntityCharset(response);
        if (encoding != null) {
            return IOUtils.toString(response.getContent(), encoding);
        } else {
            return IOUtils.toString(response.getContent());
        }
    }

    protected Document readDocument(final URI url, final Id<?> id, final String filename, final Duration maxage) throws IOException {
        final CrawlerGetRequest request = new CrawlerGetRequest(url, getCacheId(id, filename), maxage);
        return readDocument(request);
    }

    private Document readDocument(final CrawlerRequest request) throws IOException {
        final CrawlerResponse response = getHttpCrawler().execute(request);
        try {
            final String encoding = getEntityCharset(response);
            if (encoding == null) {
                return readDocument(response, new Builder());
            } else {
                return readDocument(response, new Builder(), encoding);
            }
        } finally {
            response.closeQuietly();
        }
    }

    private Document readDocument(final CrawlerResponse response, final Builder builder) throws IOException {
        try {
            final Document doc = builder.build(response.getContent());
            setDocBaseUrl(response, doc);
            return doc;
        } catch (ParsingException e) {
            throw new IOException("Error reading XML", e);
        }
    }

    private Document readDocument(final CrawlerResponse response, final Builder builder, final String encoding) throws IOException {
        try {
            final InputStreamReader isr = new InputStreamReader(response.getContent(), encoding);
            final Document doc = builder.build(isr);
            setDocBaseUrl(response, doc);
            return doc;
        } catch (ParsingException e) {
            throw new IOException("Error reading XML", e);
        }
    }

    protected static void setDocBaseUrl(final CrawlerResponse response, final Document doc) {
        String url = response.getUrl().toString();
        if (url.indexOf('#') != -1) {
            url = url.substring(0, url.indexOf('#'));
        }
        doc.setBaseURI(url);
    }

    protected  Document readHtml(final URI url, final Id<?> id, final String filename, final Duration maxage) throws IOException {
        final CrawlerGetRequest request = new CrawlerGetRequest(url, getCacheId(id, filename), maxage);
        return readHtml(request);
    }

    protected  Document readHtmlPost(final URI url, final List<? extends NameValuePair> params, final Id<?> id, final String filename, final Duration maxage) throws IOException {
        final CrawlerPostRequest request = new CrawlerPostRequest(url, params, getCacheId(id, filename), maxage);
        return readHtml(request);
    }

    protected Document readHtml(final CrawlerRequest request) throws IOException {
        final CrawlerResponse response = getHttpCrawler().execute(request);
        try {
            if (response.isFromCache()) {
                log().debug("Retrieved "+response.getUrl()+" from cache");
            } else {
                log().info("Downloaded "+response.getUrl());
            }

            final String encoding = getEntityCharset(response);
            if (encoding == null) {
                return readDocument(response, newTagSoupBuilder());
            } else {
                return readDocument(response, newTagSoupBuilder(), encoding);
            }
        } finally {
            response.closeQuietly();
        }
    }

    protected void touch(final CrawlerRequest request) throws IOException {
        final CrawlerResponse response = getHttpCrawler().execute(request);
        response.closeQuietly();
    }


    protected static Builder newTagSoupBuilder() {
        final XMLReader tagSoupReader = newTagSoupReader();
		return new Builder(tagSoupReader);
	}

    protected static XMLReader newTagSoupReader() {
        final XMLReader reader;
        try {
            reader = XMLReaderFactory.createXMLReader("org.ccil.cowan.tagsoup.Parser");
        } catch (SAXException e) {
            throw new RuntimeException("Exception whilst creating XMLReader from org.ccil.cowan.tagsoup.Parser", e);
        }
        return reader;
    }


    protected static String getEntityCharset(final CrawlerResponse response) {
        final Header contentType = response.getContentType();
        if (contentType != null) {
            // e.g. text/html; charset=utf-8
            final String value = contentType.getValue();
            for (final String s : value.split("; ")) {
                if (s.toLowerCase().startsWith("charset=")) {
                    return s.substring(8);
                }
            }
        }
        return null;
    }

    protected static String getCacheId(final Id<?> id, final String filename) {
        return id.getUid()+"::"+filename;
    }

}
