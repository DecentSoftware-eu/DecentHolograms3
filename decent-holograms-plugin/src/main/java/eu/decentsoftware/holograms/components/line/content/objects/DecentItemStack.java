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

package eu.decentsoftware.holograms.components.line.content.objects;

import com.cryptomorin.xseries.XMaterial;
import eu.decentsoftware.holograms.Lang;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.utils.ItemBuilder;
import eu.decentsoftware.holograms.hooks.HDB;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * This class represents a wrapper for an item, that can be used in a line.
 *
 * @author d0by
 * @since 3.0.0
 */
@Getter
@Setter
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
@RequiredArgsConstructor
public class DecentItemStack {

    // TODO: Add support for ItemsAdder, Oraxen

    private static final DecentHologramsAPI PLUGIN = DecentHologramsAPI.getInstance();

    private @NotNull XMaterial material;
    private String owner;
    private String texture;
    private String nbt;
    private String hdbId;
    private String itemsAdder;
    private String oraxenId;
    private boolean enchanted;
    private boolean glowing;

    /**
     * Format the given {@link String} for the given {@link Player} to be used
     * in a {@link DecentItemStack}. This method replaces all PAPI placeholders
     * and replacements the correct values.
     *
     * @param string The string to format.
     * @param player The player to format the string for. (Can be null)
     * @return The formatted string.
     */
    private String formatString(@NotNull String string, @Nullable Player player) {
        return Lang.formatString(string, player);
    }

    /**
     * Create a new {@link ItemStack} from this wrapper.
     *
     * @return The created {@link ItemStack}.
     */
    public ItemStack toItemStack() {
        return toItemStack(null);
    }

    /**
     * Create a new {@link ItemStack} from this wrapper.
     *
     * @param player The player to create the item for.
     * @return The created {@link ItemStack}.
     */
    public ItemStack toItemStack(@Nullable Player player) {
        ItemBuilder builder;
        if (hdbId != null) {
            builder = new ItemBuilder(HDB.getHeadItemStackById(hdbId));
        } else {
            builder = new ItemBuilder(material.parseItem());
        }

        // Add NBT data
        if (nbt != null) {
            builder.withNBT(formatString(nbt, player));
        }

        // Make it enchanted
        if (enchanted) {
            builder.withEnchantment(Enchantment.DURABILITY, 0);
        }

        // Add texture
        if (texture != null) {
            builder.withSkullTexture(texture);
        } else if (owner != null) {
            builder.withSkullOwner(formatString(owner, player));
        }

        return builder.build();
    }

    /**
     * Create a new instance of {@link DecentItemStack} from the given {@link ItemStack}.
     *
     * @param itemStack The {@link ItemStack} to create the wrapper from.
     * @return The created {@link DecentItemStack}.
     */
    @NotNull
    public static DecentItemStack fromItemStack(@NotNull ItemStack itemStack) {
        ItemBuilder builder = new ItemBuilder(itemStack);
        return new DecentItemStack(XMaterial.matchXMaterial(itemStack.getType()))
                .owner(builder.getSkullOwner())
                .enchanted(!itemStack.getEnchantments().isEmpty());
    }

    /**
     * Parse the given {@link String} to a {@link DecentItemStack}. The string
     * must be in the format of an item line content.
     *
     * @param string The string to parse.
     * @return The parsed {@link DecentItemStack}.
     */
    @NotNull
    public static DecentItemStack fromString(@NotNull String string) {
        string = string.trim();

        String materialName = string.split(" ")[0];
        Optional<XMaterial> material = XMaterial.matchXMaterial(materialName);
        return new DecentItemStack(material.orElse(XMaterial.STONE))
                .owner(getFlagValue(string, "--owner:"))
                .texture(getFlagValue(string, "--texture:"))
                .nbt(getFlagValue(string, "--nbt:"))
                .hdbId(getFlagValue(string, "--headDataBase:"))
                .itemsAdder(getFlagValue(string, "--itemsAdder:"))
                .oraxenId(getFlagValue(string, "--oraxen:"))
                .enchanted(string.contains("--enchanted"))
                .glowing(string.contains("--glowing") || string.contains("--glow"));
    }

    /**
     * Get the value of the given flag.
     *
     * @param content The string to get the flag from.
     * @param flag    The flag to get the value of.
     * @return The value of the flag.
     */
    @Nullable
    private static String getFlagValue(@NotNull String content, @NotNull String flag) {
        if (content.contains(flag)) {
            return content.split(flag)[1].split(" ")[0];
        }
        return null;
    }

}
