package net.Ryaas.firstmod.event;

import net.Ryaas.firstmod.FirstMod;
import net.Ryaas.firstmod.assisting.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ClientEvents {




        @Mod.EventBusSubscriber(modid = FirstMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
        public static class ClientModBusEvents {
            @SubscribeEvent
            public static void onKeyRegister(RegisterKeyMappingsEvent event) {

                event.register(KeyBinding.SECONDARY);
                event.register(KeyBinding.ULTIMATE);
                event.register(KeyBinding.TOGGLE_ULT_STATUS);

            }



        }
    }

