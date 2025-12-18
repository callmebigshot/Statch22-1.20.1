package net.bigshot.Statch22;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerSkillDataProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    private PlayerSkillData skillData = null;
    private final LazyOptional<PlayerSkillData> optional = LazyOptional.of(this::createSkillData);

    private PlayerSkillData createSkillData() {
        if (skillData == null) {
            skillData = new PlayerSkillData();
        }
        return skillData;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == PlayerSkillData.CAPABILITY) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        createSkillData().serializeNBT().getAllKeys().forEach(key -> {
            tag.put(key, createSkillData().serializeNBT().get(key));
        });
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createSkillData().deserializeNBT(nbt);
    }
}