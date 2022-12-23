package eu.decentsoftware.holograms.components.line;

import eu.decentsoftware.holograms.api.component.hologram.HologramSettings;
import eu.decentsoftware.holograms.api.component.line.Line;
import eu.decentsoftware.holograms.api.component.page.Page;
import eu.decentsoftware.holograms.components.common.DefaultPositionManager;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class DefaultLinePositionManager extends DefaultPositionManager {

    private final @NotNull Line parent;

    public DefaultLinePositionManager(@NotNull Line parent, @NotNull Location location) {
        super(location);
        this.parent = parent;
    }

    public DefaultLinePositionManager(@NotNull Line parent, @NotNull Location location, Supplier<Location> locationSupplier) {
        super(location, locationSupplier);
        this.parent = parent;
    }

    @NotNull
    @Override
    public Location getActualLocation() {
        Location actualLocation = locationSupplier != null ? locationSupplier.get().clone() : location.clone();
        Page page = parent.getParent();
        HologramSettings settings = page.getParent().getSettings();
        if (!settings.isRotateVertical() && !settings.isRotateHorizontal()) {
            return actualLocation
                    .add(
                            parent.getSettings().getOffsetX(),
                            parent.getSettings().getOffsetY(),
                            parent.getSettings().getOffsetZ()
                    )
                    .add(offsets);
        }
        return actualLocation;
    }
}
