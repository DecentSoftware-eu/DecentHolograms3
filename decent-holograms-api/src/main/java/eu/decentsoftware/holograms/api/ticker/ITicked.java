package eu.decentsoftware.holograms.api.ticker;

import eu.decentsoftware.holograms.api.DecentHolograms;

/**
 * This interface represents a ticked object.
 *
 * @author d0by
 * @since 3.0.0
 */
public interface ITicked {

    /**
     * Tick the object. This method is called every tick.
     */
    void tick();

    /**
     * Register the object to the ticker.
     */
    default void startTicking() {
        DecentHolograms.getInstance().getTicker().register(this);
    }

    /**
     * Unregister the object from the ticker.
     */
    default void stopTicking() {
        DecentHolograms.getInstance().getTicker().unregister(this);
    }

}
