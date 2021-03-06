package eu.decentsoftware.holograms.nms;

import eu.decentsoftware.holograms.api.nms.NMSAdapter;
import eu.decentsoftware.holograms.api.utils.reflect.R;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class NMSAdapter_v1_8_R3 implements NMSAdapter {

    private IChatBaseComponent s(String s) {
        return IChatBaseComponent.ChatSerializer.a(s);
    }

    private ItemStack i(org.bukkit.inventory.ItemStack itemStack) {
        return CraftItemStack.asNMSCopy(itemStack);
    }

    private BlockPosition blockPos(@NotNull Location l) {
        return new BlockPosition(l.getBlockX(), l.getBlockY(), l.getBlockZ());
    }

    /*
     *  General
     */

    @Override
    public void sendPacket(@NotNull Player player, Object packet) {
        if (packet instanceof Packet) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket((Packet<?>) packet);
        }
    }

    /*
     *  Packets
     */

    @Override
    public void sendRedstoneParticle(Player player, Color c, Location l, float size) {
        // TODO: send redstone packet
    }

    @Override
    public Object updateTimePacket(long worldAge, long day) {
        return new PacketPlayOutUpdateTime(worldAge, day, true);
    }

    @Override
    public Object packetGameState(int mode, float value) {
        return new PacketPlayOutGameStateChange(mode, value);
    }

    @Override
    public Object packetTimes(int in, int stay, int out) {
        return new PacketPlayOutTitle(in, stay, out);
    }

    @Override
    public Object packetTitleMessage(String text) {
        return new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, s(text));
    }

    @Override
    public Object packetSubtitleMessage(String text) {
        return new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, s(text));
    }

    @Override
    public Object packetActionbarMessage(String text) {
        return new PacketPlayOutChat(s(text), (byte) 2);
    }

    @Override
    public Object packetJsonMessage(String text) {
        return new PacketPlayOutChat(s(text));
    }

    @Override
    public Object packetResetTitle() {
        return new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.RESET, null);
    }

    @Override
    public Object packetClearTitle() {
        return new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.CLEAR, null);
    }

    @Override
    public Object packetHeaderFooter(String header, String footer) {
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        R r = new R(packet);
        r.set("a", s(header));
        r.set("b", s(footer));
        return packet;
    }

    @Override
    public Object packetEntityAnimation(int eid, int animation) {
        PacketPlayOutAnimation packet = new PacketPlayOutAnimation();
        R r = new R(packet);
        r.set("a", eid);
        r.set("b", animation);
        return packet;
    }

    @Override
    public Object packetBlockAction(Location l, int action, int param, int blockType) {
        return new PacketPlayOutBlockAction(blockPos(l), Block.getByCombinedId(blockType).getBlock(), action, param);
    }

    @Override
    public Object packetBlockChange(Location l, int blockId, byte blockData) {
        PacketPlayOutBlockChange packet = new PacketPlayOutBlockChange();
        R r = new R(packet);
        r.set("a", blockPos(l));
        packet.block = Block.getByCombinedId(blockId << 4 | (blockData & 15));
        return packet;
    }

    /*
     *  Entity Metadata
     */

    @Override
    public void sendEntityMetadata(Player player, int eid, Object... objects) {
        sendEntityMetadata(player, eid, Arrays.asList(objects));
    }

    @Override
    public void sendEntityMetadata(Player player, int eid, List<Object> objects) {
        PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata();
        R r = new R(packet);
        r.set("a", eid);
        r.set("b", objects);
        sendPacket(player, packet);
    }

    @Override
    public Object getMetaCustom(int id, Class<?> type, Object value) {
        return null;
    }

    @Override
    public Object getMetaEntityRemainingAir(int airTicksLeft) {
        return new DataWatcher.WatchableObject(2, 1, airTicksLeft);
    }

    @Override
    public Object getMetaEntityCustomName(String name) {
        return new DataWatcher.WatchableObject(4, 2, name);
    }

    @Override
    public Object getMetaEntityCustomNameVisible(boolean visible) {
        return new DataWatcher.WatchableObject(0, 3, (byte) (visible ? 1 : 0));
    }

    @Override
    public Object getMetaEntitySilenced(boolean silenced) {
        return new DataWatcher.WatchableObject(0, 4, (byte) (silenced ? 1 : 0));
    }

    @Override
    public Object getMetaEntityGravity(boolean gravity) {
        return new DataWatcher.WatchableObject(0, 5, (byte) (gravity ? 1 : 0));
    }

    @Override
    public Object getMetaEntityProperties(boolean onFire, boolean crouched, boolean sprinting, boolean swimming, boolean invisible, boolean glowing, boolean flyingElytra) {
        byte data = 0;
        data += onFire ? 1 : 0;
        data += crouched ? 2 : 0;
        data += sprinting ? 8 : 0;
        data += swimming ? 10 : 0;
        data += invisible ? 20 : 0;
        return new DataWatcher.WatchableObject(0, 0, data);
    }

    @Override
    public Object getMetaArmorStandProperties(boolean small, boolean arms, boolean noBasePlate, boolean marker) {
        byte data = 0;
        data += small ? 1 : 0;
        data += arms ? 2 : 0;
        data += noBasePlate ? 8 : 0;
        data += marker ? 10 : 0;
        return new DataWatcher.WatchableObject(0, 10, data);
    }

    @Override
    public Object getMetaItemStack(org.bukkit.inventory.ItemStack itemStack) {
        return new DataWatcher.WatchableObject(5, 8, i(itemStack));
    }

    /*
     *  Entity
     */

    @Override
    public int getEntityTypeId(EntityType type) {
        return 0;
    }

    @Override
    public double getEntityHeight(EntityType type) {
        return 0;
    }

    @Override
    public void spawnEntity(Player player, int eid, UUID id, EntityType type, Location l) {
        PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity();
        R r = new R(packet);
        r.set("a", eid);
        r.set("b", MathHelper.floor(l.getX() * 32.0D));
        r.set("c", MathHelper.floor(l.getY() * 32.0D));
        r.set("d", MathHelper.floor(l.getZ() * 32.0D));
        r.set("h", MathHelper.d(l.getPitch() * 256.0F / 360.0F));
        r.set("i", MathHelper.d(l.getYaw() * 256.0F / 360.0F));
        r.set("j", getEntityTypeId(type));
        sendPacket(player, packet);
    }

    @Override
    public void spawnEntityLiving(Player player, int eid, UUID id, EntityType type, Location l) {
        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving();
        R r = new R(packet);
        r.set("a", eid);
        r.set("b", getEntityTypeId(type));
        r.set("c", MathHelper.floor(l.getX() * 32.0D));
        r.set("d", MathHelper.floor(l.getY() * 32.0D));
        r.set("e", MathHelper.floor(l.getZ() * 32.0D));
        r.set("i", (byte)((int)(l.getYaw() * 256.0F / 360.0F)));
        r.set("j", (byte)((int)(l.getPitch() * 256.0F / 360.0F)));
        r.set("k", (byte)((int)(l.getYaw() * 256.0F / 360.0F)));
        r.set("g", MathHelper.d(l.getYaw() * 256.0F / 360.0F));
        sendPacket(player, packet);
    }

    @Override
    public void setHelmet(Player player, int eid, org.bukkit.inventory.ItemStack itemStack) {
        PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment(eid, 4, i(itemStack));
        sendPacket(player, packet);
    }

    @Override
    public Object packetTeleportEntity(int eid, Location l, boolean onGround) {
        return new PacketPlayOutEntityTeleport(
                eid,
                MathHelper.floor(l.getX() * 32.0),
                MathHelper.floor(l.getY() * 32.0),
                MathHelper.floor(l.getZ() * 32.0),
                (byte)((int) (l.getYaw() * 256.0F / 360.0F)),
                (byte)((int) (l.getPitch() * 256.0F / 360.0F)),
                onGround
        );
    }

    @Override
    public void teleportEntity(Player player, int eid, Location l, boolean onGround) {
        sendPacket(player, packetTeleportEntity(eid, l, onGround));
    }

    @Override
    public void updatePassengers(Player player, int eid, int... passengers) {
        PacketPlayOutAttachEntity packet = new PacketPlayOutAttachEntity();
        R r = new R(packet);
        r.set("a", 0);
        r.set("b", eid);
        if (passengers != null && passengers.length > 0) {
            r.set("c", passengers[0]);
        } else {
            r.set("c", -1);
        }
        sendPacket(player, packet);
    }

    @Override
    public void removeEntity(Player player, int eid) {
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(eid);
        sendPacket(player, packet);
    }

}
