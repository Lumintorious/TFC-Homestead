package com.lumintorious.tfchomestead.common.drinks;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.lumintorious.tfchomestead.TFCHomestead;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.fluids.*;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.registry.RegistrationHelpers;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import static net.dries007.tfc.common.fluids.TFCFluids.*;

public class HomesteadFluid {
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, TFCHomestead.MOD_ID);;

    public static final BiMap<AgedAlcohol, FlowingFluidRegistryObject<MixingFluid>> AGED_ALCOHOL;

    static {
        var oneWayMap = Helpers.mapOfKeys(AgedAlcohol.class, (fluid) -> register(fluid.getId(), "flowing_" + fluid.getId(), (properties) -> {
            properties.block(TFCBlocks.ALCOHOLS.get(fluid)).bucket(TFCItems.FLUID_BUCKETS.get(FluidType.asType(fluid)));
        }, FluidAttributes.builder(WATER_STILL, WATER_FLOW).translationKey("fluid.tfchomestead." + fluid.getId()).color(fluid.getColor()).overlay(WATER_OVERLAY).sound(SoundEvents.BUCKET_FILL, SoundEvents.BUCKET_EMPTY), MixingFluid.Source::new, MixingFluid.Flowing::new));
        AGED_ALCOHOL = HashBiMap.create(oneWayMap);
    }

    private static <F extends FlowingFluid> FlowingFluidRegistryObject<F> register(String sourceName, String flowingName, Consumer<ForgeFlowingFluid.Properties> builder, FluidAttributes.Builder attributes, Function<ForgeFlowingFluid.Properties, F> sourceFactory, Function<ForgeFlowingFluid.Properties, F> flowingFactory) {
        return RegistrationHelpers.registerFluid(FLUIDS, sourceName, flowingName, builder, attributes, sourceFactory, flowingFactory);
    }

    public static Optional<AgedAlcohol> getAlcohol(IFluidHandlerItem handler) {
        FluidStack fluidStack = handler.getFluidInTank(0);
        if(!fluidStack.isEmpty()) {
            var fluid = HomesteadFluid.AGED_ALCOHOL.inverse().keySet().stream().filter(f -> {
                System.out.println(f.getSource().getSource());
                System.out.println(fluidStack.getFluid());
                return f.getSource().getSource() == fluidStack.getFluid();
            }).findAny();
            return fluid.map(f -> HomesteadFluid.AGED_ALCOHOL.inverse().get(f));
        }
        return Optional.empty();
    }
}
