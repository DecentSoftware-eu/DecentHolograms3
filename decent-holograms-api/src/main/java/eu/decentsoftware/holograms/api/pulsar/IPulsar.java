package eu.decentsoftware.holograms.api.pulsar;

import org.jetbrains.annotations.NotNull;

/**
 * This class represents a pulsar. It is used to play some "pulse" animations.
 *
 * @author d0by
 * @since 3.0.0
 */
public interface IPulsar {

    /**
     * Play the given pulse animation.
     *
     * @param type The PulseType to play.
     */
    void pulse(@NotNull PulseType type);

}
