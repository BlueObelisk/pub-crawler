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

package wwmm.pubcrawler.crawlers.springer;

import nu.xom.Document;
import nu.xom.Node;
import org.apache.log4j.Logger;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.CrawlerRuntimeException;
import wwmm.pubcrawler.crawlers.AbstractIssueCrawler;
import wwmm.pubcrawler.model.*;
import wwmm.pubcrawler.model.id.ArticleId;
import wwmm.pubcrawler.model.id.IssueId;
import wwmm.pubcrawler.types.Doi;
import wwmm.pubcrawler.utils.XPathUtils;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>The <code>AcsIssueCrawler</code> class provides a method for obtaining
 * information about all articles from a particular issue of a journal
 * published by the American Chemical Society.</p>
 *
 * @author Nick Day
 * @author Sam Adams
 * @version 2
 */
public class SpringerIssueCrawler extends AbstractIssueCrawler {

    private static final Logger LOG = Logger.getLogger(SpringerIssueCrawler.class);

    private static final String RSC_DOI_PREFIX = "10.1039/";

    /**
     * <p>Creates an instance of the AcsIssueCrawler class and
     * specifies the issue to be crawled.</p>
     *
     * @param issue the issue to be crawled.
     */
    public SpringerIssueCrawler(Issue issue, CrawlerContext context) throws IOException {
        super(issue, context);
    }

    @Override
    protected Logger log() {
        return LOG;
    }

    @Override
    protected Document fetchHtml(Issue issue) throws IOException {
        URI url = issue.getUrl();
        Document doc = readHtml(url, issue.getId(), AGE_MAX);
        return doc;
    }

    @Override
    public IssueId getIssueId() {
        return new IssueId("springer/" + getIssn() + '/' + getVolume() + '/' + getNumber());
    }

    public String getIssn() {
        String url = getUrl().toString();
        Pattern p = Pattern.compile("springerlink\\.com/content/([^/]+)/");
        Matcher m = p.matcher(url);
        m.find();
        return m.group(1);
    }

    public String getJournalTitle() {
        String title = XPathUtils.getString(getHtml(), "//x:h1[@class='title']").trim();
        return title;
    }

    @Override
    protected List<Node> getArticleNodes() {
        return XPathUtils.queryHTML(getHtml(), "//x:li[contains(@class, 'journalArticle')]");
    }

//    @Override
//    protected Article initArticle(Node context, String issueId) {
//        Element li = (Element) context;
//        Article article = new Article();
//        article.setTitle(getArticleTitle(li));
//        article.setAuthors(getArticleAuthors(li));
//        article.setUrl(getArticleUrl(li));
//        article.setReference(getArticleReference(li));
//        return article;
//    }


    @Override
    protected ArticleId getArticleId(Node articleNode, IssueId issueId) {
        URI url = getArticleUrl(null, articleNode);
        String s = url.toString();
        int i0 = s.lastIndexOf('/');
        int i1 = s.lastIndexOf('/', i0-1);
        return new ArticleId(issueId, s.substring(i1+1, i0));
    }

    @Override
    protected Doi getArticleDoi(Article article, Node articleNode) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected URI getArticleUrl(Article article, Node context) {
        String u = XPathUtils.getString(context, "x:p[@class='title']/x:a/@href");
        return getUrl().resolve(u);
    }

    @Override
    protected URI getArticleSupportingInfoUrl(Article article, Node articleNode) {
        return null;
    }

    @Override
    protected String getArticleTitle(Article article, Node context) {
        return XPathUtils.getString(context, "x:p[@class='title']").trim().replaceAll("\\s+", " ");
    }

    @Override
    protected String getArticleTitleHtml(Article article, Node articleNode) {
        return null;
    }

    @Override
    protected List<String> getArticleAuthors(Article article, Node context) {
        List<Node> nodes = XPathUtils.queryHTML(context, "x:p[@class='authors']/x:a");
        List<String> authors = new ArrayList<String>();
        for (Node node : nodes) {
            authors.add(node.getValue().trim());
        }
        return authors;
    }

    @Override
    protected Reference getArticleReference(Article article, Node context) {
        String title = getJournalTitle();
        String volume = getVolume();
        String number = getNumber();

        String pages = XPathUtils.getString(context, "x:p[@class='contextTag']");
        Reference ref = new Reference();
        ref.setJournalTitle(title);
        ref.setVolume(volume);
        ref.setNumber(number);
        ref.setPages(pages);
        return ref;
    }

    @Override
    protected List<SupplementaryResource> getArticleSupplementaryResources(Article article, Node articleNode) {
        return null;
    }

    @Override
    protected List<FullTextResource> getArticleFullTextResources(Article article, Node articleNode) {
        return null;
    }

    @Override
    protected Issue getPreviousIssue() {
        return null;
    }

    @Override
    protected String getYear() {
        return getBib()[2];
    }

    @Override
    protected String getVolume() {
        return getBib()[0];
    }

    @Override
    protected String getNumber() {
        return getBib()[1];
    }

    protected String[] getBib() {
        // TODO Volume 48, Supplement 1 / January 2005
        String s = XPathUtils.getString(getHtml(), "//x:h2[@class='filters']");
        Pattern p = Pattern.compile("Volume (\\S+), (?:Numbers?|Supplement) (\\S+(?:\\s+-\\s*\\S)?) / .*? (\\d{4})");
        Matcher m = p.matcher(s);
        if (!m.find()) {
            throw new CrawlerRuntimeException("No match: "+s);
        }
        return new String[] {
                m.group(1), m.group(2), m.group(3)
        };
    }
}
