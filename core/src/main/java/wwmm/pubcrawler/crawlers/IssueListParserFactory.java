package wwmm.pubcrawler.crawlers;

import nu.xom.Document;

import java.net.URI;

/**
 * @author Sam Adams
 */
public interface IssueListParserFactory {

    IssueListParser createIssueListParser(Document html, URI url);

}
