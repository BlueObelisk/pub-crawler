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
import wwmm.pubcrawler.crawlers.PublicationListParser;
import wwmm.pubcrawler.model.Journal;
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
                journal.setUrl(url.resolve(href));
                list.add(journal);
            }
        }

        return list;
    }

}
