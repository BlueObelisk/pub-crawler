package wwmm.pubcrawler.data.mongo;

import com.mongodb.*;
import wwmm.pubcrawler.model.*;
import wwmm.pubcrawler.model.id.ArticleId;
import wwmm.pubcrawler.model.id.IssueId;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.types.Doi;

/**
 * @author sea36
 */
public class MongoStore {

    private DB db;
    private DBCollection articles;
    private DBCollection issues;
    private DBCollection journals;

    public MongoStore(DB db) {
        this.db = db;

        this.articles = db.getCollection("articles");
        this.articles.setObjectClass(Article.class);
        this.articles.setInternalClass("reference", Reference.class);
        this.articles.ensureIndex(BasicDBObjectBuilder.start().add("id", 1).add("unique", true).get());

        this.issues = db.getCollection("issues");
        this.issues.setObjectClass(Issue.class);
        this.issues.setInternalClass("previousIssue", Issue.class);
        this.issues.ensureIndex(BasicDBObjectBuilder.start().add("id", 1).add("unique", true).get());

        // Horrible hack!
        for (int i = 0; i < 20; i++) {
            this.articles.setInternalClass("suppResources."+i, SupplementaryResource.class);
            this.articles.setInternalClass("fullText."+i, FullTextResource.class);
        }
        for (int i = 0; i < 1000; i++) {
            this.issues.setInternalClass("articles."+i, Article.class);
            this.issues.setInternalClass("articles."+i+".reference", Reference.class);
            for (int j = 0; j < 20; j++) {
                this.issues.setInternalClass("articles."+i+".suppResources."+j, SupplementaryResource.class);
                this.issues.setInternalClass("articles."+i+".fullText."+j, FullTextResource.class);
            }
        }

        this.journals = db.getCollection("journals");
        this.journals.setObjectClass(Journal.class);
        this.journals.ensureIndex(BasicDBObjectBuilder.start().add("id", 1).add("unique", true).get());
    }


    public void saveArticle(Article article) {
        ArticleId id = article.getId();
        articles.update(new BasicDBObject("id", id.getValue()), article, true, false);
    }

    public Article findArticle(ArticleId id) {
        return (Article) articles.findOne(new BasicDBObject("id", id.getValue()));
    }
    
    public Article findArticleByDoi(Doi doi){
		return (Article) articles.findOne(new BasicDBObject("doi", doi.getValue()));
    }
    
    public boolean containsDoi(Doi doi){
    	return articles.findOne(new BasicDBObject("doi", doi.getValue()), new BasicDBObject("doi", 1)) != null;
    }

    public boolean containsArticle(ArticleId id) {
        return articles.findOne(new BasicDBObject("id", id.getValue()), new BasicDBObject("id", 1)) != null;
    }


    public void saveIssue(Issue issue) {
        IssueId id = issue.getId();
        issues.update(new BasicDBObject("id", id.getValue()), issue, true, false);
    }

    public Issue findIssue(IssueId id) {
        return (Issue) issues.findOne(new BasicDBObject("id", id.getValue()));
    }

    public boolean containsIssue(IssueId id) {
        return issues.findOne(new BasicDBObject("id", id.getValue()), new BasicDBObject("id", 1)) != null;
    }


    public void saveJournal(Journal journal) {
        JournalId id = journal.getId();
        journals.update(new BasicDBObject("id", id.getValue()), journal, true, false);
    }


    public void addIssueToJournal(Journal journal, Issue issue) {
        BasicDBObject query = new BasicDBObject("id", journal.getId().getValue());
        BasicDBObject update = new BasicDBObject("$addToSet", new BasicDBObject("issues", issue.getId().getValue()));
        DBObject r = this.journals.findAndModify(query, update);
    }

    public boolean containsJournal(JournalId id) {
        return journals.findOne(new BasicDBObject("id", id.getValue()), new BasicDBObject("id", 1)) != null;
    }

}
