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
import org.joda.time.LocalDate;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Sam Adams
 */
public class LocalDateAdapterTest {

    private static Gson getGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        return builder.create();
    }


    @Test
    public void testSerialize() {
        LocalDate date = new LocalDate(2010, 11, 17);
        LocalDateHolder holder = new LocalDateHolder();
        holder.setDate(date);
        Gson gson = getGson();
        String s = gson.toJson(holder);
        assertEquals("{\"date\":{\"day\":17,\"month\":11,\"year\":2010}}", s);
    }

    @Test
    public void testDeserialize() {
        Gson gson = getGson();
        LocalDateHolder holder = gson.fromJson("{\"date\":{\"day\":17,\"month\":11,\"year\":2010}}", LocalDateHolder.class);
        assertNotNull(holder);
        assertEquals(new LocalDate(2010, 11, 17), holder.getDate());
    }

    static class LocalDateHolder {

        private LocalDate date;

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

    }

}
