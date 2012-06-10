package wwmm.pubcrawler.http;

import java.net.URI;

/**
 * @author Sam Adams
 */
public class TextResource {

    private final URI url;
    private final String text;

    public TextResource(final URI url, final String text) {
        this.url = url;
        this.text = text;
    }

    public URI getUrl() {
        return url;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "TextResource{" +
                "url=" + url +
                '}';
    }
}
