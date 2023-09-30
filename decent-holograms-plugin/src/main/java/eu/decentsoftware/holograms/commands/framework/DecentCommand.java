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

package eu.decentsoftware.holograms.commands.framework;

import eu.decentsoftware.holograms.Lang;
import eu.decentsoftware.holograms.commands.framework.arguments.Arguments;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Represents a command that can be executed by a {@link CommandSender}. This
 * class is a wrapper around {@link Command} that allows for easier command
 * creation.
 *
 * @author d0by
 * @since 3.0.0
 */
@Getter
@Setter
public abstract class DecentCommand extends Command {

    protected static final List<DecentCommand> COMMANDS = new ArrayList<>();
    protected final List<DecentCommand> subCommands = new ArrayList<>();
    protected boolean playerOnly = false;
    protected boolean hidden = false;

    protected DecentCommand(
            final @NonNull String name,
            final @Nullable String permission,
            final @NonNull String syntax,
            final @NonNull List<String> description,
            final @NonNull String... aliases
    ) {
        this(name, permission, syntax, description, Arrays.asList(aliases));
    }

    protected DecentCommand(
            final @NonNull String name,
            final @Nullable String permission,
            final @NonNull String syntax,
            final @NonNull List<String> description,
            final @NonNull List<String> aliases
    ) {
        super(name, String.join("\n", description), syntax, aliases);
        setPermission(permission);
        COMMANDS.add(this);
    }

    public abstract boolean execute(@NonNull CommandSender sender, @NonNull Arguments args);

    public List<String> tabComplete(@NonNull CommandSender sender, @NonNull Arguments args) {
        return null;
    }

    @Override
    public boolean execute(final @NonNull CommandSender sender, final @NonNull String label, final @NonNull String[] args) {
        if (!testPermissionSilent(sender)) {
            Lang.confTell(sender, "no_permission");
            return true;
        }

        if (playerOnly && !(sender instanceof Player)) {
            Lang.confTell(sender, "only_players");
            return true;
        }

        if (args.length > 0) {
            for (DecentCommand subCommand : subCommands) {
                if (!subCommand.isAlias(args[0])) {
                    continue;
                }

                if (!subCommand.execute(sender, label, Arrays.copyOfRange(args, 1, args.length))) {
                    subCommand.sendDescription(sender);
                }
                return true;
            }
        }

        if (!execute(sender, new Arguments(Arrays.asList(args)))) {
            sendDescription(sender);
        }
        return true;
    }

    @NonNull
    @Override
    public List<String> tabComplete(final @NonNull CommandSender sender, final @NonNull String alias, final @NonNull String[] args) throws IllegalArgumentException {
        if (!testPermissionSilent(sender)) {
            return Collections.emptyList();
        }

        if (playerOnly && !(sender instanceof Player)) {
            return Collections.emptyList();
        }

        final Arguments arguments = new Arguments(Arrays.asList(args));
        for (DecentCommand subCommand : subCommands) {
            if (subCommand.isAlias(args[0])) {
                return subCommand.tabComplete(sender, arguments);
            }
        }

        return tabComplete(sender, arguments);
    }

    /**
     * Checks if the given string is a name or o valid alias of this command.
     *
     * @param alias The alias to check.
     * @return True if the given string is a valid alias for this command, false otherwise.
     */
    public boolean isAlias(final @NonNull String alias) {
        if (getName().equalsIgnoreCase(alias)) {
            return true;
        }

        for (String commandAlias : getAliases()) {
            if (commandAlias.equalsIgnoreCase(alias)) {
                return true;
            }
        }

        return false;
    }

    // ==================== UTILITY METHODS ==================== //

    protected void sendDescription(final @NonNull CommandSender sender) {
        Lang.tell(sender, description);
    }

}
