package eu.decentsoftware.holograms.ticker;

import com.google.common.collect.Sets;
import eu.decentsoftware.holograms.api.ticker.ITicked;
import eu.decentsoftware.holograms.api.ticker.Ticker;
import eu.decentsoftware.holograms.api.utils.S;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class DefaultTicker implements Ticker {

    private final Set<ITicked> tickedObjects;
    private final Set<ITicked> tickedObjectsToAdd;
    private final Set<ITicked> tickedObjectsToRemove;
    private final AtomicBoolean ticking;
    private int taskId;

    /**
     * Creates a new instance of {@link DefaultTicker}. This constructor also starts the ticker
     * using the {@link #start()} method.
     */
    public DefaultTicker() {
        this.tickedObjects = Sets.newConcurrentHashSet();
        this.tickedObjectsToAdd = Sets.newConcurrentHashSet();
        this.tickedObjectsToRemove = Sets.newConcurrentHashSet();
        this.ticking = new AtomicBoolean(false);
        this.start();
    }

    @Override
    public void register(@NotNull ITicked ticked) {
        tickedObjectsToAdd.add(ticked);
    }

    @Override
    public void unregister(@NotNull ITicked ticked) {
        tickedObjectsToRemove.remove(ticked);
    }

    @Override
    public void unregisterAll() {
        tickedObjectsToAdd.clear();
        tickedObjectsToRemove.addAll(tickedObjects);
    }

    @Override
    public void start() {
        taskId = S.scheduleAsync(() -> {
            if (ticking.compareAndSet(false, true)) {
                // Tick all ticked objects
                for (ITicked ticked : tickedObjects) {
                    ticked.tick();
                }
                // Add and remove ticked objects
                tickedObjects.addAll(tickedObjectsToAdd);
                tickedObjects.removeAll(tickedObjectsToRemove);
            }
            ticking.set(false);
        }, 1L);
    }

    @Override
    public void stop() {
        S.cancel(taskId);
    }

}
