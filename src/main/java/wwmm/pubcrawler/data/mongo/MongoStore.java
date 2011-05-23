package wwmm.pubcrawler.data.mongo;

import com.mongodb.BasicDBObject;
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
        this.articles.setInternalClass("suppResources", SupplementaryResource.class);
        this.articles.setInternalClass("fullText", FullTextResource.class);

        this.issues = db.getCollection("issues");
        this.issues.setObjectClass(Issue.class);
        this.issues.setInternalClass("articles", Article.class);

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
