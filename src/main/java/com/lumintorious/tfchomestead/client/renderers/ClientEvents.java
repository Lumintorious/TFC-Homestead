package com.lumintorious.tfchomestead.client.renderers;

import com.lumintorious.tfchomestead.common.block.*;
import com.lumintorious.tfchomestead.common.drinks.HomesteadFluid;
import com.lumintorious.tfchomestead.common.entity.HomesteadEntities;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.dries007.tfc.common.fluids.FlowingFluidRegistryObject;
import net.dries007.tfc.common.fluids.MixingFluid;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;
import java.util.Optional;

public class ClientEvents {
    public static void init() {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(ClientEvents::registerEntityRenderers);
        MinecraftForge.EVENT_BUS.addListener(ClientEvents::addTooltipsToItem);
    }

    public static void setup(FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(HomesteadBlocks.JAR.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(HomesteadBlocks.FULL_JAR.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(HomesteadBlocks.WHEAT_GRAIN_PILE.get(), RenderType.solid());
        HomesteadBlocks.HANGERS.values().forEach(reg -> ItemBlockRenderTypes.setRenderLayer(reg.get(), RenderType.cutout()));
        HomesteadBlocks.FOOD_SHELVES.values().forEach(reg -> ItemBlockRenderTypes.setRenderLayer(reg.get(), RenderType.cutout()));
    }

    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(HomesteadBlockEntities.JAR.get(), ctx -> new JarRenderer());
        event.registerBlockEntityRenderer(HomesteadBlockEntities.HANGER.get(), ctx -> new HangerRenderer());
        event.registerBlockEntityRenderer(HomesteadBlockEntities.FOOD_SHELF.get(), ctx -> new FoodShelfRenderer());

        event.registerEntityRenderer(HomesteadEntities.VILLAGER.get(), VillagerRenderer::new);
    }

    public static void addTooltipsToItem(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent(cap -> {
            FluidStack fluidStack = cap.getFluidInTank(0);
            if(!fluidStack.isEmpty()) {
                event.getToolTip().add(new TranslatableComponent(fluidStack.getTranslationKey()).withStyle(ChatFormatting.GRAY));
                var fluid = HomesteadFluid.AGED_ALCOHOL.inverse().keySet().stream().filter(f ->
                    f.getSource() == fluidStack.getFluid()
                ).findAny();
                fluid.ifPresent(f -> {
                    event.getToolTip().add(HomesteadFluid.AGED_ALCOHOL.inverse().get(f).getTooltip());
                });
            }
        });
    }
}
