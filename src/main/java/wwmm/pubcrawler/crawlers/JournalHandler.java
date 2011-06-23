package wwmm.pubcrawler.crawlers;

import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Issue;

import java.io.IOException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: seadams
 * Date: 23/06/2011
 * Time: 14:50
 * To change this template use File | Settings | File Templates.
 */
public interface JournalHandler {
    Issue fetchCurrentIssue() throws IOException;

    Issue fetchIssue(Issue issue) throws IOException;

    Article fetchArticle(Article article) throws IOException;

    List<Issue> fetchIssueList() throws IOException;
}
