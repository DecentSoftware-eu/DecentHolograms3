package eu.decentsoftware.holograms.nms;

import eu.decentsoftware.holograms.api.nms.NMSAdapter;
import eu.decentsoftware.holograms.api.nms.NMSProvider;
import eu.decentsoftware.holograms.api.nms.listener.PacketListener;

public class DefaultNMSProvider implements NMSProvider {

    private NMSAdapter adapter;
    private PacketListener packetListener;

    public DefaultNMSProvider() {
        // TODO: INIT
    }

    @Override
    public NMSAdapter getAdapter() {
        return adapter;
    }

    @Override
    public PacketListener getPacketListener() {
        return packetListener;
    }

}
