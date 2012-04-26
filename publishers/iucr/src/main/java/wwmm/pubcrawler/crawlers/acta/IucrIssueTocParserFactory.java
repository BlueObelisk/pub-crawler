package wwmm.pubcrawler.crawlers.acta;

import nu.xom.Document;
import wwmm.pubcrawler.parsers.IssueTocParser;
import wwmm.pubcrawler.crawlers.acta.parsers.IucrIssueTocParser;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.parsers.IssueTocParserFactory;

import javax.inject.Singleton;

/**
 * @author Sam Adams
 */
@Singleton
public class IucrIssueTocParserFactory implements IssueTocParserFactory<IucrFrameResource> {

    public IssueTocParser createIssueTocParser(final JournalId journalId, final IucrFrameResource resource) {
        return new IucrIssueTocParser(resource.getBody(), resource.getHead(), journalId);
    }

}
