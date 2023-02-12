package eu.decentsoftware.holograms.actions.serialization;

import com.google.gson.*;
import eu.decentsoftware.holograms.actions.Action;
import eu.decentsoftware.holograms.actions.ActionHolder;

import java.lang.reflect.Type;

public class ActionHolderSerializer implements JsonSerializer<ActionHolder>, JsonDeserializer<ActionHolder> {

    @Override
    public ActionHolder deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray elements = json.getAsJsonArray();
        ActionHolder holder = new ActionHolder();
        for (JsonElement element : elements) {
            holder.addAction(context.deserialize(element, Action.class));
        }
        return holder;
    }

    @Override
    public JsonElement serialize(ActionHolder src, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray array = new JsonArray();
        for (Action action : src.getActions()) {
            array.add(context.serialize(action));
        }
        return array;
    }

}
