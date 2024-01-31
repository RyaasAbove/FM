package net.Ryaas.firstmod;

import net.Ryaas.firstmod.Networking.ModNetworking;
import net.Ryaas.firstmod.Networking.packet.MainAbility;
import net.Ryaas.firstmod.Networking.packet.MarkPacketc2s;
import net.Ryaas.firstmod.assisting.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FirstMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeHandler {
    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event){
        if(KeyBinding.INSTANCE.MARK_KEY.consumeClick()){

            ModNetworking.sendToServer(new MarkPacketc2s());

        }
        if(KeyBinding.INSTANCE.PRIMARY.consumeClick()){

            ModNetworking.sendToServer(new MainAbility());

        }
    }
}
