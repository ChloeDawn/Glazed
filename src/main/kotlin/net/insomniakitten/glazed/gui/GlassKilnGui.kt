package net.insomniakitten.glazed.gui

import net.insomniakitten.glazed.Glazed
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

@SideOnly(Side.CLIENT)
class GlassKilnGui(
        private val te: TileEntity,
        private val player: EntityPlayer
) : GuiContainer(GlassKilnContainer(te, player)) {
    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawDefaultBackground()
        super.drawScreen(mouseX, mouseY, partialTicks)
        renderHoveredToolTip(mouseX, mouseY)
    }

    override fun drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int) {
        val kiln = te.displayName!!.unformattedText
        val inv = player.inventory.displayName.unformattedText
        fontRenderer.drawString(kiln, 88 - fontRenderer.getStringWidth(kiln) / 2, 6, 0x404040)
        fontRenderer.drawString(inv, 8, ySize - 94, 0x404040)
    }

    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        GlStateManager.color(1.0f, 1.0f, 1.0f)
        mc.textureManager.bindTexture(TEXTURE)
        val x = (width - xSize) / 2
        val y = (height - ySize) / 2
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize)
    }

    companion object {
        private val TEXTURE = ResourceLocation(Glazed.ID, "textures/gui/kiln.png")
    }
}
