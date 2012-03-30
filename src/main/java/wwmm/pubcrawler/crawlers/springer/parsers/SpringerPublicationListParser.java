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

package wwmm.pubcrawler.crawlers.springer.parsers;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import org.apache.log4j.Logger;
import wwmm.pubcrawler.crawlers.PublicationListParser;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.utils.XPathUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Sam Adams
 */
public class SpringerPublicationListParser implements PublicationListParser {

    private static final Logger LOG = Logger.getLogger(SpringerPublicationListParser.class);
    
    private List<Document> pages = new ArrayList<Document>();
    private static final Pattern P_JOURNAL_ID = Pattern.compile("/content/([^/]+)/");
    private final Document html;
    private final URI url;

    public SpringerPublicationListParser(final Document html, final URI url) {
        this.html = html;
        this.url = url;
    }

    public URI getNextPage() {
        List<Node> nodes = XPathUtils.queryHTML(html, "//x:a[text() = 'Next']");
        if (!nodes.isEmpty()) {
            Element addr = (Element) nodes.get(0);
            String href = addr.getAttributeValue("href");
            return url.resolve(href);
        }
        return null;
    }
    
    @Override
    public List<Journal> findJournals() {
        List<Journal> list = new ArrayList<Journal>();
        getTitles(list, url, html);
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
                LOG.warn("Unable to find journal id: " + href);
                continue;
            }

            String id = m.group(1);

            Journal journal = new Journal(id, title);
            journal.setUrl(url);
            list.add(journal);
        }
    }

}
