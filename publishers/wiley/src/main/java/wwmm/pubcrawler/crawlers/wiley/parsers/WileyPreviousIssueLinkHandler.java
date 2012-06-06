package wwmm.pubcrawler.crawlers.wiley.parsers;

import wwmm.pubcrawler.CrawlerRuntimeException;
import wwmm.pubcrawler.model.IssueLink;
import wwmm.pubcrawler.model.IssueLinkBuilder;
import wwmm.pubcrawler.model.id.JournalId;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;

/**
 * @author Sam Adams
 */
public class WileyPreviousIssueLinkHandler {

    private static final Pattern P_PREV_1 = Pattern.compile("/doi/10\\.\\d+/[^/]+\\.v(\\w+)[:.](.+)/issuetoc");
    private static final Pattern P_PREV_2 = Pattern.compile("/doi/10\\.\\d+/[^/]+\\.\\d{4}\\.(\\d+)\\.issue-(.+)/issuetoc");
    
    private final JournalId journalId;
    private final URI url;

    public WileyPreviousIssueLinkHandler(final JournalId journalId, final URI url) {
        this.journalId = journalId;
        this.url = url;
    }

    public IssueLink parse(final String href) {
        final Matcher m = getMatcher(href);
        final String volume = m.group(1);
        final String number = m.group(2);
        return new IssueLinkBuilder()
                .withJournalId(journalId)
                .withVolume(volume)
                .withNumber(number)
                .withUrl(url.resolve(href))
                .build();
    }

    private Matcher getMatcher(final String href) {
        for (final Pattern p : asList(P_PREV_1, P_PREV_2)) {
            final Matcher m = p.matcher(href);
            if (m.matches()) {
                return m;
            }
        }
        throw new CrawlerRuntimeException("Cannot locate prev issue ID: " + href, null, null);
    }

}
