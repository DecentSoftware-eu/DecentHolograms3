package eu.decentsoftware.holograms.conditions;

import com.google.gson.*;
import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.conditions.Condition;
import eu.decentsoftware.holograms.api.conditions.ConditionType;

import java.lang.reflect.Type;

public class ConditionSerializer implements JsonDeserializer<Condition> {

    @Override
    public Condition deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        String typeName = object.get("type").getAsString();
        ConditionType type = DecentHolograms.getInstance().getConditionTypeRegistry().get(typeName);
        if (type == null) {
            throw new JsonParseException("Unknown condition type: " + typeName);
        }
        return type.createCondition(object);

//        String condition = json.getAsString();
//        ConditionType type;
//        if (condition.contains(":")) {
//            String[] spl = condition.split(":");
//            if (spl.length != 0) {
//                type = DecentHolograms.getInstance().getConditionTypeRegistry().get(spl[0]);
//                if (type != null) {
//                    type.createCondition(spl);
//                }
//            }
//        } else {
//            type = DecentHolograms.getInstance().getConditionTypeRegistry().get(condition);
//            if (type != null) {
//                type.createCondition();
//            }
//        }
//        return null;
    }

}
