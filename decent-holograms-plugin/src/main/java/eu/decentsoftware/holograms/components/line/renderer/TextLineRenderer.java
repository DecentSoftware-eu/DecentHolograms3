package eu.decentsoftware.holograms.components.line.renderer;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.component.line.Line;
import eu.decentsoftware.holograms.api.component.line.LineType;
import eu.decentsoftware.holograms.api.profile.Profile;
import eu.decentsoftware.holograms.api.utils.Common;
import eu.decentsoftware.holograms.hooks.PAPI;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class TextLineRenderer extends AbstractLineRenderer {

    private final @NotNull String text;
    private final String hoverText;
    private final int eid;

    public TextLineRenderer(@NotNull Line parent, @NotNull String text) {
        this(parent, text, null);
    }

    public TextLineRenderer(@NotNull Line parent, @NotNull String text, String hoverText) {
        super(parent, LineType.TEXT);
        this.text = text;
        this.hoverText = hoverText;
        this.eid = DecentHolograms.getFreeEntityId();
    }

    /**
     * Get the formatted text of the line for the given player.
     *
     * @param player The player to get the text for.
     * @return The formatted text of the line.
     */
    @NotNull
    private String getFormattedText(@NotNull Player player) {
        Profile profile = DecentHolograms.getInstance().getProfileRegistry().getProfile(player.getName());
        String formattedText = text;

        if (hoverText != null) {
            // Check if the player in watching the line and if so, use the hover text.
            if (profile != null && getParent().equals(profile.getContext().getWatchedLine())) {
                formattedText = hoverText;
            }
        }

        // Replace custom replacements
        formattedText = API.getReplacementRegistry().replace(formattedText, profile);
        // Replace PAPI placeholders
        formattedText = PAPI.setPlaceholders(player, formattedText);
        // Colorize
        formattedText = Common.colorize(formattedText);

        return formattedText;
    }

    @Override
    public void display(@NotNull Player player) {
        Location loc = getParent().getPositionManager().getActualLocation();
        String formattedText = getFormattedText(player);

        // Create the metadata objects
        Object metaEntity = NMS.getMetaEntityProperties(false, false, false, false, true, false, false);
        Object metaGravity = NMS.getMetaEntityGravity(false);
        Object metaArmorStand = NMS.getMetaArmorStandProperties(false, false, true, true);
        Object metaName = NMS.getMetaEntityCustomName(formattedText);
        Object metaNameVisible = NMS.getMetaEntityCustomNameVisible(!formattedText.isEmpty());

        // Spawn the fake armor stand entity
        NMS.spawnEntityLiving(player, eid, UUID.randomUUID(), EntityType.ARMOR_STAND, loc);
        // Send the metadata
        NMS.sendEntityMetadata(player, eid, metaEntity, metaGravity, metaArmorStand, metaName, metaNameVisible);
    }

    @Override
    public void update(@NotNull Player player) {
        String formattedText = getFormattedText(player);

        // Create the metadata objects
        Object metaName = NMS.getMetaEntityCustomName(formattedText);
        Object metaNameVisible = NMS.getMetaEntityCustomNameVisible(!formattedText.isEmpty());

        // Send the metadata
        NMS.sendEntityMetadata(player, eid, metaName, metaNameVisible);
    }

    @Override
    public void hide(@NotNull Player player) {
        // Destroy the fake armor stand entity
        NMS.removeEntity(player, eid);
    }

    @Override
    public void teleport(@NotNull Player player, @NotNull Location location) {
        // Teleport the fake armor stand entity
        NMS.teleportEntity(player, eid, location, false);
    }

}
