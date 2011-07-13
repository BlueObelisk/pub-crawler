package wwmm.pubcrawler.crawlers.rsc;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.ParsingException;
import org.apache.log4j.Logger;
import org.joda.time.Duration;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerGetRequest;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerRequest;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.CrawlerRuntimeException;
import wwmm.pubcrawler.crawlers.AbstractCrawler;
import wwmm.pubcrawler.utils.XPathUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Sam Adams
 */
public class RscJournalInfo extends AbstractCrawler {

    private static final Logger LOG = Logger.getLogger(RscJournalInfo.class);

    private String journalId;
    private Document html;
    private String script;

    public RscJournalInfo(CrawlerContext context, String id) throws IOException {
        super(context);
        this.journalId = id;
        this.html = fetchHtml();
        List<Node> nodes = XPathUtils.queryHTML(getHtml(), "//x:script[contains(., 'public variables')]");
        if (nodes.size() == 1) {
            script = nodes.get(0).getValue();
        } else {
            throw new CrawlerRuntimeException("Unable to find RSC journal info: "+journalId);
        }
    }

    @Override
    protected Logger log() {
        return LOG;
    }

    protected Document fetchHtml() throws IOException {
        Duration maxAge = AGE_1DAY;
        URI url = URI.create("http://pubs.rsc.org/en/journals/journalissues/"+journalId);
        return readHtml(url, "rsc/"+journalId+".info.html", maxAge);
    }

    protected  Document readHtml(URI url, String id, Duration maxage) throws IOException {
        CrawlerGetRequest request = new CrawlerGetRequest(url, id, maxage);
        return readHtml(request);
    }

    protected Document readHtml(CrawlerRequest request) throws IOException {
        CrawlerResponse response = getHttpCrawler().execute(request);
        try {
            if (response.isFromCache()) {
                log().debug("Retrieved "+response.getUrl()+" from cache");
            } else {
                log().info("Downloaded "+response.getUrl());
            }

            String encoding = getEntityCharset(response);
            if (encoding == null) {
                return readDocument(response, newTagSoupBuilder());
            } else {
                return readDocument(response, newTagSoupBuilder(), encoding);
            }
        } finally {
            response.closeQuietly();
        }
    }

    protected Document readDocument(URI url, String id, Duration maxage) throws IOException {
        CrawlerGetRequest request = new CrawlerGetRequest(url, id, maxage);
        return readDocument(request);
    }

    protected Document readDocument(CrawlerRequest request) throws IOException {
        CrawlerResponse response = getHttpCrawler().execute(request);
        try {
            String encoding = getEntityCharset(response);
            if (encoding == null) {
                return readDocument(response, new Builder());
            } else {
                return readDocument(response, new Builder(), encoding);
            }
        } finally {
            response.closeQuietly();
        }
    }

    private Document readDocument(CrawlerResponse response, Builder builder) throws IOException {
        try {
            Document doc = builder.build(response.getContent());
            setDocBaseUrl(response, doc);
            return doc;
        } catch (ParsingException e) {
            throw new IOException("Error reading XML", e);
        }
    }

    private Document readDocument(CrawlerResponse response, Builder builder, String encoding) throws IOException {
        try {
            InputStreamReader isr = new InputStreamReader(response.getContent(), encoding);
            Document doc = builder.build(isr);
            setDocBaseUrl(response, doc);
            return doc;
        } catch (ParsingException e) {
            throw new IOException("Error reading XML", e);
        }
    }

    protected Document getHtml() {
        return html;
    }



    public String getJournalName() {
        Pattern P_JOURNALNAME = Pattern.compile("\\bthemeJournalName\\s*=\\s*'([^']+)'");
        Matcher m = P_JOURNALNAME.matcher(script);
        if (m.find()) {
            return m.group(1);
        }
        throw new CrawlerRuntimeException("Unable to locate journal name");
    }

    public String getIssnPrint() {
        Pattern P_JOURNALNAME = Pattern.compile("\\bissnPrint_\\s*=\\s*'([^']+)'");
        Matcher m = P_JOURNALNAME.matcher(script);
        if (m.find()) {
            return m.group(1);
        }
        throw new CrawlerRuntimeException("Unable to locate journal name");
    }

    public String getIssnOnline() {
        Pattern P_JOURNALNAME = Pattern.compile("\\bissnOnline_\\s*=\\s*'([^']+)'");
        Matcher m = P_JOURNALNAME.matcher(script);
        if (m.find()) {
            return m.group(1);
        }
        throw new CrawlerRuntimeException("Unable to locate journal name");
    }

    public String getIsArchive() {
        Pattern P_JOURNALNAME = Pattern.compile("\\bisArchive\\s*=\\s*'([^']+)'");
        Matcher m = P_JOURNALNAME.matcher(script);
        if (m.find()) {
            return m.group(1);
        }
        throw new CrawlerRuntimeException("Unable to locate journal name");
    }

    public String getIsContentAvailable() {
        Pattern P_JOURNALNAME = Pattern.compile("\\bisContentAvailable\\s*=\\s*'([^']+)'");
        Matcher m = P_JOURNALNAME.matcher(script);
        if (m.find()) {
            return m.group(1);
        }
        throw new CrawlerRuntimeException("Unable to locate journal name");
    }

}
