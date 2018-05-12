package net.insomniakitten.glazed.block

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

import net.insomniakitten.glazed.Glazed
import net.insomniakitten.glazed.GlazedProxy
import net.insomniakitten.glazed.block.entity.GlassKilnEntity
import net.insomniakitten.glazed.extensions.AccessPosition
import net.insomniakitten.glazed.extensions.BlockAccess
import net.insomniakitten.glazed.extensions.comparatorOutput
import net.insomniakitten.glazed.extensions.cycle
import net.insomniakitten.glazed.extensions.get
import net.insomniakitten.glazed.extensions.invoke
import net.insomniakitten.glazed.extensions.mirroredFacing
import net.insomniakitten.glazed.extensions.plus
import net.minecraft.block.BlockHorizontal
import net.minecraft.block.SoundType
import net.minecraft.block.material.MapColor
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.properties.PropertyEnum.create
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.IStringSerializable
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.util.Locale

class GlassKilnBlock : BlockHorizontal(Material.ROCK, MapColor.ADOBE) {
    init {
        defaultState += ACTIVE(false)
        unlocalizedName = "${Glazed.ID}.glass_kiln"
        setCreativeTab(Glazed.TAB)
        soundType = SoundType.STONE
        setHardness(5.0F)
        setResistance(30.0F)
    }

    private val AccessPosition.kiln get() = entity as? GlassKilnEntity
    private val AccessPosition.isActive get() = kiln?.isActive == true

    override fun isFullCube(state: IBlockState) = false

    override fun isOpaqueCube(state: IBlockState) = false

    override fun getStateFromMeta(meta: Int): IBlockState {
        val active = meta and 1 != 0
        val half = Half[meta and 2 shr 1]
        val facing = EnumFacing.getHorizontal(meta shr 2)
        return defaultState + ACTIVE(active) + HALF(half) + FACING(facing)
    }

    override fun getMetaFromState(state: IBlockState): Int {
        val active = if (state[ACTIVE]) 1 else 0
        val half = state[HALF].ordinal shl 1
        val facing = state[FACING].horizontalIndex shl 2
        return active or half or facing
    }

    override fun getActualState(
            state: IBlockState,
            access: BlockAccess,
            pos: BlockPos
    ) = state + ACTIVE(access[pos].isActive)

    override fun getPickBlock(
            state: IBlockState,
            target: RayTraceResult,
            world: World,
            pos: BlockPos,
            player: EntityPlayer
    ): ItemStack = super.getPickBlock(state, target, world, pos, player).also {
        if (world.isRemote && player.capabilities.isCreativeMode &&
            state[HALF].isUpper && GlazedProxy.instance.isCtrlKeyDown) {
            world[pos.down()].kiln?.serializeToStack(it)
        }
    }

    override fun getStateForPlacement(
            world: World,
            pos: BlockPos,
            side: EnumFacing,
            hitX: Float,
            hitY: Float,
            hitZ: Float,
            meta: Int,
            placer: EntityLivingBase,
            hand: EnumHand
    ) = defaultState + HALF(Half[world, pos]) + FACING(placer.mirroredFacing)

    @SideOnly(Side.CLIENT)
    override fun getSelectedBoundingBox(
            state: IBlockState,
            world: World,
            pos: BlockPos
    ): AxisAlignedBB = state[HALF].boundingBox.offset(pos)

    override fun breakBlock(
            world: World,
            pos: BlockPos,
            state: IBlockState
    ) {
        if (hasTileEntity(state)) {
            world[pos].kiln?.dropItems(world, pos)
            world[pos].entity = null
            world.destroyBlock(pos.up(), false)
        } else world.destroyBlock(pos.down(), false)
    }

    override fun canPlaceBlockAt(world: World, pos: BlockPos) =
            world[pos].isReplaceable && (world[pos.down()].isReplaceable || world[pos.up()].isReplaceable)

    override fun onBlockActivated(
            world: World,
            pos: BlockPos,
            state: IBlockState,
            player: EntityPlayer,
            hand: EnumHand,
            side: EnumFacing,
            hitX: Float,
            hitY: Float,
            hitZ: Float
    ): Boolean {
        state[HALF].offsetToEntity(pos).apply {
            player.openGui(Glazed, 0, world, x, y, z)
        }
        return true
    }

    override fun onBlockPlacedBy(
            world: World,
            pos: BlockPos,
            state: IBlockState,
            entity: EntityLivingBase?,
            stack: ItemStack
    ) {
        world[state[HALF](pos)].state = state cycle HALF
    }

    override fun hasComparatorInputOverride(state: IBlockState) = true

    override fun getComparatorInputOverride(
            state: IBlockState,
            world: World,
            pos: BlockPos
    ) = world[state[HALF].offsetToEntity(pos)].kiln?.items?.comparatorOutput ?: 0

    override fun createBlockState() = BlockStateContainer(this, ACTIVE, FACING, HALF)

    override fun getLightValue(state: IBlockState, access: BlockAccess, pos: BlockPos) =
            if (state[ACTIVE]) 8 else 0

    override fun doesSideBlockRendering(
            state: IBlockState,
            access: BlockAccess,
            pos: BlockPos?,
            side: EnumFacing
    ) = side != EnumFacing.DOWN && state[HALF].isLower && side != state[FACING]

    override fun hasTileEntity(state: IBlockState) = state[HALF].isLower

    override fun createTileEntity(world: World, state: IBlockState) = GlassKilnEntity()

    enum class Half constructor(val boundingBox: AxisAlignedBB) : IStringSerializable {
        LOWER(AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 2.0, 1.0)),
        UPPER(AxisAlignedBB(0.0, -1.0, 0.0, 1.0, 1.0, 1.0));

        val isUpper get() = this == UPPER
        val isLower get() = this == LOWER

        operator fun invoke(pos: BlockPos): BlockPos = when (this) {
            Half.LOWER -> pos.up()
            Half.UPPER -> pos.down()
        }

        fun offsetToEntity(pos: BlockPos): BlockPos = when (this) {
            Half.LOWER -> pos
            Half.UPPER -> pos.down()
        }

        override fun getName() = name.toLowerCase(Locale.ROOT)

        companion object {
            private val VALUES = values()

            operator fun get(ordinal: Int) = VALUES[ordinal]

            operator fun get(access: BlockAccess, pos: BlockPos) = when {
                access[pos.up()].isReplaceable -> LOWER
                access[pos.down()].isReplaceable -> UPPER
                else -> throw IllegalArgumentException("Cannot determine Half from position $pos")
            }
        }
    }

    companion object {
        val ACTIVE: PropertyBool = PropertyBool.create("active")
        val HALF: PropertyEnum<Half> = create("half", Half::class.java)
    }
}

