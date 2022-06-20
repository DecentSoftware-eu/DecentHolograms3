package eu.decentsoftware.holograms.api.component.line.content.objects;

import eu.decentsoftware.holograms.api.hooks.HDB;
import eu.decentsoftware.holograms.api.hooks.PAPI;
import eu.decentsoftware.holograms.api.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a wrapper for an item, that can be used in a line.
 *
 * @author d0by
 * @since 3.0.0
 */
public class LineItemStack {

    private final Material material;
    private final String owner;
    private final String texture;
    private final String nbt;
    private final String hdbId;
    private final boolean enchanted;

    private LineItemStack(@NotNull Material material, String owner, String texture, String nbt, String hdbId, boolean enchanted) {
        this.material = material;
        this.owner = owner;
        this.texture = texture;
        this.nbt = nbt;
        this.hdbId = hdbId;
        this.enchanted = enchanted;
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
    public ItemStack toItemStack(Player player) {
        ItemBuilder builder;
        if (hdbId != null) {
            builder = new ItemBuilder(HDB.getHeadItemStackById(hdbId));
        } else {
            builder = new ItemBuilder(material);
        }

        // Add NBT data
        if (nbt != null) {
            builder.withNBT(player == null ? nbt : PAPI.setPlaceholders(player, nbt));
        }

        // Make it enchanted
        if (enchanted) {
            builder.withEnchantment(Enchantment.DURABILITY, 0);
        }

        // Add texture
        if (texture != null) {
            builder.withSkullTexture(texture);
        } else if (owner != null) {
            if ("@".equals(owner) && player != null) {
                builder.withSkullOwner(player.getName());
            } else {
                builder.withSkullOwner(player == null ? owner : PAPI.setPlaceholders(player, owner));
            }
        }

        return builder.build();
    }

    /**
     * Create a new instance of {@link LineItemStack} from the given {@link ItemStack}.
     *
     * @param itemStack The {@link ItemStack} to create the wrapper from.
     * @return The created {@link LineItemStack}.
     */
    @NotNull
    public static LineItemStack fromItemStack(@NotNull ItemStack itemStack) {
        ItemBuilder builder = new ItemBuilder(itemStack);
        return new LineItemStack(
                itemStack.getType(),
                builder.getSkullOwner(),
                builder.getSkullTexture(),
                null, // TODO: get NBT data from itemStack
                null,
                !itemStack.getEnchantments().isEmpty()
        );
    }

    public static LineItemStack fromString(@NotNull String content) {

        // TODO: Implement
        return null;
    }

}
