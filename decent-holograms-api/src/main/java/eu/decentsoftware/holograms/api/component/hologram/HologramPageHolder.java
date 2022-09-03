package eu.decentsoftware.holograms.api.component.hologram;

import eu.decentsoftware.holograms.api.component.page.Page;
import eu.decentsoftware.holograms.api.utils.collection.DecentList;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a component of a hologram that stores its pages
 * and provides methods to manipulate them.
 *
 * @author d0by
 * @since 3.0.0
 */
public interface HologramPageHolder {

    /**
     * Get the parent {@link Hologram} of this page holder.
     *
     * @return The parent {@link Hologram} of this page holder.
     */
    @NotNull
    Hologram getParent();

    /**
     * Get the list of pages in this hologram.
     *
     * @return The list of pages in this hologram.
     */
    @NotNull
    DecentList<Page> getPages();

    /**
     * Get the page at the given index.
     *
     * @param index The index of the page to get.
     * @return The page at the given index.
     */
    default Page getPage(int index) {
        return getPages().get(index);
    }

    /**
     * Get the index of the given page.
     *
     * @param page The page to get the index of.
     * @return The index or -1 if the given page in not present in this page holder.
     */
    default int getIndex(@NotNull Page page) {
        return getPages().contains(page) ? getPages().indexOf(page) : -1;
    }

    /**
     * Add a page to this hologram.
     *
     * @param page The page to add.
     */
    default void addPage(@NotNull Page page) {
        getPages().add(page);
    }

    /**
     * Insert a page to this hologram at the specified index.
     *
     * @param index The index to insert the page at.
     * @param page The page to insert.
     */
    default void addPage(int index, @NotNull Page page) {
        getPages().add(index, page);
    }

    /**
     * Remove a page from this hologram by its index.
     *
     * @param index The index of the page to remove.
     */
    default void removePage(int index) {
        getPages().remove(index);
    }

    /**
     * Remove all pages from this hologram.
     */
    default void clearPages() {
        getPages().clear();
    }

    /**
     * Set the pages of this hologram.
     *
     * @param pages The pages to set.
     */
    default void setPages(@NotNull DecentList<Page> pages) {
        clearPages();
        getPages().addAll(pages);
    }

}
