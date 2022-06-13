package eu.decent.holograms.components.page;

import eu.decent.holograms.api.component.line.Line;
import eu.decent.holograms.api.component.page.Page;
import eu.decent.holograms.api.component.page.PageLineHolder;
import eu.decent.holograms.api.utils.collection.DList;
import org.jetbrains.annotations.NotNull;

public class DefaultPageLineHolder implements PageLineHolder {

    private final Page parent;
    private final DList<Line> lines;

    /**
     * Create a new instance of {@link DefaultPageLineHolder}.
     *
     * @param parent The parent page of this page line holder.
     * @see Page
     */
    public DefaultPageLineHolder(Page parent) {
        this.parent = parent;
        this.lines = new DList<>();
    }

    @NotNull
    @Override
    public Page getParent() {
        return parent;
    }

    @NotNull
    @Override
    public DList<Line> getLines() {
        return lines;
    }

    @Override
    public Line removeLine(int index) {
        // TODO: hide the line and recalculate the hologram
        Line line = PageLineHolder.super.removeLine(index);

        return line;
    }

    @Override
    public void addLine(@NotNull Line line) {
        // TODO: show the line and recalculate the hologram
        PageLineHolder.super.addLine(line);

    }

    @Override
    public void addLine(int index, @NotNull Line line) {
        // TODO: show the line and recalculate the hologram
        PageLineHolder.super.addLine(index, line);

    }

    @Override
    public void setLine(int index, @NotNull Line line) {
        // TODO: hide the old line and show the new line and recalculate the hologram
        PageLineHolder.super.setLine(index, line);

    }

    @Override
    public void clearLines() {
        // TODO: hide all lines and recalculate the hologram
        PageLineHolder.super.clearLines();


    }
}
