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
package wwmm.pubcrawler.crawlers.nature.parsers;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Serializer;
import org.apache.log4j.Logger;
import wwmm.pubcrawler.model.FullTextResource;
import wwmm.pubcrawler.model.Reference;
import wwmm.pubcrawler.model.SupplementaryResource;
import wwmm.pubcrawler.model.id.ArticleId;
import wwmm.pubcrawler.model.id.ResourceId;
import wwmm.pubcrawler.parsers.AbstractArticleParser;
import wwmm.pubcrawler.types.Doi;
import wwmm.pubcrawler.utils.XPathUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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
public class NatureArticleParser extends AbstractArticleParser {

    // TODO: nature compounds
    // e.g. http://dx.doi.org/10.1038/nchem.900
    // http://www.nature.com/nchem/journal/v3/n1/compound/nchem.900_compOP16H.html
    // also:
    // Nature Chemical Biology
    // Nature Communications

    private static final Logger LOG = Logger.getLogger(NatureArticleParser.class);

    public NatureArticleParser(final ArticleId articleRef, final Document html, final URI url) throws IOException {
        super(articleRef, html, url);
    }

    @Override
    protected ArticleId getArticleId() {
        return getArticleRef();
    }

    @Override
    protected Doi getDoi() {
        return null;    // TODO
    }

    @Override
    protected String getTitle() {
        return getTitleHtml().getValue();
    }

    @Override
    public Element getTitleHtml() {
        final Element element = (Element) XPathUtils.getNode(getHtml(), ".//x:h1[@class='article-heading']");
        final Element copy = new Element("h1", "http://www.w3.org/1999/xhtml");
        for (int i = 0; i < element.getChildCount(); i++) {
            copy.appendChild(element.getChild(i).copy());
        }
        return copy;
    }

    public Element getAbstractHtml() {
        final Element element = (Element) XPathUtils.getNode(getHtml(), ".//x:div[@id='abstract']/x:div[@class='content']/x:p[1]");
        final Element copy = new Element("p", "http://www.w3.org/1999/xhtml");
        for (int i = 0; i < element.getChildCount(); i++) {
            final Node child = element.getChild(i);
            if (child instanceof Element) {
                final Element e = (Element) child;
                if ("img".equals(e.getLocalName())) {
                    copy.appendChild(e);
                    continue;
                }
            }
            copy.appendChild(child.copy());
        }
        return copy;
    }

    @Override
    public List<String> getAuthors() {
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
        final Node citation = XPathUtils.getNode(getHtml(), ".//x:dl[@class='citation']");

        final String journalName = XPathUtils.getString(getHtml(), "/x:html/x:head/x:meta[@name='citation_journal_title']/@content");
        final String volume = XPathUtils.getString(getHtml(), "/x:html/x:head/x:meta[@name='citation_volume']/@content");
        final String number = XPathUtils.getString(getHtml(), "/x:html/x:head/x:meta[@name='citation_issue']/@content");

        String year = XPathUtils.getString(citation, "./x:dt[text()='Year published:']/following-sibling::x:dd[1]");
        if (year.startsWith("(") && year.endsWith(")")) {
            year = year.substring(1, year.length()-1);
        }

        final String pages = XPathUtils.getString(citation, "./x:dd[@class='page']");

        final Reference ref = new Reference();
        ref.setJournalTitle(journalName);
        ref.setYear(year);
        ref.setVolume(volume);
        ref.setNumber(number);
        ref.setPages(pages);
        return ref;
    }


    @Override
    public List<SupplementaryResource> getSupplementaryResources() {
        final List<SupplementaryResource> resources = new ArrayList<SupplementaryResource>();
        final List<Node> nodes = XPathUtils.queryHTML(getHtml(), ".//x:div[@id='supplementary-information']//x:dt/x:a");
        for (final Node node : nodes) {
            final Element address = (Element) node;
            final String linkText = address.getValue();
            final String href = address.getAttributeValue("href");
            final String filePath = getFilePath(href);
            final ResourceId id = new ResourceId(getArticleId(), filePath);
            final SupplementaryResource resource = new SupplementaryResource(id, getUrl().resolve(href), filePath);
            resource.setLinkText(linkText);
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
        return toHtml(getTitleHtml());
    }

    public String getAbstractHtmlString() {
        return toHtml(getAbstractHtml());
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
