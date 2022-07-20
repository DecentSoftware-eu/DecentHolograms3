package eu.decentsoftware.holograms.actions;

import com.google.gson.*;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.actions.Action;
import eu.decentsoftware.holograms.api.actions.ActionType;

import java.lang.reflect.Type;

public class DefaultActionDeserializer implements JsonDeserializer<Action> {

    @Override
    public Action deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject parent = json.getAsJsonObject();
        ActionType type = DecentHologramsAPI.getInstance().getActionTypeRegistry().get(parent.get("type").getAsString());
        if (type == null) {
            throw new JsonParseException("Unknown action type: " + parent.get("type").getAsString());
        }
        return type.createAction(parent);
    }

}
