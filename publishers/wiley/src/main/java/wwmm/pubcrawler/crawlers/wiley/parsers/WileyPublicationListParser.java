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

package wwmm.pubcrawler.crawlers.wiley.parsers;

import nu.xom.Document;
import nu.xom.Node;
import wwmm.pubcrawler.parsers.PublicationListParser;
import wwmm.pubcrawler.crawlers.wiley.Wiley;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.utils.XPathUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sam Adams
 */
public class WileyPublicationListParser implements PublicationListParser {

    private final Document html;
    private final URI url;

    public WileyPublicationListParser(final Document html, final URI url) {
        this.html = html;
        this.url = url;
    }

    @Override
    public List<Journal> findJournals() {
        final List<Journal> list = new ArrayList<Journal>();

        final List<Node> nodes = XPathUtils.queryHTML(html, "//x:li/x:div[@class='publication']/x:div[@class='details']");
        for (final Node node : nodes) {
            if (XPathUtils.queryHTML(node, "x:span[@class='previousTitle']").isEmpty()) {
                final String title = XPathUtils.getString(node, "x:label");
                final String href = XPathUtils.getString(node, "x:a/@href");
                final int ix = href.indexOf("/journal/");
                final String abbreviation = href.substring(ix+9);
                final Journal journal = new Journal(abbreviation, title);
                journal.setId(new JournalId(Wiley.PUBLISHER_ID, abbreviation));
                journal.setUrl(url.resolve(href));
                list.add(journal);
            }
        }

        return list;
    }

}
