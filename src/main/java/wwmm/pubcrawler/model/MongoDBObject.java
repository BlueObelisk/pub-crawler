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

    public void markAsPartialObject() {
        partialObject = true;
    }

    public boolean isPartialObject() {
        return partialObject;
    }


    public Object put(String key, Object v) {
        return super.put(key, v);
    }

    public void putAll(BSONObject o) {
        super.putAll(o.toMap());
    }

    public Object get(String key) {
        return super.get(key);
    }

    public Map toMap() {
        return this;
    }

    public Object removeField(String key) {
        return super.remove(key);
    }

    public boolean containsKey(String s) {
        return super.containsKey(s);
    }

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
