package wwmm.pubcrawler.crawlers.nature;

import wwmm.pubcrawler.crawlers.nature.parsers.NatureIssueTocParser;
import wwmm.pubcrawler.http.DocumentResource;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.parsers.IssueTocParser;
import wwmm.pubcrawler.parsers.IssueTocParserFactory;

import javax.inject.Singleton;

/**
 * @author Sam Adams
 */
@Singleton
public class NatureIssueTocParserFactory implements IssueTocParserFactory<DocumentResource> {

    @Override
    public IssueTocParser createIssueTocParser(final JournalId journalId, final DocumentResource resource) {
        return new NatureIssueTocParser(resource.getDocument(), resource.getUrl(), journalId);
    }
}
