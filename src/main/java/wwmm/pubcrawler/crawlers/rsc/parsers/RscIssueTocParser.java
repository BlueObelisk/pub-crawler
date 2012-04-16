package wwmm.pubcrawler.crawlers.rsc.parsers;

import nu.xom.Document;
import wwmm.pubcrawler.crawlers.IssueTocParser;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.id.JournalId;

import java.net.URI;
import java.util.List;

/**
 * @author Sam Adams
 */
public class RscIssueTocParser implements IssueTocParser {

    private final Document html;
    private final URI url;
    private final JournalId journalId;

    public RscIssueTocParser(final Document html, final URI url, final JournalId journalId) {
        this.html = html;
        this.url = url;
        this.journalId = journalId;
    }

    @Override
    public Issue getIssueDetails() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Article> getArticles() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Issue getPreviousIssue() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Issue> getIssueLinks() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<URI> getIssueListLinks() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

}
