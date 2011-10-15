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
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.CrawlerRuntimeException;
import wwmm.pubcrawler.crawlers.AbstractArticleCrawler;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.FullTextResource;
import wwmm.pubcrawler.model.Reference;
import wwmm.pubcrawler.model.SupplementaryResource;
import wwmm.pubcrawler.model.id.ArticleId;
import wwmm.pubcrawler.model.id.ResourceId;
import wwmm.pubcrawler.types.Doi;
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

    public RscArticleCrawler(Article article, CrawlerContext context) throws IOException {
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
        Node head = XPathUtils.getNode(getHtml(), "/x:html/x:head");

        String journalName = XPathUtils.getString(head, ".//x:meta[@name='citation_journal_title']/@content");
        String volume = XPathUtils.getString(head, ".//x:meta[@name='citation_volume']/@content");
        String issue = XPathUtils.getString(head, ".//x:meta[@name='citation_issue']/@content");
        String firstPage = XPathUtils.getString(head, ".//x:meta[@name='citation_firstpage']/@content");
        String lastPage = XPathUtils.getString(head, ".//x:meta[@name='citation_lastpage']/@content");
        String pages = firstPage+'-'+lastPage;

        String date = XPathUtils.getString(head, ".//x:meta[@name='citation_date']/@content");
        String year = date.substring(0, date.indexOf('/'));

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
        List<SupplementaryResource> resources = new ArrayList<SupplementaryResource>();
        List<Node> nodes = XPathUtils.queryHTML(getHtml(), ".//x:li[contains(@class, 'ESIright_highlight_txt_red')]/x:a");
        for (Node node : nodes) {
            Element address = (Element) node;
            String href = address.getAttributeValue("href");
            String text = address.getValue();

            URI url = URI.create(href);
            String filePath = getFilePath(href);
            ResourceId id = new ResourceId(articleId, filePath);
            SupplementaryResource resource = new SupplementaryResource(id, url, filePath);
            resource.setLinkText(text.trim());
            resources.add(resource);
        }
        return resources;
    }

    private String getFilePath(String href) {
        int i = href.lastIndexOf('/');
        return href.substring(i+1);
    }


    @Override
    public List<FullTextResource> getFullTextResources() {
        List<FullTextResource> fullTextResources = new ArrayList<FullTextResource>();
        fullTextResources.add(getFullTextHtml());
        fullTextResources.add(getFullTextPdf());
        return fullTextResources;
    }

    private FullTextResource getFullTextHtml() {
        String url = getMetaElementContent("citation_fulltext_html_url");
        FullTextResource fullText = new FullTextResource();
        fullText.setUrl(URI.create(url));
        fullText.setLinkText("HTML");
        return fullText;
    }

    private FullTextResource getFullTextPdf() {
        String url = getMetaElementContent("citation_pdf_url");
        FullTextResource fullText = new FullTextResource();
        fullText.setUrl(URI.create(url));
        fullText.setLinkText("PDF");
        return fullText;
    }

    private String getMetaElementContent(String name) {
        return XPathUtils.getString(getHtml(), "/x:html/x:head//x:meta[@name='" + name + "']/@content");
    }


    public Element getTitleHtml() {
        Element element = (Element) XPathUtils.getNode(getHtml(), ".//x:div[@class='article_chemsoc_txt_s13']/x:h1");
        Element copy = new Element("h1", "http://www.w3.org/1999/xhtml");
        for (int i = 0; i < element.getChildCount(); i++) {
            copy.appendChild(element.getChild(i).copy());
        }
        if (copy.getChildCount() == 1) {
            Node child = copy.getChild(0);
            if (child instanceof Text) {
                Text text = (Text) child;
                text.setValue(text.getValue().trim());
            }
        }
        return copy;
    }

    public String getTitleHtmlString() {
        return toHtml(getTitleHtml());
    }

    private String toHtml(Element element) {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            Serializer ser = new Serializer(bytes, "UTF-8") {
                @Override
                protected void writeXMLDeclaration() {
                    // no decl
                }
            };
            ser.write(new Document(element));

            String s = bytes.toString("UTF-8");
            return s.trim();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Element getAbstractHtml() {
        Element element = (Element) XPathUtils.getNode(getHtml(), ".//x:div[@class='abstract_new']/x:p");
        if (element == null) {
            // no abstract
            return null;
        }
        Element copy = new Element("p", "http://www.w3.org/1999/xhtml");
        for (int i = 0; i < element.getChildCount(); i++) {
            Node child = element.getChild(i);
            if (child instanceof Element) {
                Element e = (Element) child;
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
        Element xml = getAbstractHtml();
        return xml == null ? null : toHtml(xml);
    }

    private String normaliseEntityImage(Element e) {
        String src = e.getAttributeValue("src");
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

    public List<String> getAuthors() {
        List<String> authors = new ArrayList<String>();
        List<Node> nodes = XPathUtils.queryHTML(getHtml(), "/x:html/x:head/x:meta[@name='citation_author']/@content");
        for (Node node : nodes) {
            Attribute attr = (Attribute) node;
            authors.add(attr.getValue());
        }
        return authors;
    }


}
