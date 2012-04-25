package wwmm.pubcrawler.parsers;

import wwmm.pubcrawler.model.id.JournalId;

/**
 * @author Sam Adams
 */
public interface IssueListParserFactory<T> {

    IssueListParser createIssueListParser(JournalId journalId, T resource);

}
