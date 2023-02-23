/*
 * DecentHolograms
 * Copyright (C) DecentSoftware.eu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.decentsoftware.holograms.ticker;

import com.google.common.collect.Sets;
import eu.decentsoftware.holograms.utils.SchedulerUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class represents a ticker. It's used to tick the registered {@link Ticked} objects every tick.
 *
 * @author d0by
 * @see Ticked
 * @since 3.0.0
 */
public class Ticker {

    private final Set<Ticked> tickedObjects;
    private final Set<Ticked> tickedObjectsToAdd;
    private final Set<Ticked> tickedObjectsToRemove;
    private final AtomicBoolean ticking;
    private int taskId;

    /**
     * Creates a new instance of {@link Ticker}. This constructor also starts the ticker
     * using the {@link #start()} method.
     */
    public Ticker() {
        this.tickedObjects = Sets.newConcurrentHashSet();
        this.tickedObjectsToAdd = Sets.newConcurrentHashSet();
        this.tickedObjectsToRemove = Sets.newConcurrentHashSet();
        this.ticking = new AtomicBoolean(false);
        this.start();
    }

    /**
     * Destroy the ticker unregistering all objects and stopping it.
     */
    public synchronized void shutdown() {
        this.stop();
        this.tickedObjects.clear();
        this.tickedObjectsToAdd.clear();
        this.tickedObjectsToRemove.clear();
    }

    /**
     * Register the given object to the ticker.
     *
     * @param ticked The object to register.
     */
    public void register(@NotNull Ticked ticked) {
        synchronized (tickedObjectsToAdd) {
            if (!tickedObjects.contains(ticked)) {
                tickedObjectsToAdd.add(ticked);
            }
        }
    }

    /**
     * Unregister the given object from the ticker.
     *
     * @param ticked The object to unregister.
     */
    public void unregister(@NotNull Ticked ticked) {
        synchronized (tickedObjectsToRemove) {
            if (tickedObjects.contains(ticked)) {
                tickedObjectsToRemove.add(ticked);
            }
        }
    }

    /**
     * Unregister all objects from the ticker.
     */
    public void unregisterAll() {
        synchronized (tickedObjectsToAdd) {
            tickedObjectsToAdd.clear();
        }
        synchronized (tickedObjectsToRemove) {
            tickedObjectsToRemove.addAll(tickedObjects);
        }
    }

    /**
     * Start the ticker.
     */
    public synchronized void start() {
        taskId = SchedulerUtil.scheduleAsync(this::tick, 1L);
    }

    /**
     * Stop the ticker.
     */
    public synchronized void stop() {
        SchedulerUtil.cancel(taskId);
    }

    private void tick() {
        if (ticking.compareAndSet(false, true)) {
            // Tick all ticked objects
            synchronized (tickedObjects) {
                for (Ticked ticked : tickedObjects) {
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
