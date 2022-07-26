package eu.decentsoftware.holograms.api.component.page;

import eu.decentsoftware.holograms.api.component.line.Line;
import eu.decentsoftware.holograms.api.utils.collection.DList;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a page line holder. It's responsible for holding all the lines of a page
 * and providing methods for manipulating them.
 *
 * @author d0by
 * @since 3.0.0
 */
public interface PageLineHolder {

    /**
     * Get the parent page of this page line holder.
     *
     * @return The parent page of this page line holder.
     */
    @NotNull
    Page getParent();

    /**
     * Get the lines of this page.
     *
     * @return The lines of this page.
     */
    @NotNull
    DList<Line> getLines();

    /**
     * Get the line at the specified index.
     *
     * @param index The index of the line to get.
     * @return The line at the specified index.
     */
    default Line getLine(int index) {
        return getLines().get(index);
    }

    /**
     * Get the index of the given line.
     *
     * @param line The line to get the index of.
     * @return The index or -1 if the given line in not present in this line holder.
     */
    default int getIndex(@NotNull Line line) {
        return getLines().contains(line) ? getLines().indexOf(line) : -1;
    }

    /**
     * Remove the line at the specified index.
     *
     * @param index The index of the line to remove.
     * @return The removed line.
     */
    default Line removeLine(int index) {
        return getLines().remove(index);
    }

    /**
     * Add a line to the end of this page.
     *
     * @param line The line to add.
     */
    default void addLine(@NotNull Line line) {
        getLines().add(line);
    }

    /**
     * Add a line at the specified index.
     *
     * @param index The index to add the line at.
     * @param line The line to add.
     */
    default void addLine(int index, @NotNull Line line) {
        getLines().add(index, line);
    }

    /**
     * Set the line at the specified index.
     *
     * @param index The index of the line to set.
     * @param line The line to set.
     */
    default void setLine(int index, @NotNull Line line) {
        removeLine(index);
        addLine(index, line);
    }

    /**
     * Remove all lines from this page.
     */
    default void clearLines() {
        getLines().clear();
    }

    /**
     * Set the lines of this page.
     *
     * @param lines The lines to set.
     */
    default void setLines(@NotNull DList<Line> lines) {
        clearLines();
        getLines().addAll(lines);
    }

}
