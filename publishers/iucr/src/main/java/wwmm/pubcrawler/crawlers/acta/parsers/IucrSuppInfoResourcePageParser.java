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
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.SupplementaryResource;
import wwmm.pubcrawler.model.id.ArticleId;
import wwmm.pubcrawler.model.id.ResourceId;
import wwmm.pubcrawler.utils.XPathUtils;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sam Adams
 */
public class IucrSuppInfoResourcePageParser {

    private static final Logger LOG = Logger.getLogger(IucrSuppInfoResourcePageParser.class);

    private ArticleId articleRef;
    private Document html;
    private URI url;

    public IucrSuppInfoResourcePageParser(final ArticleId articleRef, final Document html, final URI url) {
        this.articleRef = articleRef;
        this.html = html;
        this.url = url;
    }

    public List<SupplementaryResource> getResources() throws IOException {
        final List<SupplementaryResource> resources = new ArrayList<SupplementaryResource>();
        final List<Node> nodes = XPathUtils.queryHTML(html, ".//x:td[@width='400']");
        for (final Node node : nodes) {
            final SupplementaryResource resource = createResource(node);
            resources.add(resource);
        }
        return resources;
    }

    private SupplementaryResource createResource(final Node node) {
        final String href = getHref(node);
        final String linkText = getLinkText(node);
        final String description = getDescription(node);
        final String filePath = getFilePath(href);
        final ResourceId id = new ResourceId(articleRef, filePath);
        final SupplementaryResource resource = new SupplementaryResource(id, url.resolve(href), filePath);
        resource.setLinkText(linkText);
        resource.setDescription(description);
        return resource;
    }

    private String getFilePath(final String href) {
        final int i = href.indexOf("file=");
        return href.substring(i+5, href.indexOf('&', i));
    }

    private String getDescription(final Node node) {
        final String s = node.getValue();
        return s.substring(s.indexOf(']')+1).trim().trim();
    }

    private String getLinkText(final Node node) {
        return XPathUtils.getString(node, "./x:p/x:b").trim();
    }

    private String getHref(final Node node) {
        final String href = XPathUtils.getString(node, "./x:a[./x:img[@alt='display file' or @alt='play file']]/@href");
        if (href == null) {
            return XPathUtils.getString(node, "./x:a[contains(@href, '&file=')]/@href");
        }
        return href;
    }

}
