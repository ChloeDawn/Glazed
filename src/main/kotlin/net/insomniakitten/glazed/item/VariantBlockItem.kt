package net.insomniakitten.glazed.item

import net.insomniakitten.glazed.GlazedVariant
import net.insomniakitten.glazed.extensions.key
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

class VariantBlockItem(block: Block) : ItemBlock(block) {
    override fun getHasSubtypes() = true

    override fun getUnlocalizedName(stack: ItemStack) = "${block.unlocalizedName}.${stack.key}"

    override fun getMetadata(damage: Int) = damage

    @SideOnly(Side.CLIENT)
    override fun canPlaceBlockOnSide(world: World, pos: BlockPos, side: EnumFacing, player: EntityPlayer, stack: ItemStack) =
            stack in GlazedVariant && super.canPlaceBlockOnSide(world, pos, side, player, stack)

    override fun placeBlockAt(stack: ItemStack, player: EntityPlayer, world: World, pos: BlockPos, side: EnumFacing, hitX: Float, hitY: Float, hitZ: Float, newState: IBlockState) =
            stack in GlazedVariant && super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState)
}
