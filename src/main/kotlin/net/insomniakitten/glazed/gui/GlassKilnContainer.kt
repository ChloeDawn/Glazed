package net.insomniakitten.glazed.gui

import net.insomniakitten.glazed.extensions.get
import net.insomniakitten.glazed.extensions.ifNotEmpty
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemStack.EMPTY
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

    private operator fun plusAssign(slot: Slot) {
        addSlotToContainer(slot)
    }

    override fun canInteractWith(player: EntityPlayer) = true

    override fun transferStackInSlot(player: EntityPlayer, sourceIndex: Int): ItemStack {
        val slot = inventorySlots[sourceIndex]

        slot.stack.ifNotEmpty {
            if (sourceIndex > KILN_SLOTS) {
                if (!mergeItemStack(it, 0, 1, false) &&
                    !mergeItemStack(it, 1, 2, false) &&
                    !mergeItemStack(it, 2, 3, false)) {
                    return EMPTY
                }
            } else if (!mergeItemStack(it, KILN_SLOTS, MAX_SLOT, true)) {
                return EMPTY
            }

            slot.onSlotChanged()

            when {
                it.isEmpty -> slot.putStack(EMPTY)
                slot.stack.count == it.count -> return EMPTY
            }

            slot.onTake(player, it)

            return it
        }

        return EMPTY
    }

    companion object {
        private const val KILN_SLOTS = 3
        private const val INVENTORY_SLOTS = 28
        private const val HOTBAR_SLOTS = 9
        private const val MAX_SLOT =
                KILN_SLOTS +
                INVENTORY_SLOTS +
                HOTBAR_SLOTS
    }
}
