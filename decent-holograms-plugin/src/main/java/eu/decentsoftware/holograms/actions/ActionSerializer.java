package eu.decentsoftware.holograms.actions;

import com.google.gson.*;
import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.actions.Action;
import eu.decentsoftware.holograms.api.actions.ActionType;

import java.lang.reflect.Type;

public class ActionSerializer implements JsonDeserializer<Action> {

    @Override
    public Action deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        String typeName = object.get("type").getAsString();
        ActionType type = DecentHolograms.getInstance().getActionTypeRegistry().get(typeName);
        if (type == null) {
            throw new JsonParseException("Unknown condition type: " + typeName);
        }
        return type.createAction(object);

//        String action = json.getAsString();
//        ActionType type;
//        if (action.contains(":")) {
//            String[] spl = action.split(":");
//            if (spl.length != 0) {
//                type = DecentHolograms.getInstance().getActionTypeRegistry().get(spl[0]);
//                if (type != null) {
//                    type.createAction(spl);
//                }
//            }
//        } else {
//            type = DecentHolograms.getInstance().getActionTypeRegistry().get(action);
//            if (type != null) {
//                type.createAction();
//            }
//        }
//        return null;
    }

}
