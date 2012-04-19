package wwmm.pubcrawler.crawlers.acs.parsers;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import wwmm.pubcrawler.CrawlerRuntimeException;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Reference;
import wwmm.pubcrawler.model.SupplementaryResource;
import wwmm.pubcrawler.model.id.ResourceId;
import wwmm.pubcrawler.types.MediaType;
import wwmm.pubcrawler.utils.XPathUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Sam Adams
 */
public class AcsArticleSuppInfoPageParser extends AcsArticleSplashPageParser {

    public AcsArticleSuppInfoPageParser(final Article articleRef, final Document document, final URI uri) {
        super(articleRef, document, uri);
    }

    @Override
    public Reference getReference() {
        if (getHtml() == null) {
            return getArticleRef().getReference();
        }
        final List<Node> nodes = XPathUtils.queryHTML(getHtml(), ".//x:div[@id='citation']");

        final Element citation = (Element) nodes.get(0);
        final String abbreviatedJournalTitle = XPathUtils.getString(citation, "./x:cite");
        final String year = XPathUtils.getString(citation, "./x:span[@class='citation_year']");
        final String volume = XPathUtils.getString(citation, "./x:span[@class='citation_volume']");

        final String s = citation.getValue();
        final Pattern p = Pattern.compile("\\(([^(]+)\\), pp? (\\S+)");
        final Matcher m = p.matcher(s);
        if (!m.find()) {
            throw new CrawlerRuntimeException("No match: "+s);
        }
        final String issue = m.group(1);
        final String pages = m.group(2);

        final Reference ref = new Reference();
        ref.setJournalTitle(getJournalTitle());
        ref.setAbbreviatedJournalTitle(abbreviatedJournalTitle);
        ref.setYear(year);
        ref.setVolume(volume);
        ref.setNumber(issue);
        ref.setPages(pages);
        return ref;
    }

    private String getJournalTitle() {
        final String title = XPathUtils.getString(getHtml(), "/x:html/x:head/x:title");
        return title.substring(0, title.indexOf('-')).trim();
    }

    @Override
    public List<SupplementaryResource> getSupplementaryResources() {
        if (getHtml() == null) {
            List<SupplementaryResource> supplementaryResources =  getArticleRef().getSupplementaryResources();
            if (supplementaryResources == null) {
                supplementaryResources = new ArrayList<SupplementaryResource>(1);
            }
            return supplementaryResources;
        }
        final List<SupplementaryResource> supplementaryResources = new ArrayList<SupplementaryResource>();
        if (getHtml() != null) {

            final List<Node> headingNodes = XPathUtils.queryHTML(getHtml(), "//x:div[@id='supInfoBox']/x:h3");
            for (final Node heading : headingNodes) {
                final String title = heading.getValue();

                final MediaType mediaType;
                if ("PDF".equals(title)) {
                    mediaType = MediaType.APPLICATION_PDF;
                }
                else if ("Crystallographic Information File".equals(title)) {
                    mediaType = MediaType.CHEMICAL_CIF;
                }
                else {
                    mediaType = null;
                }

                final List<Node> fileNodes = XPathUtils.queryHTML(heading, "./following-sibling::x:ul[1]/x:li/x:a");
                for (final Node file : fileNodes) {
                    final Element address = (Element) file;
                    final String href = address.getAttributeValue("href");
                    final URI resourceUrl = getUrl().resolve(href);
                    final String linkText = address.getValue();

                    final String filepath = getSuppFilePath(href);
                    final ResourceId id = new ResourceId(articleRef.getId(), filepath);
                    final SupplementaryResource supplementaryResource = new SupplementaryResource(id, resourceUrl, filepath);
                    supplementaryResource.setContentType(mediaType == null ? title : mediaType.getName());
                    supplementaryResource.setLinkText(linkText);
                    supplementaryResources.add(supplementaryResource);
                }
            }
        }
        return supplementaryResources;
    }

    /**
     * <p>
     * Gets the ID of the supplementary file at the publisher's site from
     * the supplementary file URL.
     * </p>
     *
     * @param href - the URL from which to obtain the filename.
     *
     * @return the filename of the supplementary file.
     */
    private String getSuppFilePath(final String href) {
        final int i = href.indexOf("/suppl_file/");
        return href.substring(i+12);
    }

}
