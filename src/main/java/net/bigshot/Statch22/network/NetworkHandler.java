package net.bigshot.Statch22.network;

import net.bigshot.Statch22.Statch22;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(Statch22.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        int id = 0;
        CHANNEL.registerMessage(id++, SkillSyncPacket.class,
                SkillSyncPacket::encode, SkillSyncPacket::new, SkillSyncPacket::handle);
    }
}