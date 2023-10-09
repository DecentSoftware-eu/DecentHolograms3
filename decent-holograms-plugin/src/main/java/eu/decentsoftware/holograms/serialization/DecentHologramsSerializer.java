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

package eu.decentsoftware.holograms.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import eu.decentsoftware.holograms.DecentHolograms;
import lombok.NonNull;
import org.jetbrains.annotations.Contract;

import java.lang.reflect.Type;

public class DecentHologramsSerializer implements JsonDeserializer<DecentHolograms> {

    private final DecentHolograms plugin;

    @Contract(pure = true)
    public DecentHologramsSerializer(@NonNull DecentHolograms plugin) {
        this.plugin = plugin;
    }

    @Override
    public DecentHolograms deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return this.plugin;
    }

}
