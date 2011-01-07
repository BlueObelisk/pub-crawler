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

import com.google.gson.*;
import org.joda.time.LocalDate;

import java.lang.reflect.Type;

/**
 * @author Sam Adams
 */
public class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        int day = obj.getAsJsonPrimitive("day").getAsInt();
        int month = obj.getAsJsonPrimitive("month").getAsInt();
        int year = obj.getAsJsonPrimitive("year").getAsInt();
        return new LocalDate(year, month, day);
    }

    public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("day", src.getDayOfMonth());
        object.addProperty("month", src.getMonthOfYear());
        object.addProperty("year", src.getYear());
        return object;
    }

}
