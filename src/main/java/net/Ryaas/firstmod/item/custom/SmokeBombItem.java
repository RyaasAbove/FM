package net.Ryaas.firstmod.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class SmokeBombItem extends Item {
    public SmokeBombItem(Properties pProperties){super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if(!pContext.getLevel().isClientSide){
            BlockPos positionClicked = pContext.getClickedPos();
            Player player = pContext.getPlayer();
            Level world = player.getCommandSenderWorld();
            ServerLevel level = (ServerLevel) world;
            boolean foundBlock = false;
            double x = positionClicked.getX();
            double y = positionClicked.getY();
            double z = positionClicked.getZ();
            addInvisEffect(player);
            //base z
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x, y + 1 , z, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x + 1, y + 1 , z, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x + 2, y + 1 , z, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x + 3, y + 1 , z, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x, y + 2 , z, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x + 1, y + 2 , z, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x + 2, y + 2 , z, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x + 3, y + 2 , z, 1,1,1,0,0);

            //base x

            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x, y + 1 , z + 1, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x, y + 1 , z, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x, y + 1 , z + 2, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x, y + 1 , z + 3, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x, y + 2 , z + 1, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x, y + 2 , z, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x, y + 2 , z + 2, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x, y + 2 , z + 3, 1,1,1,0,0);

            //base z
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x, y + 1 , z, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x + 1, y + 1 , z, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x + 2, y + 1 , z, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x + 3, y + 1 , z, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x, y + 2 , z, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x + 1, y + 2 , z, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x + 2, y + 2 , z, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x + 3, y + 2 , z, 1,1,1,0,0);

            //base x

            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x, y + 1 , z + 1, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x, y + 1 , z, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x, y + 1 , z + 2, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x, y + 1 , z + 3, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x, y + 2 , z + 1, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x, y + 2 , z, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x, y + 2 , z + 2, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x, y + 2 , z + 3, 1,1,1,0,0);

            //base z
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x, y + 1 , z, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x + 1, y + 1 , z, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x + 2, y + 1 , z, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x + 3, y + 1 , z, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x, y + 2 , z, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x + 1, y + 2 , z, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x + 2, y + 2 , z, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x + 3, y + 2 , z, 1,1,1,0,0);

            //base x

            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x, y + 1 , z + 1, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x, y + 1 , z, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x, y + 1 , z + 2, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x, y + 1 , z + 3, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x, y + 2 , z + 1, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x, y + 2 , z, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x, y + 2 , z + 2, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x, y + 2 , z + 3, 1,1,1,0,0);

            //base z
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x, y + 1 , z, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x + 1, y + 1 , z, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x + 2, y + 1 , z, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x + 3, y + 1 , z, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x, y + 2 , z, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x + 1, y + 2 , z, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x + 2, y + 2 , z, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x + 3, y + 2 , z, 1,1,1,0,0);

            //base x

            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x, y + 1 , z + 1, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x, y + 1 , z, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x, y + 1 , z + 2, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x, y + 1 , z + 3, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x, y + 2 , z + 1, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x, y + 2 , z, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x, y + 2 , z + 2, 1,1,1,0,0);
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x, y + 2 , z + 3, 1,1,1,0,0);










        }

        pContext.getItemInHand().hurtAndBreak(1, pContext.getPlayer(),
                player -> player.broadcastBreakEvent((player.getUsedItemHand())));


        return InteractionResult.SUCCESS;
    }
    public static void addInvisEffect(Player player){
        player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 600, 0));
        return;
    }

}



