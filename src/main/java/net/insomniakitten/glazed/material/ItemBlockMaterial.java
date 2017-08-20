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

import net.insomniakitten.glazed.client.model.ModelRegistry;
import net.insomniakitten.glazed.client.model.WrappedModel.ModelBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("deprecation")
public class ItemBlockMaterial extends ItemBlock {

    public ItemBlockMaterial(Block block) {
        super(block);
        assert block.getRegistryName() != null;
        setRegistryName(block.getRegistryName());
        setHasSubtypes(true);
        registerModels();
    }

    private void registerModels() {
        for (MaterialType type : MaterialType.values()) {
            ModelBuilder builder = new ModelBuilder(this, type.getMetadata());
            builder.addVariant("type=" + type.getName());
            ModelRegistry.registerModel(builder.build());
        }
    }

    @Override @SideOnly(Side.CLIENT)
    public String getUnlocalizedName(ItemStack stack) {
        int meta = stack.getMetadata() % MaterialType.values().length;
        String type = MaterialType.values()[meta].getName();
        return this.getBlock().getUnlocalizedName() + "." + type;
    }

    @Override
    public boolean placeBlockAt(
            ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side,
            float hitX, float hitY, float hitZ, IBlockState state) {
        IBlockState stateAt = world.getBlockState(pos.down());
        boolean isSolid = stateAt.getBlock().isTopSolid(stateAt);
        boolean isCrystal = MaterialType.get(stack.getMetadata()).equals(MaterialType.CRYSTAL);
        return (!isCrystal || isSolid) && super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, state);
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

}
