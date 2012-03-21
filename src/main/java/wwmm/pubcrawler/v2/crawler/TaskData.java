package wwmm.pubcrawler.v2.crawler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Sam Adams
 */
public class TaskData {

    private final Map<String, String> data;

    public TaskData(final Map<String, String> data) {
        this.data = data;
    }

    public String getString(final String key) {
        return data.get(key);
    }

    public List<String> keys() {
        return new ArrayList<String>(data.keySet());
    }
    
}
