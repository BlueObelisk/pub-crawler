package wwmm.pubcrawler.model;

import wwmm.pubcrawler.model.id.Id;

import java.net.URI;

public abstract class PubcrawlerObject<ID extends Id<ID>> {

    private ID id;
    private URI url;
    
    public ID getId() {
        return id;
    }

    public void setId(final ID id) {
        this.id = id;
    }

    public URI getUrl() {
        return url;
    }

    public void setUrl(final URI url) {
        this.url = url;
    }

}
