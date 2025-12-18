package net.bigshot.Statch22.network;

import net.bigshot.Statch22.client.gui.StatsScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public class OpenGuiPacket {
    public OpenGuiPacket() {}

    public OpenGuiPacket(FriendlyByteBuf buf) {}

    public void encode(FriendlyByteBuf buf) {}

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft.getInstance().setScreen(new StatsScreen());
        });
        ctx.get().setPacketHandled(true);
    }
}