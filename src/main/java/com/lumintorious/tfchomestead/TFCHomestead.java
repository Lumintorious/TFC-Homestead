package com.lumintorious.tfchomestead;

import com.lumintorious.tfchomestead.client.renderers.ClientEvents;
import com.lumintorious.tfchomestead.common.TFCHomesteadConfig;
import com.lumintorious.tfchomestead.common.block.HomesteadBlockEntities;
import com.lumintorious.tfchomestead.common.block.HomesteadBlocks;
import com.lumintorious.tfchomestead.common.api.StoredTrait;
import com.lumintorious.tfchomestead.common.block.entity.GrainPileBlockEntity;
import com.lumintorious.tfchomestead.common.drinks.HomesteadFluid;
import com.lumintorious.tfchomestead.common.entity.HomesteadEntities;
import com.lumintorious.tfchomestead.common.item.HomesteadItems;
import com.lumintorious.tfchomestead.common.villagers.TFCHomesteadVillager;
import com.lumintorious.tfchomestead.common.villagers.TFCVillagerProfessions;
import com.lumintorious.tfchomestead.common.world.HomesteadFeatures;
import com.lumintorious.tfchomestead.compat.jade.HomesteadTOPPlugin;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("tfchomestead")
public class TFCHomestead
{
    public static final String MOD_ID = "tfchomestead";
    public static final Logger LOGGER = LogUtils.getLogger();

    public TFCHomestead()
    {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        if(FMLEnvironment.dist == Dist.CLIENT) {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientEvents::setup);
            ClientEvents.init();
        }

        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGHEST, this::onRightClick);
        MinecraftForge.EVENT_BUS.addListener(HomesteadEntities::addLootToAnimal);

        if(TFCHomesteadConfig.COMMON.enableVillagerSpawns.get()) {
            TFCVillagerProfessions.POI_TYPES.register(eventBus);
            TFCVillagerProfessions.PROFESSIONS.register(eventBus);
            HomesteadFeatures.FEATURES.register(eventBus);
            MinecraftForge.EVENT_BUS.addListener(HomesteadEntities::resetTradesOnSpawn);
        }

        HomesteadEntities.ENTITIES.register(eventBus);
        HomesteadBlocks.BLOCKS.register(eventBus);
        HomesteadItems.ITEMS.register(eventBus);
        HomesteadBlockEntities.BLOCK_ENTITIES.register(eventBus);

        if(TFCHomesteadConfig.COMMON.enableAgedDrinks.get()) {
            HomesteadFluid.FLUIDS.register(eventBus);
            MinecraftForge.EVENT_BUS.addListener(this::onDrink);
        }
        eventBus.addListener(this::onInterModComms);
        eventBus.addListener(this::setup);
        eventBus.addListener(HomesteadEntities::registerAttributes);

    }

    public void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            StoredTrait.init();
        });
    }

    public void onInterModComms(InterModEnqueueEvent event) {
        if (ModList.get().isLoaded("theoneprobe"))
        {
            InterModComms.sendTo("theoneprobe", "getTheOneProbe", HomesteadTOPPlugin::new);
        }
    }

    public void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        if(event.getPlayer().getLevel().isClientSide()) return;
        ItemStack stack = event.getPlayer().getMainHandItem();
        if(event.getPlayer().isCrouching() && stack.is(Items.NAME_TAG)) {
            event.getPlayer().getLevel();
            Villager entity = new Villager(EntityType.VILLAGER, event.getPlayer().getLevel());
            entity.setPos(event.getPlayer().getPosition(0));
            event.getPlayer().getLevel().addFreshEntity(entity);
        }
        if(event.getPlayer().isCrouching() && stack.is(GrainPileBlockEntity.GRAIN_TAG)) {
            BlockPos pos = event.getPos().above();
            if(event.getPlayer().getLevel().getBlockState(pos).canBeReplaced(new BlockPlaceContext(
                event.getPlayer(),
                event.getHand(),
                stack,
                event.getHitVec()
            ))) {
                event.getPlayer().getLevel().setBlockAndUpdate(
                    pos, GrainPileBlockEntity.GRAIN_TO_PILES.get(stack.getItem()).defaultBlockState()
                );

                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.FAIL);
                if(event.getPlayer().getLevel().getBlockEntity(pos) instanceof GrainPileBlockEntity entity) {
                    entity.takeStack(event.getPlayer());
                }
            }
        }
    }

    public void onDrink(LivingEntityUseItemEvent.Finish event) {
        if(event.getEntity() instanceof Player player) {
            ItemStack stack = event.getItem();
            stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent(cap -> {
                HomesteadFluid.getAlcohol(cap).ifPresent(alcohol -> {
                    player.addEffect(alcohol.getEffectInstance());
                });
            });
        }
    }

    public void getSpeed(LivingEvent.LivingUpdateEvent event) {
        if(event.getEntity() instanceof Player player) {
            LOGGER.info(String.valueOf(player.getAbilities().getWalkingSpeed()));
        }
    }
}
