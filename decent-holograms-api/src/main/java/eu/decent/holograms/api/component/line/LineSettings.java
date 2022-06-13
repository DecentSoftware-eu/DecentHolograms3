package eu.decent.holograms.api.component.line;

/**
 * This class represents the settings of a line.
 *
 * @author d0by
 */
public interface LineSettings {

    /**
     * Get if the line is updating, meaning it's contents will be updated every
     * update interval.
     *
     * @return True if the line is updating, false otherwise.
     */
    boolean isUpdating();

    /**
     * Set if the line is updating, meaning it's contents will be updated every
     * update interval.
     *
     * @param updating True if the line is updating, false otherwise.
     */
    void setUpdating(boolean updating);

    /**
     * Get the height of the line.
     *
     * @return The height of the line.
     */
    double getHeight();

    /**
     * Set the height of the line.
     *
     * @param height The height of the line.
     */
    void setHeight(double height);

    /**
     * Get the X offset of the line.
     *
     * @return The X offset of the line.
     */
    double getOffsetX();

    /**
     * Set the X offset of the line.
     *
     * @param offsetX The X offset of the line.
     */
    void setOffsetX(double offsetX);

    /**
     * Get the Y offset of the line.
     *
     * @return The Y offset of the line.
     */
    double getOffsetY();

    /**
     * Set the Y offset of the line.
     *
     * @param offsetY The Y offset of the line.
     */
    void setOffsetY(double offsetY);

    /**
     * Get the Z offset of the line.
     *
     * @return The Z offset of the line.
     */
    double getOffsetZ();

    /**
     * Set the Z offset of the line.
     *
     * @param offsetZ The Z offset of the line.
     */
    void setOffsetZ(double offsetZ);

}
