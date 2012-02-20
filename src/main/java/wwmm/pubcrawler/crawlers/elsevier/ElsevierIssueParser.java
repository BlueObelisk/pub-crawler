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

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Text;
import org.apache.log4j.Logger;
import wwmm.pubcrawler.CrawlerRuntimeException;
import wwmm.pubcrawler.crawlers.AbstractIssueParser;
import wwmm.pubcrawler.model.*;
import wwmm.pubcrawler.model.id.ArticleId;
import wwmm.pubcrawler.model.id.IssueId;
import wwmm.pubcrawler.types.Doi;
import wwmm.pubcrawler.utils.XPathUtils;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Sam Adams
 */
public class ElsevierIssueParser extends AbstractIssueParser {

    private static final Logger LOG = Logger.getLogger(ElsevierIssueParser.class);

    private static final Pattern P_ID = Pattern.compile("/pii/(.*?)\\?");
    private final Issue issueRef;

    public ElsevierIssueParser(final Issue issueRef, final Document html, final Journal journal) {
        super(html, issueRef.getUrl(), journal);
        this.issueRef = issueRef;
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
        return issueRef.getId();
    }

}
