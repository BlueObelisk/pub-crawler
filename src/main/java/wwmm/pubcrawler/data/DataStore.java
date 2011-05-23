/*
 * Copyright 2010-2011 Nick Day, Sam Adams
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package wwmm.pubcrawler.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.joda.time.LocalDate;
import wwmm.pubcrawler.data.gson.DoiAdapter;
import wwmm.pubcrawler.data.gson.LocalDateAdapter;
import wwmm.pubcrawler.types.Doi;

import java.io.*;

/**
 * @author Sam Adams
 */
class DataStore {

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
        builder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        return builder.create();
    }

    private File getFile(String id) {
        return getFile(id, "json");
    }

    private File getFile(String id, String ext) {
        String fn;
        if (ext == null) {
            fn = id;
        } else {
            fn = id + '.' + ext;
        }
        return new File(root.getPath() + File.separatorChar + fn);
    }


    public boolean hasData(String id) {
        return getFile(id).isFile();
    }

    public boolean hasFile(String id) {
        return getFile(id, null).isFile();
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


    public void saveUtf8(String id, String s) throws IOException {
        File file = getFile(id, null);
        if (file.exists()) {
            throw new IOException("Data already exists: "+id);
        }
        FileUtils.forceMkdir(file.getParentFile());
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
        try {
            out.write(s);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    public String loadUtf8(String id) throws IOException {
        File file = getFile(id, null);
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
        try {
            String s = IOUtils.toString(in);
            return s;
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

}
