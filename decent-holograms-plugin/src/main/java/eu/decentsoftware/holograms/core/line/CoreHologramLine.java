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

package eu.decentsoftware.holograms.core.line;

import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.api.hologram.HologramLineType;
import eu.decentsoftware.holograms.api.util.DecentLocation;
import eu.decentsoftware.holograms.core.CoreHologramComponent;
import eu.decentsoftware.holograms.core.CoreHologramPage;
import eu.decentsoftware.holograms.core.line.renderer.LineRenderer;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class CoreHologramLine extends CoreHologramComponent {

    protected final DecentHolograms plugin;
    protected final CoreHologramPage<? extends CoreHologramLine> parent;
    protected DecentLocation location;
    protected double typeYOffset = 0.0d;
    protected CoreHologramLineSettings settings;
    protected LineRenderer renderer;
    protected String rawContent;

    public CoreHologramLine(
            @NonNull DecentHolograms plugin,
            @NonNull CoreHologramPage<? extends CoreHologramLine> parent,
            @NonNull DecentLocation location
    ) {
        this(plugin, parent, location, new CoreHologramLineSettings());
    }

    public CoreHologramLine(
            @NonNull DecentHolograms plugin,
            @NonNull CoreHologramPage<? extends CoreHologramLine> parent,
            @NonNull DecentLocation location,
            @NonNull CoreHologramLineSettings settings
    ) {
        this.plugin = plugin;
        this.parent = parent;
        this.location = location;
        this.settings = settings;
    }

    public void destroy() {
        checkDestroyed();

        if (this.renderer != null) {
            this.renderer.destroy();
            this.renderer = null;
        }

        super.destroy();
    }

    /**
     * Display this line to the specified player.
     *
     * @param player The player to display this line to.
     */
    public void display(@NonNull Player player) {
        checkDestroyed();

        if (this.renderer != null) {
            this.renderer.display(player);
        }
    }

    /**
     * Hide this line from the specified player.
     *
     * @param player The player to hide this line from.
     */
    public void hide(@NonNull Player player) {
        checkDestroyed();

        if (this.renderer != null) {
            this.renderer.hide(player);
        }
    }

    /**
     * Update the content of this line for the specified player.
     *
     * @param player The player to update this line for.
     */
    public void updateContent(@NonNull Player player) {
        checkDestroyed();

        if (this.renderer != null) {
            this.renderer.updateContent(player);
        }
    }

    /**
     * Update the location of this line for the specified player.
     *
     * @param player The player to update this line for.
     */
    public void updateLocation(@NonNull Player player) {
        checkDestroyed();

        if (this.renderer != null) {
            this.renderer.updateLocation(player, getActualBukkitLocation());
        }
    }

    @Nullable
    public String getContent() {
        return this.rawContent;
    }

    public void setContent(@NonNull String content) {
        checkDestroyed();

        this.rawContent = content;

        // Conversion to the new renderer is handled by the content parsers
        this.plugin.getContentParserManager().parse(this);
    }

    public double getBlockHeight() {
        return this.renderer != null ? this.renderer.getHeight() : 0d;
    }

    public double getBlockWidth(@NonNull Player player) {
        return this.renderer != null ? this.renderer.getWidth(player) : 0d;
    }

    @NonNull
    public CoreHologramPage<? extends CoreHologramLine> getParent() {
        return this.parent;
    }

    @NonNull
    public HologramLineType getType() {
        return this.renderer.getType();
    }

    @NonNull
    public CoreHologramLineSettings getSettings() {
        return this.settings;
    }

    @Nullable
    public LineRenderer getRenderer() {
        return this.renderer;
    }

    public void setRenderer(@NonNull LineRenderer renderer) {
        checkDestroyed();

        this.renderer = renderer;
    }

    @NonNull
    public DecentLocation getLocation() {
        return this.location;
    }

    public void setLocation(@NonNull DecentLocation location) {
        checkDestroyed();

        this.location = location;
    }

    public void setLocation(@NonNull Location location) {
        setLocation(new DecentLocation(location));
    }

    public void setTypeYOffset(double typeYOffset) {
        this.typeYOffset = typeYOffset;
    }

    public double getTypeYOffset() {
        return this.typeYOffset;
    }

    public Location getActualBukkitLocation() {
        DecentLocation actualLocation = this.location.clone();
        actualLocation.add(
                this.settings.getOffsetX(),
                this.settings.getOffsetY() + getTypeYOffset(),
                this.settings.getOffsetZ()
        );
        return actualLocation.toBukkitLocation();
    }

}
