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
package wwmm.pubcrawler.crawlers.acta.parsers;

import nu.xom.Document;
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
public class IucrPublicationListParser {

    private static final Logger LOG = Logger.getLogger(IucrPublicationListParser.class);

    private final PublisherId publisherId;

    private final Document html;
    private final URI url;

    public IucrPublicationListParser(final PublisherId publisherId, final Document html, final URI url) {
        this.publisherId = publisherId;
        this.html = html;
        this.url = url;
    }

    public List<Journal> findJournals() {
        final List<Journal> journals = new ArrayList<Journal>();

        final List<Node> nodes = XPathUtils.queryHTML(html, "//x:body/x:table/x:tr");
        for (final Node node : nodes) {
            final String title = XPathUtils.getString(node, "x:td[1]/x:img[last()]/@alt");
            final String href = XPathUtils.getString(node, "x:td[3]/x:a[1]/@href");

            final Journal journal = new Journal(publisherId, "foo", title);
            journal.setUrl(url.resolve(href));
            journals.add(journal);
        }

        return journals;
    }

}
