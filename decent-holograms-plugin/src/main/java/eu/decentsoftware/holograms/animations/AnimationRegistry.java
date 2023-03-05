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
import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.animations.text.CustomTextAnimation;
import eu.decentsoftware.holograms.animations.text.RainbowAnimation;
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
    private static final Pattern ANIMATION_REGEX = Pattern.compile("<animation: *[A-Ba-b0-9_]+>");
    private static final RainbowAnimation RAINBOW_ANIMATION = new RainbowAnimation();

    private final @NotNull Map<String, Animation> customAnimationMap;
    private final @NotNull AtomicInteger stepCounter;

    /**
     * Create a new instance of {@link AnimationRegistry}. This constructor
     * also loads all animations from config by calling the {@link #reload()} method.
     */
    public AnimationRegistry() {
        this.customAnimationMap = new ConcurrentHashMap<>();
        this.stepCounter = new AtomicInteger(0);

        this.reload();
        this.startTicking();
    }

    public synchronized void reload() {
        this.customAnimationMap.clear();

        // Load animations from config
        final long startMillis = System.currentTimeMillis();
        int counter = 0;

        File folder = new File(PLUGIN.getDataFolder(), "animations");
        List<File> files = FileUtils.getFilesFromTree(folder, "[a-zA-Z0-9_-]+\\.yml", true);
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
                Animation animation = new CustomTextAnimation(name, type, speed, pause, frames);
                registerAnimation(animation);
                counter++;
            } catch (Exception e) {
                PLUGIN.getLogger().warning("Failed to load animation from '" + file.getName() + "'! Skipping...");
                e.printStackTrace();
            }
        }
        long took = System.currentTimeMillis() - startMillis;
        BootMessenger.log(String.format("Successfully loaded %d animation%s in %d ms!", counter, counter == 1 ? "" : "s", took));

        // Reset step counter
        this.stepCounter.set(0);
    }

    public synchronized void shutdown() {
        this.customAnimationMap.clear();
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

        // Rainbow text animation
        text = text.replace("&q", RAINBOW_ANIMATION.animate(step, null));

        // Custom text animations
        Matcher matcher = ANIMATION_REGEX.matcher(text);
        while (matcher.find()) {
            String group = matcher.group();
            String name = group.substring(12, group.length() - 1);
            Animation animation = this.customAnimationMap.get(name);
            if (animation != null) {
                text = text.replace(group, animation.animate(step, null));
            }
        }

        // TODO
        return text;
    }

    /**
     * Check if the given text contains any animations.
     *
     * @param text The text.
     * @return True if the text contains any animations, false otherwise.
     */
    public boolean containsAnimation(@NotNull String text) {
        return text.contains("&q") || ANIMATION_REGEX.matcher(text).find();
    }

    /**
     * Register a new animation.
     *
     * @param animation The animation.
     * @see Animation
     */
    public void registerAnimation(@NotNull Animation animation) {
        this.customAnimationMap.put(animation.getName(), animation);
    }

    /**
     * Get animation by its name.
     *
     * @param name The name.
     * @return The animation or null if no animation with the given name is registered.
     * @see Animation
     */
    public Animation getAnimation(@NotNull String name) {
        return this.customAnimationMap.get(name);
    }

    /**
     * Remove animation by its name.
     *
     * @param name The name.
     * @return The removed animation or null if the given animation isn't registered.
     * @see Animation
     */
    public Animation removeAnimation(@NotNull String name) {
        return this.customAnimationMap.remove(name);
    }

    /**
     * Check it this registry contains an animation with the given name.
     *
     * @param name The name.
     * @return True if this registry contains an animation with the name, false otherwise.
     * @see Animation
     */
    public boolean hasAnimation(@NotNull String name) {
        return this.customAnimationMap.containsKey(name);
    }

    /**
     * Get all registered animations as a map. The returned map is immutable.
     *
     * @return Immutable map of all registered animations.
     * @see Animation
     */
    public Map<String, Animation> getAnimations() {
        return ImmutableMap.copyOf(this.customAnimationMap);
    }

}
