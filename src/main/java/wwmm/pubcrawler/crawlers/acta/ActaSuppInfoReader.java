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

package wwmm.pubcrawler.crawlers.acta;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import org.apache.log4j.Logger;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.CrawlerRuntimeException;
import wwmm.pubcrawler.crawlers.AbstractCrawler;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.SupplementaryResource;
import wwmm.pubcrawler.model.id.ArticleId;
import wwmm.pubcrawler.types.MediaType;
import wwmm.pubcrawler.utils.XHtml;
import wwmm.pubcrawler.utils.XPathUtils;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sam Adams
 */
public class ActaSuppInfoReader extends AbstractCrawler {

    private static final Logger LOG = Logger.getLogger(ActaSuppInfoReader.class);

    private Article articleRef;

    public ActaSuppInfoReader(CrawlerContext context, Article articleRef) {
        super(context);
        this.articleRef = articleRef;
    }

    @Override
    protected Logger log() {
        return LOG;
    }

    protected ArticleId getArticleId() {
        return articleRef.getId();
    }

    public List<SupplementaryResource> getSupplementaryResources(List<Node> nodes, URI context) {
        List<SupplementaryResource> resources = new ArrayList<SupplementaryResource>();

        for (Node node : nodes) {
            Element address = (Element) node;
            Element img = address.getFirstChildElement("img", XHtml.NAMESPACE);
            String alt = img.getAttributeValue("alt");
            if (alt.startsWith("[") && alt.endsWith("]")) {
                alt = alt.substring(1, alt.length()-1);
            }
            if (       "HTML version".equalsIgnoreCase(alt)
                    || "pdf version".equalsIgnoreCase(alt)
                    || "similar papers".equalsIgnoreCase(alt)
                    || "Open access".equalsIgnoreCase(alt)
                    || "cited in".equalsIgnoreCase(alt)
                    || "3d view".equalsIgnoreCase(alt)
                    || "buy article online".equalsIgnoreCase(alt)
                    || "free".equalsIgnoreCase(alt)) {
                continue;
            }
            String href = address.getAttributeValue("href");
            URI url = context.resolve(href);
            log().trace("resource: ["+href+"] "+alt);
            
            if (isCif(alt) && isMultiCif(href)) {
                try {
                    resources.addAll(getCifs(url));
                } catch (Exception e) {
                    throw new CrawlerRuntimeException("Error retrieving CIF links: "+ url, e);
                }
            } else if (isMultiFile(href)) {
                try {
                    resources.addAll(getResources(url));
                } catch (Exception e) {
                    throw new CrawlerRuntimeException("Error retrieving resource links: "+url, e);
                }
            } else {
                SupplementaryResource resource = new SupplementaryResource();
                resource.setLinkText(alt);
                resource.setUrl(url);
                if (href.contains("sendcif?")) {
                    resource.setFilePath(getCifFilePath(href));
                } else if (isCheckCif(href)) {
                    resource.setFilePath(getCheckCifFilePath(href));
                } else {
                    resource.setFilePath(href.substring(href.lastIndexOf('/')+1));
                }
                if ("CIF".equals(alt)) {
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

    private static String getCheckCifFilePath(String href) {
        int i0 = href.indexOf("cnor=");
        int i1 = href.indexOf('&', i0);
        return href.substring(i0+5, i1)+"_checkcif.html";
    }

    private static boolean isCheckCif(String href) {
        return href.endsWith("checkcif=yes");
    }

    private List<SupplementaryResource> getResources(URI url) throws IOException {
        List<SupplementaryResource> resources = new ArrayList<SupplementaryResource>();
        Document html = readHtml(url, getArticleId(), "suppl", AGE_MAX);
        List<Node> nodes = XPathUtils.queryHTML(html, ".//x:td[@width='400']");
        for (Node node : nodes) {
            String href = XPathUtils.getString(node, "./x:a[./x:img[@alt='display file' or @alt='play file']]/@href");
            String contentType = XPathUtils.getString(node, "./x:p/x:b");
            String s = node.getValue();
            String linkText = s.substring(s.indexOf(']')+1).trim();
            int i = href.indexOf("file=");
            String filePath = href.substring(i+5, href.indexOf('&', i));

            SupplementaryResource resource = new SupplementaryResource();
            resource.setUrl(url.resolve(href));
            resource.setContentType(contentType);
            resource.setLinkText(linkText);
            resource.setFilePath(filePath);

            resources.add(resource);
        }
        return resources;
    }

    private List<SupplementaryResource> getCifs(URI url) throws IOException {
        List<SupplementaryResource> resources = new ArrayList<SupplementaryResource>();
        Document html = readHtml(url, getArticleId(), "cifs", AGE_MAX);
//        List<Node> nodes = XPathUtils.queryHTML(html, ".//x:ul/x:li");
//        for (Node node : nodes) {
//            Element li = (Element) node;
//            Element address = li.getFirstChildElement("a", XHtml.NAMESPACE);
//            String href = address.getAttributeValue("href");
//            String linkText = address.getValue();
//            int n = li.indexOf(address);
//            StringBuilder s = new StringBuilder();
//            for (int i = n+1; i < li.getChildCount(); i++) {
//                s.append(li.getChild(i).getValue());
//            }
//            String contentType = s.toString().trim();
//            String filePath = getCifFilePath(href);
//
//            SupplementaryResource resource = new SupplementaryResource();
//            resource.setUrl(URI.create(href));
//            resource.setLinkText(linkText);
//            resource.setContentType(contentType);
//            resource.setFilePath(filePath);
//            resources.add(resource);
//        }
        List<Node> nodes = XPathUtils.queryHTML(html, ".//x:td[@width='400']");
        for (Node node : nodes) {
            Element li = (Element) node;
            Element address = li.getFirstChildElement("a", XHtml.NAMESPACE);
            String href = address.getAttributeValue("href");

            String s = li.getFirstChildElement("p", XHtml.NAMESPACE).getValue();
            String contentType = s.substring(s.indexOf(']')+1).trim();
            String filePath = getCifFilePath(href);

            SupplementaryResource resource = new SupplementaryResource();
            resource.setUrl(URI.create(href));
            resource.setContentType(contentType);
            resource.setFilePath(filePath);
            resources.add(resource);
        }
        return resources;
    }

    private String getCifFilePath(String href) {
        return href.substring(href.indexOf("sendcif?")+8)+".cif";
    }

    private static boolean isMultiCif(String href) {
        String filename = href.substring(href.lastIndexOf('/')+1);
        return !filename.toLowerCase().contains("sup");
    }

    private static boolean isCif(String alt) {
        return "cif file".equalsIgnoreCase(alt);
    }

    private static boolean isMultiFile(String href) {
        return href.toLowerCase().contains("/sendsup?");
    }

}
