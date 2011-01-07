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
package wwmm.pubcrawler.crawlers.acta;

import nu.xom.*;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.CrawlerRuntimeException;
import wwmm.pubcrawler.crawlers.AbstractArticleCrawler;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.FullTextResource;
import wwmm.pubcrawler.model.Reference;
import wwmm.pubcrawler.model.SupplementaryResource;
import wwmm.pubcrawler.utils.BibtexTool;
import wwmm.pubcrawler.utils.XHtml;
import wwmm.pubcrawler.utils.XPathUtils;
import wwmm.pubcrawler.httpcrawler.CrawlerPostRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.*;

/**
 * <p>
 * The <code>ActaArticleCrawler</code> class uses a provided DOI to get
 * information about an article that is published in a journal of Acta
 * Crytallographica.
 * </p>
 *
 * @author Nick Day
 * @author Sam Adams
 * @version 2
 */
public class ActaArticleCrawler extends AbstractArticleCrawler {

    private static final Logger LOG = Logger.getLogger(ActaArticleCrawler.class);

    private BibtexTool bibtex;

    public ActaArticleCrawler(Article article, CrawlerContext context) throws IOException {
        super(article, context);
        this.bibtex = fetchBibtex();
    }


    private BibtexTool fetchBibtex() throws IOException {
        String id = getId();
        String articleId = id.substring(id.lastIndexOf('/')+1);
        log().trace("fetching bibtex: "+articleId);

        CrawlerPostRequest request = new CrawlerPostRequest(
                URI.create("http://scripts.iucr.org/cgi-bin/biblio"),
                Arrays.asList(
                        new BasicNameValuePair("name", "saveas"),
                        new BasicNameValuePair("cnor", articleId),
                        new BasicNameValuePair("Action", "download")
                ), getArticleRef().getId()+"_bibtex.txt", AGE_MAX);

        String text = readString(request);
        return new BibtexTool(text);
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
        List<Node> nodes = XPathUtils.queryHTML(getHtml(), ".//x:a/x:img[@alt='Open access']");
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


    public Article toArticle() {
        Article article = super.toArticle();
        article.setTitleHtml(getTitleHtmlString());
        return article;
    }


    public Element getTitleHtml() {
        List<Node> nodes = XPathUtils.queryHTML(getHtml(), ".//x:div[@class='bibline']/following-sibling::x:h3[1]");
        if (nodes.size() != 1) {
            throw new RuntimeException("Nodes: "+nodes.size());
        }
        Element element = (Element) nodes.get(0);
        Element copy = new Element("h1", "http://www.w3.org/1999/xhtml");
        for (int i = 0; i < element.getChildCount(); i++) {
            copy.appendChild(element.getChild(i).copy());
        }
        removeSpanElements(copy);
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

    public List<String> getAuthors() {
        List<String> authors = new ArrayList<String>();
//        List<Node> nodes = Utils.queryHTML(html, ".//x:div[@class='bibline']/following-sibling::x:h3[2]/x:a");
//        for (Node node : nodes) {
//            String author = node.getValue();
//            author = author.replaceAll("  +", " ");
//            authors.add(author);
//        }
        Collections.addAll(authors, bibtex.getAuthors().split(" and "));
        return authors;
    }

    public String getJournalTitleAbbreviation() {
        Node bibnode = XPathUtils.getNode(getHtml(), ".//x:div[@class='bibline']");
        String journalTitle = XPathUtils.getString(bibnode, "./x:p/x:i");
        return journalTitle;
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
        List<SupplementaryResource> resources = new ArrayList<SupplementaryResource>();
        List<Node> nodes = XPathUtils.queryHTML(getHtml(), ".//x:div[@class='buttonlinks']/x:a");
        for (Node node : nodes) {
            Element address = (Element) node;
            Element img = address.getFirstChildElement("img", XHtml.NAMESPACE);
            String alt = img.getAttributeValue("alt");
            if (       "HTML version".equalsIgnoreCase(alt)
                    || "pdf version".equalsIgnoreCase(alt)
                    || "similar papers".equalsIgnoreCase(alt)
                    || "Open access".equalsIgnoreCase(alt)
                    || "cited in".equalsIgnoreCase(alt)
                    || "3d view".equalsIgnoreCase(alt)) {
                continue;
            }
            String href = address.getAttributeValue("href");
            URI url = getUrl().resolve(href);

            log().trace("resource: ["+href+"] "+alt);

//            if (isMultiCif(href)) {
//                continue;
//            }

            if (isCif(alt) && isMultiCif(href)) {
                try {
                    resources.addAll(getCifs(url));
                } catch (Exception e) {
                    throw new CrawlerRuntimeException("Error retrieving CIF links: "+ url, e);
                }
            } else if (isMultiFile(href)) {
                try {
                    resources.addAll(getResources(url));
                } catch (Exception e) {
                    throw new CrawlerRuntimeException("Error retrieving resource links: "+url, e);
                }
            } else {
                SupplementaryResource resource = new SupplementaryResource();
                resource.setLinkText(alt);
                resource.setUrl(url);
                if (href.contains("sendcif?")) {
                    resource.setFilePath(getCifFilePath(href));
                } else if (isCheckCif(href)) {
                    resource.setFilePath(getCheckCifFilePath(href));
                } else {
                    resource.setFilePath(href.substring(href.lastIndexOf('/')+1));
                }
                resources.add(resource);
            }
        }
        return resources;
    }

    private String getCheckCifFilePath(String href) {
        int i0 = href.indexOf("cnor=");
        int i1 = href.indexOf('&', i0);
        return href.substring(i0+5, i1)+"_checkcif.html";
    }

    private boolean isCheckCif(String href) {
        return href.endsWith("checkcif=yes");
    }

    private List<SupplementaryResource> getResources(URI url) throws IOException {
        List<SupplementaryResource> resources = new ArrayList<SupplementaryResource>();
        Document html = readHtml(url, getArticleId()+"_suppl.html", AGE_MAX);
        List<Node> nodes = XPathUtils.queryHTML(html, ".//x:td[@width='400']");
        for (Node node : nodes) {
            String href = XPathUtils.getString(node, "./x:a[./x:img[@alt='display file' or @alt='play file']]/@href");
            String contentType = XPathUtils.getString(node, "./x:p/x:b");
            String s = node.getValue();
            String linkText = s.substring(s.indexOf(']')+1).trim();
            int i = href.indexOf("file=");
            String filePath = href.substring(i+5, href.indexOf('&', i));

            SupplementaryResource resource = new SupplementaryResource();
            resource.setUrl(url.resolve(href));
            resource.setContentType(contentType);
            resource.setLinkText(linkText);
            resource.setFilePath(filePath);

            resources.add(resource);
        }
        return resources;
    }

    private List<SupplementaryResource> getCifs(URI url) throws IOException {
        List<SupplementaryResource> resources = new ArrayList<SupplementaryResource>();
        Document html = readHtml(url, getArticleId()+"_cifs.html", AGE_MAX);
        List<Node> nodes = XPathUtils.queryHTML(html, ".//x:ul/x:li");
        for (Node node : nodes) {
            Element li = (Element) node;
            Element address = li.getFirstChildElement("a", XHtml.NAMESPACE);
            String href = address.getAttributeValue("href");
            String linkText = address.getValue();
            int n = li.indexOf(address);
            StringBuilder s = new StringBuilder();
            for (int i = n+1; i < li.getChildCount(); i++) {
                s.append(li.getChild(i).getValue());
            }
            String contentType = s.toString().trim();
            String filePath = getCifFilePath(href);

            SupplementaryResource resource = new SupplementaryResource();
            resource.setUrl(URI.create(href));
            resource.setLinkText(linkText);
            resource.setContentType(contentType);
            resource.setFilePath(filePath);
            resources.add(resource);
        }
        return resources;
    }

    private String getCifFilePath(String href) {
        return href.substring(href.indexOf("sendcif?")+8)+".cif";
    }

    public List<FullTextResource> getFullTextResources() {
        List<FullTextResource> fullTextResources = new ArrayList<FullTextResource>();
        List<Node> links = XPathUtils.queryHTML(getHtml(), ".//x:a[x:img[contains(@alt, 'version')]]");
        for (Node link : links) {
            String href = XPathUtils.getString(link, "@href");
            String text = XPathUtils.getString(link, "x:img/@alt");
            FullTextResource fullText = new FullTextResource();
            fullText.setUrl(getUrl().resolve(href));
            fullText.setLinkText(text);
            fullTextResources.add(fullText);
        }
        return fullTextResources;
    }


    private boolean isMultiCif(String href) {
        String filename = href.substring(href.lastIndexOf('/')+1);
        return !filename.toLowerCase().contains("sup");
    }

    private boolean isCif(String alt) {
        return "cif file".equalsIgnoreCase(alt);
    }

    private boolean isMultiFile(String href) {
        return href.toLowerCase().contains("/sendsup?");
    }

    @Override
    protected String getArticleId() {
        return getArticleRef().getId();
    }

}
