package eu.decentsoftware.holograms.conditions.serialization;

import com.google.gson.*;
import eu.decentsoftware.holograms.conditions.Condition;
import eu.decentsoftware.holograms.conditions.ConditionHolder;

import java.lang.reflect.Type;

public class ConditionHolderSerializer implements JsonSerializer<ConditionHolder>, JsonDeserializer<ConditionHolder> {

    @Override
    public ConditionHolder deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray elements = json.getAsJsonArray();
        ConditionHolder holder = new ConditionHolder();
        for (JsonElement element : elements) {
            holder.addCondition(context.deserialize(element, Condition.class));
        }
        return holder;
    }

    @Override
    public JsonElement serialize(ConditionHolder src, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray array = new JsonArray();
        for (Condition condition : src.getConditions()) {
            array.add(context.serialize(condition));
        }
        return array;
    }

}
