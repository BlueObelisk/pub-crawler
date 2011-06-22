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

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import org.apache.log4j.Logger;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.CrawlerRuntimeException;
import wwmm.pubcrawler.crawlers.AbstractCrawler;
import wwmm.pubcrawler.journals.ChemSocJapanJournalIndex;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.model.id.IssueId;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.utils.XPathUtils;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Sam Adams
 */
public class ChemSocJapanIndexCrawler extends AbstractCrawler {

    private static final Logger LOG = Logger.getLogger(ChemSocJapanIndexCrawler.class);

    private Document html;
    private URI url;
    private Journal journal;

    public ChemSocJapanIndexCrawler(Journal journal, CrawlerContext context) throws IOException {
        super(context);
        this.journal = journal;
        this.html = fetchHtml();
        this.url = URI.create(html.getBaseURI());
    }


    @Override
    protected Logger log() {
        return LOG;
    }

    public Document getHtml() {
        return html;
    }

    public URI getUrl() {
        return url;
    }

    public Journal getJournal() {
        return journal;
    }


    private Document fetchHtml() throws IOException {
        URI url;
        if (ChemSocJapanJournalIndex.CHEMISTRY_LETTERS.equals(getJournal())) {
            url = URI.create("http://www.chemistry.or.jp/gakujutu/chem-lett/cl-cont/cl_list.html");
        } else {
            throw new CrawlerRuntimeException("Unsupported journal: "+journal.getFullTitle());
        }
        return readHtml(url, new JournalId("chemsocjapan/chem-lett"), "index", AGE_1DAY);
    }

    public List<Issue> getIssues() {
        Pattern pvol = Pattern.compile("Vol\\. (\\d+) \\((\\d{4})\\)");
        Pattern pnum = Pattern.compile("No\\. (\\d+)");

        List<Issue> issues = new ArrayList<Issue>();
        Node node = XPathUtils.getNode(getHtml(), ".//x:table[@class='link']//x:td");
        List<Node> titleNodes = XPathUtils.queryHTML(node, ".//x:b");
        for (Node titleNode : titleNodes) {
            Matcher mvol = pvol.matcher(titleNode.getValue());
            if (!mvol.find()) {
                throw new CrawlerRuntimeException("No match: "+titleNode.getValue());
            }
            String volume = mvol.group(1);
            String year = mvol.group(2);

            for (Node addressNode : XPathUtils.queryHTML(titleNode, "./following::x:a")) {
                Element address = (Element) addressNode;
                if (!"msc".equals(address.getAttributeValue("target"))) {
                    break;
                }
                String href = address.getAttributeValue("href");
                Matcher mnum = pnum.matcher(address.getValue());
                if (!mnum.find()) {
                    throw new CrawlerRuntimeException("No match: "+address.getValue());
                }
                String number = mnum.group(1);

                URI url = getUrl().resolve(href);

                Issue issue = new Issue();
                issue.setId(generateIssueId(volume, number));
                issue.setYear(year);
                issue.setVolume(volume);
                issue.setNumber(number);
                issue.setUrl(url);
                issues.add(issue);
            }
        }

        return issues;
    }

    private IssueId generateIssueId(String volume, String number) {
        return new IssueId("chemsocjapan/"+getJournal().getAbbreviation()+'/'+volume+'/'+number);
    }

}
