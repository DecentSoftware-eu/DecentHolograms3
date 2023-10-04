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

import eu.decentsoftware.holograms.utils.SchedulerUtil;
import lombok.NonNull;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
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
        this.tickedObjects = ConcurrentHashMap.newKeySet();
        this.tickedObjectsToAdd = ConcurrentHashMap.newKeySet();
        this.tickedObjectsToRemove = ConcurrentHashMap.newKeySet();
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

    public void register(@NonNull Ticked ticked) {
        synchronized (this.tickedObjectsToAdd) {
            if (!this.tickedObjects.contains(ticked)) {
                this.tickedObjectsToAdd.add(ticked);
            }
        }
    }

    public void unregister(@NonNull Ticked ticked) {
        synchronized (this.tickedObjectsToRemove) {
            if (this.tickedObjects.contains(ticked)) {
                this.tickedObjectsToRemove.add(ticked);
            }
        }
    }

    public void unregisterAll() {
        synchronized (this.tickedObjectsToAdd) {
            this.tickedObjectsToAdd.clear();
        }
        synchronized (this.tickedObjectsToRemove) {
            this.tickedObjectsToRemove.addAll(this.tickedObjects);
        }
    }

    public synchronized void start() {
        this.taskId = SchedulerUtil.scheduleAsync(this::tick, 1L);
    }

    public synchronized void stop() {
        SchedulerUtil.cancel(this.taskId);
    }

    private void tick() {
        if (this.ticking.compareAndSet(false, true)) {
            // Tick all ticked objects
            synchronized (this.tickedObjects) {
                for (Ticked ticked : this.tickedObjects) {
                    try {
                        ticked.tick();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            // Add ticked objects
            synchronized (this.tickedObjectsToAdd) {
                this.tickedObjects.addAll(this.tickedObjectsToAdd);
                this.tickedObjectsToAdd.clear();
            }

            // Remove ticked objects
            synchronized (this.tickedObjectsToRemove) {
                this.tickedObjects.removeAll(this.tickedObjectsToRemove);
                this.tickedObjectsToRemove.clear();
            }
        }
        this.ticking.set(false);
    }

}
