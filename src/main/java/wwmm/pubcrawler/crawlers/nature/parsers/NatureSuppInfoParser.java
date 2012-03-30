/*
 * Copyright 2010-2011 Nick Day, Sam Adams
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wwmm.pubcrawler.crawlers.nature.parsers;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Serializer;
import org.apache.log4j.Logger;
import wwmm.pubcrawler.crawlers.AbstractArticleParser;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.FullTextResource;
import wwmm.pubcrawler.model.Reference;
import wwmm.pubcrawler.model.SupplementaryResource;
import wwmm.pubcrawler.model.id.ResourceId;
import wwmm.pubcrawler.utils.XPathUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * The <code>NatureArticleCrawler</code> class uses a provided DOI to get
 * information about an article that is published in a journal of Nature
 * Publishing Group.
 * </p>
 *
 * @author Nick Day
 * @author Sam Adams
 * @version 2
 */
public class NatureSuppInfoParser extends AbstractArticleParser {

    // TODO: nature compounds
    // e.g. http://dx.doi.org/10.1038/nchem.900
    // http://www.nature.com/nchem/journal/v3/n1/compound/nchem.900_compOP16H.html
    // also:
    // Nature Chemical Biology
    // Nature Communications

    private static final Logger LOG = Logger.getLogger(NatureSuppInfoParser.class);

    public NatureSuppInfoParser(final Article articleRef, final Document html, final URI url) throws IOException {
        super(articleRef, html, url);
    }

    @Override
    protected String getTitle() {
        return getTitleHtml().getValue();
    }

    @Override
    public Element getTitleHtml() {
        if (getHtml() == null) {
            return null;
        }
        final Element element = (Element) XPathUtils.getNode(getHtml(), ".//x:h1[@class='article-heading']");
        final Element copy = new Element("h1", "http://www.w3.org/1999/xhtml");
        for (int i = 0; i < element.getChildCount(); i++) {
            copy.appendChild(element.getChild(i).copy());
        }
        return copy;
    }

    public Element getAbstractHtml() {
        if (getHtml() == null) {
            return null;
        }
        final Element element = (Element) XPathUtils.getNode(getHtml(), ".//x:div[@id='abstract']/x:div[@class='content']/x:p[1]");
        final Element copy = new Element("p", "http://www.w3.org/1999/xhtml");
        for (int i = 0; i < element.getChildCount(); i++) {
            final Node child = element.getChild(i);
            if (child instanceof Element) {
                final Element e = (Element) child;
                if ("img".equals(e.getLocalName())) {
                    copy.appendChild(normaliseEntityImage(e));
                    continue;
                }
            }
            copy.appendChild(child.copy());
        }
        return copy;
    }

    private String normaliseEntityImage(final Element e) {
        final String src = e.getAttributeValue("src");
        if ("/appl/literatum/publisher/achs/journals/entities/223C.gif".equals(src)) {
            return "\u223c";    // TILDE OPERATOR
        }
        if ("/appl/literatum/publisher/achs/journals/entities/2009.gif".equals(src)) {
            return "\u2009";    // THIN SPACE
        }
        if ("/appl/literatum/publisher/achs/journals/entities/2002.gif".equals(src)) {
            return "\u2002";    // EN SPACE
        }
        if ("/appl/literatum/publisher/achs/journals/entities/2225.gif".equals(src)) {
            return "\u2225";    // PARALLEL TO
        }
        if ("/appl/literatum/publisher/achs/journals/entities/22A5.gif".equals(src)) {
            return "\u22A5";    // UP TACK
        }
        if ("/appl/literatum/publisher/achs/journals/entities/21C6.gif".equals(src)) {
            return "\u21C6";    // LEFTWARDS ARROW OVER RIGHTWARDS ARROW
        }
        throw new RuntimeException("Unknown entity: "+src);
    }


    @Override
    public List<String> getAuthors() {
        if (getHtml() == null) {
            return getArticleRef().getAuthors();
        }
        final List<String> authors = new ArrayList<String>();
        final List<Node> nodes = XPathUtils.queryHTML(getHtml(), ".//x:ul[contains(@class, 'authors')]//x:span[@class='fn']");
        for (final Node node : nodes) {
            final Element span = (Element) node;
            authors.add(span.getValue());
        }
        return authors;
    }

    @Override
    public Boolean isOpenAccess() {
        final Node node = XPathUtils.getNode(getHtml(), ".//x:div[@id='articleIcons']/x:img[@alt='ACS AuthorChoice']");
        return node != null;
    }

