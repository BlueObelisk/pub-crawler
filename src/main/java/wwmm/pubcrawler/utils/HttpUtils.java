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

package wwmm.pubcrawler.utils;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Sam Adams
 */
public class HttpUtils {

    public static String readString(HttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        if (entity == null) {
            return null;
        }

        String encoding = getEntityCharset(entity);
        InputStream in = entity.getContent();
        try {
            String s = IOUtils.toString(in, encoding);
            return s;
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    private static String getEntityCharset(HttpEntity entity) {
        Header contentType = entity.getContentType();
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

    public static void closeQuietly(HttpResponse response) {
        if (response != null) {
            closeQuietly(response.getEntity());
        }
    }

    private static void closeQuietly(HttpEntity entity) {
        try {
            if (entity != null) {
                entity.consumeContent();
            }
        } catch (IOException e) {
            // ignore
        }
    }

    public static Document readDocument(HttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        if (entity == null) {
            return null;
        }
        Builder builder = new Builder();
        String charset = getEntityCharset(response.getEntity());
        if (charset == null) {
            return readXml(entity, builder);
        } else {
            return readXml(entity, builder, charset);
        }
    }

    private static Document readXml(HttpEntity entity, Builder builder) throws IOException {
        InputStream in = entity.getContent();
        try {
            Document doc = builder.build(in);
            return doc;
        } catch (ParsingException e) {
            IOException ioe = new IOException("Error parsing XML document");
            ioe.initCause(e);
            throw ioe;
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    private static Document readXml(HttpEntity entity, Builder builder, String encoding) throws IOException {
        InputStream in = entity.getContent();
        try {
            InputStreamReader isr = new InputStreamReader(in, encoding);
            Document doc = builder.build(isr);
            return doc;
        } catch (ParsingException e) {
            IOException ioe = new IOException("Error parsing XML document");
            ioe.initCause(e);
            throw ioe;
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    public static Document readHtmlDocument(HttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        if (entity == null) {
            return null;
        }
        Builder builder = createTagSoupBuilder();
        String charset = getEntityCharset(response.getEntity());
        if (charset == null) {
            return readXml(entity, builder);
        } else {
            return readXml(entity, builder, charset);
        }
    }

    private static Builder createTagSoupBuilder() {
        XMLReader tagSoupReader = createTagSoupReader();
		return new Builder(tagSoupReader);
	}

    private static XMLReader createTagSoupReader() {
        XMLReader reader;
        try {
            reader = XMLReaderFactory.createXMLReader("org.ccil.cowan.tagsoup.Parser");
        } catch (SAXException e) {
            throw new RuntimeException("Exception whilst creating XMLReader from org.ccil.cowan.tagsoup.Parser", e);
        }
        return reader;
    }

}
