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

package eu.decentsoftware.holograms.animations;

import com.google.common.collect.ImmutableMap;
import eu.decentsoftware.holograms.ticker.Ticked;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This registry is responsible for storing and replacing animations.
 *
 * @author d0by
 * @since 3.0.0
 */
public class AnimationRegistry implements Ticked {

    private final @NotNull Map<String, Animation> animationMap;
    private final @NotNull AtomicInteger stepCounter;

    /**
     * Create a new instance of {@link AnimationRegistry}. This constructor
     * also loads all animations from config by calling the {@link #reload()} method.
     */
    public AnimationRegistry() {
        this.animationMap = new ConcurrentHashMap<>();
        this.stepCounter = new AtomicInteger(0);

        this.reload();
        this.startTicking();
    }

    public void reload() {
        this.animationMap.clear();

        // Load animations
        // TODO: load

        // Reset step counter
        this.stepCounter.set(0);
    }

    public void shutdown() {
        this.animationMap.clear();
    }

    public void tick() {
        this.stepCounter.incrementAndGet();
    }

    /**
     * Animate the given text by replacing all animations in it.
     *
     * @param text The text.
     * @return The animated text.
     */
    @NotNull
    public String animate(@NotNull String text) {
        // TODO: animate
        return text;
    }

    /**
     * Register a new animation.
     *
     * @param animation The animation.
     * @see Animation
     */
    public void registerAnimation(@NotNull Animation animation) {
        this.animationMap.put(animation.getName(), animation);
    }

    /**
     * Get animation by its name.
     *
     * @param name The name.
     * @return The animation or null if no animation with the given name is registered.
     * @see Animation
     */
    public Animation getAnimation(@NotNull String name) {
        return this.animationMap.get(name);
    }

    /**
     * Remove animation by its name.
     *
     * @param name The name.
     * @return The removed animation or null if the given animation isn't registered.
     * @see Animation
     */
    public Animation removeAnimation(@NotNull String name) {
        return this.animationMap.remove(name);
    }

    /**
     * Check it this registry contains an animation with the given name.
     *
     * @param name The name.
     * @return True if this registry contains an animation with the name, false otherwise.
     * @see Animation
     */
    public boolean hasAnimation(@NotNull String name) {
        return this.animationMap.containsKey(name);
    }

    /**
     * Get all registered animations as a map. The returned map is immutable.
     *
     * @return Immutable map of all registered animations.
     * @see Animation
     */
    public Map<String, Animation> getAnimations() {
        return ImmutableMap.copyOf(this.animationMap);
    }

}
