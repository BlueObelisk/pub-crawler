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
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Sam Adams
 */
public class ResourceUtil {

    public static InputStream open(Class<?> context, String path) throws FileNotFoundException {
        InputStream in = context.getResourceAsStream(path);
        if (in == null) {
            throw new FileNotFoundException("File not found: "+path);
        }
        return in;
    }

    public static byte[] readBytes(Class<?> context, String path) throws IOException {
        InputStream in = open(context, path);
        try {
            return IOUtils.toByteArray(in);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    public static String readStringUtf8(Class<?> context, String path) throws IOException {
        return readString(context, path, "UTF-8");
    }

    public static String readString(Class<?> context, String path, String encoding) throws IOException {
        InputStream in = open(context, path);
        try {
            return IOUtils.toString(in, encoding);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    public static Document readXml(Class<?> context, String path) throws IOException, ParsingException {
        InputStream in = open(context, path);
        try {
            Builder builder = new Builder();
            return builder.build(in);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    public static final Document readHtml(Class<?> context, String path) throws IOException, ParsingException {
        InputStream in = open(context, path);
        try {
            Builder builder = createTagSoupBuilder();
            return builder.build(in);
        } finally {
            IOUtils.closeQuietly(in);
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
