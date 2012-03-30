package wwmm.pubcrawler.model;

import com.mongodb.DBObject;
import org.bson.BSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author sea36
 */
public class MongoDBObject extends LinkedHashMap implements DBObject {

    private boolean partialObject = false;

    @Override
    public void markAsPartialObject() {
        partialObject = true;
    }

    @Override
    public boolean isPartialObject() {
        return partialObject;
    }


    @Override
    public Object put(final String key, final Object v) {
        return super.put(key, v);
    }

    @Override
    public void putAll(final BSONObject o) {
        super.putAll(o.toMap());
    }

    @Override
    public Object get(final String key) {
        return super.get(key);
    }

    @Override
    public Map toMap() {
        return this;
    }

    @Override
    public Object removeField(final String key) {
        return super.remove(key);
    }

    @Override
    public boolean containsKey(final String s) {
        return super.containsKey(s);
    }

    @Override
    public boolean containsField(final String s) {
        return super.containsKey(s);
    }


    public String getString(final String key) {
        final Object o = get(key);
        return o == null ? null : o.toString();
    }

    public Long getLong(final String key) {
        final Object o = get(key);
        return o == null ? null : ((Number) o).longValue();
    }


}
