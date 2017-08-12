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

import net.insomniakitten.glazed.kiln.TileKiln.Slots;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerKiln extends Container {

    private EntityPlayer player;
    private IItemHandler inventory;

    public ContainerKiln(TileEntity tile, EntityPlayer player) {
        this.player = player;
        if (tile.hasCapability(TileKiln.CAPABILITY, null)) {
            inventory = tile.getCapability(TileKiln.CAPABILITY, null);
        }
        this.createKilnSlots();
        this.createPlayerSlots();
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
        for (Slots slot : Slots.values()) {
            this.addSlotToContainer(new SlotItemHandler(inventory, slot.ordinal(), slot.getX(), slot.getY()));
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int sourceIndex) {
        Slot holder = inventorySlots.get(sourceIndex);
        ItemStack stack = holder.getStack();

        if (holder.getHasStack()) {
            int kilnSlots = 3;
            int inventorySlots = 28;
            int hotbarIndex = kilnSlots + inventorySlots;
            int playerSlots = hotbarIndex + 9;

            if (sourceIndex > kilnSlots) {
                if (!this.mergeItemStack(stack, 0, 1, false)
                && (!this.mergeItemStack(stack, 2, 3, false))
                && (!this.mergeItemStack(stack, 1, 2, false)))
                    return ItemStack.EMPTY;
            } else {
                if (!mergeItemStack(stack, kilnSlots, playerSlots, true)) {
                    return ItemStack.EMPTY;
                }
            }

            holder.onSlotChanged();

            if (stack.isEmpty()) {
                holder.putStack(ItemStack.EMPTY);
            } else {
                if (stack.getCount() == holder.getStack().getCount()) {
                    return ItemStack.EMPTY;
                }
            }

            holder.onTake(player, stack);
            return stack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

}
