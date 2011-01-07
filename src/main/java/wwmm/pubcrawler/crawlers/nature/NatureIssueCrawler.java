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
package wwmm.pubcrawler.crawlers.nature;

import nu.xom.Node;
import org.apache.log4j.Logger;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.CrawlerRuntimeException;
import wwmm.pubcrawler.crawlers.AbstractIssueCrawler;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.types.Doi;
import wwmm.pubcrawler.utils.XPathUtils;

import java.io.IOException;
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
public class NatureIssueCrawler extends AbstractIssueCrawler {

    private static final Logger LOG = Logger.getLogger(NatureIssueCrawler.class);

    /**
	 * <p>Creates an instance of the AcsIssueCrawler class and
	 * specifies the issue to be crawled.</p>
	 *
	 * @param issue the issue to be crawled.
	 */
    public NatureIssueCrawler(Issue issue, CrawlerContext context) throws IOException {
        super(issue, context);
    }

    @Override
    protected Logger log() {
        return LOG;
    }

    @Override
    public String getIssueId() {
        return "nature/" + getJournalAbbreviation() + '/' + getVolume() + '/' + getNumber();
    }

    @Override
    public Issue getPreviousIssue() {
        String href = XPathUtils.getString(getHtml(), ".//x:a[text()='Previous']/@href");
        if (href == null) {
            return null;
        }
        Pattern p = Pattern.compile("/journal/v(\\d+)/n(\\d+)/");
        Matcher m = p.matcher(href);
        m.find();
        String volume = m.group(1);
        String number = m.group(2);
        Issue prev = new Issue();
        prev.setId("nature/"+getJournalAbbreviation()+"/"+volume+"/"+number);
        prev.setUrl(getUrl().resolve(href));
        return prev;
    }

    @Override
    public List<Article> getArticles() {
        String issueId = getIssueId();
        List<Article> idList = new ArrayList<Article>();
        List<Node> articleNodes = XPathUtils.queryHTML(getHtml(), ".//x:span[@class='doi']']");
        for (Node node : articleNodes) {
            String s = node.getValue();
            Doi doi = new Doi(s);
            String articleId = issueId + '/' + doi.getSuffix();

            Article article = new Article();
            article.setId(articleId);
            article.setDoi(doi);
            idList.add(article);
        }
        return idList;
    }

    private String getBiblio(int i) {
        String s = XPathUtils.getString(getHtml(), ".//x:h2[@class='issue']");
        if (s == null) {
            throw new CrawlerRuntimeException("not found");
        }
        // January 2011, Volume 3 No 1
        Pattern p = Pattern.compile("(\\d+), Volume (\\d+) No (\\d+)");
        Matcher m = p.matcher(s);
        if (!m.find()) {
            throw new CrawlerRuntimeException("No match: "+s);
        }
        return m.group(i);
    }

    public String getVolume() {
        return getBiblio(2);
    }

    public String getNumber() {
        return getBiblio(3);
    }

    public String getYear() {
        return getBiblio(1);
    }

    public String getJournalAbbreviation() {
        String s = getUrl().toString();
        Pattern p = Pattern.compile("nature.com/([^/]+)/journal/");
        Matcher m = p.matcher(s);
        m.find();
        return m.group(1);
    }


    @Override
    public Issue toIssue() {
        Issue issue = super.toIssue();
        issue.setUrl(getUrl());
        issue.setYear(getYear());
        issue.setVolume(getVolume());
        issue.setNumber(getNumber());
        return issue;
    }
    
}
