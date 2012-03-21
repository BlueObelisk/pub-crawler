package wwmm.pubcrawler.crawlers.elsevier.parsers;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import wwmm.pubcrawler.crawlers.PublicationListParser;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.model.id.PublisherId;
import wwmm.pubcrawler.utils.XPathUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sam Adams
 */
public class ElsevierPublicationListParser implements PublicationListParser {

    private static final URI OPML_URI = URI.create("http://feeds.sciencedirect.com/opml.xml");

    private final PublisherId publisherId;

    private final Document opml;
    private final URI url;

    public ElsevierPublicationListParser(final PublisherId publisherId, final Document opml, final URI url) {
        this.publisherId = publisherId;
        this.opml = opml;
        this.url = url;
    }

    public List<Journal> findJournals() {
        List<Journal> list = new ArrayList<Journal>();

        List<Node> nodes = XPathUtils.queryHTML(opml, "//outline");
        for (Node node : nodes) {
            Element outline = (Element) node;
            String u = outline.getAttributeValue("htmlUrl");
            String title = outline.getAttributeValue("text");

            String id = u.substring(u.lastIndexOf('/')+1);

            Journal journal = new Journal(id, title);
            list.add(journal);
        }

        return list;
    }

}
