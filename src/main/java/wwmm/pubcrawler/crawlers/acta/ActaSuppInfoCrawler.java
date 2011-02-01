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
package wwmm.pubcrawler.crawlers.acta;

import nu.xom.*;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import uk.ac.cam.ch.wwmm.httpcrawler.httpcrawler.CrawlerPostRequest;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.CrawlerRuntimeException;
import wwmm.pubcrawler.crawlers.AbstractArticleCrawler;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.FullTextResource;
import wwmm.pubcrawler.model.Reference;
import wwmm.pubcrawler.model.SupplementaryResource;
import wwmm.pubcrawler.utils.BibtexTool;
import wwmm.pubcrawler.utils.XPathUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class ActaSuppInfoCrawler extends AbstractArticleCrawler {

    private static final Logger LOG = Logger.getLogger(ActaSuppInfoCrawler.class);

//    private BibtexTool bibtex;

    public ActaSuppInfoCrawler(Article article, CrawlerContext context) throws IOException {
        super(article, context);
//        this.bibtex = fetchBibtex();
    }

    @Override
    public Document fetchHtml(Article article) throws IOException {
        return null;
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
        if (getHtml() == null) {
            return getArticleRef().getId();
        }
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
        if (getHtml() == null) {
            return false;
        }
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
        if (getHtml() == null) {
            return null;
        }
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
        Element e = getTitleHtml();
        return e == null ? null : toHtml(e);
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
        if (getHtml() == null) {
            return getArticleRef().getAuthors();
        }
        List<String> authors = XPathUtils.getStrings(getHtml(), ".//x:h3/x:a[contains(@href, 'author_name')]");
        return authors;
    }

    public String getJournalTitleAbbreviation() {
        Node bibnode = XPathUtils.getNode(getHtml(), ".//x:div[@class='bibline']");
        String journalTitle = XPathUtils.getString(bibnode, "./x:p/x:i");
        return journalTitle;
    }

    public Reference getReference() {
        if (getHtml() == null) {
            return getArticleRef().getReference();
        }
        Reference reference = new Reference();
        reference.setJournalTitle(getJournalTitle());
        reference.setYear(getYear());
        reference.setVolume(getVolume());
        reference.setNumber(getNumber());
        reference.setPages(getPages());
        return reference;
    }

    private String getPages() {
        String s = XPathUtils.getString(getHtml(), ".//x:div[@class='bibline']/x:p/x:b/following-sibling::text()[1]");
        Pattern p = Pattern.compile(", (\\w+)");
        Matcher m = p.matcher(s);
        if (!m.find()) {
            throw new CrawlerRuntimeException("No match: "+s);
        }
        return m.group(1);
    }

    private String getVolume() {
        return getBibline()[0];
    }

    private String getNumber() {
        return getBibline()[1];
    }

    private String getYear() {
        return getBibline()[2];
    }

    private String[] getBibline() {
        String s = XPathUtils.getString(getHtml(), "/x:html/x:body/x:h3[2]");
        Pattern p = Pattern.compile("Volume\\s+(\\d+),\\s+Part\\s+(\\d+)\\s+\\(\\S+\\s+(\\d+)\\)");
        Matcher m = p.matcher(s);
        if (!m.find()) {
            throw new CrawlerRuntimeException("No match: "+s);
        }
        return new String[]{m.group(1), m.group(2), m.group(3)};
    }

    private String getJournalTitle() {
        return XPathUtils.getString(getHtml(), "/x:html/x:body/x:h3[1]");
    }

    public List<SupplementaryResource> getSupplementaryResources() {
        if (getHtml() == null) {
            return getArticleRef().getSupplementaryResources();
        }
        List<Node> nodes = XPathUtils.queryHTML(getHtml(), ".//x:div[@class='buttonlinks']/x:a");
        ActaSuppInfoReader suppInfoReader = new ActaSuppInfoReader(getContext(), getArticleRef());
        return suppInfoReader.getSupplementaryResources(nodes, getUrl());
    }

    public List<FullTextResource> getFullTextResources() {
        if (getHtml() == null) {
            return getArticleRef().getFullTextResources();
        }
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



    @Override
    protected String getArticleId() {
        return getArticleRef().getId();
    }

}
