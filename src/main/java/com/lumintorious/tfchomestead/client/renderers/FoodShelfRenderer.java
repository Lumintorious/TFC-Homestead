package com.lumintorious.tfchomestead.client.renderers;

import com.lumintorious.tfchomestead.common.block.entity.FoodShelfBlockEntity;
import com.lumintorious.tfchomestead.common.block.entity.HangerBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
class FoodShelfRenderer implements BlockEntityRenderer<FoodShelfBlockEntity> {
    public void render(FoodShelfBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack stack = pBlockEntity.getStack();

        int totalDraws = 16;
        int maxStackSize = Math.min(Math.max(stack.getItem().getItemStackLimit(stack), 1), 64);
        float filled = (float) stack.getCount() / (float) maxStackSize;
        float timeD = (float)(360.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);

        var angleD = pBlockEntity.getBlockState().getValue(HorizontalDirectionalBlock.FACING);
        int angle = switch(angleD) {
            case SOUTH -> 0;
            case EAST -> 90;
            case WEST, DOWN, UP -> 270;
            case NORTH -> 180;
        };


        pPoseStack.pushPose();
        pPoseStack.translate(0.5, 0.5, 0.5);
        pPoseStack.scale(0.45f, 0.45f, 0.45f);
        pPoseStack.mulPose(Vector3f.YP.rotationDegrees(angle));
        var currentDraws = 0;
        for (int i = 0 ; i < 4 ; i++) {
            if(currentDraws >= filled * totalDraws) {
                break;
            }
            pPoseStack.pushPose();
            pPoseStack.translate(0, 0, -0.9f);
            pPoseStack.translate((i % 2 == 0) ? 0.45f : -0.45f, (i < 2) ? 0.45f : -0.45f, 0);
            for(int j = 0 ; j < 4 ; j++) {
                if(currentDraws >= filled * totalDraws) {
//                    pPoseStack.popPose();
                    break;
                } else {
                    currentDraws += 1;
                }
                pPoseStack.translate(0, 0, 0.175f);
                itemRenderer.renderStatic(
                        stack,
                        ItemTransforms.TransformType.FIXED,
                        pPackedLight,
                        pPackedOverlay,
                        pPoseStack,
                        pBufferSource,
                        0
                );
            }
            pPoseStack.popPose();
        }

        pPoseStack.popPose();

        // 1: 1 = 16
        // 2: 2 = 16, 1 = 8
        // 64: 4 = 1, 16 = 4, 32 = 8, 64 = 16

    }
}

