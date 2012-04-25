package wwmm.pubcrawler.crawlers.rsc.parsers;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import wwmm.pubcrawler.parsers.PublicationListParser;
import wwmm.pubcrawler.crawlers.rsc.Rsc;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.utils.XPathUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Sam Adams
 */
public class RscPublicationListParser implements PublicationListParser {

    private final Document document;
    private static final Pattern TITLE = Pattern.compile("(.*?)(\\(\\d+-Present\\))?");

    public RscPublicationListParser(final Document document) {
        this.document = document;
    }

    @Override
    public List<Journal> findJournals() {
        final List<Journal> journals = new ArrayList<Journal>();
        for (final Node node : XPathUtils.queryHTML(document, "//x:a[@class='jLink']")) {
            final String href = ((Element) node).getAttributeValue("href");
            final String abbrev = href.substring(href.lastIndexOf('/') + 1);
            final String title = normalise(node.getValue()).trim();
            journals.add(new Journal(Rsc.PUBLISHER_ID, abbrev, title));
        }
        return journals;
    }

    private String normalise(final String value) {
        final Matcher matcher = TITLE.matcher(value);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        return value;
    }

}
