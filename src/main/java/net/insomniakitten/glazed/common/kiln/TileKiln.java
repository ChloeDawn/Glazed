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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Locale;

public class TileKiln extends TileEntity implements ITickable {

    public static final Capability<IItemHandler> CAPABILITY = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    private static final ResourceLocation KEY = new ResourceLocation(Glazed.MOD_ID, "tile_kiln");

    private final int processTime = 160;
    public boolean isActive = false;
    private int progress = 0;

    private ItemStackHandler inventory = new ItemStackHandler(4) {

        @Override
        protected int getStackLimit(int index, ItemStack stack) {
            return (index == 0 && isSand(stack)) || (index == 1) || (index == 2 && isFuel(stack)) ?
                    super.getStackLimit(index, stack) : 0;
        }

        private boolean isSand(ItemStack stack) {
            return Arrays.stream(OreDictionary.getOreIDs(stack))
                    .anyMatch(id -> id == OreDictionary.getOreID("sand"));
        }

        private boolean isFuel(ItemStack stack) {
            return TileEntityFurnace.getItemBurnTime(stack) > 0;
        }

    };

    public static String getKey() {
        return KEY.toString();
    }

    public int getProgress() {
        return progress;
    }

    @Override
    @Nonnull
    public ITextComponent getDisplayName() {
        String name = getBlockType().getUnlocalizedName() + ".name";
        return new TextComponentTranslation(name);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability.equals(CAPABILITY) || super.hasCapability(capability, facing);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return capability.equals(CAPABILITY) ? (T) this.inventory : super.getCapability(capability, facing);
    }

    @Override
    public final void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.inventory.deserializeNBT(nbt.getCompoundTag("contents"));
        this.progress = nbt.hasKey("progress") ? nbt.getInteger("progress") : 0;
    }

    @Override
    @Nonnull
    public final NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setTag("contents", inventory.serializeNBT());
        nbt.setInteger("progress", progress);
        return nbt;
    }

    @Override
    public void update() {
        ItemStack input = Slots.getSlot(this, Slots.INPUT);
        ItemStack catalyst = Slots.getSlot(this, Slots.CATALYST);
        ItemStack fuel = Slots.getSlot(this, Slots.FUEL);
        ItemStack output = Slots.getSlot(this, Slots.OUTPUT);

        int remainingFuelTime = TileEntityFurnace.getItemBurnTime(fuel);
        boolean canBurn = remainingFuelTime > 0;

        if (!input.isEmpty() && RecipesKiln.getRecipe(input, catalyst) != null && (!fuel.isEmpty() || canBurn)) {
            isActive = true;
        }

        if (isActive) {
            ++progress;
            if (canBurn && progress == processTime && RecipesKiln.trySmelt(this, input, catalyst)) {
                progress = 0;
            }
            if (remainingFuelTime > 0) {
                --remainingFuelTime;
            }
        }

        if (!RecipesKiln.getOutput(input, catalyst).isItemEqual(output) || output.getCount() == output.getMaxStackSize()) {
            isActive = false;
        }

        markDirty();
    }

    public enum Slots {
        INPUT(34, 17), CATALYST(56, 17), FUEL(45, 53), OUTPUT(116, 35);

        private final int x, y;

        Slots(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public static ItemStack getSlot(TileKiln kiln, Slots slot) {
            return kiln.inventory.getStackInSlot(slot.ordinal());
        }

        public static void setSlot(TileKiln kiln, Slots slot, ItemStack stack) {
            kiln.inventory.setStackInSlot(slot.ordinal(), stack);
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }

    }

}
