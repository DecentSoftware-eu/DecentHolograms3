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

import java.lang.reflect.Type;

public class ActionHolderSerializer implements JsonSerializer<ActionHolder>, JsonDeserializer<ActionHolder> {

    @Override
    public ActionHolder deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray elements = json.getAsJsonArray();
        ActionHolder holder = new ActionHolder();
        for (JsonElement element : elements) {
            holder.addAction(context.deserialize(element, Action.class));
        }
        return holder;
    }

    @Override
    public JsonElement serialize(ActionHolder src, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray array = new JsonArray();
        for (Action action : src.getActions()) {
            array.add(context.serialize(action));
        }
        return array;
    }

}
