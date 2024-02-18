package net.Ryaas.firstmod;

import net.Ryaas.firstmod.assisting.KeyBinding;
import net.Ryaas.firstmod.client.UltimateHud;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = FirstMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModHandler {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event){
        event.enqueueWork(() ->{

        });
    }

    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("ultimate_ability", UltimateHud.GUI_ULT);
    }

    @SubscribeEvent
    public static void Registerkeys(RegisterKeyMappingsEvent event){
        event.register(KeyBinding.INSTANCE.MARK_KEY);
        event.register(KeyBinding.INSTANCE.PRIMARY);
    }

}
