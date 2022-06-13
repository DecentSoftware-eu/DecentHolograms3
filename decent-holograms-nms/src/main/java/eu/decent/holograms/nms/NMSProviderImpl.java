package eu.decent.holograms.nms;

import eu.decent.holograms.api.nms.NMSAdapter;
import eu.decent.holograms.api.utils.reflect.Version;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;

public class NMSProviderImpl implements eu.decent.holograms.api.nms.NMSProvider {

    private final NMSAdapter adapter;

    /**
     * Initializes the NMS adapter. If the version is not supported, an exception is thrown.
     *
     * @throws IllegalStateException If the current version is not supported.
     */
    public NMSProviderImpl() throws IllegalStateException {
        if ((adapter = init()) == null) {
            throw new IllegalStateException(String.format("Version %s is not supported!", Version.CURRENT.name()));
        }
    }

    @Override
    public NMSAdapter getAdapter() {
        return adapter;
    }

    /**
     * Attempts to find the correct NMS adapter for the current version.
     *
     * @return The NMS adapter or null if none was found.
     */
    @Nullable
    private NMSAdapter init() {
        String version = Version.CURRENT.name();
        String className = "eu.decent.holograms.nms.NMSAdapter_" + version;
        try {
            Class<?> clazz = Class.forName(className);
            return (NMSAdapter) clazz.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            return null;
        }
    }

}
