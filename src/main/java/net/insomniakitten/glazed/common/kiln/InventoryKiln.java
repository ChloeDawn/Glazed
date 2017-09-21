package net.insomniakitten.glazed.common.kiln;

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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class InventoryKiln {

    public static class KilnContainer extends Container {

        private EntityPlayer player;
        private IItemHandler inventory;

        public KilnContainer(TileEntity tile, EntityPlayer player) {
            this.player = player;
            if (tile.hasCapability(TileKiln.CAPABILITY, null)) {
                inventory = tile.getCapability(TileKiln.CAPABILITY, null);
            }
            createKilnSlots();
            createPlayerSlots();
        }

        private void createPlayerSlots() {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 9; j++) {
                    int index = j + i * 9 + 9;
                    addSlotToContainer(new Slot(player.inventory, index, 8 + (j * 18), 8 + (i * 18) + 22 + (3 * 18)));
                }
            }
            for (int k = 0; k < 9; k++) {
                addSlotToContainer(new Slot(player.inventory, k, 8 + (k * 18), 8 + 58 + 22 + (3 * 18)));
            }
        }

        private void createKilnSlots() {
            if (inventory != null) {
                for (TileKiln.Slots slot : TileKiln.Slots.values()) {
                    addSlotToContainer(new SlotItemHandler(inventory, slot.ordinal(), slot.getX(), slot.getY()));
                }
            }
        }

        @Override
        @Nonnull
        public ItemStack transferStackInSlot(EntityPlayer player, int sourceIndex) {
            Slot slot = inventorySlots.get(sourceIndex);
            ItemStack stack = slot.getStack();
            if (slot.getHasStack()) {
                int kilnSlots = 3, inventorySlots = 28, hotbarSlots = 9;
                int maxSlot = kilnSlots + inventorySlots + hotbarSlots;
                if (sourceIndex > kilnSlots) {
                    if (!mergeItemStack(stack, 0, 1, false) && (!mergeItemStack(stack, 2, 3, false)) &&
                            (!mergeItemStack(stack, 1, 2, false))) {
                        return ItemStack.EMPTY;
                    }
                } else if (!mergeItemStack(stack, kilnSlots, maxSlot, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChanged();
                if (stack.isEmpty()) {
                    slot.putStack(ItemStack.EMPTY);
                } else if (stack.getCount() == slot.getStack().getCount()) {
                    return ItemStack.EMPTY;
                }
                slot.onTake(player, stack);
                return stack;
            }
            return ItemStack.EMPTY;
        }

        @Override
        public boolean canInteractWith(EntityPlayer player) {
            return true;
        }

    }

    @SideOnly(Side.CLIENT)
    public static class KilnGui extends GuiContainer {

        private final ResourceLocation guiTexture = new ResourceLocation(Glazed.MOD_ID, "textures/gui/kiln.png");

        private TileKiln kiln;

        public KilnGui(TileEntity tile, EntityPlayer player) {
            super(new KilnContainer(tile, player));
            kiln = (TileKiln) tile;
        }

        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            drawDefaultBackground();
            super.drawScreen(mouseX, mouseY, partialTicks);
            renderHoveredToolTip(mouseX, mouseY);
        }

        @Override
        protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
            String kilnInv = kiln.getDisplayName().getUnformattedText();
            String playerInv = mc.player.inventory.getDisplayName().getUnformattedText();
            int width = fontRenderer.getStringWidth(kilnInv);
            fontRenderer.drawString(kilnInv, 88 - (width / 2), 6, 0x404040);
            fontRenderer.drawString(playerInv, 8, ySize - 94, 0x404040);
        }

        @Override
        protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
            GlStateManager.color(1f, 1f, 1f);
            mc.getTextureManager().bindTexture(guiTexture);
            int x = (width - xSize) / 2;
            int y = (height - ySize) / 2;
            drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
        }

    }

}
