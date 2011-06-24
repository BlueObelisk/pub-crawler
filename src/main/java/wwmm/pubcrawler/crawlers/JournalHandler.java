package wwmm.pubcrawler.crawlers;

import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Issue;

import java.io.IOException;
import java.util.List;

public interface JournalHandler {

    Issue fetchCurrentIssue() throws IOException;

    Issue fetchIssue(Issue issue) throws IOException;

    Article fetchArticle(Article article) throws IOException;

    List<Issue> fetchIssueList() throws IOException;

}
