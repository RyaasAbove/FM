package net.Ryaas.firstmod.item.custom;

import net.minecraft.world.food.FoodProperties;

public class ModFoods {
    public static final FoodProperties FRIES = (new FoodProperties.Builder()).nutrition(3).saturationMod(0.6F).build();
    public static final FoodProperties BURGER = (new FoodProperties.Builder()).nutrition(16).saturationMod(0.10F).build();
    public static final FoodProperties SALT = (new FoodProperties.Builder()).nutrition(1).saturationMod(0.01F).build();
}
