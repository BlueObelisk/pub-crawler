package wwmm.pubcrawler.crawlers.wiley.parsers;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import wwmm.pubcrawler.model.IssueLink;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.parsers.IssueListParser;
import wwmm.pubcrawler.utils.XPathUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sam Adams
 */
public class WileyIssueListParser implements IssueListParser {

    private final Document html;
    private final URI url;
    private final JournalId journalId;

    private final WileyIssueLinkParser issueLinkParser;

    public WileyIssueListParser(final Document html, final URI url, final JournalId journalId) {
        this.html = html;
        this.url = url;
        this.journalId = journalId;
        issueLinkParser = new WileyIssueLinkParser(journalId, url);
    }

    @Override
    public List<IssueLink> findIssues() {
        final List<Node> issueNodes = XPathUtils.queryHTML(html, "//x:ol/x:li/x:div[@class='issue']/x:a/@href");
        final List<IssueLink> issueLinks = new ArrayList<IssueLink>(issueNodes.size());
        for (final Node node : issueNodes) {
            final String href = node.getValue();
            final IssueLink issueLink = issueLinkParser.parseIssueLink(href);
            if (issueLink != null) {
                issueLinks.add(issueLink);
            }
        }
        return issueLinks;
    }
}
