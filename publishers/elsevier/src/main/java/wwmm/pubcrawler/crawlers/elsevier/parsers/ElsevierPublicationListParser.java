package wwmm.pubcrawler.crawlers.elsevier.parsers;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wwmm.pubcrawler.parsers.PublicationListParser;
import wwmm.pubcrawler.crawlers.elsevier.Elsevier;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.utils.XPathUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Sam Adams
 */
public class ElsevierPublicationListParser implements PublicationListParser {

    private static final Logger LOG = LoggerFactory.getLogger(ElsevierPublicationListParser.class);
    
    private static final Pattern ID_PATTERN = Pattern.compile("http://www.sciencedirect.com/science/journal/(\\w+)");
    
    private final Document opml;

    public ElsevierPublicationListParser(final Document opml) {
        this.opml = opml;
    }

    @Override
    public List<Journal> findJournals() {
        final List<Journal> list = new ArrayList<Journal>();

        final List<Node> nodes = XPathUtils.queryHTML(opml, "//outline");
        for (final Node node : nodes) {
            final Element outline = (Element) node;

            final String title = outline.getAttributeValue("text");
            final String url = outline.getAttributeValue("htmlUrl");
            
            final Matcher matcher = ID_PATTERN.matcher(url);
            if (!matcher.matches()) {
                LOG.warn("Journal URL does not match ID pattern '{}'", url);
                continue;
            }
            final String abbreviation = matcher.group(1);

            final Journal journal = new Journal(abbreviation, title);
            journal.setId(new JournalId(Elsevier.PUBLISHER_ID, abbreviation));
            journal.setUrl(URI.create(url));
            list.add(journal);
        }

        return list;
    }

}
