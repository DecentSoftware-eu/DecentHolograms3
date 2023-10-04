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
import eu.decentsoftware.holograms.Config;
import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.animations.text.CustomTextAnimation;
import eu.decentsoftware.holograms.animations.text.TextAnimation;
import eu.decentsoftware.holograms.animations.text.impl.*;
import eu.decentsoftware.holograms.ticker.Ticked;
import eu.decentsoftware.holograms.utils.FileUtils;
import eu.decentsoftware.holograms.utils.config.FileConfig;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This registry is responsible for storing and replacing animations. It also
 * loads all animations from config and registers them.
 * <p>
 * There are also some built-in animations that are registered by default. These
 * animations can be overridden by creating a custom animation with the same name.
 * <p>
 * Animations are updated when requested but the step counter is incremented
 * every tick. This means that when requesting the current frame of an animation
 * it always returns the frame that is currently selected by the step counter.
 *
 * @author d0by
 * @see Animation
 * @since 3.0.0
 */
public class AnimationRegistry implements Ticked {

    private static final Pattern ANIMATION_REGEX = Pattern.compile("<animation:(" + Config.NAME_REGEX + ")(?::([^/]+))?>(?:(.*)</animation>)?");
    private final Map<String, Animation<?>> animationMap;
    private final AtomicInteger stepCounter;
    private final DecentHolograms plugin;

    /**
     * Create a new instance of {@link AnimationRegistry}. This constructor
     * also loads all animations from config by calling the {@link #reload()} method.
     */
    public AnimationRegistry(@NonNull DecentHolograms plugin) {
        this.plugin = plugin;
        this.animationMap = new ConcurrentHashMap<>();
        this.stepCounter = new AtomicInteger(0);

        this.reload();
        this.startTicking();
    }

    /**
     * Reloads all custom animations from the config and resets the step counter
     * starting all animations from the beginning.
     * <p>
     * This method also registers the built-in, default animations.
     */
    public synchronized void reload() {
        this.animationMap.clear();
        this.stepCounter.set(0);

        // Register default animations
        this.registerAnimation(new RainbowAnimation());
        this.registerAnimation(new WaveAnimation());
        this.registerAnimation(new BurnAnimation());
        this.registerAnimation(new ScrollAnimation());
        this.registerAnimation(new TypewriterAnimation());

        // Load custom animations from config
        final long startMillis = System.currentTimeMillis();
        int counter = 0;

        File folder = new File(this.plugin.getDataFolder(), "animations");
        List<File> files = FileUtils.getFilesFromTree(folder, Config.NAME_REGEX + "\\.yml", true);
        if (files.isEmpty()) {
            return;
        }

        for (File file : files) {
            try {
                String name = file.getName().substring(0, file.getName().length() - 4);
                FileConfig config = new FileConfig(this.plugin, file);
                AnimationType type = AnimationType.getByName(config.getString("type", "ASCEND"));
                if (type == AnimationType.INTERNAL) {
                    this.plugin.getLogger().warning("Failed to load animation from '" + file.getName() + "' (Invalid type: 'INTERNAL')! Skipping...");
                    continue;
                }
                int interval = config.getInt("interval", 1);
                int pause = config.getInt("pause", 0);
                List<String> frames = config.getStringList("frames");
                if (frames.isEmpty()) {
                    this.plugin.getLogger().warning("Failed to load animation from '" + file.getName() + "' (No frames)! Skipping...");
                    continue;
                }
                Animation<?> animation = new CustomTextAnimation(name, type, interval, pause, frames);
                registerAnimation(animation);
                counter++;
            } catch (Exception e) {
                this.plugin.warnOrBoot("Failed to load animation from '%s'! Skipping...", file.getName());
                e.printStackTrace();
            }
        }
        long took = System.currentTimeMillis() - startMillis;
        this.plugin.logOrBoot("Successfully loaded %d animation%s in %d ms!", counter, counter == 1 ? "" : "s", took);
    }

    public synchronized void shutdown() {
        this.stopTicking();
        this.animationMap.clear();
        this.stepCounter.set(0);
    }

    @Override
    public void tick() {
        this.stepCounter.incrementAndGet();
    }

    @NonNull
    public String animate(@NonNull String text) {
        int step = this.stepCounter.get();

        // -- Special text animations
        // Rainbow text animation
        Animation<?> rainbowAnimation = this.animationMap.get("rainbow");
        if (rainbowAnimation instanceof TextAnimation) {
            text = text.replace("&u", ((TextAnimation) rainbowAnimation).animate(step, null));
        }

        // -- Generic & Custom text animations
        Matcher matcher = ANIMATION_REGEX.matcher(text);
        while (matcher.find()) {
            String group = matcher.group();
            String name = matcher.group(1);
            String args = matcher.group(2);
            String[] argsSplit = args == null ? null : args.split(",");
            String innerText = matcher.group(3);
            Animation<?> animation = this.animationMap.get(name);
            if (animation instanceof TextAnimation) {
                text = text.replace(group, ((TextAnimation) animation).animate(step, innerText, argsSplit));
            }
        }

        return text;
    }

    public boolean containsAnimation(@NonNull String text) {
        return text.contains("&u") || ANIMATION_REGEX.matcher(text).find();
    }

    public void registerAnimation(@NonNull Animation<?> animation) {
        this.animationMap.put(animation.getName(), animation);
    }

    @Nullable
    public Animation<?> getAnimation(@NonNull String name) {
        return this.animationMap.get(name);
    }

    @Nullable
    public Animation<?> removeAnimation(@NonNull String name) {
        return this.animationMap.remove(name);
    }

    public boolean hasAnimation(@NonNull String name) {
        return this.animationMap.containsKey(name);
    }

    @NonNull
    public Map<String, Animation<?>> getAnimations() {
        return ImmutableMap.copyOf(this.animationMap);
    }

}
