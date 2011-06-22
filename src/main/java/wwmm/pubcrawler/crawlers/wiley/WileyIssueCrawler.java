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

package wwmm.pubcrawler.crawlers.wiley;

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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Sam Adams
 */
public class WileyIssueCrawler extends AbstractIssueCrawler {

    private static final Logger LOG = Logger.getLogger(WileyIssueCrawler.class);

    public WileyIssueCrawler(Issue issue, Journal journal, CrawlerContext context) throws IOException {
        super(issue, journal, context);
    }

    @Override
    protected Logger log() {
        return LOG;
    }

    public String getJournalTitle() {
        return XPathUtils.getString(getHtml(), "//x:h1[@id='productTitle']").trim();
    }

    @Override
    protected String getVolume() {
        return XPathUtils.getString(getHtml(), "//x:span[@class='issueTocVolume']").substring(7);
    }

    @Override
    protected String getNumber() {
        return XPathUtils.getString(getHtml(), "//x:span[@class='issueTocIssue']").substring(6);
    }

    @Override
    protected String getYear() {
        String s = XPathUtils.getString(getHtml(), "//x:div[@id='metaData']/x:h2").trim();
        return s.substring(s.length()-4);
    }

    @Override
    protected List<Node> getArticleNodes() {
        return XPathUtils.queryHTML(getHtml(), "//x:ol[@id='issueTocGroups']//x:ol[@class='articles']/x:li");
    }

    @Override
    protected ArticleId getArticleId(Node articleNode, IssueId issueId) {
        Doi doi = getArticleDoi(null, articleNode);
        return new ArticleId(issueId, doi.getSuffix());
    }

    @Override
    protected Doi getArticleDoi(Article article, Node articleNode) {
        String s = XPathUtils.getString(articleNode, ".//x:input[@name='doi']/@value");
        if (s == null) {
            throw new CrawlerRuntimeException("Unable to find DOI in issue: "+getIssueId());
        }
        return new Doi(s);
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

    // /doi/10.1002/cmmi.v5:5/issuetoc
    // /doi/10.1002/ctpp.v50.10/issuetoc

    private static final Pattern P_PREV = Pattern.compile("(\\d+)\\.issue-(\\S+)");
    private static final Pattern P_PREV1 = Pattern.compile("\\.v(\\d+)[.:](\\d+)/");

    @Override
    protected Issue getPreviousIssue() {
        String href = XPathUtils.getString(getHtml(), "//x:a[@id='previousLink']/@href");
        if (href != null) {
            Matcher m = P_PREV.matcher(href);
            if (!m.find()) {
                m = P_PREV1.matcher(href);
                if (!m.find()) {
                    throw new CrawlerRuntimeException("Cannot locate prev issue ID: "+href);
                }
            }
            Issue issue = new Issue();
            issue.setId(new IssueId("wiley/"+getJournal().getAbbreviation()+"/"+m.group(1)+"/"+m.group(2)));
            issue.setUrl(getUrl().resolve(href));
            return issue;
        }
        return null;
    }

    @Override
    protected IssueId getIssueId() {
        return getIssueRef().getId();
    }

}
