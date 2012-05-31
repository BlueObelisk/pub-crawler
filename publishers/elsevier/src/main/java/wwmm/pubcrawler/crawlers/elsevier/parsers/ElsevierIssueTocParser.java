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

package wwmm.pubcrawler.crawlers.elsevier.parsers;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Text;
import org.apache.log4j.Logger;
import wwmm.pubcrawler.CrawlerRuntimeException;
import wwmm.pubcrawler.parsers.AbstractIssueParser;
import wwmm.pubcrawler.parsers.IssueTocParser;
import wwmm.pubcrawler.model.FullTextResource;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.SupplementaryResource;
import wwmm.pubcrawler.model.id.ArticleId;
import wwmm.pubcrawler.model.id.IssueId;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.types.Doi;
import wwmm.pubcrawler.utils.XPathUtils;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Sam Adams
 */
public class ElsevierIssueTocParser extends AbstractIssueParser implements IssueTocParser {

    private static final Logger LOG = Logger.getLogger(ElsevierIssueTocParser.class);

    private static final Pattern P_ID = Pattern.compile(".*/pii/(\\w+)");

    private static final Pattern VOLUME_ISSUE_PATTERN = Pattern.compile("Vol(?:ume)?s? (\\d+), Iss(?:ues?)? (\\S+),.*?\\(.*\\b(\\d{4})\\)");
    private static final Pattern VOLUME_PATTERN = Pattern.compile("Vol(?:ume)?s? (\\S+),.*?\\(.*\\b(\\d{4})\\)");

    // e.g. /science/journal/09254005/164/1
    private static final Pattern PREV_URL = Pattern.compile("/science/journal/([^/]+)/([^/]+)(?:/([^/]+))?");
    private static final Pattern P_PAGES = Pattern.compile("Pages? (\\S+)");

    public ElsevierIssueTocParser(final Document html, final URI url, final JournalId journalId) {
        super(html, url, journalId);
    }

    @Override
    protected Logger log() {
        return LOG;
    }

    @Override
    protected String findJournalTitle() {
        final String title = XPathUtils.getString(getHtml(), "/x:html/x:head/x:title");
        return title.substring(0, title.indexOf('|')).trim();
    }

    @Override
    protected String findVolume() {
        return getBib()[0];
    }

    @Override
    protected String findNumber() {
        return getBib()[1];
    }

    @Override
    protected String findYear() {
        return getBib()[2];
    }

    @Override
    protected List<Node> getArticleNodes() {
        return XPathUtils.queryHTML(getHtml(), "//x:table[@class='resultRow']//x:td[@width='95%' and @colspan='2']");
    }

    @Override
    protected ArticleId getArticleId(final Node context) {
        final Element addr = (Element) XPathUtils.getNode(context, "x:h3/x:a");
        final String href = addr.getAttributeValue("href");
        final Matcher m = P_ID.matcher(href);
        if (m.find()) {
            return new ArticleId(getJournalId(), m.group(1));
        } else {
            throw new CrawlerRuntimeException("No match for ID: "+href, getIssueId(), getUrl());
        }
    }

    @Override
    protected Doi getArticleDoi(final Node articleNode) {
        return null;
    }

    @Override
    protected URI getArticleUrl(final Node context) {
        final Element addr = (Element) XPathUtils.getNode(context, "x:h3/x:a");
        final String href = addr.getAttributeValue("href");
        return getUrl().resolve(href);
    }

    @Override
    protected URI getArticleSupportingInfoUrl(final Node articleNode) {
        return null;
    }

    @Override
    protected String getArticleTitle(final Node context) {
        final Element addr = (Element) XPathUtils.getNode(context, "x:h3");
        return removeDoubleQuotes(addr.getValue().trim());
    }

    private String removeDoubleQuotes(final String title) {
        if (title.startsWith("\"") && title.endsWith("\"")) {
            return title.substring(1, title.length() - 1);
        }
        return title;
    }

