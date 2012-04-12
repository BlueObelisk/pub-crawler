package wwmm.pubcrawler.crawlers.wiley.parsers;

import org.junit.Test;
import wwmm.pubcrawler.crawlers.wiley.Wiley;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.id.IssueId;
import wwmm.pubcrawler.model.id.JournalId;

import java.net.URI;

import static org.junit.Assert.assertEquals;

/**
 * @author Sam Adams
 */
public class WileyPreviousIssueLinkHandlerTest {
    
    private static final JournalId JOURNAL_ID = new JournalId(Wiley.PUBLISHER_ID, "journal-id");
    private static final URI URL = java.net.URI.create("http://onlinelibrary.wiley.com/doi/journal/issue/toc");

    @Test
    public void testHandlerPreviousIssueLink() {
        Issue issueRef = new WileyPreviousIssueLinkHandler(JOURNAL_ID, URL).parse("/doi/10.1002/abc.v16.3/issuetoc");
        assertEquals("16", issueRef.getVolume());
        assertEquals("3", issueRef.getNumber());
        assertEquals(new IssueId(JOURNAL_ID, "16", "3"), issueRef.getId());
        assertEquals(URI.create("http://onlinelibrary.wiley.com/doi/10.1002/abc.v16.3/issuetoc"), issueRef.getUrl());
    }

    @Test
    public void testHandleAlternativePreviousIssueLink() {
        Issue issueRef = new WileyPreviousIssueLinkHandler(JOURNAL_ID, URL).parse("/doi/10.1111/abac.2011.47.issue-1/issuetoc");
        assertEquals("47", issueRef.getVolume());
        assertEquals("1", issueRef.getNumber());
        assertEquals(new IssueId(JOURNAL_ID, "47", "1"), issueRef.getId());
        assertEquals(URI.create("http://onlinelibrary.wiley.com/doi/10.1111/abac.2011.47.issue-1/issuetoc"), issueRef.getUrl());
    }

    @Test
    public void testHandleMultiplePreviousIssueLink() {
        Issue issueRef = new WileyPreviousIssueLinkHandler(JOURNAL_ID, URL).parse("/doi/10.1002/abio.v23:2/3/issuetoc");
        assertEquals("23", issueRef.getVolume());
        assertEquals("2/3", issueRef.getNumber());
        assertEquals(new IssueId(JOURNAL_ID, "23", "2/3"), issueRef.getId());
        assertEquals(URI.create("http://onlinelibrary.wiley.com/doi/10.1002/abio.v23:2/3/issuetoc"), issueRef.getUrl());
    }

    @Test
    public void testHandleNonNumericNumberInPreviousIssueLink() {
        Issue issueRef = new WileyPreviousIssueLinkHandler(JOURNAL_ID, URL).parse("/doi/10.1111/apa.2012.101.issue-s464/issuetoc");
        assertEquals("101", issueRef.getVolume());
        assertEquals("s464", issueRef.getNumber());
        assertEquals(new IssueId(JOURNAL_ID, "101", "s464"), issueRef.getId());
        assertEquals(URI.create("http://onlinelibrary.wiley.com/doi/10.1111/apa.2012.101.issue-s464/issuetoc"), issueRef.getUrl());
    }

    @Test
    public void testHandleNonNumericVolumeInPreviousIssueLink() {
        Issue issueRef = new WileyPreviousIssueLinkHandler(JOURNAL_ID, URL).parse("/doi/10.1002/ajmg.a.v158a.3/issuetoc");
        assertEquals("158a", issueRef.getVolume());
        assertEquals("3", issueRef.getNumber());
        assertEquals(new IssueId(JOURNAL_ID, "158a", "3"), issueRef.getId());
        assertEquals(URI.create("http://onlinelibrary.wiley.com/doi/10.1002/ajmg.a.v158a.3/issuetoc"), issueRef.getUrl());
    }

}
