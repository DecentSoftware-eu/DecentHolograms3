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

import lombok.NonNull;
import net.md_5.bungee.api.chat.*;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Contract;

/**
 * This class is a wrapper for the {@link ComponentBuilder} class. It provides
 * a few methods to simplify the creation of chat component messages.
 *
 * @author d0by
 * @since 3.0.0
 */
public class ComponentMessage {

    private final @NonNull ComponentBuilder builder;

    /**
     * Creates a new instance of the {@link ComponentMessage} class
     * with no text.
     */
    public ComponentMessage() {
        this(new ComponentBuilder(""));
    }

    /**
     * Creates a new instance of the {@link ComponentMessage} class
     * with the given text.
     *
     * @param text The text to be displayed.
     */
    public ComponentMessage(@NonNull String text) {
        this(new ComponentBuilder(text));
    }

    /**
     * Creates a new instance of the {@link ComponentMessage} class
     * from the given {@link ComponentBuilder}.
     *
     * @param builder The {@link ComponentBuilder} to be used.
     */
    @Contract(pure = true)
    public ComponentMessage(@NonNull ComponentBuilder builder) {
        this.builder = builder;
    }

    /**
     * Send this message to the given {@link CommandSender}.
     *
     * @param commandSender The command sender to send the message to.
     */
    public void send(@NonNull CommandSender commandSender) {
        commandSender.spigot().sendMessage(build());
    }

    /**
     * Add a new line to the message.
     *
     * @return This message.
     */
    @NonNull
    public ComponentBuilder newLine() {
        builder.append("\n");
        return builder;
    }

    /**
     * Add the given {@link BaseComponent}s to the message.
     *
     * @param components The {@link BaseComponent}s to be added.
     * @return This message.
     */
    @NonNull
    public ComponentMessage append(@NonNull BaseComponent... components) {
        builder.append(components);
        return this;
    }

    /**
     * Append the message with the given text.
     *
     * @param text The text to be added.
     * @return This message.
     */
    @NonNull
    public ComponentMessage append(@NonNull String text) {
        builder.append(text);
        return this;
    }

    /**
     * Append the message with the given text on a new line.
     *
     * @param text The text to be added.
     * @return This message.
     */
    @NonNull
    public ComponentMessage appendLine(@NonNull String text) {
        return this.append("\n" + text);
    }

    /**
     * Add the given hover text to the message.
     *
     * @param text The text to be displayed.
     * @return This message.
     */
    @NonNull
    public ComponentMessage hoverText(@NonNull String text) {
        builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(text)));
        return this;
    }

    /**
     * Add the given command to be executed on click.
     *
     * @param command The command to be executed.
     * @return This message.
     */
    @NonNull
    public ComponentMessage clickCommand(@NonNull String command) {
        builder.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        return this;
    }

    /**
     * Add the given command to be suggested on click.
     *
     * @param command The command to be suggested.
     * @return This message.
     */
    @NonNull
    public ComponentMessage clickSuggest(@NonNull String command) {
        builder.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
        return this;
    }

    /**
     * Add the given url to be opened on click.
     *
     * @param url The url to be opened.
     * @return This message.
     */
    @NonNull
    public ComponentMessage clickOpenUrl(@NonNull String url) {
        builder.event(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
        return this;
    }

    /**
     * Get the {@link ComponentBuilder} used by this message.
     *
     * @return The {@link ComponentBuilder} used by this message.
     */
    @NonNull
    public ComponentBuilder getBuilder() {
        return builder;
    }

    /**
     * Build this message.
     *
     * @return The array of {@link BaseComponent}s representing this message.
     */
    @NonNull
    public BaseComponent[] build() {
        return builder.create();
    }

}