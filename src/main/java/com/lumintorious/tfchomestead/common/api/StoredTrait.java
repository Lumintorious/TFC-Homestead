package com.lumintorious.tfchomestead.common.api;

import com.lumintorious.tfchomestead.TFCHomestead;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.capabilities.food.FoodTrait;
import net.dries007.tfc.config.TFCConfig;
import net.dries007.tfc.util.Helpers;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public final class StoredTrait {
    public static FoodTrait COOL = register(0.50F, "cool");
    public static FoodTrait SHELVED = register(0.70F, "shelved");
    public static FoodTrait HUNG = register(0.60F, "hung");
    public static FoodTrait JAR = register(0.09F, "jar");
    public static FoodTrait SHELTERED = register(0.40F, "sheltered");

    public static void eraseAll(ItemStack stack) {
        FoodCapability.removeTrait(stack, COOL);
        FoodCapability.removeTrait(stack, SHELVED);
        FoodCapability.removeTrait(stack, HUNG);
        FoodCapability.removeTrait(stack, JAR);
        FoodCapability.removeTrait(stack, SHELTERED);

        stack.getCapability(FoodCapability.CAPABILITY).ifPresent((cap) -> {
            cap.getTraits().removeIf(Objects::isNull);
        });
    }

    private static FoodTrait register(float decayModifier, String name) {
        return FoodTrait.register(new ResourceLocation(TFCHomestead.MOD_ID, name), new FoodTrait(decayModifier, "tfchomestead.food_trait." + name));
    }

    // the classloading causes all these food traits to get registered automagically
    public static void init() {}
}
