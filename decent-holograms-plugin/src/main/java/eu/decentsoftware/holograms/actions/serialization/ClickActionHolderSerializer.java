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
import eu.decentsoftware.holograms.actions.ActionType;
import eu.decentsoftware.holograms.actions.ClickActionHolder;
import eu.decentsoftware.holograms.api.hologram.click.ClickType;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public class ClickActionHolderSerializer implements JsonSerializer<ClickActionHolder>, JsonDeserializer<ClickActionHolder> {

    @Override
    public ClickActionHolder deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject elements = json.getAsJsonObject();
        ClickActionHolder holder = new ClickActionHolder();
        for (ClickType clickType : ClickType.values()) {
            JsonArray array = elements.getAsJsonArray(clickType.name());
            if (array == null) {
                continue;
            }
            for (JsonElement element : array) {
                JsonObject object = element.getAsJsonObject();
                String typeName = object.get("type").getAsString();
                ActionType type = ActionType.fromString(typeName);
                if (type == null) {
                    throw new JsonParseException("Could not deserialize ActionHolder: " + typeName + " is not a valid ActionType");
                }

                holder.addAction(clickType, context.deserialize(element, type.getActionClass()));
            }
        }
        return holder;
    }

    @Override
    public JsonElement serialize(ClickActionHolder src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        for (ClickType clickType : ClickType.values()) {
            if (src.getActions(clickType).isEmpty()) {
                return null;
            }
            JsonArray array = new JsonArray();
            for (Action action : src.getActions(clickType)) {
                array.add(serializeAction(action, context));
            }
            object.add(clickType.name(), array);
        }
        return object;
    }

    @NotNull
    private JsonObject serializeAction(@NotNull Action action, @NotNull JsonSerializationContext context) {
        JsonObject object = context.serialize(action).getAsJsonObject();
        if (object.has("type")) {
            return object;
        }

        ActionType type = ActionType.fromClass(action.getClass());
        if (type == null) {
            throw new JsonParseException("Could not serialize ActionHolder: " + action.getClass().getName() + " is not a valid ActionType");
        }
        object.addProperty("type", type.name());
        return object;
    }

}