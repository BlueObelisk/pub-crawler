package wwmm.pubcrawler.repositories;

import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.id.IssueId;

import java.util.List;

/**
 * @author Sam Adams
 */
public interface IssueRepository {
    
    Issue getIssue(IssueId issueId);

    List<Issue> getIssuesForJournal(String journal);

    void addArticles(Issue issue, String... articles);

    void updateIssue(Issue issue);

    void saveOrUpdateIssue(Issue issue);

    List<Issue> getIssueBatch(long offset, int maxResults);

    long getNextSequence();
}
