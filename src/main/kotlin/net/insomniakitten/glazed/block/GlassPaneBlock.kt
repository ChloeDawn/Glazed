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
import net.insomniakitten.glazed.GlazedVariant
import net.insomniakitten.glazed.GlazedVariant.VARIANTS
import net.insomniakitten.glazed.extensions.BlockAccess
import net.insomniakitten.glazed.extensions.BlockState
import net.insomniakitten.glazed.extensions.description
import net.insomniakitten.glazed.extensions.get
import net.insomniakitten.glazed.extensions.invoke
import net.insomniakitten.glazed.extensions.plus
import net.insomniakitten.glazed.extensions.variant
import net.minecraft.block.BlockPane
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.NonNullList
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.Explosion
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

class GlassPaneBlock : BlockPane(Material.GLASS, true) {
    init {
        unlocalizedName = "${Glazed.ID}.glass_pane"
        soundType = SoundType.GLASS
        setCreativeTab(Glazed.TAB)
    }

    override fun getStateFromMeta(meta: Int) = defaultState + GlazedVariant(meta)

    override fun getMetaFromState(state: BlockState) = state.variant.ordinal

    override fun createBlockState() = BlockStateContainer(
            this, NORTH, EAST, WEST, SOUTH, GlazedVariant
    )

    override fun canProvidePower(state: BlockState) = state.variant.redstoneLevel > 0

    override fun getBlockHardness(
            state: BlockState,
            world: World,
            pos: BlockPos
    ) = state.variant.hardness

    override fun getWeakPower(
            state: BlockState,
            access: BlockAccess,
            pos: BlockPos,
            side: EnumFacing
    ) = state.variant.redstoneLevel

    override fun getSubBlocks(
            tab: CreativeTabs,
            items: NonNullList<ItemStack>
    ) = VARIANTS.forEach {
        items += ItemStack(this, 1, it.ordinal)
    }

    override fun getPickBlock(
            state: BlockState,
            target: RayTraceResult?,
            world: World,
            pos: BlockPos,
            player: EntityPlayer
    ) = ItemStack(this, 1, state.variant.ordinal)

    override fun canRenderInLayer(
            state: BlockState,
            layer: BlockRenderLayer
    ) = state.variant.renderLayer == layer

    override fun getSoundType(
            state: BlockState,
            world: World,
            pos: BlockPos,
            entity: Entity?
    ) = state.variant.soundType

    @SideOnly(Side.CLIENT)
    override fun addInformation(
            stack: ItemStack,
            world: World?,
            tooltip: MutableList<String>,
            flag: ITooltipFlag
    ) {
        stack.description.run(tooltip::add)
    }

    override fun getLightValue(
            state: BlockState,
            access: BlockAccess?,
            pos: BlockPos?
    ) = state.variant.lightLevel

    override fun doesSideBlockRendering(state: BlockState, access: BlockAccess, pos: BlockPos, side: EnumFacing): Boolean {
        val offset = pos.offset(side)
        val other = access[offset].state
        return if (side.axis.isVertical) {
            state(access, pos) == other(access, offset)
        } else other.block == this
    }

    override fun getExplosionResistance(
            world: World,
            pos: BlockPos,
            exploder: Entity?,
            explosion: Explosion
    ) = world[pos].state.variant.resistance

    override fun canConnectRedstone(
            state: BlockState,
            world: BlockAccess,
            pos: BlockPos,
            side: EnumFacing?
    ) = false // Glass panes shouldn't touch the adjacent dust block

    @SideOnly(Side.CLIENT)
    override fun shouldSideBeRendered(
            state: BlockState,
            access: BlockAccess,
            pos: BlockPos,
            side: EnumFacing
    ) = !access[pos.offset(side)].doesSideBlockRendering(side.opposite)
}

