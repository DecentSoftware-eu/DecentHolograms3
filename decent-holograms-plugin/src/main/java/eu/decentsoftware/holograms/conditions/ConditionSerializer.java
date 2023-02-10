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

package eu.decentsoftware.holograms.conditions;

import com.google.gson.*;

import java.lang.reflect.Type;

public class ConditionSerializer implements JsonDeserializer<Condition> {

    @Override
    public Condition deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        // TODO
//        JsonObject object = json.getAsJsonObject();
//        String typeName = object.get("type").getAsString();
//        ConditionType type = DecentHolograms.getInstance().getConditionTypeRegistry().get(typeName);
//        if (type == null) {
//            throw new JsonParseException("Unknown condition type: " + typeName);
//        }
//        return type.createCondition(object);

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
        return null;
    }

}
