package net.bigshot.Statch22.network;

import net.bigshot.Statch22.client.ClientSkillData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SkillSyncPacket {
    private final Map<Integer, Integer> skillStats;

    public SkillSyncPacket(Map<Integer, Integer> skillStats) {
        this.skillStats = skillStats;
    }

    public SkillSyncPacket(FriendlyByteBuf buf) {
        this.skillStats = new HashMap<>();
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            int skillId = buf.readInt();
            int statValue = buf.readInt();
            skillStats.put(skillId, statValue);
        }
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(skillStats.size());
        for (Map.Entry<Integer, Integer> entry : skillStats.entrySet()) {
            buf.writeInt(entry.getKey());
            buf.writeInt(entry.getValue());
        }
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientSkillData.updateFromServer(skillStats);
        });
        ctx.get().setPacketHandled(true);
    }
}