package eu.decentsoftware.holograms.api.component.line;

/**
 * This class represents the settings of a line.
 *
 * @author d0by
 * @since 3.0.0
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
     * Get the offset X of the line.
     *
     * @return The offset X of the line.
     */
    double getOffsetX();

    /**
     * Set the offset X of the line.
     *
     * @param offsetX The offset X of the line.
     */
    void setOffsetX(double offsetX);

    /**
     * Get the offset Y of the line.
     *
     * @return The offset Y of the line.
     */
    double getOffsetY();

    /**
     * Set the offset Y of the line.
     *
     * @param offsetY The offset Y of the line.
     */
    void setOffsetY(double offsetY);

    /**
     * Get the offset Z of the line.
     *
     * @return The offset Z of the line.
     */
    double getOffsetZ();

    /**
     * Set the offset Z of the line.
     *
     * @param offsetZ The offset Z of the line.
     */
    void setOffsetZ(double offsetZ);

}
