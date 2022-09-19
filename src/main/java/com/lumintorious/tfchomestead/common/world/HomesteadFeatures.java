package com.lumintorious.tfchomestead.common.world;

import com.lumintorious.tfchomestead.TFCHomestead;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class HomesteadFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(
        ForgeRegistries.FEATURES,
        TFCHomestead.MOD_ID
    );

    public static final RegistryObject<VillagerHomesteadFeature> MUD_HUT_FEATURE =
        FEATURES.register("villager_homestead", () -> new VillagerHomesteadFeature(VillagerHomesteadConfig.CODEC));

//    public static void placeTemplate(StructureTemplate template, StructurePlaceSettings placementIn, LevelAccessor level, BlockPos pos) {
//        List<StructureTemplate.StructureBlockInfo> transformedBlockInfos = placementIn.getRandomPalette(((StructureTemplateAccessor)template).accessor$getPalettes(), pos).blocks();
//        List<StructureTemplate.StructureEntityInfo> transformedEntities = ((StructureTemplateEntitiesAccessor)template).accessor$getEntityInfoList();
//
//        BoundingBox boundingBox = placementIn.getBoundingBox();
//        Iterator var6 = StructureTemplate.processBlockInfos(level, pos, pos, placementIn, transformedBlockInfos, template).iterator();
////        ((StructureTemplateAccessor) template).accessor$getPalettes().ge
//
//        for(var entityInfo : transformedEntities) {
////            var entity = LivingEntity
//            ((StructureTemplateEntitiesAccessor) template).accessor$getEntityInfoList()
////            StructureProcessor
//        }
//        while(true) {
//            StructureTemplate.StructureBlockInfo blockInfo;
//            BlockPos posAt;
//            BlockState stateAt;
//            do {
//                do {
//                    if (!var6.hasNext()) {
//                        return;
//                    }
//
//                    blockInfo = (StructureTemplate.StructureBlockInfo)var6.next();
//                    posAt = blockInfo.pos;
//                } while(boundingBox != null && !boundingBox.isInside(posAt));
//
//                stateAt = level.getBlockState(posAt);
//            } while(!EnvironmentHelpers.isWorldgenReplaceable(stateAt) && !Helpers.isBlock(stateAt.getBlock(), BlockTags.LEAVES));
//
//            BlockState stateReplace = blockInfo.state.mirror(placementIn.getMirror()).rotate(placementIn.getRotation());
//            level.setBlock(posAt, stateReplace, 2);
//        }
//    }
}
