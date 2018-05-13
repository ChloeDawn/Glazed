package net.insomniakitten.glazed.gui

import net.insomniakitten.glazed.Glazed
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.renderer.GlStateManager.color
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
        val kilnInventory = te.displayName!!.unformattedText
        val playerInventory = player.inventory.displayName.unformattedText
        with(fontRenderer) {
            val offset = getStringWidth(kilnInventory) / 2
            drawString(kilnInventory, 88 - offset, 6, 0x404040)
            drawString(playerInventory, 8, ySize - 94, 0x404040)
        }
    }

    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        val x = (width - xSize) / 2
        val y = (height - ySize) / 2
        color(1.0F, 1.0F, 1.0F, 1.0F)
        mc.textureManager.bindTexture(TEXTURE)
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize)
    }

    companion object {
        private val TEXTURE = ResourceLocation(Glazed.ID, "textures/gui/kiln.png")
    }
}
