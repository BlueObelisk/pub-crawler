package wwmm.pubcrawler.data.mongo;

import java.util.ArrayList;
import java.util.List;

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

    public MongoStore(final DB db) {
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


    public void saveArticle(final Article article) {
        final ArticleId id = article.getId();
        articles.update(new BasicDBObject("id", id.getUid()), article, true, false);
    }

    public Article findArticle(final ArticleId id) {
        return (Article) articles.findOne(new BasicDBObject("id", id.getUid()));
    }
    
    public Article findArticleByDoi(final Doi doi){
		return (Article) articles.findOne(new BasicDBObject("doi", doi.getValue()));
    }
    
    public boolean containsDoi(final Doi doi){
    	return articles.findOne(new BasicDBObject("doi", doi.getValue()), new BasicDBObject("doi", 1)) != null;
    }

    public boolean containsArticle(final ArticleId id) {
        return articles.findOne(new BasicDBObject("id", id.getUid()), new BasicDBObject("id", 1)) != null;
    }


    public void saveIssue(final Issue issue) {
        final IssueId id = issue.getId();
        issues.update(new BasicDBObject("id", id.getUid()), issue, true, false);
    }

    public Issue findIssue(final IssueId id) {
        return (Issue) issues.findOne(new BasicDBObject("id", id.getUid()));
    }

    public boolean containsIssue(final IssueId id) {
        return issues.findOne(new BasicDBObject("id", id.getUid()), new BasicDBObject("id", 1)) != null;
    }


    public void saveJournal(final Journal journal) {
        final JournalId id = journal.getId();
        journals.update(new BasicDBObject("id", id.getUid()), journal, true, false);
    }


    public void addIssueToJournal(final Journal journal, final Issue issue) {
        final BasicDBObject query = new BasicDBObject("id", journal.getId().getUid());
        final BasicDBObject update = new BasicDBObject("$addToSet", new BasicDBObject("issues", issue.getId().getUid()));
        this.journals.update(query, update);
    }

    public boolean containsJournal(final JournalId id) {
        return journals.findOne(new BasicDBObject("id", id.getUid()), new BasicDBObject("id", 1)) != null;
    }
    
    public Journal findJournal(final JournalId id){
    	return (Journal) journals.findOne(new BasicDBObject("id", id.getUid()));
    }
    
    public List<Journal> listJournals(){
    	final List<Journal> results=new ArrayList<Journal>();
    	final DBCursor cursor=journals.find(new BasicDBObject(),new BasicDBObject("id",1));
    	for(final DBObject object:cursor){
    		final Journal journal=(Journal) object;
    		results.add(journal);
    	}
		return results;
    }

}
