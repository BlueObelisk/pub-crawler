package wwmm.pubcrawler.crawlers;

import wwmm.pubcrawler.model.Issue;

public interface IssueHandler {

    void handleIssue(String journal, Issue issue);
    
}