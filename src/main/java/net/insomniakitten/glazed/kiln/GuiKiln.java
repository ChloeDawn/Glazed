package net.insomniakitten.glazed.kiln;

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
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class GuiKiln extends GuiContainer {

    private ResourceLocation texture = new ResourceLocation(
            Glazed.MOD_ID, "textures/gui/kiln.png");

    public GuiKiln(TileEntity tile, EntityPlayer player) {
        super(new ContainerKiln(
                tile, player));
    }

    @Override
    public void drawScreen(
            int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(
            int mouseX, int mouseY) {
        String label = I18n.format("tile.glazed.kiln.name");
        int width = this.fontRenderer.getStringWidth(label);
        int color = 4210752;
        this.fontRenderer.drawString(
                label, 88 - (width / 2), 6, color);
        this.fontRenderer.drawString(
                I18n.format("container.inventory"), 8, ySize - 94, color);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(
            float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1f, 1f, 1f);
        mc.getTextureManager().bindTexture(texture);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }

}