package wwmm.pubcrawler.model;

import wwmm.pubcrawler.model.id.ArticleId;
import wwmm.pubcrawler.model.id.IssueId;
import wwmm.pubcrawler.model.id.PublisherId;
import wwmm.pubcrawler.types.Doi;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang.Validate.notNull;

/**
 * @author Sam Adams
 */
public class ArticleBuilder {

    private String id;
    private String title;
    private List<String> authors = new ArrayList<String>();
    private String pages; 
    
    private IssueId issueId;
    private String journalTitle;
    private String volume;
    private String number;
    private String year;

    private Doi doi;
    private URI url;

    public ArticleBuilder withPublisherId(final PublisherId publisherId) {
        return this;
    }

    public ArticleBuilder withId(final String id) {
        this.id = id;
        return this;
    }

    public ArticleBuilder withTitle(final String title) {
        this.title = title;
        return this;
    }

    public ArticleBuilder withAuthor(final String author) {
        this.authors.add(author);
        return this;
    }

    public ArticleBuilder withIssueId(final IssueId issueId) {
        this.issueId = issueId;
        return this;
    }

    public ArticleBuilder withJournalTitle(final String journalTitle) {
        this.journalTitle = journalTitle;
        return this;
    }

    public ArticleBuilder withVolume(final String volume) {
        this.volume = volume;
        return this;
    }

    public ArticleBuilder withNumber(final String number) {
        this.number = number;
        return this;
    }

    public ArticleBuilder withPages(final String pages) {
        this.pages = pages;
        return this;
    }

    public ArticleBuilder withYear(final String year) {
        this.year = year;
        return this;
    }


    public ArticleBuilder withUrl(final URI url) {
        this.url = url;
        return this;
    }

    public ArticleBuilder withDoi(final Doi doi) {
        this.doi = doi;
        return this;
    }

    public Article build() {
        notNull(title);
        
        notNull(issueId);
        notNull(journalTitle);
        notNull(volume);
        notNull(number);
        notNull(year);
        notNull(pages);

        final ArticleId articleId = new ArticleId(issueId, id);
        final Reference reference = new Reference(journalTitle, volume, number, year, pages);
        return new Article(articleId, title, authors, reference, url, doi);
    }
    
}
