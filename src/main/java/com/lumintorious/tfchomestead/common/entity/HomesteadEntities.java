package com.lumintorious.tfchomestead.common.entity;

import com.lumintorious.tfchomestead.TFCHomestead;
import com.lumintorious.tfchomestead.common.villagers.TFCHomesteadVillager;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class HomesteadEntities {
    public static DeferredRegister<EntityType<?>> ENTITIES =
        DeferredRegister.create(ForgeRegistries.ENTITIES, TFCHomestead.MOD_ID);

    public static RegistryObject<EntityType<TFCHomesteadVillager>> VILLAGER =
        ENTITIES.register(
            "villager",
                () -> EntityType.Builder.of(TFCHomesteadVillager::new, MobCategory.CREATURE)
                    .sized(1, 2)
                    .clientTrackingRange(10)
                    .build("tfchomestead:villager")
        );

    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(VILLAGER.get(), TFCHomesteadVillager.createAttributes().build());
    }

    public static void resetTradesOnSpawn(EntityJoinWorldEvent event) {
        if(!event.loadedFromDisk() && event.getEntity() instanceof TFCHomesteadVillager villager) {
            villager.randomizeData();
        }
        if(event.getEntity() instanceof ServerPlayer player) {
            player.sendMessage(new TextComponent(
                "TFC Homestead WARNING: The Hangers and the Food Shelves are currently in the Firmalife mod, and the ones from this mod could be removed! If you have any in your world, please remove them and the items inside before updating to the next Homestead Version"
            ), player.getUUID());
        }
    }

    public static final TagKey<Item> RAW_HIDES =
            TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("tfc", "raw_hides"));

}
