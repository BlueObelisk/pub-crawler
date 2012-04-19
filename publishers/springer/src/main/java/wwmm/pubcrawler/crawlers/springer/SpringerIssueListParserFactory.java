package wwmm.pubcrawler.crawlers.springer;

import nu.xom.Document;
import wwmm.pubcrawler.crawlers.springer.parsers.SpringerIssueListParser;

import javax.inject.Singleton;
import java.net.URI;

/**
 * @author Sam Adams
 */
@Singleton
public class SpringerIssueListParserFactory {
    
    public SpringerIssueListParser createIssueListParser(final Document html, final URI url, final String journal) {
        return new SpringerIssueListParser(html, url, journal);
    }

}
