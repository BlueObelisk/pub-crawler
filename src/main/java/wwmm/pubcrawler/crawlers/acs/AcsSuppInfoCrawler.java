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
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.CrawlerRuntimeException;
import wwmm.pubcrawler.crawlers.AbstractArticleCrawler;
import wwmm.pubcrawler.httpcrawler.CrawlerGetRequest;
import wwmm.pubcrawler.httpcrawler.CrawlerPostRequest;
import wwmm.pubcrawler.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.FullTextResource;
import wwmm.pubcrawler.model.Reference;
import wwmm.pubcrawler.model.SupplementaryResource;
import wwmm.pubcrawler.types.Doi;
import wwmm.pubcrawler.types.MediaType;
import wwmm.pubcrawler.utils.BibtexTool;
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
 * The <code>AcsArticleCrawler</code> class uses a provided DOI to get
 * information about an article that is published in a journal of the
 * American Chemical Society.
 * </p>
 *
 * @author Nick Day
 * @author Sam Adams
 * @version 2
 */
public class AcsSuppInfoCrawler extends AbstractArticleCrawler {

    private static final Logger LOG = Logger.getLogger(AcsSuppInfoCrawler.class);

    private String articleId;

    public AcsSuppInfoCrawler(Article article, CrawlerContext context) throws IOException {
        super(article, context);
        this.articleId = article.getId();
    }

    @Override
    public Document fetchHtml(Article article) throws IOException {
        URI url = article.getSupplementaryResourceUrl();
        if (url != null) {
            return readHtml(url, article.getId()+"_supp.html", AGE_MAX);
        }
        return null;
    }

    @Override
    protected Logger log() {
        return LOG;
    }

    public String getArticleId() {
        return articleId;
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
        if (getHtml() != null) {
            List<Node> headingNodes = XPathUtils.queryHTML(getHtml(), ".//x:div[@id='supInfoBox']/x:h3");
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

                    SupplementaryResource supplementaryResource = new SupplementaryResource();
                    supplementaryResource.setContentType(mediaType == null ? title : mediaType.getName());
                    supplementaryResource.setUrl(resourceUrl);
                    supplementaryResource.setLinkText(linkText);
                    supplementaryResource.setFilePath(filepath);
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

    @Override
    public List<String> getAuthors() {
        return getArticleRef().getAuthors();
    }

}