package net.insomniakitten.glazed.common.kiln;

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

import net.insomniakitten.glazed.Glazed;
import net.insomniakitten.glazed.common.RegistryManager;
import net.insomniakitten.glazed.common.item.ItemBlockKiln;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.Locale;
import java.util.Random;

@SuppressWarnings("deprecation")
public class BlockKiln extends Block {

    private static final PropertyEnum<KilnHalf> HALF = PropertyEnum.create("half", KilnHalf.class);
    private static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    private static final PropertyBool ACTIVE = PropertyBool.create("active");

    private static final AxisAlignedBB AABB_UPPER = new AxisAlignedBB(
            0.0625D, 0.0000D, 0.0625D, 0.9375D, 0.8125D, 0.9375D
    );

    public BlockKiln() {
        super(Material.ROCK);
        setRegistryName("kiln");
        setUnlocalizedName(Glazed.MOD_ID + ".kiln");
        setCreativeTab(Glazed.CTAB);
        setSoundType(SoundType.STONE);
        setHardness(5.0f);
        setResistance(30.0f);
        setDefaultState(this.getDefaultState()
                .withProperty(HALF, KilnHalf.LOWER)
                .withProperty(FACING, EnumFacing.NORTH)
                .withProperty(ACTIVE, false));
        RegistryManager.registerItemBlock(new ItemBlockKiln(this));
    }

    public static boolean isUpper(IBlockState state) {
        return state.getValue(HALF).equals(KilnHalf.UPPER);
    }

    public static BlockPos getTilePos(IBlockState state, BlockPos pos) {
        return isUpper(state) ? pos.down() : pos;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int half = state.getValue(HALF).ordinal() << 1;
        int facing = state.getValue(FACING).getHorizontalIndex() << 2;
        int active = state.getValue(ACTIVE) ? 1 : 0;
        return half | facing | active;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        KilnHalf half = KilnHalf.values()[((meta & 0b10) >> 1)];
        EnumFacing facing = EnumFacing.getHorizontal(meta >> 2);
        boolean active = (meta & 0b1) != 0;
        return this.getDefaultState()
                .withProperty(HALF, half)
                .withProperty(FACING, facing)
                .withProperty(ACTIVE, active);
    }

    @Override
    public IBlockState getStateForPlacement(
            World world, BlockPos pos, EnumFacing facing,
            float hitX, float hitY, float hitZ, int meta,
            EntityLivingBase placer, EnumHand hand) {
        EnumFacing opposite = placer.getHorizontalFacing().getOpposite();
        return this.getDefaultState().withProperty(FACING, opposite);
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getValue(ACTIVE) ? 8 : 0;
    }

    @Override
    public ItemStack getPickBlock(
            IBlockState state, RayTraceResult target, World world,
            BlockPos pos, EntityPlayer player) {
        return new ItemStack(this);
    }

    @Override
    public boolean onBlockActivated(
            World world, BlockPos pos, IBlockState state, EntityPlayer player,
            EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        BlockPos tilePos = BlockKiln.getTilePos(state, pos);
        FMLNetworkHandler.openGui(player, Glazed.instance, 0, world, tilePos.getX(), tilePos.getY(), tilePos.getZ());
        return true;
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        Block lower = world.getBlockState(pos).getBlock();
        Block upper = world.getBlockState(pos.up()).getBlock();
        boolean canPlaceLower = lower.isReplaceable(world, pos);
        boolean canPlaceUpper = upper.isReplaceable(world, pos.up());
        return canPlaceLower && canPlaceUpper;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
        world.setBlockState(pos.up(), state.withProperty(HALF, KilnHalf.UPPER));
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        BlockPos tilePos = BlockKiln.getTilePos(state, pos);
        Capability<IItemHandler> items = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
        TileEntity tile = world.getTileEntity(tilePos);
        if (tile != null && tile.hasCapability(items, null)) {
            IItemHandler inventory = tile.getCapability(items, null);
            if (inventory != null && world.getGameRules().getBoolean("doTileDrops")) {
                Random rand = new Random();
                for (int i = 0; i < inventory.getSlots(); i++) {
                    ItemStack stack = inventory.getStackInSlot(i);
                    float x = tilePos.getX() + (rand.nextFloat() * 1.0F);
                    float y = tilePos.getY() + (rand.nextFloat() * 1.0F);
                    float z = tilePos.getZ() + (rand.nextFloat() * 1.0F);
                    while (!stack.isEmpty()) {
                        EntityItem drop = new EntityItem(world, x, y, z, stack.splitStack(rand.nextInt(21) + 10));
                        drop.motionX = rand.nextGaussian() * 0.05D;
                        drop.motionY = rand.nextGaussian() * 0.07D;
                        drop.motionZ = rand.nextGaussian() * 0.05D;
                        world.spawnEntity(drop);
                    }
                }
                world.setBlockToAir(isUpper(state) ? pos.down() : pos.up());
            }
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return isUpper(state) ? AABB_UPPER : FULL_BLOCK_AABB;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileKiln tile = (TileKiln) world.getTileEntity(BlockKiln.getTilePos(state, pos));
        return tile != null ? state.withProperty(ACTIVE, tile.isActive) : state;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return !isUpper(state);
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return !isUpper(state) ? new TileKiln() : null;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, HALF, FACING, ACTIVE);
    }

    private enum KilnHalf implements IStringSerializable {
        UPPER, LOWER;

        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }

}
