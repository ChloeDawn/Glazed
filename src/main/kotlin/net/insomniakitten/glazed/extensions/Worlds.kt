@file:JvmName("Worlds")

package net.insomniakitten.glazed.extensions

import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import kotlin.reflect.KClass

fun IBlockAccess.isReplaceable(pos: BlockPos) = getBlockState(pos).block.isReplaceable(this, pos)

fun IBlockAccess.doesSideBlockRendering(pos: BlockPos, side: EnumFacing) =
        getBlockState(pos).doesSideBlockRendering(this, pos, side)

fun <T : TileEntity> IBlockAccess.getTileEntity(pos: BlockPos, clazz: KClass<T>): T? {
    return getTileEntity(pos)?.let {
        if (clazz.java.isAssignableFrom(it::class.java)) {
            return clazz.java.cast(it)
        } else null
    }
}
