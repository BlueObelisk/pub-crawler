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
package wwmm.pubcrawler.crawlers.rsc;

import nu.xom.*;
import org.apache.log4j.Logger;
import wwmm.pubcrawler.crawlers.old.CrawlerContext;
import wwmm.pubcrawler.crawlers.old.AbstractArticleCrawler;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.FullTextResource;
import wwmm.pubcrawler.model.Reference;
import wwmm.pubcrawler.model.SupplementaryResource;
import wwmm.pubcrawler.model.id.ArticleId;
import wwmm.pubcrawler.model.id.ResourceId;
import wwmm.pubcrawler.utils.XPathUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * The <code>RscArticleCrawler</code> class uses a provided DOI to get
 * information about an article that is published in a journal of the
 * Royal Society of Chemistry.
 * </p>
 *
 * @author Nick Day
 * @author Sam Adams
 * @version 2
 */
public class RscArticleCrawler extends AbstractArticleCrawler {

    private static final Logger LOG = Logger.getLogger(RscArticleCrawler.class);

    private ArticleId articleId;

    public RscArticleCrawler(final Article article, final CrawlerContext context) throws IOException {
        super(article, context);
        this.articleId = article.getId();
    }

    @Override
    protected Logger log() {
        return LOG;
    }

    @Override
    protected ArticleId getArticleId() {
        return articleId;
    }

    @Override
    public Reference getReference() {
        final Node head = XPathUtils.getNode(getHtml(), "/x:html/x:head");

        final String journalName = XPathUtils.getString(head, ".//x:meta[@name='citation_journal_title']/@content");
        final String volume = XPathUtils.getString(head, ".//x:meta[@name='citation_volume']/@content");
        final String issue = XPathUtils.getString(head, ".//x:meta[@name='citation_issue']/@content");
        final String firstPage = XPathUtils.getString(head, ".//x:meta[@name='citation_firstpage']/@content");
        final String lastPage = XPathUtils.getString(head, ".//x:meta[@name='citation_lastpage']/@content");
        final String pages = firstPage+'-'+lastPage;

        final String date = XPathUtils.getString(head, ".//x:meta[@name='citation_date']/@content");
        final String year = date.substring(0, date.indexOf('-'));

        final Reference ref = new Reference();
        ref.setJournalTitle(journalName);
        ref.setYear(year);
        ref.setVolume(volume);
        ref.setNumber(issue);
        ref.setPages(pages);
        return ref;
    }

    @Override
    public List<SupplementaryResource> getSupplementaryResources() {
        final List<SupplementaryResource> resources = new ArrayList<SupplementaryResource>();
        final List<Node> nodes = XPathUtils.queryHTML(getHtml(), ".//x:li[contains(@class, 'ESIright_highlight_txt_red')]/x:a");
        for (final Node node : nodes) {
            final Element address = (Element) node;
            final String href = address.getAttributeValue("href");
            final String text = address.getValue();

            final URI url = URI.create(href);
            final String filePath = getFilePath(href);
            final ResourceId id = new ResourceId(articleId, filePath);
            final SupplementaryResource resource = new SupplementaryResource(id, url, filePath);
            resource.setLinkText(text.trim());
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
        fullTextResources.add(getFullTextHtml());
        fullTextResources.add(getFullTextPdf());
        return fullTextResources;
    }

    private FullTextResource getFullTextHtml() {
        final String url = getMetaElementContent("citation_fulltext_html_url");
        final FullTextResource fullText = new FullTextResource();
        fullText.setUrl(URI.create(url));
        fullText.setLinkText("HTML");
        return fullText;
    }

    private FullTextResource getFullTextPdf() {
        final String url = getMetaElementContent("citation_pdf_url");
        final FullTextResource fullText = new FullTextResource();
        fullText.setUrl(URI.create(url));
        fullText.setLinkText("PDF");
        return fullText;
    }

    private String getMetaElementContent(final String name) {
        return XPathUtils.getString(getHtml(), "/x:html/x:head//x:meta[@name='" + name + "']/@content");
    }


    public Element getTitleHtml() {
        final Element element = (Element) XPathUtils.getNode(getHtml(), ".//x:div[@class='article_chemsoc_txt_s13']/x:h1");
        final Element copy = new Element("h1", "http://www.w3.org/1999/xhtml");
        for (int i = 0; i < element.getChildCount(); i++) {
            copy.appendChild(element.getChild(i).copy());
        }
        if (copy.getChildCount() == 1) {
            final Node child = copy.getChild(0);
            if (child instanceof Text) {
                final Text text = (Text) child;
                text.setValue(text.getValue().trim());
            }
        }
        return copy;
    }

    public String getTitleHtmlString() {
        return toHtml(getTitleHtml());
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

    public Element getAbstractHtml() {
        final Element element = (Element) XPathUtils.getNode(getHtml(), ".//x:div[@class='abstract_new']/x:p");
        if (element == null) {
            // no abstract
            return null;
        }
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

    public String getAbstractHtmlString() {
        final Element xml = getAbstractHtml();
        return xml == null ? null : toHtml(xml);
    }

    private String normaliseEntityImage(final Element e) {
        final String src = e.getAttributeValue("src");
//        if ("/appl/literatum/publisher/achs/journals/entities/223C.gif".equals(src)) {
//            return "\u223c";    // TILDE OPERATOR
//        }
//        if ("/appl/literatum/publisher/achs/journals/entities/2009.gif".equals(src)) {
//            return "\u2009";    // THIN SPACE
//        }
//        if ("/appl/literatum/publisher/achs/journals/entities/2002.gif".equals(src)) {
//            return "\u2002";    // EN SPACE
//        }
//        if ("/appl/literatum/publisher/achs/journals/entities/2225.gif".equals(src)) {
//            return "\u2225";    // PARALLEL TO
//        }
//        if ("/appl/literatum/publisher/achs/journals/entities/22A5.gif".equals(src)) {
//            return "\u22A5";    // UP TACK
//        }
//        if ("/appl/literatum/publisher/achs/journals/entities/21C6.gif".equals(src)) {
//            return "\u21C6";    // LEFTWARDS ARROW OVER RIGHTWARDS ARROW
//        }
        throw new RuntimeException("Unknown entity: "+src);
    }

    @Override
    public List<String> getAuthors() {
        final List<String> authors = new ArrayList<String>();
        final List<Node> nodes = XPathUtils.queryHTML(getHtml(), "/x:html/x:head/x:meta[@name='citation_author']/@content");
        for (final Node node : nodes) {
            final Attribute attr = (Attribute) node;
            authors.add(attr.getValue());
        }
        return authors;
    }


}
