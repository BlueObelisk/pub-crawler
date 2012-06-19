package wwmm.pubcrawler.archivers;

import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.repositories.JournalRepository;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class JournalRepositoryArchiver implements JournalArchiver {

    private final JournalRepository repository;

    @Inject
    public JournalRepositoryArchiver(final JournalRepository repository) {
        this.repository = repository;
    }

    @Override
    public void archive(final Journal journal) {
        repository.saveOrUpdateJournal(journal);
    }

}
