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

package eu.decentsoftware.holograms.components.serialization;

import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.lang.reflect.Type;

public class LocationSerializer implements JsonSerializer<Location>, JsonDeserializer<Location> {

    @Override
    public Location deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject parent = json.getAsJsonObject();
        World world = Bukkit.getWorld(parent.get("world").getAsString());
        double x = parent.get("x").getAsDouble();
        double y = parent.get("y").getAsDouble();
        double z = parent.get("z").getAsDouble();
        float yaw = parent.get("yaw").getAsFloat();
        float pitch = parent.get("pitch").getAsFloat();
        return new Location(world, x, y, z, yaw, pitch);
    }

    @Override
    public JsonElement serialize(Location src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject parent = new JsonObject();
        parent.addProperty("world", src.getWorld().getName());
        parent.addProperty("x", src.getX());
        parent.addProperty("y", src.getY());
        parent.addProperty("z", src.getZ());
        parent.addProperty("yaw", src.getYaw());
        parent.addProperty("pitch", src.getPitch());
        return parent;
    }

}
