package wwmm.pubcrawler.core.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import wwmm.pubcrawler.core.data.gson.DoiAdapter;
import wwmm.pubcrawler.core.types.Doi;

import java.io.*;

/**
 * @author Sam Adams
 */
public class DataStore {

    private File root;
    private Gson gson;

    public DataStore(File path) throws IOException {
        if (path == null) {
            throw new IllegalArgumentException("Null path");
        }
        FileUtils.forceMkdir(path);
        this.root = path;
        this.gson = initGson();
    }

    private Gson initGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.disableHtmlEscaping();
        builder.setPrettyPrinting();
        builder.registerTypeAdapter(Doi.class, new DoiAdapter());
        return builder.create();
    }

    private File getFile(String id) {
        return new File(root.getPath() + File.separatorChar + id + ".json");
    }

    public boolean hasData(String id) {
        return getFile(id).isFile();
    }

    public void save(String id, Object o) throws IOException {
        File file = getFile(id);
        if (file.exists()) {
            throw new IOException("Data already exists: "+id);
        }
        FileUtils.forceMkdir(file.getParentFile());
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
        try {
            gson.toJson(o, out);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    public <T> T load(String id, Class<T> type) throws IOException {
        File file = getFile(id);
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
        try {
            return gson.fromJson(in, type);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    public void update(String id, Object o) throws IOException {
        File file = getFile(id);
        if (!file.exists()) {
            throw new IOException("Data missing: "+id);
        }
        FileUtils.forceMkdir(file.getParentFile());
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
        try {
            gson.toJson(o, out);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }
}
