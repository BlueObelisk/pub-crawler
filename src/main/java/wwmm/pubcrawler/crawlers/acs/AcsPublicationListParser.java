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
package wwmm.pubcrawler.crawlers.acs;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import org.apache.log4j.Logger;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.model.id.PublisherId;
import wwmm.pubcrawler.utils.XPathUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>The <code>AcsIssueCrawler</code> class provides a method for obtaining
 * information about all articles from a particular issue of a journal
 * published by the American Chemical Society.</p>
 *
 * @author Nick Day
 * @author Sam Adams
 * @version 2.0
 */
public class AcsPublicationListParser {

    private static final Logger LOG = Logger.getLogger(AcsPublicationListParser.class);

    private final PublisherId publisherId;
    
    private final Document html;
    private final URI url;

    public AcsPublicationListParser(final PublisherId publisherId, final Document html, final URI url) {
        this.publisherId = publisherId;
        this.html = html;
        this.url = url;
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
                journals.add(new Journal(publisherId, abbrev, title));
            }
        }

        return journals;
    }

}