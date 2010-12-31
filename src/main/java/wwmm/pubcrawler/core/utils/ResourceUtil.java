package wwmm.pubcrawler.core.utils;

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
        if (path == null) {
            throw new FileNotFoundException("File not found: "+path);
        }
        return in;
    }

    public static String readString(Class<?> context, String path) throws IOException {
        InputStream in = open(context, path);
        try {
            return IOUtils.toString(in);
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
