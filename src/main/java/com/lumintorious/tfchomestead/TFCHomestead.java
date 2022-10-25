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
import com.lumintorious.tfchomestead.common.villagers.TFCVillagerProfessions;
import com.lumintorious.tfchomestead.common.world.HomesteadFeatures;
import com.lumintorious.tfchomestead.compat.jade.HomesteadTOPPlugin;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

import net.dries007.tfc.common.items.TFCItems;
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
            eventBus.addListener(ClientEvents::setup);
            ClientEvents.init();
        }

        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGHEST, this::onRightClick);
        MinecraftForge.EVENT_BUS.addListener(HomesteadEntities::resetTradesOnSpawn);
        MinecraftForge.EVENT_BUS.addListener(this::onDrink);

        TFCVillagerProfessions.POI_TYPES.register(eventBus);
        TFCVillagerProfessions.PROFESSIONS.register(eventBus);
        HomesteadFeatures.FEATURES.register(eventBus);
        HomesteadEntities.ENTITIES.register(eventBus);
        HomesteadBlocks.BLOCKS.register(eventBus);
        HomesteadItems.ITEMS.register(eventBus);
        HomesteadBlockEntities.BLOCK_ENTITIES.register(eventBus);
        HomesteadFluid.FLUIDS.register(eventBus);

        eventBus.addListener(this::onInterModComms);
        eventBus.addListener(this::setup);
        eventBus.addListener(HomesteadEntities::registerAttributes);

        TFCHomesteadConfig.init();
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
        final Player player = event.getPlayer();
        final Level level = player.getLevel();
        if(level.isClientSide()) return;
        ItemStack stack = player.getMainHandItem();
        if(player.isCrouching() && stack.is(GrainPileBlockEntity.GRAIN_TAG)) {
            BlockPos pos = event.getPos().above();
            if(level.getBlockState(pos).canBeReplaced(new BlockPlaceContext(
                player,
                event.getHand(),
                stack,
                event.getHitVec()
            ))) {
                HomesteadBlocks.GRAIN_PILES.forEach((grain, pile) -> {
                    if (TFCItems.FOOD.get(grain.getFood()).get().equals(stack.getItem())) {
                        level.setBlockAndUpdate(
                            pos, pile.get().defaultBlockState()
                        );
                    }
                });

                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
                if(level.getBlockEntity(pos) instanceof GrainPileBlockEntity entity) {
                    entity.takeStack(player);
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
}
