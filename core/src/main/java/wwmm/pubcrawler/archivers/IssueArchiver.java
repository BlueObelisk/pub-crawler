package wwmm.pubcrawler.archivers;

import wwmm.pubcrawler.model.Issue;

/**
 * @author Sam Adams
 */
public interface IssueArchiver {
    
    void archive(Issue issue);
    
}
