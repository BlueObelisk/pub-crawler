package wwmm.pubcrawler.model;

import wwmm.pubcrawler.model.id.Id;

import java.net.URI;

public abstract class PubcrawlerObject<ID extends Id<ID>> extends MongoDBObject {

    public ID getId() {
        String id = getString("id");
        return id == null ? null : createId(id);
    }

    public void setId(ID id) {
        put("id", id == null ? null : id.getUid());
    }

    protected abstract ID createId(String id);


    public URI getUrl() {
        String s = getString("url");
        return s == null ? null : URI.create(s);
    }

    public void setUrl(URI url) {
        String s = url == null ? null : url.toString();
        put("url", s);
    }

}
