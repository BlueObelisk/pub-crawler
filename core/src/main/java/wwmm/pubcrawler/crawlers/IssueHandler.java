package wwmm.pubcrawler.crawlers;

import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.IssueLink;

public interface IssueHandler {

    void handleIssueLink(IssueLink issueLink);
}
