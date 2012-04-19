package wwmm.pubcrawler.crawlers;

import wwmm.pubcrawler.model.Issue;

import java.util.List;

/**
 * @author Sam Adams
 */
public interface IssueListParser {

    List<Issue> findIssues();

}
