package wwmm.pubcrawler.core.data.gson;

import com.google.gson.*;
import wwmm.pubcrawler.core.types.Doi;

import java.lang.reflect.Type;

/**
 * @author Sam Adams
 */
public class DoiAdapter implements JsonSerializer<Doi>, JsonDeserializer<Doi> {

    public Doi deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return new Doi(json.getAsString());
    }

    public JsonElement serialize(Doi src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getValue());
    }
}
