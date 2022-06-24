package eu.decentsoftware.holograms.components.page;

import eu.decentsoftware.holograms.api.component.line.Line;
import eu.decentsoftware.holograms.api.component.page.Page;
import eu.decentsoftware.holograms.api.component.page.PageLineHolder;
import eu.decentsoftware.holograms.api.utils.collection.DList;
import org.bukkit.entity.Player;
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
        // Remove the line from the list
        Line line = PageLineHolder.super.removeLine(index);

        // Hide the line to all viewers
        for (Player viewerPlayer : parent.getParent().getVisibilityManager().getViewerPlayers()) {
            line.getRenderer().hide(viewerPlayer);
        }

        // Realign other lines
        parent.recalculate();
        return line;
    }

    @Override
    public void addLine(@NotNull Line line) {
        // Add the line to the list
        PageLineHolder.super.addLine(line);

        // Show the line to all viewers
        for (Player viewerPlayer : parent.getParent().getVisibilityManager().getViewerPlayers()) {
            // TODO: check view conditions
            line.getRenderer().display(viewerPlayer);
        }

        // Realign other lines
        parent.recalculate();
    }

    @Override
    public void addLine(int index, @NotNull Line line) {
        // Add the line to the list
        PageLineHolder.super.addLine(index, line);

        // Show the line to all viewers
        for (Player viewerPlayer : parent.getParent().getVisibilityManager().getViewerPlayers()) {
            // TODO: check view conditions
            line.getRenderer().display(viewerPlayer);
        }

        // Realign other lines
        parent.recalculate();
    }

    @Override
    public void setLine(int index, @NotNull Line line) {
        // Remove the previous line from the list
        Line previousLine = PageLineHolder.super.removeLine(index);

        // Hide the previous line to all viewers
        for (Player viewerPlayer : parent.getParent().getVisibilityManager().getViewerPlayers()) {
            previousLine.getRenderer().hide(viewerPlayer);
        }

        // Add the new line
        addLine(index, line);
    }

    @Override
    public void clearLines() {
        // Hide all lines from all viewers
        for (Line line : lines) {
            for (Player viewerPlayer : parent.getParent().getVisibilityManager().getViewerPlayers()) {
                line.getRenderer().hide(viewerPlayer);
            }
        }

        // Clear the list
        PageLineHolder.super.clearLines();
    }
}
