package net.insomniakitten.glazed.client.render.selection;

/*
 *  Copyright 2017 InsomniaKitten
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.function.Function;

public interface ISelectionBoxRenderer {

    @SideOnly(Side.CLIENT)
    static void renderSelectionBoxes(IBlockState state, BlockPos pos, ISelectionBoxRenderer iface, EntityPlayer player, float partialTicks) {
        iface.getSelectionBoxes().apply(Pair.of(pos, state)).forEach((offsetPos, selectionBox) -> {
            GlStateManager.disableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(
                    GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                    GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
            );
            GlStateManager.glLineWidth(2.0F);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
            double offsetX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
            double offsetY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
            double offsetZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
            AxisAlignedBB target = selectionBox.grow(0.002D).offset(offsetPos).offset(-offsetX, -offsetY, -offsetZ);
            RenderGlobal.drawSelectionBoundingBox(target, 0.0F, 0.0F, 0.0F, 0.4F);
            GlStateManager.depthMask(true);
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.enableAlpha();
        });
    }

    Function<Pair<BlockPos, IBlockState>, Map<BlockPos, AxisAlignedBB>> getSelectionBoxes();

}
