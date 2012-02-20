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
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.crawlers.AbstractCrawler;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.utils.XPathUtils;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sam Adams
 */
public class ElsevierIssueBibTexCrawler extends AbstractCrawler {

    private static final Logger LOG = Logger.getLogger(ElsevierIssueBibTexCrawler.class);

    private final String bibtex;

    private final Issue issueRef;

    public ElsevierIssueBibTexCrawler(Issue issue, CrawlerContext context, Document html) throws IOException {
        super(context);
        this.issueRef = issue;
        this.bibtex = fetchBibtex(html);
    }

    private String fetchBibtex(final Document issueHtml) throws IOException {
        Document html = getDownloadPage(issueHtml);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        List<Node> fields = XPathUtils.queryHTML(html, "//x:form[@name='exportCite']/x:input");
        for (Node node : fields) {
            Element input = (Element) node;
            String name = input.getAttributeValue("name");
            String value = input.getAttributeValue("value");
            params.add(new BasicNameValuePair(name, value));
        }
        params.add(new BasicNameValuePair("format", "cite-abs"));
        params.add(new BasicNameValuePair("citation-type", "BIBTEX"));
        params.add(new BasicNameValuePair("Export", "Export"));

        URI url = URI.create("http://www.sciencedirect.com/science");

        String s = readStringPost(url, params, issueRef.getId(), "bibtex.txt", AGE_MAX);
        return s;
    }

    private Document getDownloadPage(Document html) throws IOException {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        List<Node> fields = XPathUtils.queryHTML(html, "//x:form[@name='Tag']/x:input");
        for (Node node : fields) {
            Element input = (Element) node;
            String name = input.getAttributeValue("name");
            String value = input.getAttributeValue("value");
            params.add(new BasicNameValuePair(name, value));
        }
        params.add(new BasicNameValuePair("export", "Export citations"));
        params.add(new BasicNameValuePair("pdfDownload", ""));

        List<Node> articles = XPathUtils.queryHTML(html, "//x:form[@name='Tag']//x:input[@name='art']");
        for (Node node : articles) {
            Element input = (Element) node;
            String articleId = input.getAttributeValue("value");
            params.add(new BasicNameValuePair("art", articleId));
        }

        String query = URLEncodedUtils.format(params, "UTF-8");

        URI url = URI.create("http://www.sciencedirect.com/science?" + query);
        return readHtml(url, issueRef.getId(), "bibtex.html", AGE_MAX);
    }

    @Override
    protected Logger log() {
        return LOG;
    }

}
