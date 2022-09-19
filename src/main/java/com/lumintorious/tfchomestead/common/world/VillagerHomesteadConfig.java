package com.lumintorious.tfchomestead.common.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.dries007.tfc.world.Codecs;
import net.dries007.tfc.world.feature.BoulderConfig;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.List;
import java.util.Map;

public record VillagerHomesteadConfig(Map<String, Integer> offsetsY) implements FeatureConfiguration {
    public static final Codec<VillagerHomesteadConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codecs.mapListCodec(Codecs.recordPairCodec(
                Codec.STRING, "structure",
                Codec.INT, "offsetY"
        )).fieldOf("offsetsY").forGetter(c -> c.offsetsY)
    ).apply(instance, VillagerHomesteadConfig::new));
}
