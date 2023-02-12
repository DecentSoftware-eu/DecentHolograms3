package eu.decentsoftware.holograms.actions;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * This class serves as a serializer and deserializer for {@link Action}s.
 * It is used to serialize and deserialize {@link Action}s to and from JSON.
 *
 * @author d0by
 * @since 3.0.0
 */
public class ActionSerializer implements JsonSerializer<Action>, JsonDeserializer<Action> {

    @Override
    public Action deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            final JsonObject parent = json.getAsJsonObject();

            // First we need to get the type of the Action, because we need to know which class to
            // deserialize to.
            final String typeName = parent.get("type").getAsString();
            final ActionType type = ActionType.valueOf(typeName);

            // Then we can simply use the context to deserialize the object. We need to pass the
            // class of the Action, because the context doesn't know which class to deserialize to.
            return context.deserialize(json, type.getActionClass());
        } catch (Exception e) {
            throw new JsonParseException("Could not deserialize Action", e);
        }
    }

    @Override
    public JsonElement serialize(Action src, Type typeOfSrc, JsonSerializationContext context) {
        try {
            final JsonObject parent = new JsonObject();

            // We need to add the type to the JSON, because the Action objects don't have a type field,
            // it is only determined by the class.
            final ActionType type = ActionType.fromClass(src.getClass());
            parent.addProperty("type", type.name());

            // Then we can simply use the context to serialize the object and merge the JSON objects
            // together.
            final JsonElement child = context.serialize(src, src.getClass());
            child.getAsJsonObject().entrySet().forEach(entry -> parent.add(entry.getKey(), entry.getValue()));

            return parent;
        } catch (Exception e) {
            throw new JsonParseException("Could not serialize Action", e);
        }
    }

}
