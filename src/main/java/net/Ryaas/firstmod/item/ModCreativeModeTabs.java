package net.Ryaas.firstmod.item;

import net.Ryaas.firstmod.FirstMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB,
            FirstMod.MOD_ID);

    public static RegistryObject<CreativeModeTab> RANDOMBS = CREATIVE_MODE_TABS.register("randombs_tab", () ->
            CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.FRIES.get()))
                    .title(Component.translatable("creativetab.randombs_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        //put items here
                        pOutput.accept(ModItems.AMULET.get());
                        pOutput.accept(ModItems.WIZARD_HAT.get());
                        pOutput.accept(ModItems.BUNGER_SPAWN_EGG.get());
                        pOutput.accept(ModItems.MOONLIT_DAGGER.get());
                        pOutput.accept(ModItems.SMOKE_BOMB.get());
                        pOutput.accept(ModItems.FRIES.get());
                        pOutput.accept(ModItems.BURGER.get());
                        pOutput.accept(ModItems.SALT.get());
                        pOutput.accept(ModItems.TELEKINETIC_ASCENDER.get());
                        pOutput.accept(ModItems.ABILITY_REMOVER.get());

                    })
                    .build());
    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }

}
