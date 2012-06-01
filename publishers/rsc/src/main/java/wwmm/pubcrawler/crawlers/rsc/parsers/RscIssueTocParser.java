package wwmm.pubcrawler.crawlers.rsc.parsers;

import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import org.apache.log4j.Logger;
import wwmm.pubcrawler.CrawlerRuntimeException;
import wwmm.pubcrawler.parsers.AbstractIssueTocParser;
import wwmm.pubcrawler.model.FullTextResource;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.SupplementaryResource;
import wwmm.pubcrawler.model.id.ArticleId;
import wwmm.pubcrawler.model.id.IssueId;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.types.Doi;
import wwmm.pubcrawler.utils.XPathUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.DOTALL;

/**
 * @author Sam Adams
 */
public class RscIssueTocParser extends AbstractIssueTocParser {

    private static final Logger LOG = Logger.getLogger(RscIssueTocParser.class);
    private static final Pattern P_JOURNAL_TITLE = Pattern.compile("JournalName='(.*?)'");
    private static final Pattern P_ID_STRING = Pattern.compile("(.+)(\\d{3})(\\d{3})$");
    private static final Pattern P_ISSUE_NUMBER = Pattern.compile("IssueNo='(\\d+(-\\d+)?)'");
    private static final Pattern P_YEAR = Pattern.compile("SubYear='(\\d+)'");
    private static final Pattern P_ISSUE_ID = Pattern.compile("IssueId='([^']+)'");
    private static final Pattern P_PAGES = Pattern.compile("(\\d+(?:-\\d+)?)\\s+DOI:", DOTALL);
    private static final Pattern P_DOI = Pattern.compile("DOI:\\s+(\\S+)", DOTALL);

    // /en/journals/journal/cc?issueid=cc047024&amp;issnprint=1359-7345
    private static final Pattern P_ISSUE_REF = Pattern.compile("\\b([a-z]+)(\\d{3})(\\d{3})($|&issnprint)", Pattern.CASE_INSENSITIVE);

    public RscIssueTocParser(final Document html, final URI url, final JournalId journalId) {
        super(html, url, journalId);
    }

    @Override
    protected Logger log() {
        return LOG;
    }


    @Override
    protected String findJournalTitle() {
        final String script = findScript();
        final Matcher m = P_JOURNAL_TITLE.matcher(script);
        if (m.find()) {
            return m.group(1);
        }
        throw new RuntimeException("Unable to find journal title");
    }

    private String findScript() {
        final List<String> scripts = XPathUtils.getStrings(getHtml(), ".//x:script");
        return scripts.get(scripts.size()-1);
    }

    private String findIdString() {
        final String script = findScript();
        final Matcher matcher = P_ISSUE_ID.matcher(script);
        if (!matcher.find()) {
            throw new CrawlerRuntimeException("No match: "+script, getIssueId(), getUrl());
        }
        return matcher.group(1);
    }

    @Override
    protected String findVolume() {
        final Matcher matcher = P_ID_STRING.matcher(findIdString());
        if (!matcher.find()) {
            throw new CrawlerRuntimeException("No match: "+ findIdString(), getIssueId(), getUrl());
        }
        return String.valueOf(Integer.parseInt(matcher.group(2)));
    }

    @Override
    protected String findNumber() {
        final String script = findScript();
        final Matcher matcher = P_ISSUE_NUMBER.matcher(script);
        if (!matcher.find()) {
            log().warn("Unable to locate issue number: "+script);
            return "-";
        }
        return matcher.group(1);
    }

    @Override
    protected String findYear() {
        final String script = findScript();
        final Matcher matcher = P_YEAR.matcher(script);
        if (!matcher.find()) {
            throw new CrawlerRuntimeException("No match: " + script, getIssueId(), getUrl());
        }
        return String.valueOf(Integer.parseInt(matcher.group(1)));
    }

    @Override
    public Issue getPreviousIssue() {
        final Element node = (Element) XPathUtils.getNode(getHtml(), ".//x:a[@title='Previous Issue']");
        if (node == null) {
            return null;
        }

        final String href = node.getAttributeValue("href");
        final Matcher m = P_ISSUE_REF.matcher(href);
        if (!m.find()) {
            throw new CrawlerRuntimeException("No match: ["+href+"]", getIssueId(), getUrl());
        }

        final String volume = trim(m.group(2), '0');
        final String number = trim(m.group(3), '0');

        final Issue prev = new Issue();
        prev.setId(new IssueId(getJournalId(), volume, number));
        prev.setJournalTitle(getJournalTitle());
        prev.setVolume(volume);
        prev.setNumber(number);
        prev.setUrl(getUrl().resolve(href));

        return prev;
    }

    private String trim(final String text, final char c) {
        int i = 0;
        for (; i < text.length(); i++) {
            if (text.charAt(i) != c) {
                return text.substring(i);
            }
        }
        return text;
    }


    @Override
    protected List<Node> getArticleNodes() {
        return XPathUtils.queryHTML(getHtml(), ".//x:div[@class='grey_box_wrapper'][//x:input[@class='toCheck']]");
    }

    @Override
    protected ArticleId getArticleId(final Node articleNode) {
        final Attribute attr = (Attribute) XPathUtils.getNode(articleNode, ".//x:input[@class='toCheck']/@id");
        final String id = attr.getValue();
        return new ArticleId(getJournalId(), id);
    }

    @Override
    protected String getArticleTitle(final Node articleNode) {
        final String title = XPathUtils.getString(articleNode, ".//x:h2[@class='title_text_s4_jrnls']");
        return title.trim();
    }

    @Override
    protected List<String> getArticleAuthors(final Node articleNode) {
        final List<String> authors = new ArrayList<String>();
        final String text = XPathUtils.getString(articleNode, ".//x:span[@class='red_txt_s4']");
        if (text != null) {
            for (String author : text.split(",| and ")) {
                authors.add(author.trim());
            }
        }
        return authors;
    }

    @Override
    protected String findArticlePages(final Node articleNode) {
        final String text = XPathUtils.getString(articleNode, ".//x:div[@class='grey_left_box_text_s4_new']");
        final Matcher matcher = P_PAGES.matcher(text);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    @Override
    protected URI getArticleUrl(final Node articleNode) {
        final String href = XPathUtils.getString(articleNode, ".//x:div[@class='title_text_s4_jrnls']//x:a/@href");
        return href == null ? null : getUrl().resolve(href);
    }

    @Override
    protected Doi getArticleDoi(final Node articleNode) {
        final String text = XPathUtils.getString(articleNode, ".//x:div[@class='grey_left_box_text_s4_new']");
        final Matcher matcher = P_DOI.matcher(text);
        if (matcher.find()) {
            return new Doi(matcher.group(1));
        }
        return null;
    }

    @Override
    protected URI getArticleSupportingInfoUrl(final Node articleNode) {
        return null;
    }

    @Override
    protected String getArticleTitleHtml(final Node articleNode) {
        return null;
    }

    @Override
    protected List<SupplementaryResource> getArticleSupplementaryResources(final ArticleId articleId, final Node articleNode) {
        return null;
    }

    @Override
    protected List<FullTextResource> getArticleFullTextResources(final Node articleNode) {
        return null;
    }

}
