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
        List<Node> nodes = XPathUtils.queryHTML(getHtml(), ".//x:div[@id='citation']");

        Element citation = (Element) nodes.get(0);
        String journalName = XPathUtils.getString(citation, "./x:cite");
        String year = XPathUtils.getString(citation, "./x:span[@class='citation_year']");
        String volume = XPathUtils.getString(citation, "./x:span[@class='citation_volume']");

        String s = citation.getValue();
        Pattern p = Pattern.compile("\\(([^(]+)\\), pp? (\\S+)");
        Matcher m = p.matcher(s);
        if (!m.find()) {
            throw new CrawlerRuntimeException("No match: "+s);
        }
        String issue = m.group(1);
        String pages = m.group(2);

        Reference ref = new Reference();
        ref.setJournalTitle(journalName);
        ref.setYear(year);
        ref.setVolume(volume);
        ref.setNumber(issue);
        ref.setPages(pages);
        return ref;
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
        List<SupplementaryResource> supplementaryResources = new ArrayList<SupplementaryResource>();
        if (getHtml() != null) {

            List<Node> headingNodes = XPathUtils.queryHTML(getHtml(), "//x:div[@id='supInfoBox']/x:h3");
            for (Node heading : headingNodes) {
                String title = heading.getValue();

                MediaType mediaType;
                if ("PDF".equals(title)) {
                    mediaType = MediaType.APPLICATION_PDF;
                }
                else if ("Crystallographic Information File".equals(title)) {
                    mediaType = MediaType.CHEMICAL_CIF;
                }
                else {
                    mediaType = null;
                }

                List<Node> fileNodes = XPathUtils.queryHTML(heading, "./following-sibling::x:ul[1]/x:li/x:a");
                for (Node file : fileNodes) {
                    Element address = (Element) file;
                    String href = address.getAttributeValue("href");
                    URI resourceUrl = getUrl().resolve(href);
                    String linkText = address.getValue();

                    String filepath = getSuppFilePath(href);
                    ResourceId id = new ResourceId(articleRef.getId(), filepath);
                    SupplementaryResource supplementaryResource = new SupplementaryResource(id, resourceUrl, filepath);
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
    private String getSuppFilePath(String href) {
        int i = href.indexOf("/suppl_file/");
        String filepath = href.substring(i+12);
        return filepath;
    }




}
