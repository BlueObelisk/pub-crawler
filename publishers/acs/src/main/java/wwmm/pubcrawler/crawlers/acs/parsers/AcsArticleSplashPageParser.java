/*
 * Copyright 2010-2011 Nick Day, Sam Adams
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wwmm.pubcrawler.crawlers.acs.parsers;

import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import org.apache.log4j.Logger;
import wwmm.pubcrawler.CrawlerRuntimeException;
import wwmm.pubcrawler.HtmlUtil;
import wwmm.pubcrawler.parsers.AbstractArticleParser;
import wwmm.pubcrawler.model.*;
import wwmm.pubcrawler.model.id.ArticleId;
import wwmm.pubcrawler.utils.XPathUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * The <code>AcsArticleCrawler</code> class uses a provided DOI to get
 * information about an article that is published in a journal of the
 * American Chemical Society.
 * </p>
 *
 * @author Nick Day
 * @author Sam Adams
 * @version 2
 */
public class AcsArticleSplashPageParser extends AbstractArticleParser {

    private static final Logger LOG = Logger.getLogger(AcsArticleSplashPageParser.class);
    private static final Pattern WHITESPACE = Pattern.compile("\\s+");

    public AcsArticleSplashPageParser(final Article articleRef, final Document document, final URI uri) {
        super(articleRef, document, uri);
    }

    protected Logger log() {
        return LOG;
    }


    /**
	 * <p>
	 * Crawls the abstract webpage to find a link to a webpage listing the
	 * article supplementary files.
	 * </p>
	 *
	 * @return URL of the webpage listing the article supplementary files.
	 */
    public URI getSupportingInfoUrl() {
        final String href = XPathUtils.getString(getHtml(), ".//x:a[@title='View Supporting Information']/@href");
        log().trace("Supporting Information URL: "+href);
        return href == null ? null : getUrl().resolve(href);
    }

    @Override
    public ArticleId getArticleId() {
        return getArticleRef().getId();
    }

    @Override
    protected String getTitle() {
        final Element element = (Element) XPathUtils.getNode(getHtml(), ".//x:h1[@class='articleTitle']");
        // TODO handle entity images
        return element.getValue();
    }

    @Override
    protected Element getTitleHtml() {
        final Element element = (Element) XPathUtils.getNode(getHtml(), ".//x:h1[@class='articleTitle']");
        final Element copy = new Element("h1", "http://www.w3.org/1999/xhtml");
        for (int i = 0; i < element.getChildCount(); i++) {
            copy.appendChild(element.getChild(i).copy());
        }
        return copy;
    }

    public Element getAbstractHtml() {
        final Element element = (Element) XPathUtils.getNode(getHtml(), ".//x:p[@class='articleBody_abstractText']");
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

    public String getAbstractAsHtml() {
        Element element = getAbstractHtml();
        return normaliseWhiteSpace(HtmlUtil.writeAscii(new Document(element)));
    }

    private String normaliseWhiteSpace(final String s) {
        return WHITESPACE.matcher(s).replaceAll(" ");
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
        final List<String> authors = new ArrayList<String>();
        final List<Node> nodes = XPathUtils.queryHTML(getHtml(), "/x:html/x:head/x:meta[@name='dc.Creator']/@content");
        for (final Node node : nodes) {
            final Attribute attr = (Attribute) node;
            authors.add(attr.getValue());
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
        final List<Node> nodes = XPathUtils.queryHTML(getHtml(), ".//x:div[@id='citation']");

        final Element citation = (Element) nodes.get(0);
        final String journalName = XPathUtils.getString(citation, "./x:cite");
        final String year = XPathUtils.getString(citation, "./x:span[@class='citation_year']");
        final String volume = XPathUtils.getString(citation, "./x:span[@class='citation_volume']");

        final String s = citation.getValue();
        final Pattern p = Pattern.compile("\\(([^(]+)\\), pp? (\\S+)");
        final Matcher m = p.matcher(s);
        if (!m.find()) {
            throw new CrawlerRuntimeException("No match: "+s, getArticleId(), getUrl());
        }
        final String issue = m.group(1);
        final String pages = m.group(2);

        final Reference ref = new Reference();
        ref.setJournalTitle(journalName);
        ref.setYear(year);
        ref.setVolume(volume);
        ref.setNumber(issue);
        ref.setPages(pages);
        return ref;
    }

    @Override
    protected List<SupplementaryResource> getSupplementaryResources() {
        return null;
    }

    @Override
    public List<FullTextResource> getFullTextResources() {
        final List<FullTextResource> fullTextResources = new ArrayList<FullTextResource>();
        final List<Node> links = XPathUtils.queryHTML(getHtml(), ".//x:div[@id='links']/x:ul[1]/x:li[1]/following-sibling::*/x:a");
        for (final Node link : links) {
            final Element address = (Element) link;
            final String href = ((Element) link).getAttributeValue("href");
            final String text = XPathUtils.getString(address, "./x:span[1]");
            final FullTextResource fullText = new FullTextResource();
            fullText.setUrl(getUrl().resolve(href));
            fullText.setLinkText(text);
            fullTextResources.add(fullText);
        }
        return fullTextResources;
    }

    public List<Author> getAuthorDetails() {
        final Map<String,String> affiliations = getAffiliations();

        final List<Author> authors = new ArrayList<Author>();
        final String correspondenceAddress = getCorrespondenceAddress();

        final Node node = XPathUtils.getNode(getHtml(), "//x:div[@id='articleMeta']/x:div[@id='authors']");
        final Element content = new Element("p");

        boolean corresponding = false;
        for (int i = 0; i < node.getChildCount(); i++) {
            Node child = node.getChild(i);
            if (child instanceof Element) {
                final Element element = (Element) child;
                if ("NLM_xref-aff".equals(element.getAttributeValue("class"))) {
                    final String affiliation = affiliations.get(child.getValue());
                    final String name = getAuthorName(content);

                    final Author author = new Author(name);
                    author.setAffiliation(affiliation);
                    if (corresponding) {
                        author.setEmailAddress(correspondenceAddress);
                    }
                    authors.add(author);

                    content.removeChildren();
                    corresponding = false;
                    continue;
                }
                if ("ref".equals(element.getAttributeValue("class"))) {
                    corresponding = true;
                    continue;
                }
            }
            content.appendChild(child.copy());
        }

        return authors;
    }

    private String getAuthorName(final Element content) {
        String name = content.getValue().trim();
        if (name.startsWith(", ")) {
            name = name.substring(2).trim();
        }
        if (name.startsWith("and ")) {
            name = name.substring(4).trim();
        }
        return name;
    }

    private String getCorrespondenceAddress() {
        return XPathUtils.getString(getHtml(), "//x:div[@id='correspondence']/x:a").trim();
    }

    private Map<String, String> getAffiliations() {
        final Map<String,String> affiliations = new LinkedHashMap<String, String>();
        for (Node node : XPathUtils.queryHTML(getHtml(), "//x:div[@id='articleMeta']/x:div[@class='affiliations']/x:div")) {
            addAffiliation((Element) node, affiliations);
        }
        return affiliations;
    }

    private void addAffiliation(final Element node, final Map<String, String> affiliations) {
        final Element sup = (Element) XPathUtils.getNode(node, "x:sup[1]");
        final Element content = new Element("p");
        for (int i = node.indexOf(sup) + 1; i < node.getChildCount(); i++) {
            Node child = node.getChild(i);
            content.appendChild(child.copy());
        }
        affiliations.put(sup.getValue().trim(), content.getValue().trim());
    }
}
