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

package wwmm.pubcrawler.data.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;
import wwmm.pubcrawler.types.Doi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Sam Adams
 */
public class DoiAdapterTest {

    private static Gson getGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Doi.class, new DoiAdapter());
        return builder.create();
    }


    @Test
    public void testSerialize() {
        Doi doi = new Doi("10.1/foobar");
        DoiHolder holder = new DoiHolder();
        holder.setDoi(doi);
        Gson gson = getGson();
        String s = gson.toJson(holder);
        assertEquals("{\"doi\":\"10.1/foobar\"}", s);
    }

    @Test
    public void testDeserialize() {
        Gson gson = getGson();
        DoiHolder holder = gson.fromJson("{\"doi\":\"10.1/foobar\"}", DoiHolder.class);
        assertNotNull(holder);
        assertEquals(new Doi("10.1/foobar"), holder.getDoi());
    }

    static class DoiHolder {

        private Doi doi;

        public Doi getDoi() {
            return doi;
        }

        public void setDoi(Doi doi) {
            this.doi = doi;
        }

    }

}
