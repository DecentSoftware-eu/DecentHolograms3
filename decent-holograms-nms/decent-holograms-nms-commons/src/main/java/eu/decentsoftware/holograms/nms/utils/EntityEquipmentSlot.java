package eu.decentsoftware.holograms.nms.utils;

/**
 * This enum holds all equipment slots of entities. It is used
 * for getting the right ID of the slot, used by the equipment
 * packet in NMS.
 *
 * @author d0by
 */
public enum EntityEquipmentSlot {
    MAINHAND(0, 0),
    OFFHAND(1, -1),
    FEET(2, 1),
    LEGS(3, 2),
    CHEST(4, 3),
    HEAD(5, 4);

    private final int slotId;
    private final int legacySlotId;

    EntityEquipmentSlot(int slotId, int legacySlotId) {
        this.slotId = slotId;
        this.legacySlotId = legacySlotId;
    }

    /**
     * Get the ID of this slot for versions 1.9 and higher.
     *
     * @return The ID.
     */
    public int getSlotId() {
        return slotId;
    }

    /**
     * Get the ID of this slot for versions 1.8 and lower.
     *
     * @return The ID.
     */
    public int getLegacySlotId() {
        return legacySlotId;
    }

}