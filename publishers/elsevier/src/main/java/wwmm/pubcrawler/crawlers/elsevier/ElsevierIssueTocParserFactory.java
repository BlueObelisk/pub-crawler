package wwmm.pubcrawler.crawlers.elsevier;

import wwmm.pubcrawler.parsers.IssueTocParser;
import wwmm.pubcrawler.parsers.IssueTocParserFactory;
import wwmm.pubcrawler.crawlers.elsevier.parsers.ElsevierIssueTocParser;
import wwmm.pubcrawler.http.DocumentResource;
import wwmm.pubcrawler.model.id.JournalId;

import javax.inject.Singleton;

/**
 * @author Sam Adams
 */
@Singleton
public class ElsevierIssueTocParserFactory implements IssueTocParserFactory<DocumentResource> {

    @Override
    public IssueTocParser createIssueTocParser(final JournalId journalId, final DocumentResource htmlDoc) {
        return new ElsevierIssueTocParser(htmlDoc.getDocument(), htmlDoc.getUrl(), journalId);
    }

}
