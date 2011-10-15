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
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.joda.time.Duration;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerPostRequest;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerRequest;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.CrawlerRuntimeException;
import wwmm.pubcrawler.crawlers.AbstractIssueCrawler;
import wwmm.pubcrawler.model.*;
import wwmm.pubcrawler.model.id.ArticleId;
import wwmm.pubcrawler.model.id.Id;
import wwmm.pubcrawler.model.id.IssueId;
import wwmm.pubcrawler.types.Doi;
import wwmm.pubcrawler.utils.XPathUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Arrays;
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
public class RscIssueCrawler extends AbstractIssueCrawler {

    private static final Logger LOG = Logger.getLogger(RscIssueCrawler.class);

    private static final String RSC_DOI_PREFIX = "10.1039/";

    /**
     * <p>Creates an instance of the AcsIssueCrawler class and
     * specifies the issue to be crawled.</p>
     *
     * @param issue the issue to be crawled.
     */
    public RscIssueCrawler(Issue issue, Journal journal, CrawlerContext context) throws IOException {
        super(issue, journal, context);
    }

    @Override
    protected Logger log() {
        return LOG;
    }

    @Override
    protected Document fetchHtml(Issue issue) throws IOException {
        String rscId = issue.getUrl().toString();

        Duration maxAge;
        CrawlerRequest request;
        if (issue.isCurrent()) {
            maxAge = AGE_1DAY;
        } else {
            maxAge = AGE_MAX;
        }

        Document doc = readHtmlPost(
                URI.create("http://pubs.rsc.org/en/journals/issues"),
                Arrays.asList(
                    new BasicNameValuePair("name", getJournal().getAbbreviation().toUpperCase()),
                    new BasicNameValuePair("issueid", rscId),
                    new BasicNameValuePair("jname", ""),    // getJournal().getTitle()),
                    new BasicNameValuePair("isarchive", "False"),
                    new BasicNameValuePair("issnprint", ""),
                    new BasicNameValuePair("issnonline", ""),
                    new BasicNameValuePair("iscontentavailable", "True")
                ), issue.getId(), "toc.html", maxAge);

        log().trace("done");
        return doc;
    }

    @Override
    public IssueId getIssueId() {
        return new IssueId(getJournal().getId(), getVolume(), getNumber());
    }


    @Override
    protected List<Node> getArticleNodes() {
        return XPathUtils.queryHTML(getHtml(), ".//x:input[@class='toCheck']/@id");
    }


    @Override
    protected ArticleId getArticleId(Node context, IssueId issueId) {
        Attribute attr = (Attribute) context;
        String id = attr.getValue();
        return new ArticleId(issueId, id);
    }

    @Override
    protected Doi getArticleDoi(Article article, Node context) {
        Attribute attr = (Attribute) context;
        String id = attr.getValue();
        return new Doi(RSC_DOI_PREFIX + id);
    }

    @Override
    protected URI getArticleUrl(Article article, Node articleNode) {
        // TODO
        return null;
    }

    @Override
    protected URI getArticleSupportingInfoUrl(Article article, Node articleNode) {
        // TODO
        return null;
    }

    @Override
    protected String getArticleTitle(Article article, Node articleNode) {
        // TODO
        return null;
    }

    @Override
    protected String getArticleTitleHtml(Article article, Node articleNode) {
        // TODO
        return null;
    }

    @Override
    protected List<String> getArticleAuthors(Article article, Node articleNode) {
        // TODO
        return null;
    }

    @Override
    protected Reference getArticleReference(Article article, Node articleNode) {
        // TODO
        return null;
    }

    @Override
    protected List<SupplementaryResource> getArticleSupplementaryResources(Article article, Node articleNode) {
        // TODO
        return null;
    }

    @Override
    protected List<FullTextResource> getArticleFullTextResources(Article article, Node articleNode) {
        // TODO
        return null;
    }

    @Override
    public Issue getPreviousIssue() {
        Element node = (Element) XPathUtils.getNode(getHtml(), ".//x:a[@title='Previous Issue']");
        if (node == null) {
            return null;
        }
        String s = node.getAttributeValue("href");
        // /en/journals/journal/cc?issueid=cc047024&amp;issnprint=1359-7345
        Pattern p = Pattern.compile("\\b([a-z]+)(\\d{3})(\\d{3})($|&issnprint)", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(s);
        if (!m.find()) {
            throw new CrawlerRuntimeException("No match: ["+s+"]");
        }
        String issueId = "rsc/"+m.group(1)+'/'+Integer.parseInt(m.group(2))+'/'+Integer.parseInt(m.group(3));
        Issue prev = new Issue();
        prev.setId(new IssueId(issueId));
        if (node.getAttributeValue("url") != null) {
            prev.setUrl(URI.create(node.getAttributeValue("url")));
        } else {
            prev.setUrl(URI.create(m.group(1) + m.group(2) + m.group(3)));
        }
        return prev;
    }

    private String getScriptText() {
        return XPathUtils.getString(getHtml(), ".//x:script");
    }


    public String getVolume() {
        Pattern p = Pattern.compile("(.+)(\\d{3})(\\d{3})$");
        Matcher m = p.matcher(getIdString());
        if (!m.find()) {
            throw new CrawlerRuntimeException("No match: "+getIdString());
        }
        return String.valueOf(Integer.parseInt(m.group(2)));
    }

    public String getNumber() {
        String s = getScriptText();
        Pattern p = Pattern.compile("IssueNo='(\\d+(-\\d+)?)'");
        Matcher m = p.matcher(s);
        if (!m.find()) {
            log().warn("Unable to locate issue number: "+s);
            return "0";
        }
        return m.group(1);
    }

    public String getIdString() {
        String s = getScriptText();
        Pattern p = Pattern.compile("IssueId='([^']+)'");
        Matcher m = p.matcher(s);
        if (!m.find()) {
            File file = new File("rsc.html");
            try {
                Serializer ser = new Serializer(new FileOutputStream(file));
                ser.write(getHtml());
            } catch (Exception e) {
                e.printStackTrace();
            }
            throw new CrawlerRuntimeException("No match: "+s);
        }
        return m.group(1);
    }

    public String getYear() {
        String s = getScriptText();
        Pattern p = Pattern.compile("SubYear='([^']+)'");
        Matcher m = p.matcher(s);
        m.find();
        return m.group(1);
    }

    @Override
    protected URI getUrl() {
        return getIssueRef().getUrl();
    }

}
