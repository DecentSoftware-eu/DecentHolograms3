package eu.decentsoftware.holograms.conditions.serialization;

import com.google.gson.*;
import eu.decentsoftware.holograms.conditions.Condition;
import eu.decentsoftware.holograms.conditions.ConditionType;

import java.lang.reflect.Type;

/**
 * This class serves as a serializer and deserializer for {@link Condition}s.
 * It is used to serialize and deserialize {@link Condition}s to and from JSON.
 *
 * @author d0by
 * @since 3.0.0
 */
public class ConditionSerializer implements JsonSerializer<Condition>, JsonDeserializer<Condition> {

    @Override
    public Condition deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            final JsonObject parent = json.getAsJsonObject();

            // First we need to get the type of the Condition, because we need to know which class to
            // deserialize to.
            final String typeName = parent.get("type").getAsString();
            final ConditionType type = ConditionType.valueOf(typeName);

            // Then we can simply use the context to deserialize the object. We need to pass the
            // class of the Condition, because the context doesn't know which class to deserialize to.
            return context.deserialize(json, type.getConditionClass());
        } catch (Exception e) {
            throw new JsonParseException("Could not deserialize Condition", e);
        }
    }

    @Override
    public JsonElement serialize(Condition src, Type typeOfSrc, JsonSerializationContext context) {
        try {
            final JsonObject parent = new JsonObject();

            // We need to add the type to the JSON, because the Condition objects don't have a type field,
            // it is only determined by the class.
            final ConditionType type = ConditionType.fromClass(src.getClass());
            parent.addProperty("type", type.name());

            // Then we can simply use the context to serialize the object and merge the JSON objects
            // together.
            final JsonElement child = context.serialize(src, src.getClass());
            child.getAsJsonObject().entrySet().forEach(entry -> parent.add(entry.getKey(), entry.getValue()));

            return parent;
        } catch (Exception e) {
            throw new JsonParseException("Could not serialize Condition", e);
        }
    }

}
