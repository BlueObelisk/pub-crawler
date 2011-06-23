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

import nu.xom.Document;
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
import java.util.List;

/**
 * @author Sam Adams
 */
public class WileyPublicationCrawler extends AbstractCrawler {

    private static final Logger LOG = Logger.getLogger(WileyPublicationCrawler.class);

    private static final URI PUBS_URI = URI.create("http://onlinelibrary.wiley.com/browse/publications?type=journal&&start=1&resultsPerPage=10000");

    private final Document html;
    private final URI url;

    public WileyPublicationCrawler(CrawlerContext context) throws IOException {
        super(context);
        this.html = readHtml(PUBS_URI, new PublisherId("wiley"), "journal-index", AGE_28DAYS);
        this.url = URI.create(html.getBaseURI());
    }

    @Override
    protected Logger log() {
        return LOG;
    }

    public List<Journal> getJournals() {
        List<Journal> list = new ArrayList<Journal>();

        List<Node> nodes = XPathUtils.queryHTML(html, "//x:li/x:div[@class='publication']/x:div[@class='details']");
        int i = 1;
        for (Node node : nodes) {
            if (XPathUtils.queryHTML(node, "x:span[@class='previousTitle']").isEmpty()) {
                String title = XPathUtils.getString(node, "x:label");
                String href = XPathUtils.getString(node, "x:a/@href");
                int ix = href.indexOf("/journal/");
                String id = href.substring(ix+9);
                Journal journal = new Journal(id, title);
                list.add(journal);
            }
        }

        return list;
    }

    public static void main(String[] args) throws IOException {
        for (Journal j : new WileyPublicationCrawler(new DefaultCrawlerContext(null)).getJournals()) {
            System.out.println(j.getAbbreviation() +"\t"+ j.getTitle());
        }
    }


}
