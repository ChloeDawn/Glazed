package net.insomniakitten.glazed.gui;

/*
 *  Copyright 2018 InsomniaKitten
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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import static net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;

public final class GlassKilnContainer extends Container {
    private final EntityPlayer player;
    private final IItemHandler inventory;

    public GlassKilnContainer(TileEntity te, EntityPlayer player) {
        this.player = player;
        if (te.hasCapability(ITEM_HANDLER_CAPABILITY, null)) {
            this.inventory = te.getCapability(ITEM_HANDLER_CAPABILITY, null);
        } else this.inventory = null;
        createKilnSlots();
        createPlayerSlots();
    }

    private void createPlayerSlots() {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                final int i = column + (row * 9) + 9;
                final int x = 8 + (column * 18);
                final int y = 8 + (row * 18) + 22 + (3 * 18);
                addSlotToContainer(new Slot(player.inventory, i, x, y));
            }
        }

        for (int column = 0; column < 9; column++) {
            final int x = 8 + (column * 18);
            final int y = 8 + 58 + 22 + (3 * 18);
            addSlotToContainer(new Slot(player.inventory, column, x, y));
        }
    }

    private void createKilnSlots() {
        if (inventory != null && inventory.getSlots() >= 4) {
            addSlotToContainer(new SlotItemHandler(inventory, 0, 34, 17));
            addSlotToContainer(new SlotItemHandler(inventory, 1, 56, 17));
            addSlotToContainer(new SlotItemHandler(inventory, 2, 45, 53));
            addSlotToContainer(new SlotItemHandler(inventory, 3, 116, 35));
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int sourceIndex) {
        final Slot slot = inventorySlots.get(sourceIndex);
        final ItemStack stack = slot.getStack();

        if (stack.isEmpty()) return ItemStack.EMPTY;

        final int kilnSlots = 3, inventorySlots = 28, hotbarSlots = 9;
        final int maxSlot = kilnSlots + inventorySlots + hotbarSlots;

        if (sourceIndex <= kilnSlots) {
            if (!mergeItemStack(stack, kilnSlots, maxSlot, true)) {
                return ItemStack.EMPTY;
            }
        } else if (!mergeItemStack(stack, 0, 1, false)
                && (!mergeItemStack(stack, 2, 3, false))
                && (!mergeItemStack(stack, 1, 2, false))) {
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

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }
}
