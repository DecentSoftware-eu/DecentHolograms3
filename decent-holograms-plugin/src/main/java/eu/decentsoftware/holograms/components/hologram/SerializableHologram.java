package eu.decentsoftware.holograms.components.hologram;

import eu.decentsoftware.holograms.api.component.page.Page;
import eu.decentsoftware.holograms.api.conditions.ConditionHolder;
import eu.decentsoftware.holograms.api.utils.collection.DecentList;
import eu.decentsoftware.holograms.components.page.DefaultPage;
import eu.decentsoftware.holograms.components.page.SerializablePage;
import eu.decentsoftware.holograms.conditions.DefaultConditionHolder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Location;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * This class is used to (de)serialize holograms from/to json.
 *
 * @author d0by
 * @since 3.0.0
 */
@Getter
@AllArgsConstructor
public class SerializableHologram {

    private final Location location;
    private DefaultHologramSettings settings;
    private ConditionHolder viewConditions;
    private final @NotNull List<SerializablePage> pages;

    /**
     * Create a new instance of {@link SerializableHologram} from the given {@link DefaultHologram}.
     *
     * @param hologram The hologram.
     * @return The new {@link SerializableHologram}.
     */
    @Contract("_ -> new")
    @NotNull
    public static SerializableHologram fromHologram(@NotNull DefaultHologram hologram) {
        DecentList<SerializablePage> pages = new DecentList<>();
        for (Page page : hologram.getPageHolder().getPages()) {
            DefaultPage defaultPage = (DefaultPage) page;
            SerializablePage serializablePage = SerializablePage.fromPage(defaultPage);
            pages.add(serializablePage);
        }
        return new SerializableHologram(
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
    public DefaultHologram toHologram(@NotNull String name) {
        if (location == null) {
            throw new IllegalArgumentException("Location cannot be null.");
        }
        if (settings == null) {
            settings = new DefaultHologramSettings(true, true);
        }
        if (viewConditions == null) {
            viewConditions = new DefaultConditionHolder();
        }
        DefaultHologram hologram = new DefaultHologram(name, location, settings, viewConditions);
        DecentList<Page> pages = new DecentList<>();
        for (SerializablePage page : this.pages) {
            DefaultPage defaultPage = page.toPage(hologram);
            pages.add(defaultPage);
        }
        hologram.getPageHolder().setPages(pages);
        return hologram;
    }

}
