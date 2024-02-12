package net.Ryaas.firstmod.event;

import net.Ryaas.firstmod.FirstMod;
import net.Ryaas.firstmod.util.ChargeManager;
import net.Ryaas.firstmod.util.ModGameLogicManager;
import net.Ryaas.firstmod.util.TelekinesisHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FirstMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEventsForge {
    public static final ModGameLogicManager gameLogicManager = FirstMod.getGameLogicManager(); // Example method to get the manager
    public static TelekinesisHandler telekinesisHandler;

    public static void setTelekinesisHandler(TelekinesisHandler handler) {
        telekinesisHandler = handler;
    }

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        MinecraftServer server = event.getServer();
        TelekinesisHandler telekinesisHandler = new TelekinesisHandler();

        // If you need to store the handler for later use
        ModGameLogicManager.setTelekinesisHandler(telekinesisHandler);
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            TelekinesisHandler handler = ModGameLogicManager.getTelekinesisHandler();
            if (handler != null) {
                handler.updateAllTelekinesis();
                ChargeManager.RestoreCharges();
            }
        }


    }
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();
        if (player != null) {
            ChargeManager.initializePlayerCharge(player);
        }
    }




}