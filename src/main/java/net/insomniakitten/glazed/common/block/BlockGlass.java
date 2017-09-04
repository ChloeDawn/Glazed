package net.insomniakitten.glazed.common.block;

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

import net.insomniakitten.glazed.client.ClientHelper;
import net.insomniakitten.glazed.common.RegistryManager;
import net.insomniakitten.glazed.client.SpecialsManager;
import net.insomniakitten.glazed.common.item.ItemBlockBase;
import net.insomniakitten.glazed.common.type.GlassType;
import net.insomniakitten.glazed.common.util.IOverlayProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.UUID;

public class BlockGlass extends BlockBase<GlassType> implements IOverlayProvider {

    public BlockGlass() {
        super("glass", GlassType.class);
    }

    @Override
    public void registerItemBlock() {
        RegistryManager.registerItemBlock(new ItemBlockBase<GlassType>(this) {
            @Override
            @SideOnly(Side.CLIENT)
            public String getItemStackDisplayName(ItemStack stack) {
                String type = getType(stack.getMetadata()).getName();
                Pair<UUID, String> match = Pair.of(ClientHelper.getPlayerUUID(), type);
                return SpecialsManager.SPECIALS.keySet().contains(match) ?
                        SpecialsManager.SPECIALS.get(match)
                        : super.getItemStackDisplayName(stack);
            }
        });
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    @Deprecated
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {
        return getType(state).isOpaque() ? 255 : 0;
    }

    @Override
    public void onEntityWalk(World world, BlockPos pos, Entity entity) {
        if (!entity.isSneaking()) {
            getType(world.getBlockState(pos)).onCollidedWithBlock(entity, world, pos);
        }
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
        getType(state).onCollidedWithBlock(entity, world, pos);
    }

    @Override
    @Deprecated
    public void addCollisionBoxToList(
            IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox,
            List<AxisAlignedBB> collidingBoxes, Entity entity, boolean flag) {
        if (getType(state).canCollideWithBlock(entity, world, pos)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, state.getCollisionBoundingBox(world, pos));
        }
    }

    @Override
    @Deprecated
    public int getStrongPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return getType(state).getRedstonePower();
    }

    @Override
    @Deprecated
    public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return getType(state).getRedstonePower();
    }

    @Override
    public ResourceLocation getOverlayTexture(IBlockState state) {
        return getType(state).getOverlayTexture(state);
    }

}
