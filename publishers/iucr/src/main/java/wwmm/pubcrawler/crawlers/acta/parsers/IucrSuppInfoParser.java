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

import nu.xom.Element;
import nu.xom.Node;
import org.apache.log4j.Logger;
import wwmm.pubcrawler.model.SupplementaryResource;
import wwmm.pubcrawler.model.id.ArticleId;
import wwmm.pubcrawler.model.id.ResourceId;
import wwmm.pubcrawler.types.MediaType;
import wwmm.pubcrawler.utils.XHtml;
import wwmm.pubcrawler.utils.XPathUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sam Adams
 */
public class IucrSuppInfoParser {

    private static final Logger LOG = Logger.getLogger(IucrSuppInfoParser.class);

    private final ArticleId articleId;
    private final URI baseUrl;
    private final Node articleNode;

    private List<SupplementaryResource> supplementaryResources;
    private List<SupplementaryResource> supplementaryResourceLists;
    
    public IucrSuppInfoParser(final ArticleId articleId, final URI baseUrl, final Node articleNode) {
        this.articleId = articleId;
        this.baseUrl = baseUrl;
        this.articleNode = articleNode;
    }

    public List<SupplementaryResource> getSupplementaryResources() {
        List<SupplementaryResource> resources = this.supplementaryResources;
        if (resources == null) {
            resources = findSupplementaryResources();
            this.supplementaryResources = resources;
        }
        return resources;
    }

    private List<SupplementaryResource> findSupplementaryResources() {
        final List<SupplementaryResource> resources = new ArrayList<SupplementaryResource>();

        for (final Node node : getLinks()) {
            final Element address = (Element) node;
            final Element img = address.getFirstChildElement("img", XHtml.NAMESPACE);
            String altTest = img.getAttributeValue("alt");
            if (altTest.startsWith("[") && altTest.endsWith("]")) {
                altTest = altTest.substring(1, altTest.length()-1);
            }
            if (   "HTML version".equalsIgnoreCase(altTest)
                || "pdf version".equalsIgnoreCase(altTest)
                || "similar papers".equalsIgnoreCase(altTest)
                || "Open access".equalsIgnoreCase(altTest)
                || "cited in".equalsIgnoreCase(altTest)
                || "3d view".equalsIgnoreCase(altTest)
                || "buy article online".equalsIgnoreCase(altTest)
                || "free".equalsIgnoreCase(altTest)) {
                continue;
            }
            final String href = address.getAttributeValue("href");
            final URI url = baseUrl.resolve(href);
            LOG.trace("resource: [" + href + "] " + altTest);

            if (isCif(altTest) && isMultiCif(href)) {
//                try {
//                    resources.addAll(getCifs(url));
//                } catch (Exception e) {
//                    throw new CrawlerRuntimeException("Error retrieving CIF links: "+ url, e);
//                }
            } else if (isMultiFile(href)) {
//                try {
//                    resources.addAll(getResources(url));
//                } catch (Exception e) {
//                    throw new CrawlerRuntimeException("Error retrieving resource links: "+url, e);
//                }
            } else {
                final String path = getFilePath(href);
                final ResourceId id = new ResourceId(articleId, path);
                final SupplementaryResource resource = new SupplementaryResource(id, url, path);
                resource.setLinkText(altTest);
                if ("CIF".equals(altTest)) {
                    resource.setContentType(MediaType.CHEMICAL_CIF.getName());
                }
                else if (url.toString().endsWith(".pdf")) {
                    resource.setContentType(MediaType.APPLICATION_PDF.getName());
                }

                resources.add(resource);
            }
        }
        return resources;
    }

    private List<Node> getLinks() {
        return XPathUtils.queryHTML(articleNode, ".//x:a[x:img]");
    }

    private String getFilePath(final String href) {
        if (href.contains("sendcif?")) {
            return getCifFilePath(href);
        }
        if (isCheckCif(href)) {
            return getCheckCifFilePath(href);
        }
        return href.substring(href.lastIndexOf('/')+1);
    }

    private static String getCheckCifFilePath(final String href) {
        final int i0 = href.indexOf("cnor=");
        final int i1 = href.indexOf('&', i0);
        return href.substring(i0+5, i1)+"_checkcif.html";
    }

    private static boolean isCheckCif(final String href) {
        return href.endsWith("checkcif=yes");
    }

   

    private String getCifFilePath(final String href) {
        return href.substring(href.indexOf("sendcif?")+8)+".cif";
    }

    private static boolean isMultiCif(final String href) {
        final String filename = href.substring(href.lastIndexOf('/')+1);
        return !filename.toLowerCase().contains("sup");
    }

    private static boolean isCif(final String alt) {
        return "cif file".equalsIgnoreCase(alt);
    }

    private static boolean isMultiFile(final String href) {
        return href.toLowerCase().contains("/sendsup?");
    }

}
