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

import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Node;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.joda.time.Duration;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerPostRequest;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerRequest;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.CrawlerRuntimeException;
import wwmm.pubcrawler.crawlers.AbstractIssueCrawler;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.types.Doi;
import wwmm.pubcrawler.utils.XPathUtils;

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
        CrawlerRequest request;
        if (issue.isCurrent()) {
            request = createIssueRequest(rscId, issue.getId()+".html", AGE_1DAY);
        } else {
            request = createIssueRequest(rscId, issue.getId()+".html", AGE_MAX);
        }
        Document doc = readHtml(request);
        log().trace("done");
        return doc;
    }

    @Override
    public String getIssueId() {
        return "rsc/" + getJournal().getAbbreviation() + '/' + getVolume() + '/' + getNumber();
    }

    private CrawlerRequest createIssueRequest(String issueId, String id, Duration maxAge) throws UnsupportedEncodingException {
        log().trace("fetching issue: "+issueId);
        CrawlerPostRequest request = new CrawlerPostRequest(
                URI.create("http://pubs.rsc.org/en/Journals/issues"),
                Arrays.asList(
                    new BasicNameValuePair("name", getJournal().getAbbreviation()),
                    new BasicNameValuePair("issueID", issueId),
                    new BasicNameValuePair("jname", getJournal().getFullTitle()),
                    new BasicNameValuePair("isArchive", "False"),
                    new BasicNameValuePair("issnprint", ""),
                    new BasicNameValuePair("isContentAvailable", "True")
                ), id, maxAge);
        return request;
    }

    @Override
    protected List<Node> getArticleNodes() {
        return XPathUtils.queryHTML(getHtml(), ".//x:input[@class='toCheck']/@id");
    }

    @Override
    protected Article getArticleDetails(Node context, String issueId) {
        Attribute attr = (Attribute) context;
        String id = attr.getValue();
        String articleId = issueId + '/' + id;

        Article article = new Article();
        article.setId(articleId);
        article.setDoi(new Doi(RSC_DOI_PREFIX + id));
        return article;
    }

    @Override
    public Issue getPreviousIssue() {
        String s = XPathUtils.getString(getHtml(), ".//x:a[@title='Previous Issue']/@href");
        if (s == null) {
            return null;
        }
        // TODO /en/Journals/Journal/AC?issueID=OB008999&issnprint=1359-7337
        Pattern p = Pattern.compile("(.+)(\\d{3})(\\d{3})$");
        Matcher m = p.matcher(s);
        if (!m.find()) {
            throw new CrawlerRuntimeException("No match: "+s);
        }
        String issueId = "rsc/"+m.group(1)+'/'+Integer.parseInt(m.group(2))+'/'+Integer.parseInt(m.group(3));
        Issue prev = new Issue();
        prev.setId(issueId);
        prev.setUrl(URI.create(s));
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
        Pattern p = Pattern.compile("IssueNo='(\\d+)'");
        Matcher m = p.matcher(s);
        if (!m.find()) {
            log().warn("Unable to locate issue number: "+getIdString());
            return "0";
        }
        return m.group(1);
    }

    public String getIdString() {
        String s = getScriptText();
        Pattern p = Pattern.compile("IssueId='([^']+)'");
        Matcher m = p.matcher(s);
        if (!m.find()) {
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

}
