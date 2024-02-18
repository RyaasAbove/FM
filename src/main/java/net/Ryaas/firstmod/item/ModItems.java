package net.Ryaas.firstmod.item;

import net.Ryaas.firstmod.FirstMod;
import net.Ryaas.firstmod.entity.client.ModEntities;
import net.Ryaas.firstmod.item.custom.*;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, FirstMod.MOD_ID);


    public static final RegistryObject<Item> FRIES = ITEMS.register("fries",
            () -> new Item(new Item.Properties().food(ModFoods.FRIES)));
    public static final RegistryObject<Item> BURGER = ITEMS.register("burger",
            () -> new Item(new Item.Properties().food(ModFoods.BURGER)));
    public static final RegistryObject<Item> SALT = ITEMS.register("salt",
            () -> new Item(new Item.Properties().food(ModFoods.SALT)));

    public static final RegistryObject<Item> SMOKE_BOMB = ITEMS.register("smoke_bomb",
            () -> new SmokeBombItem(new Item.Properties().durability(1)));

    public static final RegistryObject<Item> AMULET = ITEMS.register("amulet",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> MOONLIT_DAGGER = ITEMS.register("moonlit_dagger",
            () -> new MoonlitDaggerItem(Tiers.DIAMOND, 4, 1.5f,  new Item.Properties()));



//    public static final RegistryObject<Item> MOONLIT_DAGGER = ITEMS.register("moonlit_dagger",
//            () -> new SwordItem(new Item.Properties()));



    public static final RegistryObject<Item> BUNGER_SPAWN_EGG = ITEMS.register("bunger_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.BUNGER, 0xD57E36, 0x1D0D00, new Item.Properties()));

    public static final RegistryObject<Item> TELEKINETIC_ASCENDER = ITEMS.register("telekinetic_ascender",
            () -> new TelekineticAscenderItem(new Item.Properties()));

    public static final RegistryObject<Item> ABILITY_REMOVER = ITEMS.register("ability_remover",
            () -> new AbilityRemoverItem(new Item.Properties()));



    public static final RegistryObject<ArmorItem> WIZARD_HAT = ITEMS.register("wizard_hat",
            () -> new WizardHatItem(ModArmorMaterials.WIZARD_HAT, ArmorItem.Type.HELMET, new Item.Properties()));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
