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

package eu.decentsoftware.holograms.conditions.serialization;

import com.google.gson.*;
import eu.decentsoftware.holograms.conditions.Condition;
import eu.decentsoftware.holograms.conditions.ConditionHolder;
import eu.decentsoftware.holograms.conditions.ConditionType;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public class ConditionHolderSerializer implements JsonSerializer<ConditionHolder>, JsonDeserializer<ConditionHolder> {

    @Override
    public ConditionHolder deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray elements = json.getAsJsonArray();
        ConditionHolder holder = new ConditionHolder();
        for (JsonElement element : elements) {
            JsonObject object = element.getAsJsonObject();
            String typeName = object.get("type").getAsString();
            ConditionType type = ConditionType.fromString(typeName);
            if (type == null) {
                throw new JsonParseException("Could not deserialize ConditionHolder: " + typeName + " is not a valid ConditionType");
            }

            holder.addCondition(context.deserialize(element, type.getConditionClass()));
        }
        return holder;
    }

    @Override
    public JsonElement serialize(ConditionHolder src, Type typeOfSrc, JsonSerializationContext context) {
        if (src.getConditions().isEmpty()) {
            return null;
        }

        JsonArray array = new JsonArray();
        for (Condition condition : src.getConditions()) {
            array.add(serializeCondition(condition, context));
        }
        return array;
    }

    @NotNull
    private JsonElement serializeCondition(Condition condition, JsonSerializationContext context) {
        JsonObject object = context.serialize(condition).getAsJsonObject();
        if (object.has("type")) {
            return object;
        }

        ConditionType type = ConditionType.fromClass(condition.getClass());
        if (type == null) {
            throw new JsonParseException("Could not serialize ConditionHolder: " + condition.getClass().getName() + " is not a valid ConditionType");
        }
        object.addProperty("type", type.name());
        return object;
    }

}
