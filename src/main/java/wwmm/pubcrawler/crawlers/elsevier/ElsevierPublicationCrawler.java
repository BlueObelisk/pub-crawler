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

package wwmm.pubcrawler.crawlers.elsevier;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import org.apache.log4j.Logger;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.crawlers.AbstractCrawler;
import wwmm.pubcrawler.utils.XPathUtils;

import java.io.IOException;
import java.net.URI;
import java.util.List;

/**
 * @author Sam Adams
 */
public class ElsevierPublicationCrawler extends AbstractCrawler {

    private static final Logger LOG = Logger.getLogger(ElsevierPublicationCrawler.class);

    private static final URI OPML_URI = URI.create("http://feeds.sciencedirect.com/opml.xml");

    public ElsevierPublicationCrawler(CrawlerContext context) throws IOException {
        super(context);

        Document doc = readDocument(OPML_URI, "elsevier/opml", AGE_28DAYS);
        List<Node> nodes = XPathUtils.queryHTML(doc, "//outline");
        int i = 1;
        for (Node node : nodes) {
            Element outline = (Element) node;
            URI url = URI.create(outline.getAttributeValue("htmlUrl"));
            String title = outline.getAttributeValue("text");

            // TODO generate object
        }

    }

    @Override
    protected Logger log() {
        return LOG;
    }

}
