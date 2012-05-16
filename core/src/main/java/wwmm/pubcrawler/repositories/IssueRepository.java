package wwmm.pubcrawler.repositories;

import wwmm.pubcrawler.model.Issue;

import java.util.List;

/**
 * @author Sam Adams
 */
public interface IssueRepository {
    
    Issue getIssue(String publisher, String journal, String id);

    List<Issue> getIssuesForJournal(String journal);

    void addArticles(Issue issue, String... issues);

    void updateIssue(Issue issue);

    void saveOrUpdateIssue(Issue issue);

    List<Issue> getIssueBatch(long offset, int maxResults);
}
