package net.insomniakitten.glazed.gui

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

import net.insomniakitten.glazed.extensions.from
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.SlotItemHandler

class GlassKilnContainer(te: TileEntity, player: EntityPlayer) : Container() {
    init {
        (CapabilityItemHandler.ITEM_HANDLER_CAPABILITY from te)?.let {
            this += SlotItemHandler(it, 0, 34, 17)
            this += SlotItemHandler(it, 1, 56, 17)
            this += SlotItemHandler(it, 2, 45, 53)
            this += SlotItemHandler(it, 3, 116, 35)
        }

        for (row in 0..2) for (column in 0..8) {
            val i = column + row * 9 + 9
            val x = 8 + column * 18
            val y = 8 + row * 18 + 22 + 3 * 18
            this += Slot(player.inventory, i, x, y)
        }

        for (column in 0..8) {
            val x = 8 + column * 18
            val y = 8 + 58 + 22 + 3 * 18
            this += Slot(player.inventory, column, x, y)
        }
    }

    private operator fun Container.plusAssign(slot: Slot) {
        addSlotToContainer(slot)
    }

    override fun canInteractWith(player: EntityPlayer) = true

    override fun transferStackInSlot(player: EntityPlayer, sourceIndex: Int): ItemStack {
        val slot = inventorySlots[sourceIndex]
        val stack = slot.stack

        if (stack.isEmpty) return ItemStack.EMPTY

        val kilnSlots = 3
        val inventorySlots = 28
        val hotbarSlots = 9
        val maxSlot = kilnSlots + inventorySlots + hotbarSlots

        if (sourceIndex <= kilnSlots) {
            if (!mergeItemStack(stack, kilnSlots, maxSlot, true)) {
                return ItemStack.EMPTY
            }
        } else if (!mergeItemStack(stack, 0, 1, false) &&
                   !mergeItemStack(stack, 1, 2, false) &&
                   !mergeItemStack(stack, 2, 3, false)) {
            return ItemStack.EMPTY
        }

        slot.onSlotChanged()

        if (stack.isEmpty) {
            slot.putStack(ItemStack.EMPTY)
        } else if (stack.count == slot.stack.count) {
            return ItemStack.EMPTY
        }

        slot.onTake(player, stack)

        return stack
    }
}
