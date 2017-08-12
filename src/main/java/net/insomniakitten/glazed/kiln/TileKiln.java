package net.insomniakitten.glazed.kiln;

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

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import java.util.Locale;

public class TileKiln extends TileEntity implements ITickable {


    public boolean isActive = false;
    private int progress = 0;

    public static final Capability<IItemHandler> CAPABILITY =
            CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;

    private ItemStackHandler ITEMS = new ItemStackHandler(4) {

        @Override
        protected int getStackLimit(int index, ItemStack stack) {
            switch (index) {
                case 0:
                    if (!isSand(stack)) {
                        return 0;
                    }

                case 2:
                    if (TileEntityFurnace.getItemBurnTime(stack) <= 0) {
                        return 0;
                    }

                case 3: return 0;
            }
            return super.getStackLimit(index, stack);
        }

        private boolean isSand(ItemStack stack) {
            int[] ids = OreDictionary.getOreIDs(stack);
            int sand = OreDictionary.getOreID("sand");
            for (int id : ids) {
                if (id == sand) {
                    return true;
                }
            } return false;
        }

    };

    public int getProgress() { return progress; }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability.equals(CAPABILITY) || super.hasCapability(capability, facing);
    }

    @Override @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability.equals(CAPABILITY)) {
            return (T) this.ITEMS;
        } else return super.getCapability(capability, facing);
    }

    @Override
    public final void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.ITEMS.deserializeNBT(nbt.getCompoundTag("contents"));
        this.progress = nbt.hasKey("progress") ?
                nbt.getInteger("progress") : 0;
    }

    @Override @Nonnull
    public final NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setTag("contents", ITEMS.serializeNBT());
        nbt.setInteger("progress", progress);
        return nbt;
    }

    @Override
    public void update() {
        if (!Slots.getSlot(this, Slots.INPUT).isEmpty()) {

            ItemStack   input = Slots.getSlot(this, Slots.INPUT),
                        catalyst = Slots.getSlot(this, Slots.CATALYST),
                        fuel = Slots.getSlot(this, Slots.FUEL),
                        output = Slots.getSlot(this, Slots.OUTPUT);

            int burnTime = ForgeEventFactory.getItemBurnTime(fuel);
            int remainingFuelTime = 0;

            if (RecipesKiln.getRecipe(input, catalyst) != null && (!fuel.isEmpty() || remainingFuelTime > 0)) {
                isActive = RecipesKiln.trySmelt(this, input, catalyst);
            }

            if (isActive) {

            }

            if (!RecipesKiln.getOutput(input, catalyst).isItemEqual(output)
                    || output.getCount() == output.getMaxStackSize())
                isActive = false;

            this.markDirty();
        }
    }

    public enum Slots {

        INPUT(34, 17), CATALYST(56, 17),
        FUEL(45, 53), OUTPUT(116, 35);

        private final int x, y;

        Slots(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() { return x; }
        public int getY() { return y; }

        public String getName() { return name().toLowerCase(Locale.ENGLISH); }

        public static ItemStack getSlot(TileKiln tile, Slots slot){
            return tile.ITEMS.getStackInSlot(slot.ordinal());
        }

        public static void setSlot(TileKiln tile, Slots slot, ItemStack stack) {
            tile.ITEMS.setStackInSlot(slot.ordinal(), stack);
        }

    }

}
