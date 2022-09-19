package com.lumintorious.tfchomestead.common.api;

import com.lumintorious.tfchomestead.TFCHomestead;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.capabilities.food.FoodTrait;
import net.dries007.tfc.config.TFCConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public class StoredTrait extends FoodTrait {
    public static StoredTrait COOL = new StoredTrait(0.50F, "cool");
    public static StoredTrait SHELVED = new StoredTrait(0.70F, "shelved");
    public static StoredTrait HUNG = new StoredTrait(0.60F, "hung");
    public static StoredTrait JAR = new StoredTrait(0.09F, "jar");
    public static StoredTrait SHELTERED = new StoredTrait(0.40F, "sheltered");

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

    public final String translationKey;

    public StoredTrait(float decayModifier, String translationKey) {
        super(decayModifier, "tfchomestead.food_trait." + translationKey);
        this.translationKey = "tfchomestead.food_trait." + translationKey;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    private static void register(StoredTrait trait) {
        FoodTrait.register(new ResourceLocation(TFCHomestead.MOD_ID, trait.getTranslationKey()), trait);
    }

    public static void init() {
        register(COOL);
        register(SHELVED);
        register(HUNG);
        register(JAR);
        register(SHELTERED);
    }
}
