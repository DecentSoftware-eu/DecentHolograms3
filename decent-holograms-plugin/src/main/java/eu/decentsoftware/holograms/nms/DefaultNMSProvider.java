package eu.decentsoftware.holograms.nms;

import eu.decentsoftware.holograms.api.nms.NMSAdapter;
import eu.decentsoftware.holograms.api.nms.NMSProvider;
import eu.decentsoftware.holograms.api.nms.listener.PacketListener;
import eu.decentsoftware.holograms.api.utils.reflect.Version;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;

public class DefaultNMSProvider implements NMSProvider {

    private final NMSAdapter adapter;
    private final PacketListener packetListener;

    /**
     * Initializes the NMS adapter. If the version is not supported, an exception is thrown.
     *
     * @throws IllegalStateException If the current version is not supported.
     */
    public DefaultNMSProvider() throws IllegalStateException {
        if ((adapter = initNMSAdapter()) == null) {
            throw new IllegalStateException(String.format("Version %s is not supported!", Version.CURRENT.name()));
        }
        packetListener = new PacketListener();
    }

    @Override
    public NMSAdapter getAdapter() {
        return adapter;
    }

    @Override
    public PacketListener getPacketListener() {
        return packetListener;
    }

    /**
     * Attempts to find the correct NMS adapter for the current version.
     *
     * @return The NMS adapter or null if none was found.
     */
    @Nullable
    private NMSAdapter initNMSAdapter() {
        String version = Version.CURRENT.name();
        String className = "eu.decentsoftware.holograms.nms.NMSAdapter_" + version;
        try {
            Class<?> clazz = Class.forName(className);
            return (NMSAdapter) clazz.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            return null;
        }
    }

}
