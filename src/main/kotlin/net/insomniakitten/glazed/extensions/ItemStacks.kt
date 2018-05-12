@file:JvmName("ItemStacks")

package net.insomniakitten.glazed.extensions

import net.minecraft.block.Block
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.items.ItemHandlerHelper

inline val ItemStack.isNotEmpty: Boolean get() = !isEmpty

inline fun ItemStack.ifEmpty(consumer: (ItemStack) -> Unit) {
    if (isEmpty) consumer(this)
}

inline fun ItemStack.ifNotEmpty(consumer: (ItemStack) -> Unit) {
    if (isNotEmpty) consumer(this)
}

fun ItemStack.spawnAsEntity(world: World, pos: BlockPos) {
    Block.spawnAsEntity(world, pos, this)
}

fun ItemStack.copy(newCount: Int): ItemStack =
        if (newCount > 0) {
            copy().apply { count = newCount }
        } else ItemStack.EMPTY

infix fun ItemStack.canStackWith(other: ItemStack) =
        ItemHandlerHelper.canItemStacksStack(this, other)
