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

package wwmm.pubcrawler.crawlers.elsevier;

import nu.xom.*;
import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerPostRequest;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerRequest;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.CrawlerRuntimeException;
import wwmm.pubcrawler.crawlers.AbstractIssueCrawler;
import wwmm.pubcrawler.model.*;
import wwmm.pubcrawler.model.id.ArticleId;
import wwmm.pubcrawler.model.id.IssueId;
import wwmm.pubcrawler.types.Doi;
import wwmm.pubcrawler.utils.XPathUtils;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Sam Adams
 */
public class ElsevierIssueCrawler extends AbstractIssueCrawler {

    private static final Logger LOG = Logger.getLogger(ElsevierIssueCrawler.class);

    private boolean fetchBibtex = true;

    private final String bibtex;

    public ElsevierIssueCrawler(Issue issue, Journal journal, CrawlerContext context) throws IOException {
        super(issue, journal, context);
         this.bibtex = fetchBibtex();
    }

    @Override
    protected Document readHtml(CrawlerRequest request) throws IOException {
        CrawlerResponse response = getHttpCrawler().execute(request);
        String s;
        try {
            String encoding = getEntityCharset(response);
            if (encoding != null) {
                s = IOUtils.toString(response.getContent(), encoding);
            } else {
                s = IOUtils.toString(response.getContent());
            }
        } finally {
            response.closeQuietly();
        }

        // Fix broken DTD!
        s = s.replace("_http://www.w3.org/TR/html4/loose.dtd", "http://www.w3.org/TR/html4/loose.dtd");

        try {
            Builder builder = newTagSoupBuilder();
            Document doc = builder.build(new StringReader(s));
            setDocBaseUrl(response, doc);
            return doc;
        } catch (ParsingException e) {
            throw new IOException("Error reading XML", e);
        }
    }

    private String fetchBibtex() throws IOException {
        if (!fetchBibtex) {
            return null;
        }

        Document html = getDownloadPage();

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        List<Node> fields = XPathUtils.queryHTML(html, "//x:form[@name='exportCite']/x:input");
        for (Node node : fields) {
            Element input = (Element)node;
            String name = input.getAttributeValue("name");
            String value = input.getAttributeValue("value");
            params.add(new BasicNameValuePair(name, value));
        }
        params.add(new BasicNameValuePair("format", "cite-abs"));
        params.add(new BasicNameValuePair("citation-type", "BIBTEX"));
        params.add(new BasicNameValuePair("Export", "Export"));

        URI url = URI.create("http://www.sciencedirect.com/science");

        String s = readStringPost(url, params, getIssueId(), "bibtex.txt", AGE_MAX);
        return s;
    }

    private Document getDownloadPage() throws IOException {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        List<Node> fields = XPathUtils.queryHTML(getHtml(), "//x:form[@name='Tag']/x:input");
        for (Node node : fields) {
            Element input = (Element)node;
            String name = input.getAttributeValue("name");
            String value = input.getAttributeValue("value");
            params.add(new BasicNameValuePair(name, value));
        }
        params.add(new BasicNameValuePair("export", "Export citations"));
        params.add(new BasicNameValuePair("pdfDownload", ""));

        List<Node> articles = XPathUtils.queryHTML(getHtml(), "//x:form[@name='Tag']//x:input[@name='art']");
        for (Node node : articles) {
            Element input = (Element)node;
            String articleId = input.getAttributeValue("value");
            params.add(new BasicNameValuePair("art", articleId));
        }

        String query = URLEncodedUtils.format(params, "UTF-8");

        URI url = URI.create("http://www.sciencedirect.com/science?"+query);
        Document html = readHtml(url, getIssueId(), "bibtex.html", AGE_MAX);
        return html;
    }

    @Override
    protected Logger log() {
        return LOG;
    }

    public String getJournalTitle() {
        return XPathUtils.getString(getHtml(), "//x:span[@class='pubTitle']").trim();
    }

    @Override
    protected String getVolume() {
        return getBib()[0];
    }

    @Override
    protected String getNumber() {
        return getBib()[1];
    }

    @Override
    protected String getYear() {
        return getBib()[2];
    }

    @Override
    protected List<Node> getArticleNodes() {
        return XPathUtils.queryHTML(getHtml(), "//x:table[@class='resultRow']//x:td[@width='95%' and @colspan='2']");
    }

//    private static final Pattern P_ID = Pattern.compile("udi=(.*?)&");

