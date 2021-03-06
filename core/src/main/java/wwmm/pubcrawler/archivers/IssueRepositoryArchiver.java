package wwmm.pubcrawler.archivers;

import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.repositories.IssueRepository;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class IssueRepositoryArchiver implements IssueArchiver {

    private final IssueRepository repository;

    @Inject
    public IssueRepositoryArchiver(final IssueRepository repository) {
        this.repository = repository;
    }

    @Override
    public void archive(final Issue issue) {
        repository.saveOrUpdateIssue(issue);
    }

}
