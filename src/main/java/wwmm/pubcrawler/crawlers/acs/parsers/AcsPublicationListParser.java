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
package wwmm.pubcrawler.crawlers.acs.parsers;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import wwmm.pubcrawler.crawlers.PublicationListParser;
import wwmm.pubcrawler.crawlers.acs.Acs;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.utils.XPathUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

/**
 * <p>The <code>AcsIssueCrawler</code> class provides a method for obtaining
 * information about all articles from a particular issue of a journal
 * published by the American Chemical Society.</p>
 *
 * @author Nick Day
 * @author Sam Adams
 * @version 2.0
 */
public class AcsPublicationListParser implements PublicationListParser {

    private final Document html;

    public AcsPublicationListParser(final Document html) {
        this.html = html;
    }

    public List<Journal> findJournals() {
        final List<Journal> journals = new ArrayList<Journal>();

        final List<Node> nodes = XPathUtils.queryHTML(html, "//x:div[@id='contentMain']//x:div[@class='column']/x:ul/x:li/x:a");
        for (final Node node : nodes) {
            final Element element = (Element) node;
            final String href = element.getAttributeValue("href");
            if (href.startsWith("/journal/")) {
                final String title = element.getValue().trim();
                final String abbrev = href.substring(href.lastIndexOf('/') + 1);
                final Journal journal = new Journal(Acs.PUBLISHER_ID, abbrev, title);
                journal.setUrl(URI.create(format("http://pubs.acs.org/toc/%s/current", abbrev)));
                journals.add(journal);
            }
        }

        return journals;
    }

}
