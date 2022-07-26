package eu.decentsoftware.holograms.components.hologram;

import eu.decentsoftware.holograms.api.component.hologram.Hologram;
import eu.decentsoftware.holograms.api.component.hologram.HologramPageHolder;
import eu.decentsoftware.holograms.api.component.hologram.HologramVisibilityManager;
import eu.decentsoftware.holograms.api.component.page.Page;
import eu.decentsoftware.holograms.api.utils.collection.DList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class DefaultHologramPageHolder implements HologramPageHolder {

    private final @NotNull Hologram parent;
    private final @NotNull DList<Page> pages;

    /**
     * Creates a new instance of {@link DefaultHologramPageHolder} with the given parent.
     *
     * @param parent The parent hologram.
     */
    public DefaultHologramPageHolder(@NotNull Hologram parent) {
        this.parent = parent;
        this.pages = new DList<>();
    }

    @NotNull
    @Override
    public Hologram getParent() {
        return parent;
    }

    @NotNull
    @Override
    public DList<Page> getPages() {
        return pages;
    }

    @Override
    public void addPage(int index, @NotNull Page page) {
        HologramPageHolder.super.addPage(index, page);

        // Shift the player page indexes in visibility manager.
        shiftPlayerPages(index, 1);
    }

    @Override
    public void removePage(int index) {
        HologramPageHolder.super.removePage(index);

        // Shift the player page indexes in visibility manager.
        shiftPlayerPages(index, -1);
    }

    @Override
    public void clearPages() {
        HologramPageHolder.super.clearPages();

        // Reset the player page indexes in visibility manager to 0.
        getParent().getVisibilityManager().getPlayerPages().replaceAll((k, v) -> 0);
    }

    /**
     * Shift the player page indexes in visibility manager by the given amount at the given index.
     *
     * @param index The index to start shifting from.
     * @param shift The amount to shift by.
     */
    private void shiftPlayerPages(int index, int shift) {
        HologramVisibilityManager visibilityManager = getParent().getVisibilityManager();
        for (Map.Entry<String, Integer> entry : visibilityManager.getPlayerPages().entrySet()) {
            if (entry.getValue() >= index) {
                Player player = Bukkit.getPlayer(entry.getKey());
                if (player != null) {
                    visibilityManager.setPage(player, entry.getValue() + shift);
                }
            }
        }
    }

}
