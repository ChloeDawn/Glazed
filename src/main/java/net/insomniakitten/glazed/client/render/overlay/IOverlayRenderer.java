package net.insomniakitten.glazed.client.render.overlay;

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
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.function.Function;

public interface IOverlayRenderer {

    @SideOnly(Side.CLIENT)
    static void renderOverlay(IBlockState state, IOverlayRenderer renderer, int screenWidth, int screenHeight) {
        ResourceLocation overlay = renderer.getOverlayTexture().apply(state);
        if (overlay == null) return;
        GlStateManager.enableBlend();
        Minecraft.getMinecraft().renderEngine.bindTexture(overlay);
        Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(0, 0, 0, 0, screenWidth, screenHeight);
        GlStateManager.disableBlend();
    }

    Function<IBlockState, ResourceLocation> getOverlayTexture();

}
