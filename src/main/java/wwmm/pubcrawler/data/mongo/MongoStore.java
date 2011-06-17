package wwmm.pubcrawler.data.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import wwmm.pubcrawler.model.*;

/**
 * @author sea36
 */
public class MongoStore {

    private DB db;
    private DBCollection articles;
    private DBCollection issues;
//    private DBCollection journals;

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

//        this.journals = db.getCollection("journals");
//        journals.setObjectClass(Journal.class);
//        journals.setInternalClass("issues", Issue.class);

    }


    public void saveArticle(Article article) {
        String id = article.getId();
        articles.update(new BasicDBObject("id", id), article, true, false);
    }

    public Article findArticle(String id) {
        return (Article) articles.findOne(new BasicDBObject("id", id));
    }
    
    public Article findArticleByDoi(String doi){
    	doi=cleanDoi(doi);
		return (Article) articles.findOne(new BasicDBObject("doi",doi));
    }
    
    private String cleanDoi(String doi){
    	String prefix="http://dx.doi.org/";
    	if(doi.startsWith(prefix)){
    		doi=doi.substring(prefix.length());
    	}
    	return doi;
    }
    
    public boolean containsDoi(String doi){
    	doi=cleanDoi(doi);
    	return articles.findOne(new BasicDBObject("doi", doi), new BasicDBObject("doi", 1)) != null;
    }

    public boolean containsArticle(String id) {
        return articles.findOne(new BasicDBObject("id", id), new BasicDBObject("id", 1)) != null;
    }


    public void saveIssue(Issue issue) {
        String id = issue.getId();
        issues.update(new BasicDBObject("id", id), issue, true, false);
    }

    public Issue findIssue(String id) {
        return (Issue) issues.findOne(new BasicDBObject("id", id));
    }

    public boolean containsIssue(String id) {
        return issues.findOne(new BasicDBObject("id", id), new BasicDBObject("id", 1)) != null;
    }

}
