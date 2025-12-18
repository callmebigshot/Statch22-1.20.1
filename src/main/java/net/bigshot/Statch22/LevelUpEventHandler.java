package net.bigshot.Statch22;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.bigshot.Statch22.network.NetworkHandler;
import net.bigshot.Statch22.network.SkillSyncPacket;
import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber
public class LevelUpEventHandler {
    private static final Map<String, Integer> lastCheckedStats = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Player player = event.player;
        if (player.level().isClientSide) return;

        ServerPlayer serverPlayer = (ServerPlayer) player;
        String playerKey = player.getStringUUID();

        checkSkillLevelUp(serverPlayer, "mining", Statch22.MINING_STAT.get(), playerKey + "_mining", 5);
        checkSkillLevelUp(serverPlayer, "farming", Statch22.FARMING_STAT.get(), playerKey + "_farming", 2);
        checkSkillLevelUp(serverPlayer, "combat", Statch22.COMBAT_STAT.get(), playerKey + "_combat", 1);
        checkSkillLevelUp(serverPlayer, "fishing", Statch22.FISHING_STAT.get(), playerKey + "_fishing", 3);
        checkSkillLevelUp(serverPlayer, "forage", Statch22.FORAGING_STAT.get(), playerKey + "_forage", 4);
        checkSkillLevelUp(serverPlayer, "building", Statch22.BUILDING_STAT.get(), playerKey + "_building", 0);
    }

    private static void checkSkillLevelUp(ServerPlayer player, String skillName, ResourceLocation statLocation, String statKey, int skillId) {
        int currentStat = getDirectStat(player, statLocation);
        int lastStat = lastCheckedStats.getOrDefault(statKey, 0);

        if (currentStat != lastStat) {
            player.getCapability(PlayerSkillData.CAPABILITY).ifPresent(skillData -> {
                int currentLevel = skillData.getLevel(skillId);
                int newLevel = LevelManager.getLevelFromStat(currentStat);

                if (newLevel > currentLevel) {
                    skillData.setLevel(skillId, newLevel);
                    skillData.setPendingLevelUp(skillId, true);

                    sendLevelUpMessage(player, skillName, newLevel);

                    if (newLevel == 5 || newLevel == 10) {
                        sendProfessionMessage(player, skillName, newLevel);
                    }
                }
            });

            syncAllStatsToClient(player);
            lastCheckedStats.put(statKey, currentStat);
        }
    }

    private static void syncAllStatsToClient(ServerPlayer player) {
        Map<Integer, Integer> stats = new HashMap<>();
        stats.put(0, Statch22.getBuildingStat(player));
        stats.put(1, Statch22.getCombatStat(player));
        stats.put(2, Statch22.getFarmingStat(player));
        stats.put(3, Statch22.getFishingStat(player));
        stats.put(4, Statch22.getForageStat(player));
        stats.put(5, Statch22.getMiningStat(player));

        NetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),
                new SkillSyncPacket(stats));
    }

    private static int getDirectStat(ServerPlayer player, ResourceLocation statLocation) {
        try {
            Stat<ResourceLocation> stat = Stats.CUSTOM.get(statLocation);
            return player.getStats().getValue(stat);
        } catch (Exception e) {
            return 0;
        }
    }

    private static void sendLevelUpMessage(Player player, String skillName, int newLevel) {
        String formattedSkill = skillName.substring(0, 1).toUpperCase() + skillName.substring(1);
        player.sendSystemMessage(
                Component.literal("§6★ " + formattedSkill + " Level Up! §7(Level " + newLevel + ")")
        );
        player.sendSystemMessage(
                Component.literal("§eYou've got some new ideas to sleep on...").withStyle(ChatFormatting.ITALIC)
        );
    }

    private static void sendProfessionMessage(Player player, String skillName, int level) {
        player.sendSystemMessage(
                Component.literal("§bSleep to choose your level " + level + " " + skillName + " specialty!")
        );
    }
}