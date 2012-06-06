package wwmm.pubcrawler.parsers;

import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.IssueLink;

import java.util.List;

/**
 * @author Sam Adams
 */
public interface IssueListParser {

    List<IssueLink> findIssues();

}
