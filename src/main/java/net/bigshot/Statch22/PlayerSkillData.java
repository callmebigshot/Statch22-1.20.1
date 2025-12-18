package net.bigshot.Statch22;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Statch22.MODID)
public class PlayerSkillData implements INBTSerializable<CompoundTag> {
    public static final Capability<PlayerSkillData> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

    private final Map<Integer, Integer> levels = new HashMap<>();
    private final Map<Integer, Integer> professions = new HashMap<>();
    private final Map<Integer, Boolean> pendingLevelUps = new HashMap<>();

    public PlayerSkillData() {
        for (int i = 0; i < 6; i++) {
            levels.put(i, 0);
            professions.put(i, 0);
            pendingLevelUps.put(i, false);
        }
    }

    public int getLevel(int skillId) {
        return levels.getOrDefault(skillId, 0);
    }

    public void setLevel(int skillId, int level) {
        levels.put(skillId, level);
    }

    public int getProfession(int skillId) {
        return professions.getOrDefault(skillId, 0);
    }

    public void setProfession(int skillId, int profession) {
        professions.put(skillId, profession);
    }

    public boolean hasPendingLevelUp(int skillId) {
        return pendingLevelUps.getOrDefault(skillId, false);
    }

    public void setPendingLevelUp(int skillId, boolean pending) {
        pendingLevelUps.put(skillId, pending);
    }

    public List<Integer> getPendingLevelUps() {
        List<Integer> pending = new ArrayList<>();
        for (Map.Entry<Integer, Boolean> entry : pendingLevelUps.entrySet()) {
            if (entry.getValue()) {
                pending.add(entry.getKey());
            }
        }
        return pending;
    }

    public void clearPendingLevelUps() {
        for (int skillId : pendingLevelUps.keySet()) {
            pendingLevelUps.put(skillId, false);
        }
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        CompoundTag levelsTag = new CompoundTag();
        CompoundTag professionsTag = new CompoundTag();
        CompoundTag pendingTag = new CompoundTag();

        for (Map.Entry<Integer, Integer> entry : levels.entrySet()) {
            levelsTag.putInt(String.valueOf(entry.getKey()), entry.getValue());
        }

        for (Map.Entry<Integer, Integer> entry : professions.entrySet()) {
            professionsTag.putInt(String.valueOf(entry.getKey()), entry.getValue());
        }

        for (Map.Entry<Integer, Boolean> entry : pendingLevelUps.entrySet()) {
            pendingTag.putBoolean(String.valueOf(entry.getKey()), entry.getValue());
        }

        tag.put("levels", levelsTag);
        tag.put("professions", professionsTag);
        tag.put("pending", pendingTag);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        if (tag.contains("levels")) {
            CompoundTag levelsTag = tag.getCompound("levels");
            for (String key : levelsTag.getAllKeys()) {
                levels.put(Integer.parseInt(key), levelsTag.getInt(key));
            }
        }

        if (tag.contains("professions")) {
            CompoundTag professionsTag = tag.getCompound("professions");
            for (String key : professionsTag.getAllKeys()) {
                professions.put(Integer.parseInt(key), professionsTag.getInt(key));
            }
        }

        if (tag.contains("pending")) {
            CompoundTag pendingTag = tag.getCompound("pending");
            for (String key : pendingTag.getAllKeys()) {
                pendingLevelUps.put(Integer.parseInt(key), pendingTag.getBoolean(key));
            }
        }
    }

    public static class Provider implements ICapabilitySerializable<CompoundTag> {
        private final PlayerSkillData skillData = new PlayerSkillData();
        private final LazyOptional<PlayerSkillData> optional = LazyOptional.of(() -> skillData);

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            if (cap == CAPABILITY) {
                return optional.cast();
            }
            return LazyOptional.empty();
        }

        @Override
        public CompoundTag serializeNBT() {
            return skillData.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            skillData.deserializeNBT(nbt);
        }
    }

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(Statch22.id("skill_data"), new Provider());
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) {
            event.getOriginal().reviveCaps();
            event.getOriginal().getCapability(CAPABILITY).ifPresent(oldData -> {
                event.getEntity().getCapability(CAPABILITY).ifPresent(newData -> {
                    CompoundTag tag = oldData.serializeNBT();
                    newData.deserializeNBT(tag);
                });
            });
            event.getOriginal().invalidateCaps();
        }
    }
}