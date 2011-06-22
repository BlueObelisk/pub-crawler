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

package wwmm.pubcrawler.crawlers.springer;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import org.apache.log4j.Logger;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.DefaultCrawlerContext;
import wwmm.pubcrawler.crawlers.AbstractCrawler;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.model.id.PublisherId;
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
public class SpringerPublicationCrawler extends AbstractCrawler {

    private static final Logger LOG = Logger.getLogger(SpringerPublicationCrawler.class);

    private static final String URL_BASE = "http://www.springerlink.com/journals/all/%s/";
    private static final List<String> KEYS = Collections.unmodifiableList(Arrays.asList(
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
            "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0-9"
    ));

    private List<Document> pages = new ArrayList<Document>();
    private static final Pattern P_JOURNAL_ID = Pattern.compile("/content/([^/]+)/");

    public SpringerPublicationCrawler(CrawlerContext context) throws IOException {
        super(context);

        for (String key : KEYS) {
            String u = String.format(URL_BASE, key);
            URI uri = URI.create(u);

            // Fetch pages
            int page = 1;
            while (uri != null) {
                Document html = this.readHtml(uri, new PublisherId("springer"), "journalindex-"+key+"-"+page, AGE_28DAYS);
                pages.add(html);
                List<Node> nodes = XPathUtils.queryHTML(html, "//x:a[text() = 'Next']");
                if (!nodes.isEmpty()) {
                    Element addr = (Element) nodes.get(0);
                    String href = addr.getAttributeValue("href");
                    uri = uri.resolve(href);
                    page++;
                } else {
                    uri = null;
                }
            }
        }

    }

    protected List<Document> getPages() {
        return pages;
    }

    public List<Journal> getJournals() {
        List<Journal> list = new ArrayList<Journal>();
        for (Document page : getPages()) {
            URI uri = URI.create(page.getBaseURI());
            getTitles(list, uri, page);
        }
        return list;
    }

    private void getTitles(List<Journal> list, URI uri, Document html) {
        List<Node> nodes = XPathUtils.queryHTML(html, "//x:p[@class='title']/x:a");
        for (Node node : nodes) {
            Element addr = (Element) node;
            String href = addr.getAttributeValue("href");
            URI url = uri.resolve(href);
            String title = addr.getValue().trim();

            Matcher m = P_JOURNAL_ID.matcher(href);
            if (!m.find()) {
                log().warn("Unable to find journal id: "+href);
                continue;
            }

            String id = m.group(1);

            Journal journal = new Journal(id, title);
            list.add(journal);
        }
    }

    @Override
    protected Logger log() {
        return LOG;
    }

    public static void main(String[] args) throws IOException {
        for (Journal journal : new SpringerPublicationCrawler(new DefaultCrawlerContext(null)).getJournals()) {
            System.out.println(journal.getFullTitle());
        }
    }

}
