package com.kryeit.stuff.listener;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;

public class DragonDeath implements ServerLivingEntityEvents.AfterDeath {
    @Override
    public void afterDeath(LivingEntity entity, DamageSource damageSource) {
        if (entity instanceof EnderDragonEntity) {
            if (damageSource.getSource() instanceof ServerPlayerEntity player) {
                int dragonsKilled = player.getStatHandler().getStat(Stats.KILLED.getOrCreateStat(EntityType.ENDER_DRAGON));
                if (dragonsKilled == 1) {
                    player.giveItemStack(Items.ELYTRA.getDefaultStack());
                    player.sendMessage(Text.of("You've killed the ender dragon for the first time! Here's an elytra :)"));
                }
            }
        }
    }
}
