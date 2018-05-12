package net.insomniakitten.glazed.gui

import net.insomniakitten.glazed.extensions.get
import net.insomniakitten.glazed.extensions.isNotEmpty
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.SlotItemHandler

class GlassKilnContainer(te: TileEntity, player: EntityPlayer) : Container() {
    init {
        (te[CapabilityItemHandler.ITEM_HANDLER_CAPABILITY])?.let {
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

        if (stack.isNotEmpty) {
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

            if (stack.isNotEmpty) {
                if (slot.stack.count == stack.count) {
                    return ItemStack.EMPTY
                }
            } else slot.putStack(ItemStack.EMPTY)

            slot.onTake(player, stack)

            return stack
        }
        return ItemStack.EMPTY
    }
}
