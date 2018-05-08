package net.insomniakitten.glazed.block.entity;

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

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;

import static net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;

public final class GlassKilnEntity extends TileEntity implements ITickable {
    private static final String NBT_KEY_ACTIVE = "active";
    private static final String NBT_KEY_ITEMS = "items";

    private final ItemStackHandler items = new ItemStackHandler(4) {
        @Override
        protected int getStackLimit(int slot, ItemStack stack) {
            final int limit = super.getStackLimit(slot, stack);
            switch (slot) {
                case 0: return isSand(stack) ? limit : 0;
                case 1: return limit;
                case 2: return isFuel(stack) ? limit : 0;
                default: return 0;
            }
        }

        private boolean isSand(ItemStack stack) {
            final int sand = OreDictionary.getOreID("sand");
            for (int id : OreDictionary.getOreIDs(stack)) {
                if (sand == id) return true;
            }
            return false;
        }

        private boolean isFuel(ItemStack stack) {
            return TileEntityFurnace.getItemBurnTime(stack) > 0;
        }
    };

    private boolean active = false;

    public boolean isActive() {
        return active;
    }

    public void dropItems(World world, BlockPos pos) {
        final int size = items.getSlots();
        for (int i = 0; i < size; i++) {
            final ItemStack stack = items.getStackInSlot(i);
            Block.spawnAsEntity(world, pos, stack);
        }
    }

    public int getComparatorOutput() {
        return ItemHandlerHelper.calcRedstoneFromInventory(items);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        active = nbt.getBoolean(NBT_KEY_ACTIVE);
        items.deserializeNBT(nbt.getCompoundTag(NBT_KEY_ITEMS));
        super.readFromNBT(nbt);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setBoolean(NBT_KEY_ACTIVE, active);
        nbt.setTag(NBT_KEY_ITEMS, items.serializeNBT());
        return super.writeToNBT(nbt);
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(getPos(), -1, getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        final NBTTagCompound nbt = new NBTTagCompound();
        nbt.setBoolean(NBT_KEY_ACTIVE, active);
        return nbt;
    }

    @Override
    public ITextComponent getDisplayName() {
        final String key = getBlockType().getUnlocalizedName();
        return new TextComponentTranslation(key + ".name");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        final NBTTagCompound nbt = pkt.getNbtCompound();
        active = nbt.getBoolean(NBT_KEY_ACTIVE);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side) {
        return capability == ITEM_HANDLER_CAPABILITY;
    }

    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
        return hasCapability(capability, side) ? ITEM_HANDLER_CAPABILITY.cast(items) : null;
    }

    @Override
    public void update() {

    }
}
