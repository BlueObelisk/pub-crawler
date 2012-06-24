package wwmm.pubcrawler.crawlers.wiley.parsers;

import nu.xom.Document;
import nu.xom.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.utils.XPathUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sam Adams
 */
public class WileyIssueListIndexParser {

    private static final Logger LOG = LoggerFactory.getLogger(WileyIssueListIndexParser.class);

    private final Document document;
    private final URI url;

    public WileyIssueListIndexParser(final Document document, final URI url) {
        this.document = document;
        this.url = url;
    }

    public List<String> getIssueYears() {
        final List<Node> nodes = XPathUtils.queryHTML(document, "//x:ol[@class='issueVolumes']/x:li/x:a/@href");
        if (nodes.isEmpty()) {
            LOG.warn("Unable to locate issue volumes: '{}'", url);
        }
        final List<String> years = new ArrayList<String>(nodes.size());
        for (final Node node : nodes) {
            final String href = node.getValue();
            int i = href.indexOf("activeYear=");
            if (i == -1) {
                LOG.warn("Unable to locate year: '{}'", href);
            } else {
                years.add(href.substring(i+11));
            }
        }
        return years;
    }
}
