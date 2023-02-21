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

import com.google.common.collect.Lists;
import dev.dbassett.skullcreator.SkullCreator;
import eu.decentsoftware.holograms.nms.utils.Version;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * A simple ItemStack builder.
 *
 * @author d0by
 */
@SuppressWarnings({"deprecation", "unused"})
public class ItemBuilder implements Cloneable {

    private static Field PROFILE_FIELD;
    private ItemStack itemStack;

    /*
     *	Constructors
     */

    public ItemBuilder(Material material) {
        this(material, 1);
    }

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemBuilder(Material material, int amount) {
        this(new ItemStack(material, amount));
    }

    public ItemBuilder(Material material, int amount, short durability) {
        this(new ItemStack(material, amount, durability));
    }

    /*
     *	General Methods
     */

    @Override
    public ItemBuilder clone() {
        return new ItemBuilder(itemStack);
    }

    public ItemStack toItemStack() {
        return itemStack;
    }

    public ItemStack build() {
        return itemStack;
    }

    public ItemBuilder withItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
        return this;
    }

    /*
     *	Item Methods
     */

    public ItemBuilder withMaterial(Material material) {
        itemStack.setType(material);
        return this;
    }

    public ItemBuilder withAmount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder withDurability(short durability) {
        itemStack.setDurability(durability);
        return this;
    }

    public ItemBuilder withInfiniteDurability() {
        itemStack.setDurability(Short.MAX_VALUE);
        return this;
    }

    public ItemBuilder withName(String displayName) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(Common.colorize(displayName));
        }
        itemStack.setItemMeta(meta);
        return this;
    }

    public String getName() {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            return meta.getDisplayName();
        }
        return null;
    }

    public ItemBuilder withEmptyName() {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(" ");
        }
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder withLore(List<String> lore) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            meta.setLore(Common.colorize(lore));
        }
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder withLore(String... lore) {
        return withLore(Arrays.asList(lore));
    }

    public ItemBuilder withLoreLine(String line) {
        List<String> lore = getLore();
        if (lore != null) {
            lore.add(line);
            withLore(lore);
        } else {
            withLore(line);
        }
        return this;
    }

    public ItemBuilder withLoreLines(String... lines) {
        return withLoreLines(Arrays.asList(lines));
    }

    public ItemBuilder withLoreLines(List<String> lines) {
        List<String> lore = getLore();
        if (lore != null) {
            lore.addAll(lines);
            withLore(lore);
        } else {
            withLore(lines);
        }
        return this;
    }

    public List<String> getLore() {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            return meta.getLore();
        }
        return Lists.newArrayList();
    }

    public ItemBuilder removeLore() {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            meta.setLore(null);
        }
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder withItemFlags(ItemFlag... flags) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            meta.addItemFlags(flags);
        }
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder removeItemFlags(ItemFlag... flags) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            meta.removeItemFlags(flags);
        }
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder withEnchantment(Enchantment enchantment, int level) {
        itemStack.removeEnchantment(enchantment);
        itemStack.addEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder withUnsafeEnchantment(Enchantment enchantment, int level) {
        itemStack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder removeEnchantment(Enchantment enchantment) {
        itemStack.removeEnchantment(enchantment);
        return this;
    }

    public ItemBuilder withDyeColor(DyeColor color) {
        itemStack.setDurability(color.getDyeData());
        return this;
    }

    public ItemBuilder withNBT(String nbt) {
        try {
            Bukkit.getUnsafe().modifyItemStack(itemStack, nbt);
        } catch (Throwable ignored) {
        }
        return this;
    }

    /*
     *	Item Meta Methods
     */

    public ItemBuilder withLeatherArmorColor(Color color) {
        try {
            LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
            if (meta != null) {
                meta.setColor(color);
            }
            itemStack.setItemMeta(meta);
        } catch (ClassCastException ignored) {
        }
        return this;
    }

    public ItemBuilder withPotionType(PotionEffectType type) {
        try {
            PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
            if (meta != null) {
                meta.setMainEffect(type);
            }
            itemStack.setItemMeta(meta);
        } catch (ClassCastException ignored) {
        }
        return this;
    }

    public ItemBuilder withCustomPotionEffect(PotionEffect effect, boolean overwrite) {
        try {
            PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
            if (meta != null) {
                meta.addCustomEffect(effect, overwrite);
            }
            itemStack.setItemMeta(meta);
        } catch (ClassCastException ignored) {
        }
        return this;
    }

    public ItemBuilder removeCustomPotionEffect(PotionEffectType type) {
        try {
            PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
            if (meta != null) {
                meta.removeCustomEffect(type);
            }
            itemStack.setItemMeta(meta);
        } catch (ClassCastException ignored) {
        }
        return this;
    }

    public ItemBuilder clearCustomPotionEffects() {
        try {
            PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
            if (meta != null) {
                meta.clearCustomEffects();
            }
            itemStack.setItemMeta(meta);
        } catch (ClassCastException ignored) {
        }
        return this;
    }

    public ItemBuilder withSkullOwner(String playerName) {
        try {
            SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
            if (meta != null) meta.setOwner(playerName);
            itemStack.setItemMeta(meta);
            if (Version.before(13)) {
                this.withDurability((short) SkullType.PLAYER.ordinal());
            }
        } catch (ClassCastException ignored) {
        }
        return this;
    }

    public String getSkullOwner() {
        try {
            SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
            if (meta != null) return meta.getOwner();
        } catch (ClassCastException ignored) {
        }
        return null;
    }

    public ItemBuilder withSkullTexture(String texture) {
        this.itemStack = SkullCreator.itemWithBase64(this.itemStack, texture);
        return this;
    }

    public ItemBuilder withData(MaterialData data) {
        itemStack.setData(data);
        return this;
    }

    public ItemBuilder withMeta(ItemMeta meta) {
        itemStack.setItemMeta(meta);
        return this;
    }

}