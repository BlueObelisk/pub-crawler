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

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.joda.time.Duration;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerRequest;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.crawlers.AbstractCrawler;
import wwmm.pubcrawler.crawlers.IssueCrawler;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.Journal;

import java.io.IOException;
import java.io.StringReader;

/**
 * @author Sam Adams
 */
public class ElsevierIssueCrawler extends AbstractCrawler implements IssueCrawler {

    private static final Logger LOG = Logger.getLogger(ElsevierIssueCrawler.class);

    private final Journal journal;
    private final Issue issueRef;

    private final Document document;

    public ElsevierIssueCrawler(Issue issue, Journal journal, CrawlerContext context) throws IOException {
        super(context);
        this.journal = journal;
        this.issueRef = issue;

        document = fetchHtml(issue);
    }

    protected Document fetchHtml(Issue issue) throws IOException {
        Duration maxAge;
        if (issue.isCurrent()) {
            maxAge = AGE_1DAY;
        } else {
            maxAge = AGE_MAX;
        }
        return readHtml(issue.getUrl(), issue.getId(), "toc.html", maxAge);
    }

    @Override
    protected Document readHtml(CrawlerRequest request) throws IOException {
        CrawlerResponse response = getHttpCrawler().execute(request);
        String s;
        try {
            String encoding = getEntityCharset(response);
            if (encoding != null) {
                s = IOUtils.toString(response.getContent(), encoding);
            } else {
                s = IOUtils.toString(response.getContent());
            }
        } finally {
            response.closeQuietly();
        }

        // Fix broken DTD!
        s = s.replace("_http://www.w3.org/TR/html4/loose.dtd", "http://www.w3.org/TR/html4/loose.dtd");

        try {
            Builder builder = newTagSoupBuilder();
            Document doc = builder.build(new StringReader(s));
            setDocBaseUrl(response, doc);
            return doc;
        } catch (ParsingException e) {
            throw new IOException("Error reading XML", e);
        }
    }

    @Override
    protected Logger log() {
        return LOG;
    }

    @Override
    public Issue toIssue() {
        final ElsevierIssueParser parser = new ElsevierIssueParser(issueRef, document, journal);
        return parser.toIssue();
    }

}
