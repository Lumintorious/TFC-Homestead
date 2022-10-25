package com.lumintorious.tfchomestead.common.world;

import com.lumintorious.tfchomestead.TFCHomestead;
import com.lumintorious.tfchomestead.common.TFCHomesteadConfig;
import com.mojang.serialization.Codec;
import net.dries007.tfc.mixin.accessor.StructureTemplateAccessor;
import net.dries007.tfc.world.feature.tree.TreeHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockRotProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class VillagerHomesteadFeature extends Feature<VillagerHomesteadConfig> {
    public VillagerHomesteadFeature(Codec<VillagerHomesteadConfig> pCodec) {
        super(pCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<VillagerHomesteadConfig> context) {
        if(!TFCHomesteadConfig.COMMON.enableVillagerSpawns.get()) return false;
        final WorldGenLevel level = context.level();
        final Random random = context.random();
        final VillagerHomesteadConfig config = context.config();
        final List<Map.Entry<String, Integer>> entries = config.offsetsY().entrySet().stream().toList();
        final Map.Entry<String, Integer> entry = entries.get(random.nextInt(entries.size()));
        final BlockPos pos = context.origin().above(entry.getValue());

        final ChunkPos chunkPos = new ChunkPos(pos);
        final StructureManager manager = TreeHelpers.getStructureManager(level);
        final StructurePlaceSettings settings = TreeHelpers.getPlacementSettings(level, chunkPos, random);
        settings.clearProcessors().addProcessor(new BlockRotProcessor(Mth.clamp(1, 0.0F, 1.0F))).setRandom(random);
        final ResourceLocation structureId = new ResourceLocation(entry.getKey());//config.structureNames().get(random.nextInt(config.structureNames().size()));
        final StructureTemplate structure = manager.getOrCreate(structureId);
        final StructureTemplate template = structure;
        if (((StructureTemplateAccessor) structure).accessor$getPalettes().isEmpty())
        {
            throw new IllegalStateException("Empty structure: " + structureId);
        }
            settings.setIgnoreEntities(false);
            settings.setKeepLiquids(true);
            settings.setKnownShape(true);

            structure.placeInWorld(
                level,
                pos, pos, settings, random, 0
            );
        final List<StructureTemplate.StructureBlockInfo> transformedBlockInfos = settings.getRandomPalette(((StructureTemplateAccessor) template).accessor$getPalettes(), pos).blocks();
        BoundingBox boundingBox = settings.getBoundingBox();
        for (StructureTemplate.StructureBlockInfo blockInfo : StructureTemplate.processBlockInfos(level, pos, pos, settings, transformedBlockInfos, template))
        {
            BlockPos posAt = blockInfo.pos;
            if (boundingBox == null || boundingBox.isInside(posAt))
            {
                BlockState stateAt = level.getBlockState(posAt);
                    // No world, can't rotate with world context
                @SuppressWarnings("deprecation")
                BlockState stateReplace = blockInfo.state.mirror(settings.getMirror()).rotate(settings.getRotation());
                TFCHomestead.LOGGER.info(stateReplace.toString());
                level.setBlock(posAt, Blocks.AIR.defaultBlockState(), 2);
                level.setBlock(posAt, stateReplace, 2);
            }
        }
//            TreeHelpers.placeTemplate(structure, settings, level, mutablePos.subtract(TreeHelpers.transformCenter(structure.getSize(), settings)));
            return true;
    }
}
