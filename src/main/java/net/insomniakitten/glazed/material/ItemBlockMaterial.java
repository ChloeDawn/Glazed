package net.insomniakitten.glazed.material;

/*
 *  Copyright 2017 InsomniaKitten
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

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class ItemBlockMaterial extends ItemBlock {

    public ItemBlockMaterial(Block block) {
        super(block);
        assert block.getRegistryName() != null;
        setRegistryName(block.getRegistryName());
        setHasSubtypes(true);
    }

    @Override
    @Nonnull
    @SideOnly(Side.CLIENT)
    public String getUnlocalizedName(ItemStack stack) {
        int meta = stack.getMetadata() % MaterialType.values().length;
        String type = MaterialType.values()[meta].getName();
        return this.getBlock().getUnlocalizedName() + "." + type;
    }

    @Override
    public boolean placeBlockAt(
            @Nonnull ItemStack stack,
            @Nonnull EntityPlayer player,
            World world,
            @Nonnull BlockPos pos,
            EnumFacing side,
            float hitX, float hitY, float hitZ,
            @Nonnull IBlockState newState) {
        if (!MaterialType.getType(stack.getMetadata())
                .equals(MaterialType.CRYSTAL))
            return super.placeBlockAt(
                    stack, player, world, pos, side,
                    hitX, hitY, hitZ, newState);
        else {
            boolean isEndStone = world.getBlockState(pos.down())
                    .getBlock().equals(Blocks.END_STONE);
            return isEndStone
                    && super.placeBlockAt(
                    stack, player, world, pos, side,
                    hitX, hitY, hitZ, newState);
        }
    }

    @Override public int getMetadata(int damage) { return damage; }

}