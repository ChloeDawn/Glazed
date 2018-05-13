package net.insomniakitten.glazed.block

import net.insomniakitten.glazed.Glazed
import net.insomniakitten.glazed.GlazedVariant
import net.insomniakitten.glazed.extensions.description
import net.insomniakitten.glazed.extensions.doesSideBlockRendering
import net.insomniakitten.glazed.extensions.invoke
import net.insomniakitten.glazed.extensions.plus
import net.insomniakitten.glazed.extensions.variant
import net.minecraft.block.BlockPane
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

class GlassPaneBlock : BlockPane(Material.GLASS, true) {
    override fun getUnlocalizedName() = "tile.${Glazed.ID}.glass_block"

    override fun getCreativeTabToDisplayOn() = Glazed.TAB

    override fun getStateFromMeta(meta: Int) = defaultState + GlazedVariant(meta)

    override fun getMetaFromState(state: IBlockState) = state.variant.ordinal

    override fun createBlockState() = BlockStateContainer(this, NORTH, EAST, WEST, SOUTH, GlazedVariant)

    override fun getSoundType(state: IBlockState, world: World, pos: BlockPos, entity: Entity?) =
            state.variant.soundType

    override fun getBlockHardness(state: IBlockState, world: World, pos: BlockPos) =
            state.variant.hardness

    override fun getExplosionResistance(world: World, pos: BlockPos, entity: Entity?, explosion: Explosion) =
            world.getBlockState(pos).variant.resistance

    override fun getLightValue(state: IBlockState, access: IBlockAccess, pos: BlockPos) =
            state.variant.lightLevel

    override fun canRenderInLayer(state: IBlockState, layer: BlockRenderLayer) =
            state.variant.renderLayer == layer

    override fun canProvidePower(state: IBlockState) = state.variant.redstoneLevel > 0

    override fun getWeakPower(state: IBlockState, access: IBlockAccess, pos: BlockPos, side: EnumFacing) =
            state.variant.redstoneLevel

    override fun canConnectRedstone(state: IBlockState, world: IBlockAccess, pos: BlockPos, side: EnumFacing?) =
            false // Glass panes shouldn't touch the adjacent dust block

    override fun getSubBlocks(tab: CreativeTabs, items: NonNullList<ItemStack>) =
            GlazedVariant.VARIANTS.forEach { items += ItemStack(this, 1, it.ordinal) }

    @SideOnly(Side.CLIENT)
    override fun addInformation(stack: ItemStack, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag) {
        stack.description.run(tooltip::add)
    }

    override fun getPickBlock(state: IBlockState, hit: RayTraceResult, world: World, pos: BlockPos, player: EntityPlayer) =
            ItemStack(this, 1, state.variant.ordinal)

    @SideOnly(Side.CLIENT)
    override fun shouldSideBeRendered(state: IBlockState, access: IBlockAccess, pos: BlockPos, side: EnumFacing) =
            !access.doesSideBlockRendering(pos.offset(side), side.opposite)

    override fun doesSideBlockRendering(state: IBlockState, access: IBlockAccess, pos: BlockPos, side: EnumFacing): Boolean {
        val offset = pos.offset(side)
        val other = access.getBlockState(pos)
        return if (side.axis.isVertical) {
            state(access, pos) == other(access, offset)
        } else other.block == this
    }
}
