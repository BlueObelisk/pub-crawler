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

import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Text;
import org.apache.log4j.Logger;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.CrawlerRuntimeException;
import wwmm.pubcrawler.crawlers.AbstractIssueCrawler;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.Reference;
import wwmm.pubcrawler.utils.XPathUtils;

import java.io.IOException;
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
    
    public ElsevierIssueCrawler(Issue issue, CrawlerContext context) throws IOException {
        super(issue, context);
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
        return XPathUtils.queryHTML(getHtml(), "//x:table[@class='resultRow']//x:td[@width='95%']");
    }

    @Override
    protected Article getArticleDetails(Node context, String issueId) {
        Element addr = (Element) XPathUtils.getNode(context, "x:a");
        String href = addr.getAttributeValue("href");
        URI url = getUrl().resolve(href);
        String title = addr.getValue().trim();

        Reference ref = getArticleReference(context);
        Article article = new Article();
        article.setTitle(title);
        article.setAuthors(getArticleAuthors(context));
        article.setUrl(url);
        article.setReference(ref);
        return article;
    }

    private Reference getArticleReference(Node node) {
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

    private List<String> getArticleAuthors(Node node) {
        Node n = XPathUtils.getNode(node, "./x:i/following-sibling::x:br/following-sibling::text()");
        if (n != null) {
            Text text = (Text) n;
            String s = text.getValue();
            return Arrays.asList(s.split(", "));
        }
        return Collections.emptyList();
    }
    
    private String getArticlePages(Node node) {
        String s = XPathUtils.getString(node, "x:i");
        Pattern p = Pattern.compile("Pages? (\\S+)");
        Matcher m = p.matcher(s);
        if (!m.find()) {
            throw new CrawlerRuntimeException("No match: "+s);
        }
        return m.group(1);
    }

    @Override
    protected Issue getPreviousIssue() {
        Node node = XPathUtils.getNode(getHtml(), ".//x:a[@title='Previous volume/issue'][1]");
        if (node != null) {
            Element addr = (Element) node;
            String href = addr.getAttributeValue("href");
            Issue issue = new Issue();
            issue.setUrl(getUrl().resolve(href));
            return issue;
        }
        return null;
    }

    private String[] getBib() {
        String s = XPathUtils.getString(getHtml(), "/x:html/x:head/x:title");
        Pattern p = Pattern.compile("Volume (\\d+), Issue (\\d+), .*? \\(\\S+ (\\d{4})\\)");
        Matcher m = p.matcher(s);
        if (!m.find()) {
            throw new CrawlerRuntimeException("No match: "+s);
        }
        return new String[]{m.group(1), m.group(2), m.group(3)};
    }


    @Override
    protected String getIssueId() {
        return null;
    }


}
