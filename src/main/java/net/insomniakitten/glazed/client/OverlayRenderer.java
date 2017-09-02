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
import net.insomniakitten.glazed.common.util.IOverlayProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = Glazed.MOD_ID, value = Side.CLIENT)
public class OverlayRenderer {

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGameOverlayEvent.Pre event) {
        if (ClientHelper.isWorldLoaded() && event.getType().equals(ElementType.ALL)) {

            IBlockState stateAt = ClientHelper.getWorld().getBlockState(ClientHelper.getPlayerPos().up());
            if (stateAt.getBlock() instanceof IOverlayProvider) {
                IOverlayProvider provider = (IOverlayProvider) stateAt.getBlock();
                ResourceLocation overlay = provider.getOverlayTexture(stateAt);
                if (overlay != null) {
                    ScaledResolution res = event.getResolution();
                    GlStateManager.enableBlend();
                    Minecraft.getMinecraft().renderEngine.bindTexture(overlay);
                    Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(
                            0, 0, 0, 0, res.getScaledWidth(), res.getScaledHeight());
                    GlStateManager.disableBlend();
                }
            }
        }
    }

}
