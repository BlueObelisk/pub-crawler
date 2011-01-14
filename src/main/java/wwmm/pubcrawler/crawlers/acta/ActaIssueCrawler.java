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

import nu.xom.Document;
import nu.xom.Node;
import org.apache.log4j.Logger;
import wwmm.pubcrawler.CrawlerRuntimeException;
import wwmm.pubcrawler.crawlers.AbstractIssueCrawler;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Issue;
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
 * @version 2.0
 */
public class ActaIssueCrawler extends AbstractIssueCrawler {

    private static final Logger LOG = Logger.getLogger(ActaIssueCrawler.class);

    private final Document headHtml;
    private URI headerUrl;

    /**
	 * <p>Creates an instance of the ActaIssueCrawler class and
	 * specifies the issue to be crawled.</p>
	 *
	 * @param issue the issue to be crawled.
	 */
    public ActaIssueCrawler(Issue issue, CrawlerContext context) throws IOException {
        super(issue, context);
        this.headHtml = getHeadHtml();
    }

    private Document getHeadHtml() throws IOException {
        this.headerUrl = getUrl().resolve("./isscontshdr.html");
        return readHtml(headerUrl, getIssueId()+"_head.html", AGE_MAX);
    }

    @Override
    protected Logger log() {
        return LOG;
    }

    @Override
    protected Document fetchHtml(Issue issue) throws IOException {
        return readHtml(issue.getUrl(), getIssueId(issue.getUrl())+".html", AGE_MAX);
    }


    @Override
    public Issue getPreviousIssue() {
        String href = XPathUtils.getString(headHtml, ".//x:a[./x:img/@alt='Previous']/@href");
        if (href == null) {
            return null;
        }
        URI url = headerUrl.resolve(href);
        url = url.resolve("./isscontsbdy.html");
        Issue issue = new Issue();
        issue.setId(getIssueId(url));
        issue.setUrl(url);
        return issue;
    }


    @Override
    public List<Article> getArticles() {
        String issueId = getIssueId();
        List<Article> articles = new ArrayList<Article>();
        List<Node> nodes = XPathUtils.queryHTML(getHtml(), ".//x:font[@size='2' and contains(.,'doi:10.1107/')]");
        if (nodes.isEmpty()) {
            nodes = XPathUtils.queryHTML(getHtml(), ".//x:a[contains(@href,'dx.doi.org/10.1107/')]");
        }
        for (Node node : nodes) {
            Doi doi = new Doi(node.getValue());
            String id = getArticleId(node);
            String articleId = issueId + '/' + id;

            Article article = new Article();
            article.setId(articleId);
            article.setDoi(doi);
            articles.add(article);
        }
        return articles;
    }

    private String getArticleId(Node node) {
        String idString = XPathUtils.getString(node, "ancestor::x:div/x:p/x:a[./x:img/@alt='[HTML version]']/@href");
//        String idString = XPathUtils.getString(node, "../../x:p/x:a[./x:img/@alt='[HTML version]']/@href");
        if (idString == null) {
            throw new CrawlerRuntimeException("not found");
        }
        Pattern p = Pattern.compile("/([^/]+)/index.html");
        Matcher m = p.matcher(idString);
        if (!m.find()) {
            throw new CrawlerRuntimeException("No match: "+idString);
        }
        return m.group(1);
    }

    @Override
    public String getIssueId() {
        return getIssueId(getUrl());
    }

    private String getIssueId(URI url) {
        // http://journals.iucr.org/e/issues/2011/01/00/isscontsbdy.html
        Pattern p = Pattern.compile("journals.iucr.org/([^/]+)/issues/(\\d+)/(\\d+)/(\\d+)");
        Matcher m = p.matcher(url.toString());
        if (!m.find()) {
            throw new CrawlerRuntimeException("Unable to parse URL: "+url);
        }
        return "acta/" + m.group(1) + '/' + m.group(2) + '/' + m.group(3) + '-' + m.group(4);
    }


    private String getBib(int i) {
        // Volume 66, Part 1 (February 2010)
//        String s = XPathUtils.getString(getHtml(), ".//x:h3[starts-with(., 'Volume')]");
        // http://journals.iucr.org/s/issues/2011/02/00/isscontsbdy.html
        // For publication in Volume 18, Part 2 (March 2011)
        String s = XPathUtils.getString(getHtml(), ".//x:h3[contains(., 'Volume') and contains(., 'Part')]");
        if (s == null) {
            throw new CrawlerRuntimeException("Volume info not found");
        }
        Pattern p = Pattern.compile("Volume (\\d+), Part (\\d+) +\\(\\S+ (\\d+)\\)");
        Matcher m = p.matcher(s);
        if (!m.find()) {
            throw new CrawlerRuntimeException("No match: "+s);
        }
        return m.group(i);
    }

    public String getVolume() {
        return getBib(1);
    }

    public String getNumber() {
        return getBib(2);
    }

    public String getYear() {
        return getBib(3);
    }
    
}
