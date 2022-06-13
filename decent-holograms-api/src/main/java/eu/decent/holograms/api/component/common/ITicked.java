package eu.decent.holograms.api.component.common;

/**
 * This interface represents a ticked object.
 */
public interface ITicked {

    /**
     * Tick the object.
     */
    void tick();

    /**
     * Starts the ticking of the object.
     */
    void startTicking();

    /**
     * Stops the ticking of the object.
     */
    void stopTicking();

}
