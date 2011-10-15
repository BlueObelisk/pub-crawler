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
package wwmm.pubcrawler.crawlers.chemsocjapan;

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
import wwmm.pubcrawler.utils.BibtexTool;
import wwmm.pubcrawler.utils.XHtml;
import wwmm.pubcrawler.utils.XPathUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * The <code>ChemSocJapanArticleCrawler</code> class uses a provided DOI to get
 * information about an article that is published in a journal of the Chemical
 * Society of Japan
 * </p>
 *
 * @author Nick Day
 * @author Sam Adams
 * @version 2
 */
public class ChemSocJapanArticleCrawler extends AbstractArticleCrawler {

    private static final Logger LOG = Logger.getLogger(ChemSocJapanArticleCrawler.class);

    private Document suppHtml;
    private BibtexTool bibtex;

    public ChemSocJapanArticleCrawler(Article article, CrawlerContext context) throws IOException {
        super(article, context);
        this.bibtex = fetchBibtex();
        this.suppHtml = fetchSuppHtml();
    }

    private Document fetchSuppHtml() throws IOException {
        String href = XPathUtils.getString(getHtml(), ".//x:td[@align='center']/x:a[text()='Supplementary Materials']/@href");
        log().debug("Supp html: "+href);
        if (href == null) {
            return null;
        }
        URI url = getUrl().resolve(href);
        return readHtml(url, getArticleId(), "supp.html", AGE_MAX);
    }

    private Document fetchReferencesHtml() throws IOException {
        String href = XPathUtils.getString(getHtml(), ".//x:a[text()='References']/@href");
        if (href == null) {
            throw new CrawlerRuntimeException("not found");
        }
        URI url = getUrl().resolve(href);
        return readHtml(url, getArticleId(), "refs.html", AGE_MAX);
    }

    private BibtexTool fetchBibtex() throws IOException {
        String href = XPathUtils.getString(getHtml(), ".//x:a[text()='BibTeX']/@href");
        if (href == null) {
            throw new CrawlerRuntimeException("not found");
        }
        URI url = getUrl().resolve(href);
        String bibtex = readString(url, getArticleId(), "bibtex.txt", AGE_MAX);
        return new BibtexTool(bibtex);
    }


    public Element getTitleHtml() {
        Element td = (Element) XPathUtils.getNode(getHtml(), "//x:td/x:font[@size='+1']/x:b");

        Element copy = (Element) td.copy();
        copy.setLocalName("h1");
        return copy;
    }

    public String getTitleHtmlString() {
        return toHtml(getTitleHtml());
    }


    public Element getAbstractHtml() {
        Element td = (Element) XPathUtils.getNode(getHtml(), "//x:table[@cellpadding='2']//x:td/x:br/..");

        Element copy = (Element) td.copy();
        copy.getFirstChildElement("br", XHtml.NAMESPACE).detach();
        copy.setLocalName("p");
        while (copy.getAttributeCount() > 0) {
            copy.removeAttribute(copy.getAttribute(copy.getAttributeCount()-1));
        }
        return copy;
    }

    public String getAbstractHtmlString() {
        return toHtml(getAbstractHtml());
    }




    private String getId() {
        String id = XPathUtils.getString(getHtml(), ".//x:input[@name='cnor']/@value");
        if (id == null) {
            throw new CrawlerRuntimeException("Unable to locate article ID");
        }
        return id;
    }

    @Override
    protected Logger log() {
        return LOG;
    }


    public boolean isOpenAccess() {
        List<Node> nodes = XPathUtils.queryHTML(getHtml(), ".//x:img[@alt='OpenAccess']");
        return !nodes.isEmpty();
    }


    public Element getAbstract(Document html) {
        List<Node> nodes = XPathUtils.queryHTML(html, ".//x:p[x:b='Abstract:']");
        Element p = (Element) nodes.get(0);
        p = (Element) p.copy();

        removeAbstractPrefix(p);
        trimWhitespace(p);
        removeSpanElements(p);

        return p;
    }

    private void removeAbstractPrefix(Element p) {
        Element b = p.getFirstChildElement("b", "http://www.w3.org/1999/xhtml");
        b.detach();
    }

    private void trimWhitespace(Element p) {
        if (p.getChild(0) instanceof Text) {
            Text t = (Text) p.getChild(0);
            String value = t.getValue();
            if (value.startsWith(" ")) {
                t.setValue(value.substring(1));
            }
        }
    }

    private void removeSpanElements(Element p) {
        for (int i = 0; i < p.getChildCount(); i++) {
            Node child = p.getChild(i);
            if (child instanceof Element) {
                Element element = (Element) child;
                if ("span".equals(element.getLocalName())) {
                    element.detach();
                    while (element.getChildCount() > 0) {
                        Node n = element.getChild(0);
                        n.detach();
                        p.insertChild(n, i);
                        i++;
                    }
                    i--;
                }
            }
        }
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

    public List<String> getAuthors() {
        List<String> authors = new ArrayList<String>();
        Collections.addAll(authors, bibtex.getAuthors().split(" and "));
        return authors;
    }

    public Reference getReference() {
        Reference reference = new Reference();
        reference.setJournalTitle(bibtex.getValue("journal"));
        reference.setYear(bibtex.getValue("year"));
        reference.setVolume(bibtex.getValue("volume"));
        reference.setNumber(bibtex.getValue("number"));
        reference.setPages(bibtex.getValue("pages"));
        return reference;
    }

    public List<SupplementaryResource> getSupplementaryResources() {
        ArticleId articleId = getArticleId();

        List<SupplementaryResource> resources = new ArrayList<SupplementaryResource>();
        if (suppHtml != null) {
            List<Node> rows = XPathUtils.queryHTML(suppHtml, ".//x:th[text()='Supplementary Materials']/../following-sibling::*[.//x:a]");
            for (Node row : rows) {
                String linkText = XPathUtils.getString(row, "./x:td[1]");
                String href = XPathUtils.getString(row, "./x:td/x:a/@href");
                String contentType = XPathUtils.getString(row, "./x:td[2]");
                URI url = getUrl().resolve(href);

                String path = getFilePath(href);
                ResourceId id = new ResourceId(articleId, path);

                SupplementaryResource resource = new SupplementaryResource(id, url, path);
                resource.setLinkText(linkText.replace('\u00a0', ' ').trim());       // Trim &nbsp;
                resource.setContentType(contentType);
                resources.add(resource);
            }
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
        List<Node> links = XPathUtils.queryHTML(getHtml(), ".//x:td[@align='right']/x:a[contains(@href,'_pdf') and contains(.,'PDF')]");
        for (Node link : links) {
            String text = link.getValue().trim();
            String href = XPathUtils.getString(link, "@href");
            FullTextResource fullText = new FullTextResource();
            fullText.setUrl(getUrl().resolve(href));
            fullText.setLinkText(text);
            fullTextResources.add(fullText);
        }
        return fullTextResources;
    }

    @Override
    protected ArticleId getArticleId() {
        return getArticleRef().getId();
    }


    public String getJournalTitle() {
        return null;
    }
    
}
