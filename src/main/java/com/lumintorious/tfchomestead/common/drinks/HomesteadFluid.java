package com.lumintorious.tfchomestead.common.drinks;

import com.lumintorious.tfchomestead.TFCHomestead;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.fluids.FlowingFluidRegistryObject;
import net.dries007.tfc.common.fluids.FluidType;
import net.dries007.tfc.common.fluids.MixingFluid;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.registry.RegistrationHelpers;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import static net.dries007.tfc.common.fluids.TFCFluids.*;

public class HomesteadFluid {
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, TFCHomestead.MOD_ID);;

    public static final Map<AgedAlcohol, FlowingFluidRegistryObject<MixingFluid>> AGED_ALCOHOL;

    static {
        AGED_ALCOHOL = Helpers.mapOfKeys(AgedAlcohol.class, (fluid) -> register(fluid.getId(), "flowing_" + fluid.getId(), (properties) -> {
            properties.block(TFCBlocks.ALCOHOLS.get(fluid)).bucket(TFCItems.FLUID_BUCKETS.get(FluidType.asType(fluid)));
        }, FluidAttributes.builder(WATER_STILL, WATER_FLOW).translationKey("fluid.tfchomestead." + fluid.getId()).color(fluid.getColor()).overlay(WATER_OVERLAY).sound(SoundEvents.BUCKET_FILL, SoundEvents.BUCKET_EMPTY), MixingFluid.Source::new, MixingFluid.Flowing::new));
    }

    private static <F extends FlowingFluid> FlowingFluidRegistryObject<F> register(String sourceName, String flowingName, Consumer<ForgeFlowingFluid.Properties> builder, FluidAttributes.Builder attributes, Function<ForgeFlowingFluid.Properties, F> sourceFactory, Function<ForgeFlowingFluid.Properties, F> flowingFactory) {
        return RegistrationHelpers.registerFluid(FLUIDS, sourceName, flowingName, builder, attributes, sourceFactory, flowingFactory);
    }

    public static Optional<AgedAlcohol> getAlcohol(IFluidHandlerItem handler) {
        FluidStack fluidStack = handler.getFluidInTank(0);
        if(!fluidStack.isEmpty()) {
            return HomesteadFluid.AGED_ALCOHOL
                    .entrySet()
                    .stream()
                    .filter(entry -> entry.getValue().getSource().getSource() == fluidStack.getFluid())
                    .map(Map.Entry::getKey).findAny();
        }
        return Optional.empty();
    }
}
