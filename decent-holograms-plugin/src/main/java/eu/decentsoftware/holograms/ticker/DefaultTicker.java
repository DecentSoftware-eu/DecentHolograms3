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
    public void shutdown() {
        this.stop();
        this.tickedObjects.clear();
        this.tickedObjectsToAdd.clear();
        this.tickedObjectsToRemove.clear();
    }

    @Override
    public void register(@NotNull ITicked ticked) {
        synchronized (tickedObjectsToAdd) {
            if (!tickedObjects.contains(ticked)) {
                tickedObjectsToAdd.add(ticked);
            }
        }
    }

    @Override
    public void unregister(@NotNull ITicked ticked) {
        synchronized (tickedObjectsToRemove) {
            if (tickedObjects.contains(ticked)) {
                tickedObjectsToRemove.add(ticked);
            }
        }
    }

    @Override
    public void unregisterAll() {
        synchronized (tickedObjectsToAdd) {
            tickedObjectsToAdd.clear();
        }
        synchronized (tickedObjectsToRemove) {
            tickedObjectsToRemove.addAll(tickedObjects);
        }
    }

    @Override
    public void start() {
        taskId = S.scheduleAsync(this::tick, 1L);
    }

    @Override
    public void stop() {
        S.cancel(taskId);
    }

    private void tick() {
        if (ticking.compareAndSet(false, true)) {
            // Tick all ticked objects
            synchronized (tickedObjects) {
                for (ITicked ticked : tickedObjects) {
                    try {
                        ticked.tick();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            // Add ticked objects
            synchronized (tickedObjectsToAdd) {
                tickedObjects.addAll(tickedObjectsToAdd);
                tickedObjectsToAdd.clear();
            }

            // Remove ticked objects
            synchronized (tickedObjectsToRemove) {
                tickedObjects.removeAll(tickedObjectsToRemove);
                tickedObjectsToRemove.clear();
            }
        }
        ticking.set(false);
    }

}
