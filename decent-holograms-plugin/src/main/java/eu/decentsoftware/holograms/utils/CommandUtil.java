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

package eu.decentsoftware.holograms.utils;

import eu.decentsoftware.holograms.nms.utils.ReflectField;
import eu.decentsoftware.holograms.nms.utils.ReflectUtil;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Utility class for commands.
 *
 * @author d0by
 * @since 3.0.0
 */
@UtilityClass
public final class CommandUtil {

    private static final Class<?> CRAFT_SERVER_CLASS;
    private static final Method GET_COMMAND_MAP_METHOD;
    private static final ReflectField COMMAND_MAP_KNOWN_COMMANDS_FIELD;

    static {
        CRAFT_SERVER_CLASS = ReflectUtil.getObcClass("CraftServer");
        GET_COMMAND_MAP_METHOD = ReflectUtil.getMethod(CRAFT_SERVER_CLASS, "getCommandMap");
        COMMAND_MAP_KNOWN_COMMANDS_FIELD = new ReflectField(SimpleCommandMap.class, "knownCommands");
    }

    /**
     * Register a command directly to the {@link SimpleCommandMap}. This method
     * will unregister the command first if it is already registered.
     *
     * @param plugin The plugin to register the command for.
     * @param command The command to register.
     * @since 3.0.0
     */
    public static void register(final @NonNull JavaPlugin plugin, final @NonNull Command command) {
        SimpleCommandMap commandMap;
        try {
            commandMap = (SimpleCommandMap) GET_COMMAND_MAP_METHOD.invoke(Bukkit.getServer());
        } catch (IllegalAccessException | InvocationTargetException e) {
            plugin.getLogger().warning("Failed to register command " + command.getName() + "!");
            e.printStackTrace();
            return;
        }

        unregister(plugin, command);
        commandMap.register(plugin.getName(), command);
    }

    /**
     * Unregister a command directly from the {@link SimpleCommandMap}.
     *
     * @param plugin The plugin to unregister the command for.
     * @param command The command to unregister.
     * @since 3.0.0
     */
    @SuppressWarnings("unchecked")
    public static void unregister(final @NonNull JavaPlugin plugin, final @NonNull Command command) {
        SimpleCommandMap commandMap;
        Map<String, Command> cmdMap;
        try {
            commandMap = (SimpleCommandMap) GET_COMMAND_MAP_METHOD.invoke(Bukkit.getServer());
            cmdMap = (Map<String, Command>) COMMAND_MAP_KNOWN_COMMANDS_FIELD.get(commandMap);
        } catch (IllegalAccessException | InvocationTargetException e) {
            plugin.getLogger().warning("Failed to unregister command " + command.getName() + "!");
            e.printStackTrace();
            return;
        }

        if (cmdMap != null && !cmdMap.isEmpty()) {
            cmdMap.remove(command.getLabel());
            for (final String alias : command.getAliases()) {
                cmdMap.remove(alias);
            }
        }
    }

}
