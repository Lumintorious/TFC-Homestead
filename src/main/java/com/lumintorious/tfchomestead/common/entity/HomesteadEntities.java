package com.lumintorious.tfchomestead.common.entity;

import com.lumintorious.tfchomestead.TFCHomestead;
import com.lumintorious.tfchomestead.common.TFCHomesteadConfig;
import com.lumintorious.tfchomestead.common.block.entity.HangerBlockEntity;
import com.lumintorious.tfchomestead.common.villagers.TFCHomesteadVillager;
import net.dries007.tfc.common.entities.livestock.TFCAnimal;
import net.dries007.tfc.common.fluids.TFCFluids;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedList;
import java.util.List;

public class HomesteadEntities {
    public static DeferredRegister<EntityType<?>> ENTITIES =
        DeferredRegister.create(ForgeRegistries.ENTITIES, TFCHomestead.MOD_ID);

    public static RegistryObject<EntityType<TFCHomesteadVillager>> VILLAGER =
//            TFCEntities
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
                "TFC Homestead WARNING: The Hangers and the Food Shelves will be moved to the mod Firmalife. If you have any in your world, please remove them and the items inside before updating to the next Homestead Version"
            ), player.getUUID());
        }
    }

    public static final TagKey<Item> RAW_HIDES =
            TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("tfc", "raw_hides"));

}
