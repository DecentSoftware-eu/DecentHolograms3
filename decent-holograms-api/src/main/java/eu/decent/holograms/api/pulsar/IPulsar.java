package eu.decent.holograms.api.pulsar;

import org.jetbrains.annotations.NotNull;

public interface IPulsar {

    /**
     * Play the given pulse animation.
     *
     * @param type The PulseType to play.
     */
    void pulse(@NotNull PulseType type);

}
