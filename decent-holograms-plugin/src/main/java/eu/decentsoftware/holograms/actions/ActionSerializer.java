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

package eu.decentsoftware.holograms.actions;

import com.google.gson.*;

import java.lang.reflect.Type;

public class ActionSerializer implements JsonDeserializer<Action> {

    @Override
    public Action deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        // TODO
//        JsonObject object = json.getAsJsonObject();
//        String typeName = object.get("type").getAsString();
//        ActionType type = DecentHolograms.getInstance().getActionTypeRegistry().get(typeName);
//        if (type == null) {
//            throw new JsonParseException("Unknown condition type: " + typeName);
//        }
//        return type.createAction(object);

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
        return null;
    }

}
