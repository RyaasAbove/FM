package net.Ryaas.firstmod.entity.client;

import net.Ryaas.firstmod.FirstMod;
import net.Ryaas.firstmod.entity.custom.Beamattack;
import net.Ryaas.firstmod.entity.custom.Bungerentity;
import net.Ryaas.firstmod.entity.custom.MageHand;
import net.Ryaas.firstmod.entity.custom.Moon;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, FirstMod.MOD_ID);

    public static final RegistryObject<EntityType<Bungerentity>> BUNGER =
        ENTITY_TYPES.register("bunger", () -> EntityType.Builder.of(Bungerentity::new, MobCategory.CREATURE)
                .sized(1.0f, 1.0f).build("bunger"));

    public static final RegistryObject<EntityType<MageHand>> MAGE_HAND = ENTITY_TYPES.register("mage_hand",
            () -> EntityType.Builder.<MageHand>of(MageHand::new, MobCategory.MISC)
                    .sized(5f, 5f) // Set the appropriate size
                    .setTrackingRange(80)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true)
                    .build("mage_hand"));

    public static final RegistryObject<EntityType<Beamattack>> BEAM_ATTACK = ENTITY_TYPES.register("beam_attack",
            () -> EntityType.Builder.<Beamattack>of(Beamattack::new, MobCategory.MISC)
                    .sized(5f, 5f) // Set the appropriate size
                    .setTrackingRange(80)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true)
                    .build("beam_attack"));

    public static final RegistryObject<EntityType<Moon>> MOON = ENTITY_TYPES.register("moon",
            () -> EntityType.Builder.<Moon>of(Moon::new, MobCategory.MISC)
                    .sized(5f, 5f) // Set the appropriate size
                    .setTrackingRange(80)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true)
                    .build("moon"));


//    public static final RegistryObject<EntityType<MageHand>> MAGE_HAND_CHARGE_1 = ENTITY_TYPES.register("mage_hand_charge_1",
//            () -> EntityType.Builder.<MageHand>of(MageHand::new, MobCategory.MISC)
//                    .sized(1f, 1f) // Set the appropriate size
//                    .setTrackingRange(80)
//                    .setUpdateInterval(3)
//                    .setShouldReceiveVelocityUpdates(true)
//                    .build("mage_hand_charge_1"));
//
//    public static final RegistryObject<EntityType<MageHand>> MAGE_HAND_CHARGE_2 = ENTITY_TYPES.register("mage_hand_charge_2",
//            () -> EntityType.Builder.<MageHand>of(MageHand::new, MobCategory.MISC)
//                    .sized(2f, 2f) // Set the appropriate size
//                    .setTrackingRange(80)
//                    .setUpdateInterval(3)
//                    .setShouldReceiveVelocityUpdates(true)
//                    .build("mage_hand_charge_2"));
//
//    public static final RegistryObject<EntityType<MageHand>> MAGE_HAND_CHARGE_3 = ENTITY_TYPES.register("mage_hand_charge_3",
//            () -> EntityType.Builder.<MageHand>of(MageHand::new, MobCategory.MISC)
//                    .sized(4f, 4f) // Set the appropriate size
//                    .setTrackingRange(80)
//                    .setUpdateInterval(3)
//                    .setShouldReceiveVelocityUpdates(true)
//                    .build("mage_hand_charge_3"));
//
//    public static final RegistryObject<EntityType<MageHand>> MAGE_HAND_CHARGE_4 = ENTITY_TYPES.register("mage_hand_charge_4",
//            () -> EntityType.Builder.<MageHand>of(MageHand::new, MobCategory.MISC)
//                    .sized(8f, 8f) // Set the appropriate size
//                    .setTrackingRange(80)
//                    .setUpdateInterval(3)
//                    .setShouldReceiveVelocityUpdates(true)
//                    .build("mage_hand_charge_4"));
//
//    public static final RegistryObject<EntityType<MageHand>> MAGE_HAND_CHARGE_5 = ENTITY_TYPES.register("mage_hand_charge_5",
//            () -> EntityType.Builder.<MageHand>of(MageHand::new, MobCategory.MISC)
//                    .sized(16f, 16f) // Set the appropriate size
//                    .setTrackingRange(80)
//                    .setUpdateInterval(3)
//                    .setShouldReceiveVelocityUpdates(true)
//                    .build("mage_hand_charge_5"));





    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
}
