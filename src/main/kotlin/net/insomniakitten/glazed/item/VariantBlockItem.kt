package net.insomniakitten.glazed.item

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
    init {
        setHasSubtypes(true)
    }

    override fun getUnlocalizedName(stack: ItemStack) = "${block.unlocalizedName}.${stack.key}"

    override fun getMetadata(damage: Int) = damage

    @SideOnly(Side.CLIENT)
    override fun canPlaceBlockOnSide(
            world: World,
            pos: BlockPos,
            side: EnumFacing,
            player: EntityPlayer,
            stack: ItemStack
    ) = stack in GlazedVariant && super.canPlaceBlockOnSide(world, pos, side, player, stack)

    override fun placeBlockAt(
            stack: ItemStack,
            player: EntityPlayer,
            world: World,
            pos: BlockPos,
            side: EnumFacing,
            hitX: Float,
            hitY: Float,
            hitZ: Float,
            newState: IBlockState
    ) = stack in GlazedVariant && super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState)
}
