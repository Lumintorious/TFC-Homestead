package com.lumintorious.tfchomestead.common.entity;

import com.lumintorious.tfchomestead.TFCHomestead;
import com.lumintorious.tfchomestead.common.TFCHomesteadConfig;
import com.lumintorious.tfchomestead.common.block.entity.HangerBlockEntity;
import com.lumintorious.tfchomestead.common.villagers.TFCHomesteadVillager;
import net.dries007.tfc.common.entities.livestock.TFCAnimal;
import net.dries007.tfc.common.fluids.TFCFluids;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
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
    }

    public static final TagKey<Item> RAW_HIDES =
            TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("tfc", "raw_hides"));

    public static void addLootToAnimal(net.minecraftforge.event.entity.living.LivingDropsEvent event) {
        if(!TFCHomesteadConfig.SERVER.enableMoreLootForDomesticatedAnimals.get()) return;
        if(event.getEntity() instanceof TFCAnimal animal) {
            float familiarity = animal.getFamiliarity();
            List<ItemEntity> additions = new LinkedList<>();
            for(ItemEntity entity : event.getDrops()) {
                if(entity.getItem().is(HangerBlockEntity.RAW_MEAT) || entity.getItem().is(RAW_HIDES) || entity.getItem().is(Items.FEATHER)) {
                    ItemEntity added = entity.copy();
                    added.setItem(added.getItem().copy());
                    ItemStack stack = added.getItem();
                    if(stack.getCount() == 1 && familiarity < 0.5f) {
                        stack.setCount(0);
                    } else {
                        stack.setCount((int) Math.ceil(familiarity) * stack.getCount());
                    }
                    additions.add(added);
                }
            }
            event.getDrops().addAll(additions);
        }
    }
}
