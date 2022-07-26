package eu.decentsoftware.holograms.components.hologram;

import eu.decentsoftware.holograms.api.component.page.Page;
import eu.decentsoftware.holograms.api.conditions.ConditionHolder;
import eu.decentsoftware.holograms.api.utils.collection.DList;
import eu.decentsoftware.holograms.components.page.DefaultPage;
import eu.decentsoftware.holograms.components.page.SerializablePage;
import lombok.Data;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

/**
 * This class is used to (de)serialize holograms from/to json.
 *
 * @author d0by
 * @since 3.0.0
 */
@Data
public class SerializableHologram {

    private final @NotNull String name;
    private final @NotNull Location location;
    private final @NotNull DefaultHologramSettings settings;
    private final @NotNull ConditionHolder viewConditions;
    private final @NotNull DList<SerializablePage> pages;

    /**
     * Create a new instance of {@link SerializableHologram} from the given {@link DefaultHologram}.
     *
     * @param hologram The hologram.
     * @return The new {@link SerializableHologram}.
     */
    @NotNull
    public static SerializableHologram fromHologram(@NotNull DefaultHologram hologram) {
        DList<SerializablePage> pages = new DList<>();
        for (Page page : hologram.getPageHolder().getPages()) {
            DefaultPage defaultPage = (DefaultPage) page;
            SerializablePage serializablePage = SerializablePage.fromPage(defaultPage);
            pages.add(serializablePage);
        }
        return new SerializableHologram(
                hologram.getName(),
                hologram.getPositionManager().getLocation(),
                (DefaultHologramSettings) hologram.getSettings(),
                hologram.getViewConditionHolder(),
                pages
        );
    }

    /**
     * Create a {@link DefaultHologram} from this {@link SerializableHologram}.
     *
     * @return The new {@link DefaultHologram}.
     */
    @NotNull
    public DefaultHologram toHologram() {
        DefaultHologram hologram = new DefaultHologram(name, location, settings, viewConditions);
        DList<Page> pages = new DList<>();
        for (SerializablePage page : this.pages) {
            DefaultPage defaultPage = page.toPage(hologram);
            pages.add(defaultPage);
        }
        hologram.getPageHolder().setPages(pages);
        return hologram;
    }

}
