package wwmm.pubcrawler.data.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import org.apache.commons.io.IOUtils;
import wwmm.pubcrawler.model.*;

import java.io.IOException;

/**
 * @author sea36
 */
public class MongoStore {

    private DB db;
    private DBCollection articles;
    private DBCollection issues;
//    private DBCollection journals;
    private GridFS gridFs;

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

        this.gridFs = new GridFS(db);
    }


    public byte[] loadFile(String id) throws IOException {
        GridFSDBFile f = gridFs.findOne(id);
        if (f != null) {
            return IOUtils.toByteArray(f.getInputStream());
        }
        return null;
    }

    public void saveFile(String id, byte[] bytes) {
        GridFSInputFile file = gridFs.createFile(bytes);
        file.setFilename(id);
        file.save();
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
