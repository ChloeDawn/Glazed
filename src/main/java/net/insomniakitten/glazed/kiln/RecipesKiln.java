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

import com.google.common.collect.ImmutableList;
import net.insomniakitten.glazed.kiln.TileKiln.Slots;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class RecipesKiln {

    private static final List<KilnRecipe> KILN_RECIPES = new ArrayList<KilnRecipe>();

    /**
     * Registers a new Kiln recipe.
     *
     * @param input    The input ingredient. This cannot be empty.
     * @param catalyst The catalyst ingredient. This can be empty.
     * @param output   The output ingredient. This cannot be empty.
     * @return Whether the registration was successful.
     */
    public static boolean addKilnRecipe(
            @Nonnull ItemStack input,
            @Nonnull ItemStack catalyst,
            @Nonnull ItemStack output) {
        return KILN_RECIPES.add(
                new KilnRecipe(input, catalyst, output));
    }

    @Nullable
    public static KilnRecipe getRecipe(
            @Nonnull ItemStack input,
            @Nonnull ItemStack catalyst) {
        for (KilnRecipe recipe : KILN_RECIPES) {
            if (recipe.canSmelt(input, catalyst))
                return recipe;
        }
        return null;
    }

    @Nonnull
    public static ItemStack getOutput(
            @Nonnull ItemStack input,
            @Nonnull ItemStack catalyst) {
        for (KilnRecipe recipe : KILN_RECIPES) {
            if (recipe.canSmelt(input, catalyst))
                return recipe.getOutput();
        }
        return ItemStack.EMPTY;
    }

    public static boolean trySmelt(
            @Nonnull TileKiln tile,
            @Nonnull ItemStack input,
            @Nonnull ItemStack catalyst) {
        KilnRecipe recipe = getRecipe(input, catalyst);
        if (recipe == null) return false;
        ItemStack slot = Slots.getSlot(tile, Slots.OUTPUT);

        if (!slot.isEmpty() && !slot.isItemEqual(recipe.getOutput()))
            return false;

        if (slot.getCount() < slot.getMaxStackSize()) {
            input.shrink(recipe.getInput().getCount());
            catalyst.shrink(recipe.getCatalyst().getCount());

            if (slot.isEmpty()) {
                Slots.setSlot(tile, Slots.OUTPUT, recipe.getOutput().copy());
            } else slot.grow(recipe.getOutput().getCount());

        } else return false;

        return true;
    }

    public static ImmutableList<KilnRecipe> getRecipes() {
        return ImmutableList.copyOf(KILN_RECIPES);
    }

    public static class KilnRecipe {

        private final ItemStack input;
        private final ItemStack catalyst;
        private final ItemStack output;

        public KilnRecipe(
                @Nonnull ItemStack input,
                @Nonnull ItemStack catalyst,
                @Nonnull ItemStack output) {
            if (input.isEmpty() || output.isEmpty()) {
                throw new IllegalArgumentException(
                        "Kiln recipe cannot have an empty ingredient!");
            }

            this.input = input;
            this.catalyst = catalyst;
            this.output = output;
        }

        public ItemStack getOutput() { return output; }
        public ItemStack getInput() { return input; }
        public ItemStack getCatalyst() { return catalyst; }

        public boolean canSmelt(
                @Nonnull ItemStack input,
                @Nonnull ItemStack catalyst) {
            return !(this.input.getCount() > input.getCount()
                    || this.catalyst.getCount() > catalyst.getCount())
                    && this.input.isItemEqual(input)
                    && this.catalyst.isItemEqual(catalyst);
        }

    }

}