    @Override
    public Reference getReference() {
        if (getHtml() == null) {
            return getArticleRef().getReference();
        }

        final String journalName = findJournalName();
        final String volume = findJournalVolume();
        final String number = XPathUtils.getString(getHtml(), "/x:html/x:head/x:meta[@name='citation_issue']/@content");

        String year = XPathUtils.getString(getHtml(), "/x:html/x:head/x:meta[@name='prism.publicationDate']/@content");
        String start = null, end = null;
        if (year != null) {
            year = year.substring(0, year.indexOf('-'));
            start = XPathUtils.getString(getHtml(), "/x:html/x:head/x:meta[@name='prism.startingPage']/@content");
            end = XPathUtils.getString(getHtml(), "/x:html/x:head/x:meta[@name='prism.endingPage']/@content");
        } else {
            final String s = XPathUtils.getString(getHtml(), "//x:p[@class='journal']");
            if (s != null) {
                final Pattern p = Pattern.compile("(\\d+) *- *(\\d+) *\\((\\d{4})\\)");
                final Matcher m = p.matcher(s);
                if (m.find()) {
                    year = m.group(3);
                    start = m.group(1);
                    end = m.group(2);
                } else {
                    LOG.warn("Unable to find year for article: "+getArticleId() + " ["+s+"]");
                }
            } else {
                LOG.warn("Unable to find year for article: "+getArticleId());
            }
        }

//        Node citation = XPathUtils.getNode(getHtml(), ".//x:dl[@class='citation']");
//        String pages = XPathUtils.getString(citation, "./x:dd[@class='page']");

        if (start == null || end == null) {
            LOG.warn("Unable to find pages for article: "+getArticleId());
        }
        final String pages = start+"-"+end;

        final Reference ref = new Reference();
        ref.setJournalTitle(journalName);
        ref.setYear(year);
        ref.setVolume(volume);
        ref.setNumber(number);
        ref.setPages(pages);
        return ref;
    }

    private String findJournalVolume() {
        String volume = XPathUtils.getString(getHtml(), "/x:html/x:head/x:meta[@name='citation_volume']/@content");
        if (volume == null) {
            volume = XPathUtils.getString(getHtml(), "//x:p[@class='journal']/x:span[@class='journalnumber']");
        }
        if (volume == null) {
            LOG.warn("Unable to find journal volume: "+getArticleId());
        } else {
            volume = volume.trim();
        }
        return volume;
    }

    private String findJournalName() {
        String journalName = XPathUtils.getString(getHtml(), "/x:html/x:head/x:meta[@name='citation_journal_title']/@content");
        if (journalName == null) {
            journalName = XPathUtils.getString(getHtml(), "//x:p[@class='journal']/x:span[@class='journalname']");
        }
        if (journalName == null) {
            LOG.warn("Unable to find journal name: "+getArticleId());
        } else {
            journalName = journalName.trim();
        }
        return journalName;
    }


    @Override
    public List<SupplementaryResource> getSupplementaryResources() {
        if (getHtml() == null) {
            if  (getArticleRef().getSupplementaryResources() == null) {
                return new ArrayList<SupplementaryResource>(1);
            }
            return getArticleRef().getSupplementaryResources();
        }
        final List<SupplementaryResource> resources = new ArrayList<SupplementaryResource>();
        List<Node> nodes = XPathUtils.queryHTML(getHtml(), ".//x:div[@id='supplementary-information']//x:dt/x:a");
        if (nodes.isEmpty()) {
            nodes = XPathUtils.queryHTML(getHtml(), ".//x:div[@class='container-supplementary']//x:a");
        }
        for (final Node node : nodes) {
            final Element address = (Element) node;
            final String linkText = address.getValue().replaceAll("\\s+", " ").trim();
            final String href = address.getAttributeValue("href");
            final String filepath = getFilePath(href);
            final ResourceId id = new ResourceId(getArticleId(), filepath);
            final SupplementaryResource resource = new SupplementaryResource(id, getUrl().resolve(href), filepath);
            resource.setLinkText(linkText);

            final String description = XPathUtils.getString(node, "../following-sibling::x:dd");
            resource.setDescription(description);

            resources.add(resource);
        }
        return resources;
    }

    private String getFilePath(final String href) {
        final int i = href.lastIndexOf('/');
        return href.substring(i+1);
    }

    @Override
    public List<FullTextResource> getFullTextResources() {
        final List<FullTextResource> fullTextResources = new ArrayList<FullTextResource>();

        return fullTextResources;
    }

    public String getTitleHtmlString() {
        final Element titleHtml = getTitleHtml();
        return titleHtml == null ? null : toHtml(titleHtml);
    }

    public String getAbstractHtmlString() {
        final Element abstractHtml = getAbstractHtml();
        return abstractHtml == null ? null : toHtml(abstractHtml);
    }

    private String toHtml(final Element element) {
        try {
            final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            final Serializer ser = new Serializer(bytes, "UTF-8") {
                @Override
                protected void writeXMLDeclaration() {
                    // no decl
                }
            };
            ser.write(new Document(element));

            final String s = bytes.toString("UTF-8");
            return s.trim();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
