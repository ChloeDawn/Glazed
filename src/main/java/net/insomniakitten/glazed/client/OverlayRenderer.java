package net.insomniakitten.glazed.client;

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

import net.insomniakitten.glazed.Glazed;
import net.insomniakitten.glazed.glass.BlockGlass;
import net.insomniakitten.glazed.glass.GlassType;
import net.insomniakitten.glazed.material.BlockMaterial;
import net.insomniakitten.glazed.material.MaterialType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(Side.CLIENT)
public class OverlayRenderer {

    private static final ResourceLocation VOIDIC_OVERLAY = new ResourceLocation(
            Glazed.MOD_ID, "textures/overlay/voided.png");

    private  static final ResourceLocation SLAG_OVERLAY = new ResourceLocation(
            Glazed.MOD_ID, "textures/overlay/slag.png");

    private static final BlockPos.MutableBlockPos MUTABLE = new BlockPos.MutableBlockPos();

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGameOverlayEvent.Pre event) {
        if (event.getType().equals(RenderGameOverlayEvent.ElementType.ALL)) {
            EntityPlayer player = Minecraft.getMinecraft().player;

            if (player == null || player.world == null) {
                return;
            }

            MUTABLE.setPos(player.posX, player.posY + 1, player.posZ);
            IBlockState state = player.world.getBlockState(MUTABLE);

            boolean isBlockGlass = state.getBlock() instanceof BlockGlass;
            boolean isBlockMaterial = state.getBlock() instanceof BlockMaterial;

            int width = event.getResolution().getScaledWidth();
            int height = event.getResolution().getScaledHeight();

            if (isBlockGlass && GlassType.get(state).equals(GlassType.VOIDIC)) {
                drawOverlay(width, height, VOIDIC_OVERLAY);
            }

            if (isBlockMaterial && MaterialType.get(state).equals(MaterialType.SLAG)) {
                drawOverlay(width, height, SLAG_OVERLAY);
            }
        }
    }

    public static void drawOverlay(int width, int height, ResourceLocation texture) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(0, 0, 0, 0, width, height);
        GlStateManager.popMatrix();
    }

}
