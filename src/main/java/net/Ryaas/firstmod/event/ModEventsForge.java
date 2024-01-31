package net.Ryaas.firstmod.event;

import net.Ryaas.firstmod.FirstMod;
import net.Ryaas.firstmod.util.ModGameLogicManager;
import net.Ryaas.firstmod.util.TelekinesisHandler;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FirstMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEventsForge {
    public static final ModGameLogicManager gameLogicManager = FirstMod.getGameLogicManager(); // Example method to get the manager

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            TelekinesisHandler telekinesisHandler = FirstMod.getGameLogicManager().getTelekinesisHandler();
            telekinesisHandler.updateAll();
        }
    }
}