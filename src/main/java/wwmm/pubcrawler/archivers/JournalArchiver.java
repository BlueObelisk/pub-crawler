package wwmm.pubcrawler.archivers;

import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.v2.repositories.JournalRepository;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class JournalArchiver implements Archiver<Journal> {

    private final JournalRepository repository;

    @Inject
    public JournalArchiver(final JournalRepository repository) {
        this.repository = repository;
    }

    @Override
    public void archive(final Journal journal) {
        repository.saveOrUpdateJournal(journal);
    }

}