    private static final Pattern P_ID = Pattern.compile("/pii/(.*?)\\?");

//    @Override
//    protected Article initArticle(Node context, String issueId) {
//        Article article = new Article();
//
//        Element addr = (Element) XPathUtils.getNode(context, "x:a");
//        String href = addr.getAttributeValue("href");
//        Matcher m = P_ID.matcher(href);
//        if (m.find()) {
//            String id = getIssueId()+"/"+m.group(1);
//            article.setId(id);
//        } else {
//            log().warn("Unable to locate article ID: "+getIssueId()+" ["+href+"]");
//        }
//
//        URI url = getUrl().resolve(href);
//        String title = addr.getValue().trim();
//
//        Reference ref = getArticleReference(context);
//        article.setTitle(title);
//        article.setAuthors(getArticleAuthors(context));
//        article.setUrl(url);
//        article.setReference(ref);
//        return article;
//    }


    @Override
    protected ArticleId getArticleId(Node context, IssueId issueId) {
        Element addr = (Element) XPathUtils.getNode(context, "x:a");
        String href = addr.getAttributeValue("href");
        Matcher m = P_ID.matcher(href);
        if (m.find()) {
            return new ArticleId(getIssueId(), m.group(1));
        } else {
            throw new CrawlerRuntimeException("No match for ID: "+href);
        }
    }

    @Override
    protected Doi getArticleDoi(Article article, Node articleNode) {
        return null;
    }

    @Override
    protected URI getArticleUrl(Article article, Node context) {
        Element addr = (Element) XPathUtils.getNode(context, "x:a");
        String href = addr.getAttributeValue("href");
        return getUrl().resolve(href);
    }

    @Override
    protected URI getArticleSupportingInfoUrl(Article article, Node articleNode) {
        return null;
    }

    @Override
    protected String getArticleTitle(Article article, Node context) {
        Element addr = (Element) XPathUtils.getNode(context, "x:a");
        return addr.getValue().trim();
    }

    @Override
    protected String getArticleTitleHtml(Article article, Node articleNode) {
        return null;
    }

    @Override
    protected List<String> getArticleAuthors(Article article, Node node) {
        // TODO e.g. italics in name
        // http://www.sciencedirect.com/science?_ob=PublicationURL&_tockey=%23TOC%2356481%232010%23999519996%232414745%23FLP%23&_cdi=56481&_pubType=J&_auth=y&_acct=C000053194&_version=1&_urlVersion=0&_userid=1495569&md5=499d6ade479e102277248101889f472a
        Node n = XPathUtils.getNode(node, "./x:i[starts-with(text(), 'Page')]/following-sibling::x:br/following-sibling::text()");
        if (n != null) {
            Text text = (Text) n;
            String s = text.getValue();
            return Arrays.asList(s.split(", "));
        }
        return Collections.emptyList();
    }

    @Override
    protected Reference getArticleReference(Article article, Node node) {
        String journalTitle = getJournalTitle();
        String volume = getVolume();
        String number = getNumber();

        String pages = getArticlePages(node);
        Reference ref = new Reference();
        ref.setJournalTitle(journalTitle);
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



    private String getArticlePages(Node node) {
        String s = XPathUtils.getString(node, "x:i[starts-with(text(), 'Page')]");
        Pattern p = Pattern.compile("Pages? (\\S+)");
        Matcher m = p.matcher(s);
        if (!m.find()) {
            throw new CrawlerRuntimeException("No match: "+s);
        }
        return m.group(1);
    }

    @Override
    protected Issue getPreviousIssue() {
        List<Node> nodes = XPathUtils.queryHTML(getHtml(), ".//x:a[@title='Previous volume/issue'][1]");
        if (!nodes.isEmpty()) {
            Element addr = (Element) nodes.get(0);
            String href = addr.getAttributeValue("href");
            Issue issue = new Issue();
            issue.setId(new IssueId("elsevier/"+getJournal().getAbbreviation()+"/"+getVolume()+"/"+getNumber()+"_prev"));
            issue.setUrl(getUrl().resolve(href));
            return issue;
        }
        return null;
    }

    private String[] getBib() {
        String s = XPathUtils.getString(getHtml(), "/x:html/x:head/x:title");
        // Acta Histochemica, Volume 110, Issue 5, Pages 351-432 (8 September 2008
        // ... , Volume 101, Issue 8, Pages 657-738 (2010)
        Pattern p = Pattern.compile("Volume (\\d+), Issues? (\\S+), .*? \\(.*\\b(\\d{4})\\)");
        Matcher m = p.matcher(s);
        if (!m.find()) {
            throw new CrawlerRuntimeException("No match: "+s);
        }
        return new String[]{m.group(1), m.group(2), m.group(3)};
    }


    @Override
    protected IssueId getIssueId() {
        return getIssueRef().getId();
    }

}
