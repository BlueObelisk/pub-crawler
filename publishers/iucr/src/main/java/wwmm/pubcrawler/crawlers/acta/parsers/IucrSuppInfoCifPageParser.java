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
import nu.xom.Element;
import nu.xom.Node;
import org.apache.log4j.Logger;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.SupplementaryResource;
import wwmm.pubcrawler.model.id.ResourceId;
import wwmm.pubcrawler.utils.XHtml;
import wwmm.pubcrawler.utils.XPathUtils;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sam Adams
 */
public class IucrSuppInfoCifPageParser {

    private static final Logger LOG = Logger.getLogger(IucrSuppInfoCifPageParser.class);

    private Article articleRef;
    private Document html;

    public IucrSuppInfoCifPageParser(final Article articleRef, final Document html) {
        this.articleRef = articleRef;
        this.html = html;
    }

    public List<SupplementaryResource> getCifs(final URI url) throws IOException {
        final List<SupplementaryResource> resources = new ArrayList<SupplementaryResource>();
        final List<Node> nodes = XPathUtils.queryHTML(html, ".//x:td[@width='400']");
        for (final Node node : nodes) {
            final Element li = (Element) node;
            final Element address = li.getFirstChildElement("a", XHtml.NAMESPACE);
            final String href = address.getAttributeValue("href");

            final String s = li.getFirstChildElement("p", XHtml.NAMESPACE).getValue();
            final String contentType = s.substring(s.indexOf(']')+1).trim();
            final String filePath = getCifFilePath(href);
            final ResourceId id = new ResourceId(articleRef.getId(), filePath);
            final SupplementaryResource resource = new SupplementaryResource(id, url.resolve(href), filePath);
            resource.setContentType(contentType);
            resources.add(resource);
        }
        return resources;
    }

    private String getCifFilePath(final String href) {
        return href.substring(href.indexOf("sendcif?")+8)+".cif";
    }

}
