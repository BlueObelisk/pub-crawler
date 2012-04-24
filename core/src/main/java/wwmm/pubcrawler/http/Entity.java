package wwmm.pubcrawler.http;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.joda.time.DateTime;

import java.net.URI;
import java.util.List;

/**
 * @author Sam Adams
 */
public class Entity {

    private String id;
    private URI url;
    private DateTime timestamp;
    private List<Header> headers;
    private byte[] content;

    public Entity(final String id, final URI url, final DateTime timestamp, final List<Header> headers, final byte[] content) {
        this.id = id;
        this.url = url;
        this.timestamp = timestamp;
        this.headers = headers;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public URI getUrl() {
        return url;
    }

    public DateTime getTimestamp() {
        return timestamp;
    }

    public List<Header> getHeaders() {
        return headers;
    }

    public byte[] getContent() {
        return content;
    }

    public String getCharSet() {
        final Header contentType = getContentTypeHeader();
        if (contentType != null) {
            final HeaderElement values[] = contentType.getElements();
            if (values.length > 0) {
                final NameValuePair param = values[0].getParameterByName("charset");
                if (param != null) {
                    return param.getValue();
                }
            }
        }
        return null;
    }

    private Header getContentTypeHeader() {
        for (final Header header : headers) {
            if ("Content-Type".equalsIgnoreCase(header.getName())) {
                return header;
            }
        }
        return null;
    }
}
