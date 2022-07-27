package eu.decentsoftware.holograms.conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.conditions.Condition;
import eu.decentsoftware.holograms.api.conditions.ConditionType;

import java.lang.reflect.Type;

public class ConditionSerializer implements JsonDeserializer<Condition> {

    @Override
    public Condition deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String condition = json.getAsString();
        ConditionType type;
        if (condition.contains(":")) {
            String[] spl = condition.split(":");
            if (spl.length != 0) {
                type = DecentHologramsAPI.getInstance().getConditionTypeRegistry().get(spl[0]);
                if (type != null) {
                    type.createCondition(spl);
                }
            }
        } else {
            type = DecentHologramsAPI.getInstance().getConditionTypeRegistry().get(condition);
            if (type != null) {
                type.createCondition();
            }
        }
        return null;
    }

}
