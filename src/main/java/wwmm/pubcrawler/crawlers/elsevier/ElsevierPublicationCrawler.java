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
public class ElsevierPublicationCrawler extends AbstractCrawler {

    private static final Logger LOG = Logger.getLogger(ElsevierPublicationCrawler.class);

    private static final URI OPML_URI = URI.create("http://feeds.sciencedirect.com/opml.xml");

    private static final PublisherId ELSEVIER_ID = new PublisherId("elsevier");

    private final Document doc;
    private final URI url;

    public ElsevierPublicationCrawler(CrawlerContext context) throws IOException {
        super(context);

        this.doc = readDocument(OPML_URI, ELSEVIER_ID, "opml.xml", AGE_28DAYS);
        this.url = OPML_URI;
    }

    public List<Journal> getJournals() {
        List<Journal> list = new ArrayList<Journal>();

        List<Node> nodes = XPathUtils.queryHTML(doc, "//outline");
        int i = 1;
        for (Node node : nodes) {
            Element outline = (Element) node;
            String u = outline.getAttributeValue("htmlUrl");
            URI url = URI.create(u);
            String title = outline.getAttributeValue("text");

            String id = u.substring(u.lastIndexOf('/')+1);

            Journal journal = new Journal(id, title);
            list.add(journal);
        }

        return list;
    }

    @Override
    protected Logger log() {
        return LOG;
    }

    public static void main(String[] args) throws IOException {
        for (Journal j : new ElsevierPublicationCrawler(new DefaultCrawlerContext(null)).getJournals()) {
            System.out.println(j.getTitle());
        }
    }

}
