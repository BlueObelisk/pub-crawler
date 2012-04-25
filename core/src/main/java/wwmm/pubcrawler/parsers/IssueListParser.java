package wwmm.pubcrawler.parsers;

import wwmm.pubcrawler.model.Issue;

import java.util.List;

/**
 * @author Sam Adams
 */
public interface IssueListParser {

    List<Issue> findIssues();

}
