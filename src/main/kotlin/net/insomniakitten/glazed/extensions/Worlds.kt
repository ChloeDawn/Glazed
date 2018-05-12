@file:JvmName("Worlds")

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
import net.minecraft.block.state.IBlockState
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World

operator fun IBlockAccess.get(pos: BlockPos) = AccessPosition(this, pos)

operator fun World.get(pos: BlockPos) = WorldPosition(this, pos)

fun IBlockAccess.canReplace(pos: BlockPos) = this[pos].block.isReplaceable(this, pos)

data class AccessPosition(private val access: IBlockAccess, private val pos: BlockPos) {
    val state: IBlockState get() = access.getBlockState(pos)
    val block: Block get() = state.block
    val entity: TileEntity? get() = access.getTileEntity(pos)
}

data class WorldPosition(private val world: World, private val pos: BlockPos) {
    var state: IBlockState
        get() = world.getBlockState(pos)
        set(value) {
            world.setBlockState(pos, value)
        }

    var block: Block
        get() = state.block
        set(value) {
            world.setBlockState(pos, value.defaultState)
        }

    var entity: TileEntity?
        get() = world.getTileEntity(pos)
        set(value) = if (value != null) {
            world.setTileEntity(pos, value)
        } else world.removeTileEntity(pos)
}
