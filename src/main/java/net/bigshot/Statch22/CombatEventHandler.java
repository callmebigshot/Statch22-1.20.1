package net.bigshot.Statch22;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;

public class CombatEventHandler {

    @SubscribeEvent
    public void onMobKilled(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof Player player && player instanceof ServerPlayer) {
            LivingEntity killed = event.getEntity();
            ResourceLocation entityId = ForgeRegistries.ENTITY_TYPES.getKey(killed.getType());

            if (entityId != null && !(killed instanceof Player)) {
                int killWeight = getKillWeight(entityId.toString(), killed);
                if (killWeight > 0) {
                    CombatStatCalculator.addMobKill(player, killWeight);
                    updateCombatStatImmediately(player);
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerKilled(LivingDeathEvent event) {
        if (event.getEntity() instanceof ServerPlayer killedPlayer &&
                event.getSource().getEntity() instanceof Player killer &&
                killer instanceof ServerPlayer) {
            if (killer != killedPlayer) {
                CombatStatCalculator.addPlayerKill(killer);
                updateCombatStatImmediately(killer);
            }
        }
    }

    @SubscribeEvent
    public void onDamageDealt(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof Player player && player instanceof ServerPlayer) {
            float damage = event.getAmount();
            int damagePoints = (int) Math.ceil(damage * 0.05);
            if (damagePoints > 0) {
                CombatStatCalculator.addDamageDealt(player, damagePoints);
                updateCombatStatImmediately(player);
            }
        }
    }

    @SubscribeEvent
    public void onDamageBlocked(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player player && player instanceof ServerPlayer && event.getSource().isIndirect()) {
            float damage = event.getAmount();
            int blockedPoints = (int) Math.ceil(damage * 0.1);
            if (blockedPoints > 0) {
                CombatStatCalculator.addDamageBlocked(player, blockedPoints);
                updateCombatStatImmediately(player);
            }
        }
    }

    @SubscribeEvent
    public void onDamageResisted(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player player && player instanceof ServerPlayer) {
            float originalDamage = event.getAmount();
            float damageAfterArmor = Math.max(0, originalDamage - getArmorProtection(player, originalDamage));
            if (damageAfterArmor < originalDamage) {
                int resistedPoints = (int) Math.ceil((originalDamage - damageAfterArmor) * 0.05);
                if (resistedPoints > 0) {
                    CombatStatCalculator.addDamageResisted(player, resistedPoints);
                    updateCombatStatImmediately(player);
                }
            }
        }
    }

    private void updateCombatStatImmediately(Player player) {
        int currentCombatScore = CombatStatCalculator.getCombatScore(player);
        int lastCombatScore = CombatStatCalculator.getLastCombatScore(player);
        if (currentCombatScore > lastCombatScore) {
            int difference = currentCombatScore - lastCombatScore;
            player.awardStat(Stats.CUSTOM.get(Statch22.COMBAT_STAT.get()), difference);
            CombatStatCalculator.setLastCombatScore(player, currentCombatScore);
        }
    }

    private int getKillWeight(String entityId, LivingEntity entity) {
        int vanillaWeight = getVanillaWeight(entityId);
        if (vanillaWeight > 0) return vanillaWeight;

        return calculateDynamicWeight(entity);
    }

    private int getVanillaWeight(String entityId) {
        if (entityId.equals("minecraft:ender_dragon") ||
                entityId.equals("minecraft:wither") ||
                entityId.equals("minecraft:warden")) {
            return 100;
        }
        if (entityId.equals("minecraft:elder_guardian")) {
            return 80;
        }
        if (entityId.equals("minecraft:evoker")) {
            return 40;
        }
        if (entityId.equals("minecraft:ravager")) {
            return 35;
        }
        if (entityId.equals("minecraft:piglin_brute") ||
                entityId.equals("minecraft:illusioner")) {
            return 25;
        }
        if (entityId.equals("minecraft:blaze") ||
                entityId.equals("minecraft:ghast")) {
            return 20;
        }
        if (entityId.equals("minecraft:guardian") ||
                entityId.equals("minecraft:shulker")) {
            return 15;
        }
        if (entityId.equals("minecraft:vindicator") ||
                entityId.equals("minecraft:witch") ||
                entityId.equals("minecraft:creeper")) {
            return 12;
        }
        if (entityId.equals("minecraft:enderman") ||
                entityId.equals("minecraft:pillager") ||
                entityId.equals("minecraft:wither_skeleton") ||
                entityId.equals("minecraft:phantom") ||
                entityId.equals("minecraft:hoglin") ||
                entityId.equals("minecraft:zoglin")) {
            return 8;
        }
        if (entityId.equals("minecraft:magma_cube") ||
                entityId.equals("minecraft:slime")) {
            return 6;
        }
        if (entityId.equals("minecraft:skeleton") ||
                entityId.equals("minecraft:zombie") ||
                entityId.equals("minecraft:spider") ||
                entityId.equals("minecraft:cave_spider") ||
                entityId.equals("minecraft:drowned") ||
                entityId.equals("minecraft:husk") ||
                entityId.equals("minecraft:stray") ||
                entityId.equals("minecraft:zombie_villager") ||
                entityId.equals("minecraft:zombified_piglin")) {
            return 5;
        }
        if (entityId.equals("minecraft:endermite") ||
                entityId.equals("minecraft:silverfish")) {
            return 3;
        }
        if (entityId.equals("minecraft:vex")) {
            return 2;
        }
        return 0;
    }

    private int calculateDynamicWeight(LivingEntity entity) {
        float health = entity.getMaxHealth();
        float armor = entity.getArmorValue();
        float toughness = armor * 2;

        if (health >= 500) return 150;

        if (health >= 400) return 120;

        if (health >= 300) return 100;

        if (health >= 200) return 80;

        if (health >= 100) {
            if (armor >= 15) return 60;
            if (armor >= 10) return 50;
            return 40;
        }

        if (health >= 50) {
            if (armor >= 15) return 25;
            if (armor >= 10) return 20;
            if (armor >= 5) return 15;
            return 12;
        }

        if (health >= 25) {
            if (armor >= 10) return 10;
            if (armor >= 5) return 8;
            return 6;
        }

        if (health >= 10) {
            if (armor >= 5) return 5;
            return 4;
        }

        if (health >= 5) return 3;
        return 2;
    }

    private float estimateEntityDamage(LivingEntity entity) {
        AttributeInstance attackAttr = entity.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attackAttr != null) {
            return (float) attackAttr.getValue();
        }
        return 5.0f;
    }

    private float getArmorProtection(Player player, float damage) {
        return player.getArmorValue() * 0.04f * damage;
    }
}