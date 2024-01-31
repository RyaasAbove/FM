package net.Ryaas.firstmod;

import com.mojang.logging.LogUtils;
import net.Ryaas.firstmod.Networking.ModNetworking;
import net.Ryaas.firstmod.block.ModBlocks;
import net.Ryaas.firstmod.entity.client.*;
import net.Ryaas.firstmod.item.ModCreativeModeTabs;
import net.Ryaas.firstmod.item.ModItems;
import net.Ryaas.firstmod.particles.ModParticles;
import net.Ryaas.firstmod.util.ModGameLogicManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;
import software.bernie.geckolib.GeckoLib;

import static net.Ryaas.firstmod.event.ModEventsForge.gameLogicManager;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(FirstMod.MOD_ID)
public class FirstMod
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "firstmod";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    public FirstMod()
    {

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModCreativeModeTabs.register(modEventBus);

        ModItems.register(modEventBus);

        ModEntities.register(modEventBus);


        ModBlocks.register(modEventBus);

        ModParticles.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        modEventBus.addListener(this::addCreative);

         GeckoLib.initialize();

        MinecraftForge.EVENT_BUS.register(this);
    }

    public static ModGameLogicManager getGameLogicManager() {
        return gameLogicManager;
    }


    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
        LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));
        ModNetworking.register();
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event){
        if(event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS){
            event.accept(ModItems.FRIES);
            event.accept(ModItems.BURGER);
            event.accept(ModItems.SALT);
        }

        if(event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS){
            event.accept(ModBlocks.SALT_ORE);
        }

        if(event.getTabKey() == CreativeModeTabs.COMBAT){
            event.accept((ModItems.SMOKE_BOMB));

        }
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
            EntityRenderers.register(ModEntities.BUNGER.get(), BungerRenderer::new);
            EntityRenderers.register(ModEntities.MAGE_HAND.get(), MageHandRenderer::new);
            EntityRenderers.register(ModEntities.BEAM_ATTACK.get(), BeamattackRenderer::new);
            EntityRenderers.register(ModEntities.MOON.get(), MoonRenderer::new);
//            EntityRenderers.register(ModEntities.MAGE_HAND_PROJ, MageHandRenderer::new);


        }



    }
}



