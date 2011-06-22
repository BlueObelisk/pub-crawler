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
 * @version 2.0
 */
public class ChemSocJapanIssueCrawler extends AbstractIssueCrawler {

    private static final Logger LOG = Logger.getLogger(ChemSocJapanIssueCrawler.class);

    /**
	 * <p>Creates an instance of the ActaIssueCrawler class and
	 * specifies the issue to be crawled.</p>
	 *
	 * @param issue the issue to be crawled.
	 */
    public ChemSocJapanIssueCrawler(Issue issue, CrawlerContext context) throws IOException {
        super(issue, context);
    }

    @Override
    protected Logger log() {
        return LOG;
    }

    @Override
    public Issue getPreviousIssue() {
        // issues are retrieved from index
        return null;
    }

    @Override
    protected List<Node> getArticleNodes() {
        return XPathUtils.queryHTML(getHtml(), ".//x:table[x:tr/x:td[@class='title']]");
    }

    @Override
    protected ArticleId getArticleId(Node articleNode, IssueId issueId) {
        Doi doi = getArticleDoi(null, articleNode);
        return new ArticleId(issueId, doi.getSuffix());
    }

    @Override
    protected Doi getArticleDoi(Article article, Node context) {
        Node doiNode = XPathUtils.getNode(context, ".//x:a[starts-with(.,'Full Text')]/@href");
        String link = doiNode.getValue();
        int idx = link.indexOf("id=");
        String articleId = link.substring(idx+3).replaceAll("/", ".");
        return new Doi("10.1246/"+articleId);
    }

    @Override
    protected URI getArticleUrl(Article article, Node articleNode) {
        return null;
    }

    @Override
    protected URI getArticleSupportingInfoUrl(Article article, Node articleNode) {
        return null;
    }

    @Override
    protected String getArticleTitle(Article article, Node node) {
        return XPathUtils.getString(node, ".//x:td[@class='title']");
    }

    @Override
    protected String getArticleTitleHtml(Article article, Node articleNode) {
        return null;
    }

    @Override
    protected List<String> getArticleAuthors(Article article, Node node) {
        String s = XPathUtils.getString(node, ".//x:td[@class='authors']");
        return Arrays.asList(s.split(", and |, | and "));
    }

    @Override
    protected Reference getArticleReference(Article article, Node articleNode) {
        return null;
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
    public IssueId getIssueId() {
        return new IssueId("chemsocjapan/chem-lett/"+getVolume()+'/'+getNumber());
    }


    private String getBib(int i) {
        String s = XPathUtils.getString(getHtml(), ".//x:span[@class='augr']");
        if (s == null) {
            s = XPathUtils.getString(getHtml(), ".//x:font[starts-with(.,'Vol.')]");
            if (s == null) {
                throw new CrawlerRuntimeException("not found");
            }
        }
        // Vol. 38, No. 9 (September, 2009)
        Pattern p = Pattern.compile("Vol\\. (\\d+), No\\. (\\d+) +\\(\\S+ (\\d+)\\)");
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
