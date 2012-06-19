package wwmm.pubcrawler.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wwmm.pubcrawler.archivers.JournalArchiver;
import wwmm.pubcrawler.crawlers.JournalHandler;
import wwmm.pubcrawler.crawlers.ResourceProcessor;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.parsers.PublicationListParser;
import wwmm.pubcrawler.parsers.PublicationListParserFactory;
import wwmm.pubcrawler.tasks.HttpCrawlTaskData;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * @author Sam Adams
 */
@Singleton
public class PublicationListProcessor<R> implements ResourceProcessor<R, HttpCrawlTaskData> {

    private static final Logger LOG = LoggerFactory.getLogger(PublicationListProcessor.class);

    private final PublicationListParserFactory<R> parserFactory;
    private final JournalArchiver journalArchiver;
    private final JournalHandler journalHandler;

    @Inject
    public PublicationListProcessor(final PublicationListParserFactory<R> parserFactory, final JournalArchiver journalArchiver, final JournalHandler journalHandler) {
        this.parserFactory = parserFactory;
        this.journalArchiver = journalArchiver;
        this.journalHandler = journalHandler;
    }

    @Override
    public void process(final String taskId, final HttpCrawlTaskData httpCrawlTask, final R resource) {
        final PublicationListParser parser = parserFactory.createPublicationListParser(resource);
        final List<Journal> journals = parser.findJournals();
        for (final Journal journal : journals) {
            LOG.debug("Found journal: {}", journal.getTitle());
            journalArchiver.archive(journal);
            journalHandler.handleJournal(journal);
        }
    }
    
}
