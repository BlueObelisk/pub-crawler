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
package wwmm.pubcrawler.crawlers.acs;

import nu.xom.*;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerGetRequest;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerPostRequest;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
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
import wwmm.pubcrawler.utils.XPathUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
public class AcsArticleCrawler extends AbstractArticleCrawler {

    private static final Logger LOG = Logger.getLogger(AcsArticleCrawler.class);

    private final Document suppHtml;
    private final URI suppUrl;

    private final BibtexTool bibtex;

    private ArticleId articleId;

    public AcsArticleCrawler(Article article, CrawlerContext context) throws IOException {
        super(article, context);
        this.suppHtml = fetchSuppHtml();
        if (suppHtml == null) {
            this.suppUrl = null;
        } else {
            this.suppUrl = URI.create(suppHtml.getBaseURI());
        }
        this.articleId = article.getId();
        this.bibtex = fetchBibtex();
    }

    private BibtexTool fetchBibtex() throws IOException {
        Doi doi = getDoi();

        // This is necessary for correct cookies to be set...
        touchDownloadPage(doi);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("doi", doi.getValue()));
        params.add(new BasicNameValuePair("downloadFileName", "bibtex"));
        params.add(new BasicNameValuePair("direct", "true"));
        params.add(new BasicNameValuePair("format", "bibtex"));
        params.add(new BasicNameValuePair("include", "cit"));
        params.add(new BasicNameValuePair("submit", "Download Citation(s)"));

        URI url = URI.create("http://pubs.acs.org/action/downloadCitation");
        String bibtex = readStringPost(url, params, getArticleId(), "bibtex.txt", AGE_MAX);
        return new BibtexTool(bibtex);
    }
        
    private void touchDownloadPage(Doi doi) throws IOException {
        URI url = URI.create("http://pubs.acs.org/action/showCitFormats?doi="+doi.getValue());
        CrawlerGetRequest request = new CrawlerGetRequest(url, getArticleId()+"_bib_touch", AGE_MAX);
        touch(request);
    }


    @Override
    protected Logger log() {
        return LOG;
    }

    private Document fetchSuppHtml() throws IOException {
        URI supportingInfoUrl = getSupportingInfoUrl();
        if (supportingInfoUrl == null) {
            return null;
        }
        return readHtml(supportingInfoUrl, getArticleRef().getId(), "supp", AGE_MAX);
    }

    /**
	 * <p>
	 * Crawls the abstract webpage to find a link to a webpage listing the
	 * article supplementary files.
	 * </p>
	 *
	 * @return URL of the webpage listing the article supplementary files.
	 */
    private URI getSupportingInfoUrl() {
        String url = XPathUtils.getString(getHtml(), ".//x:a[@title='View Supporting Information']/@href");
        log().trace("Supporting Information URL: "+url);
        return url == null ? null : getUrl().resolve(url);
    }


    protected Document getSuppHtml() {
        return suppHtml;
    }

    protected URI getSuppUrl() {
        return suppUrl;
    }


    public ArticleId getArticleId() {
        return articleId;
    }

    public Element getTitleHtml() {
        Element element = (Element) XPathUtils.getNode(getHtml(), ".//x:h1[@class='articleTitle']");
        Element copy = new Element("h1", "http://www.w3.org/1999/xhtml");
        for (int i = 0; i < element.getChildCount(); i++) {
            copy.appendChild(element.getChild(i).copy());
        }
        return copy;
    }

    public Element getAbstractHtml() {
        Element element = (Element) XPathUtils.getNode(getHtml(), ".//x:p[@class='articleBody_abstractText']");
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

    private String normaliseEntityImage(Element e) {
        String src = e.getAttributeValue("src");
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
        List<String> authors = new ArrayList<String>();
        List<Node> nodes = XPathUtils.queryHTML(getHtml(), "/x:html/x:head/x:meta[@name='dc.Creator']/@content");
        for (Node node : nodes) {
            Attribute attr = (Attribute) node;
            authors.add(attr.getValue());
        }
        return authors;
    }

    public boolean isOpenAccess() {
        Node node = XPathUtils.getNode(getHtml(), ".//x:div[@id='articleIcons']/x:img[@alt='ACS AuthorChoice']");
        return node != null;
    }

    public Reference getReference() {
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


    public List<SupplementaryResource> getSupplementaryResources() {
        List<SupplementaryResource> supplementaryResources = new ArrayList<SupplementaryResource>();
        if (getSuppHtml() != null) {
            List<Node> headingNodes = XPathUtils.queryHTML(getSuppHtml(), ".//x:div[@id='supInfoBox']//x:h3");
            for (Node heading : headingNodes) {
                String title = heading.getValue();

                List<Node> fileNodes = XPathUtils.queryHTML(heading, "./following-sibling::x:ul[1]/x:li/x:a");
                for (Node file : fileNodes) {
                    Element address = (Element) file;
                    String href = address.getAttributeValue("href");
                    URI resourceUrl = getSuppUrl().resolve(href);
                    String linkText = address.getValue();

                    String filepath = getSuppFilePath(href);
                    ResourceId id = new ResourceId(articleId, filepath);
                    SupplementaryResource supplementaryResource = new SupplementaryResource(id, resourceUrl, filepath);
                    supplementaryResource.setContentType(title == null ? null : title.trim());
                    supplementaryResource.setLinkText(linkText == null ? null : linkText.trim());
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


    public List<FullTextResource> getFullTextResources() {
        List<FullTextResource> fullTextResources = new ArrayList<FullTextResource>();
        List<Node> links = XPathUtils.queryHTML(getHtml(), ".//x:div[@id='links']/x:ul[1]/x:li[1]/following-sibling::*/x:a");
        for (Node link : links) {
            Element address = (Element) link;
            String href = ((Element) link).getAttributeValue("href");
            String text = XPathUtils.getString(address, "./x:span[1]");
            FullTextResource fullText = new FullTextResource();
            fullText.setUrl(getUrl().resolve(href));
            fullText.setLinkText(text);
            fullTextResources.add(fullText);
        }
        return fullTextResources;
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


    public BibtexTool getBibtex() {
        return bibtex;
    }
    
}
