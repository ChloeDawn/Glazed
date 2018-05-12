@file:JvmName("Worlds")

package net.insomniakitten.glazed.extensions

import net.minecraft.block.state.IBlockState
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World

operator fun IBlockAccess.get(pos: BlockPos) = AccessPosition(this, pos)

operator fun World.get(pos: BlockPos) = WorldPosition(this, pos)

open class AccessPosition internal constructor(
        protected val access: IBlockAccess,
        protected val pos: BlockPos
) {
    open val state: IBlockState
        get() = access.getBlockState(pos)

    open val entity: TileEntity?
        get() = access.getTileEntity(pos)

    fun doesSideBlockRendering(side: EnumFacing): Boolean {
        return state.doesSideBlockRendering(access, pos, side)
    }

    val isReplaceable get() = state.block.isReplaceable(access, pos)
}

class WorldPosition internal constructor(
        world: World,
        pos: BlockPos
) : AccessPosition(world, pos) {
    private val world = access as World

    override var state: IBlockState
        get() = super.state
        set(value) {
            world.setBlockState(pos, value)
        }

    override var entity: TileEntity?
        get() = super.entity
        set(value) = if (value != null) {
            world.setTileEntity(pos, value)
        } else world.removeTileEntity(pos)
}