    @Override
    protected String getArticleTitleHtml(final Node articleNode) {
        return null;
    }

    @Override
    protected List<String> getArticleAuthors(final Node node) {
        // TODO e.g. italics in name
        // http://www.sciencedirect.com/science?_ob=PublicationURL&_tockey=%23TOC%2356481%232010%23999519996%232414745%23FLP%23&_cdi=56481&_pubType=J&_auth=y&_acct=C000053194&_version=1&_urlVersion=0&_userid=1495569&md5=499d6ade479e102277248101889f472a
        final Node n = XPathUtils.getNode(node, "./x:i[starts-with(text(), 'Page')]/following-sibling::x:br/following-sibling::text()");
        if (n != null) {
            final Text text = (Text) n;
            final String s = text.getValue();
            final List<String> authors = Arrays.asList(s.split(", "));
            for (ListIterator<String> it = authors.listIterator(); it.hasNext();) {
                final String author = it.next();
                it.set(removeDoubleQuotes(author));
            }
            return authors;
        }
        return Collections.emptyList();
    }

    @Override
    protected List<SupplementaryResource> getArticleSupplementaryResources(final ArticleId articleId, final Node articleNode) {
        return null;
    }

    @Override
    protected List<FullTextResource> getArticleFullTextResources(final Node articleNode) {
        return null;
    }

    @Override
    protected String findArticlePages(final Node node) {
        final String s = XPathUtils.getString(node, "x:i[starts-with(text(), 'Page')]");
        final Matcher m = P_PAGES.matcher(s);
        if (m.find()) {
            return m.group(1);
        }
        throw new CrawlerRuntimeException("Unable to find pages: "+s, getIssueId(), getUrl());
    }

    @Override
    public Issue getPreviousIssue() {
        final List<Node> nodes = XPathUtils.queryHTML(getHtml(), ".//x:a[@title='Previous volume/issue'][1]");
        if (!nodes.isEmpty()) {
            final Element addr = (Element) nodes.get(0);
            final String href = addr.getAttributeValue("href");
            final Matcher m = PREV_URL.matcher((href));
            if (m.matches()) {
                final String volume = m.group(2);
                final String number = m.group(3) != null ? m.group(3) : Issue.NULL_NUMBER;

                final Issue issue = new Issue();
                issue.setId(new IssueId(getJournalId(), volume, number));
                issue.setVolume(volume);
                issue.setNumber(number);
                issue.setUrl(getUrl().resolve(href));
                return issue;
            } else {
                LOG.warn("Error parsing previous issue link: " + href);
            }
        }
        return null;
    }

    private String[] getBib() {
        final String s = XPathUtils.getString(getHtml(), "/x:html/x:head/x:title");
        // Acta Histochemica, Volume 110, Issue 5, Pages 351-432 (8 September 2008
        // ... , Volume 101, Issue 8, Pages 657-738 (2010)

        // Chemometrics and Intelligent Laboratory Systems | Vol 112, Pgs 1-70, (15 March, 2012) | ScienceDirect.com
        // Volume 111, Issue 1, Pages 1-66 (15 February 2012)
        // <title>Chemometrics and Intelligent Laboratory Systems | Vol 111, Iss 1, Pgs 1-66, (15 February, 2012) | ScienceDirect.com</title>
        // EMC - Pediatría | Vol 46, Iss 4, Pgs 1-67, ,(2011) | ScienceDirect.com

        // Computers & Graphics | Vol 36, Iss 1, Pgs 1-48, (February, 2012) | ScienceDirect.com

        Matcher m = VOLUME_ISSUE_PATTERN.matcher(s);
        if (m.find()) {
            return new String[]{m.group(1), m.group(2), m.group(3)};
        }
        m = VOLUME_PATTERN.matcher(s);
        if (m.find()) {
            return new String[]{m.group(1), "-", m.group(2)};
        }
        throw new CrawlerRuntimeException("Unable to match volume/issue: "+s, getIssueId(), getUrl());
    }

}
