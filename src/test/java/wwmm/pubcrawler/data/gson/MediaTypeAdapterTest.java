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
import wwmm.pubcrawler.types.MediaType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Sam Adams
 */
public class MediaTypeAdapterTest {

    private static Gson getGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(MediaType.class, new MediaTypeAdapter());
        return builder.create();
    }


    @Test
    public void testSerialize() {
        MediaType mediaType = new MediaType("text/plain");
        MediaTypeHolder holder = new MediaTypeHolder();
        holder.setMediaType(mediaType);
        Gson gson = getGson();
        String s = gson.toJson(holder);
        assertEquals("{\"mediaType\":\"text/plain\"}", s);
    }

    @Test
    public void testDeserialize() {
        Gson gson = getGson();
        MediaTypeHolder holder = gson.fromJson("{\"mediaType\":\"text/plain\"}", MediaTypeHolder.class);
        assertNotNull(holder);
        assertEquals(new MediaType("text/plain"), holder.getMediaType());
    }

    static class MediaTypeHolder {

        private MediaType mediaType;

        public MediaType getMediaType() {
            return mediaType;
        }

        public void setMediaType(MediaType mediaType) {
            this.mediaType = mediaType;
        }

    }

}
