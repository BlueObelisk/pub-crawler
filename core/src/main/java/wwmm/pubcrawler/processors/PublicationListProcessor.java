package wwmm.pubcrawler.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wwmm.pubcrawler.archivers.JournalArchiver;
import wwmm.pubcrawler.crawlers.JournalHandler;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.parsers.PublicationListParser;
import wwmm.pubcrawler.parsers.PublicationListParserFactory;

import java.util.List;

/**
 * @author Sam Adams
 */
public class PublicationListProcessor<T> {

    private static final Logger LOG = LoggerFactory.getLogger(PublicationListProcessor.class);

    private final PublicationListParserFactory<T> parserFactory;
    private final JournalArchiver journalArchiver;
    private final JournalHandler journalHandler;

    public PublicationListProcessor(final PublicationListParserFactory<T> parserFactory, final JournalArchiver journalArchiver, final JournalHandler journalHandler) {
        this.parserFactory = parserFactory;
        this.journalArchiver = journalArchiver;
        this.journalHandler = journalHandler;
    }

    public void processPublicationList(final T resource) {
        final PublicationListParser parser = parserFactory.createPublicationListParser(resource);
        final List<Journal> journals = parser.findJournals();
        for (final Journal journal : journals) {
            LOG.debug("Found journal: {}", journal.getTitle());
            journalArchiver.archive(journal);
            journalHandler.handleJournal(journal);
        }
    }
    
}
