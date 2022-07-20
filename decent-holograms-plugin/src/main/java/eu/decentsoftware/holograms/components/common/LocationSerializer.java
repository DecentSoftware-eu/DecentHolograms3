package eu.decentsoftware.holograms.components.common;

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
