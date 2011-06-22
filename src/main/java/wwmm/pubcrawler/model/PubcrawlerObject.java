package wwmm.pubcrawler.model;

import wwmm.pubcrawler.model.id.Id;
import wwmm.pubcrawler.model.id.IssueId;

public abstract class PubcrawlerObject<ID extends Id<ID>> extends MongoDBObject {

    public ID getId() {
        String id = getString("id");
        return id == null ? null : createId(id);
    }

    public void setId(ID id) {
        put("id", id == null ? null : id.getValue());
    }

    protected abstract ID createId(String id);


}
