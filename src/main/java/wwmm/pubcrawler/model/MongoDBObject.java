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
    public Object put(String key, Object v) {
        return super.put(key, v);
    }

    @Override
    public void putAll(BSONObject o) {
        super.putAll(o.toMap());
    }

    @Override
    public Object get(String key) {
        return super.get(key);
    }

    @Override
    public Map toMap() {
        return this;
    }

    @Override
    public Object removeField(String key) {
        return super.remove(key);
    }

    @Override
    public boolean containsKey(String s) {
        return super.containsKey(s);
    }

    @Override
    public boolean containsField(String s) {
        return super.containsKey(s);
    }


    public String getString(String key) {
        Object o = get(key);
        return o == null ? null : o.toString();
    }

    public Long getLong(String key) {
        Object o = get(key);
        return o == null ? null : ((Number) o).longValue();
    }


}
