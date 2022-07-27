package eu.decentsoftware.holograms.actions;

import com.google.gson.*;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.actions.Action;
import eu.decentsoftware.holograms.api.actions.ActionType;

import java.lang.reflect.Type;

public class ActionSerializer implements JsonDeserializer<Action> {

    @Override
    public Action deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String action = json.getAsString();
        ActionType type;
        if (action.contains(":")) {
            String[] spl = action.split(":");
            if (spl.length != 0) {
                type = DecentHologramsAPI.getInstance().getActionTypeRegistry().get(spl[0]);
                if (type != null) {
                    type.createAction(spl);
                }
            }
        } else {
            type = DecentHologramsAPI.getInstance().getActionTypeRegistry().get(action);
            if (type != null) {
                type.createAction();
            }
        }
        return null;
    }

}
