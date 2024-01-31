package net.Ryaas.firstmod.event;

import net.Ryaas.firstmod.FirstMod;
import net.Ryaas.firstmod.entity.client.ModEntities;
import net.Ryaas.firstmod.entity.custom.Bungerentity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod.EventBusSubscriber(modid = FirstMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents {
    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event){
    event.put(ModEntities.BUNGER.get(), Bungerentity.setAttributes());
    }



    public static void init() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModEntities.register(modEventBus);
    }



}
