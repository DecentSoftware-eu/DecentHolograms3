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
import eu.decentsoftware.holograms.BootMessenger;
import eu.decentsoftware.holograms.Config;
import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.animations.text.CustomTextAnimation;
import eu.decentsoftware.holograms.animations.text.RainbowAnimation;
import eu.decentsoftware.holograms.animations.text.TextAnimation;
import eu.decentsoftware.holograms.ticker.Ticked;
import eu.decentsoftware.holograms.utils.FileUtils;
import eu.decentsoftware.holograms.utils.config.FileConfig;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This registry is responsible for storing and replacing animations.
 *
 * @author d0by
 * @since 3.0.0
 */
public class AnimationRegistry implements Ticked {

    private static final DecentHolograms PLUGIN = DecentHolograms.getInstance();
    private static final Pattern ANIMATION_REGEX = Pattern.compile("<animation: *(" + Config.NAME_REGEX + ")>(?:(.*)</animation>)?");
    private final @NotNull Map<String, Animation<?>> animationMap;
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

    /**
     * Reloads all custom animations from the config and resets the step counter
     * starting all animations from the beginning.
     */
    public synchronized void reload() {
        this.shutdown();

        // Register default animations
        this.registerAnimation(new RainbowAnimation());

        // Load custom animations from config
        final long startMillis = System.currentTimeMillis();
        int counter = 0;

        File folder = new File(PLUGIN.getDataFolder(), "animations");
        List<File> files = FileUtils.getFilesFromTree(folder, Config.NAME_REGEX + "\\.yml", true);
        if (files.isEmpty()) {
            return;
        }

        for (File file : files) {
            try {
                String name = file.getName().substring(0, file.getName().length() - 4);
                FileConfig config = new FileConfig(PLUGIN, file);
                AnimationType type = AnimationType.getByName(config.getString("type", "ASCEND"));
                int speed = config.getInt("speed", 1);
                int pause = config.getInt("pause", 0);
                List<String> frames = config.getStringList("frames");
                if (frames.isEmpty()) {
                    PLUGIN.getLogger().warning("Failed to load animation from '" + file.getName() + "'! Skipping...");
                    continue;
                }
                Animation<?> animation = new CustomTextAnimation(name, type, speed, pause, frames);
                registerAnimation(animation);
                counter++;
            } catch (Exception e) {
                PLUGIN.getLogger().warning("Failed to load animation from '" + file.getName() + "'! Skipping...");
                e.printStackTrace();
            }
        }
        long took = System.currentTimeMillis() - startMillis;
        BootMessenger.log(String.format("Successfully loaded %d animation%s in %d ms!", counter, counter == 1 ? "" : "s", took));

        // Start ticking again after shutdown
        this.startTicking();
    }

    /**
     * Clear all animations, reset the step counter and stop ticking.
     */
    public synchronized void shutdown() {
        this.startTicking();
        this.animationMap.clear();
        this.stepCounter.set(0);
    }

    @Override
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
        int step = this.stepCounter.get();

        // -- Special text animations
        // Rainbow text animation
        Animation<?> rainbowAnimation = this.animationMap.get("rainbow");
        if (rainbowAnimation instanceof TextAnimation) {
            text = text.replace("&u", ((TextAnimation) rainbowAnimation).animate(step, null));
        }

        // -- Generic text animations
        Matcher matcher = ANIMATION_REGEX.matcher(text);
        while (matcher.find()) {
            String group = matcher.group();
            String name = matcher.group(1);
            String innerText = matcher.group(2);
            Animation<?> animation = this.animationMap.get(name);
            if (animation instanceof TextAnimation) {
                text = text.replace(group, ((TextAnimation) animation).animate(step, innerText));
            }
        }

        return text;
    }

    /**
     * Check if the given text contains any animations.
     *
     * @param text The text.
     * @return True if the text contains any animations, false otherwise.
     */
    public boolean containsAnimation(@NotNull String text) {
        return text.contains("&u") || ANIMATION_REGEX.matcher(text).find();
    }

    /**
     * Register a new animation.
     *
     * @param animation The animation.
     * @see Animation
     */
    public void registerAnimation(@NotNull Animation<?> animation) {
        this.animationMap.put(animation.getName(), animation);
    }

    /**
     * Get animation by its name.
     *
     * @param name The name.
     * @return The animation or null if no animation with the given name is registered.
     * @see Animation
     */
    public Animation<?> getAnimation(@NotNull String name) {
        return this.animationMap.get(name);
    }

    /**
     * Remove animation by its name.
     *
     * @param name The name.
     * @return The removed animation or null if the given animation isn't registered.
     * @see Animation
     */
    public Animation<?> removeAnimation(@NotNull String name) {
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
    public Map<String, Animation<?>> getAnimations() {
        return ImmutableMap.copyOf(this.animationMap);
    }

}
