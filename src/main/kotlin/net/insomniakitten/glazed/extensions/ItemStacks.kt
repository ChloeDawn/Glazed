@file:JvmName("ItemStacks")

package net.insomniakitten.glazed.extensions

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

import net.minecraft.block.Block
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.items.ItemHandlerHelper

inline val ItemStack.isNotEmpty: Boolean get() = !isEmpty

inline val ItemStack.creatorModId get() = item.getCreatorModId(this)

fun ItemStack.spawnAsEntity(world: World, pos: BlockPos) {
    Block.spawnAsEntity(world, pos, this)
}

fun ItemStack.copy(newCount: Int): ItemStack =
        if (newCount > 0) {
            copy().apply { count = newCount }
        } else ItemStack.EMPTY

infix fun ItemStack.canStackWith(other: ItemStack) =
        ItemHandlerHelper.canItemStacksStack(this, other)
