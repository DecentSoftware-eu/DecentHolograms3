package eu.decentsoftware.holograms.animations;

import eu.decentsoftware.holograms.api.animations.Animation;
import eu.decentsoftware.holograms.api.animations.AnimationRegistry;
import eu.decentsoftware.holograms.api.ticker.ITicked;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultAnimationRegistry implements AnimationRegistry, ITicked {

    private final @NotNull Map<String, Animation> animationMap;
    private final @NotNull AtomicInteger stepCounter;

    /**
     * Create a new instance of {@link DefaultAnimationRegistry}. This constructor
     * also loads all animations from config by calling the {@link #reload()} method.
     */
    public DefaultAnimationRegistry() {
        this.animationMap = new ConcurrentHashMap<>();
        this.stepCounter = new AtomicInteger(0);

        this.reload();
        this.startTicking();
    }

    @Override
    public void reload() {
        this.animationMap.clear();

        // Load animations
        // TODO: load

        // Reset step counter
        this.stepCounter.set(0);
    }

    @Override
    public void shutdown() {
        this.animationMap.clear();
    }

    @Override
    public void tick() {
        this.stepCounter.incrementAndGet();
    }

    @NotNull
    @Override
    public String animate(@NotNull String text) {
        // TODO: animate
        return text;
    }

    @Override
    public void registerAnimation(@NotNull Animation animation) {
        this.animationMap.putIfAbsent(animation.getName(), animation);
    }

    @Override
    public Animation getAnimation(@NotNull String name) {
        return this.animationMap.get(name);
    }

    @Override
    public Animation removeAnimation(@NotNull String name) {
        return this.animationMap.remove(name);
    }

    @Override
    public boolean hasAnimation(@NotNull String name) {
        return this.animationMap.containsKey(name);
    }

    @Override
    public Map<String, Animation> getAnimations() {
        return this.animationMap;
    }

}
