/*
 * DecentHolograms
 * Copyright (C) DecentSoftware.eu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.decentsoftware.holograms.actions.serialization;

import com.google.gson.*;
import eu.decentsoftware.holograms.actions.Action;
import eu.decentsoftware.holograms.actions.ActionHolder;
import eu.decentsoftware.holograms.actions.ActionType;
import lombok.NonNull;

import java.lang.reflect.Type;

public class ActionHolderSerializer implements JsonSerializer<ActionHolder>, JsonDeserializer<ActionHolder> {

    @Override
    public ActionHolder deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray elements = json.getAsJsonArray();
        ActionHolder holder = new ActionHolder();
        for (JsonElement element : elements) {
            JsonObject object = element.getAsJsonObject();
            String typeName = object.get("type").getAsString();
            ActionType type = ActionType.byNameOrAlias(typeName);
            if (type == null) {
                throw new JsonParseException("Could not deserialize ActionHolder: " + typeName + " is not a valid ActionType");
            }

            holder.addAction(context.deserialize(element, type.getActionClass()));
        }
        return holder;
    }

    @Override
    public JsonElement serialize(ActionHolder src, Type typeOfSrc, JsonSerializationContext context) {
        if (src.getActions().isEmpty()) {
            return null;
        }
        JsonArray array = new JsonArray();
        for (Action action : src.getActions()) {
            array.add(serializeAction(action, context));
        }
        return array;
    }

    @NonNull
    private JsonObject serializeAction(@NonNull Action action, @NonNull JsonSerializationContext context) {
        JsonObject object = context.serialize(action).getAsJsonObject();
        if (object.has("type")) {
            return object;
        }

        ActionType type = ActionType.byClass(action.getClass());
        if (type == null) {
            throw new JsonParseException("Could not serialize ActionHolder: " + action.getClass().getName() + " is not a valid ActionType");
        }
        object.addProperty("type", type.name());
        return object;
    }

}
