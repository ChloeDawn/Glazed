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
import net.insomniakitten.glazed.extensions.description
import net.insomniakitten.glazed.extensions.get
import net.insomniakitten.glazed.extensions.plus
import net.insomniakitten.glazed.extensions.variant
import net.minecraft.block.BlockGlass
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
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
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

class GlassBlock : BlockGlass(Material.GLASS, true) {
    init {
        unlocalizedName = Glazed.ID + ".glass_block"
        soundType = SoundType.GLASS
        setCreativeTab(Glazed.TAB)
    }

    override fun getStateFromMeta(meta: Int) = defaultState + GlazedVariant(meta)

    override fun getMetaFromState(state: IBlockState) = state.variant.ordinal

    override fun createBlockState() = BlockStateContainer(this, GlazedVariant)

    override fun canProvidePower(state: IBlockState) = state.variant.redstoneLevel > 0

    override fun getBlockHardness(
            state: IBlockState,
            world: World,
            pos: BlockPos
    ) = state.variant.hardness

    override fun getWeakPower(
            state: IBlockState,
            access: IBlockAccess,
            pos: BlockPos,
            side: EnumFacing
    ) = state.variant.redstoneLevel

    override fun getLightValue(
            state: IBlockState,
            access: IBlockAccess,
            pos: BlockPos
    ) = state.variant.lightLevel

    override fun getSubBlocks(
            tab: CreativeTabs,
            items: NonNullList<ItemStack>
    ) = VARIANTS.forEach {
        items += ItemStack(this, 1, it.ordinal)
    }

    @SideOnly(Side.CLIENT)
    override fun addInformation(
            stack: ItemStack,
            world: World?,
            tooltip: MutableList<String>,
            flag: ITooltipFlag
    ) {
        stack.description.run(tooltip::add)
    }

    override fun doesSideBlockRendering(
            state: IBlockState,
            world: IBlockAccess,
            pos: BlockPos,
            side: EnumFacing
    ) = world[pos.offset(side)].state.let {
        it.block == this && it.variant == state.variant
    }

    override fun getExplosionResistance(
            world: World,
            pos: BlockPos,
            exploder: Entity?,
            explosion: Explosion
    ) = world[pos].state.variant.resistance

    override fun getPickBlock(
            state: IBlockState,
            target: RayTraceResult,
            world: World,
            pos: BlockPos,
            player: EntityPlayer
    ) = ItemStack(this, 1, state.variant.ordinal)

    override fun canRenderInLayer(
            state: IBlockState,
            layer: BlockRenderLayer
    ) = state.variant.renderLayer == layer

    override fun getSoundType(
            state: IBlockState,
            world: World,
            pos: BlockPos,
            entity: Entity?
    ) = state.variant.soundType

    @SideOnly(Side.CLIENT)
    override fun shouldSideBeRendered(
            state: IBlockState,
            access: IBlockAccess,
            pos: BlockPos,
            side: EnumFacing
    ) = pos.offset(side).let { !access[it].state.doesSideBlockRendering(access, it, side.opposite) }
}
