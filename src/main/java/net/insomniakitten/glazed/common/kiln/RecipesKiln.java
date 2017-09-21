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

import com.google.common.collect.ImmutableList;
import net.insomniakitten.glazed.common.kiln.TileKiln.Slots;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class RecipesKiln {

    private static final Set<KilnRecipe> KILN_RECIPES = new HashSet<>();

    /**
     * Registers a new Kiln recipe.
     *
     * @param input    The input ingredient. This cannot be empty.
     * @param catalyst The catalyst ingredient. This can be empty.
     * @param output   The output ingredient. This cannot be empty.
     * @return Whether the registration was successful.
     */
    public static boolean addKilnRecipe(ItemStack input, ItemStack catalyst, ItemStack output) {
        return KILN_RECIPES.add(new KilnRecipe(input, catalyst, output));
    }

    @Nullable
    public static KilnRecipe getRecipe(ItemStack input, ItemStack catalyst) {
        return KILN_RECIPES.stream().filter(recipe -> recipe.canSmelt(input, catalyst)).findFirst().orElse(null);
    }

    @Nonnull
    public static ItemStack getOutput(ItemStack input, ItemStack catalyst) {
        return KILN_RECIPES.stream().filter(recipe -> recipe.canSmelt(input, catalyst)).findFirst()
                .map(KilnRecipe::getOutput).orElse(ItemStack.EMPTY);
    }

    public static boolean trySmelt(TileKiln tile, ItemStack input, ItemStack catalyst) {
        KilnRecipe recipe = getRecipe(input, catalyst);
        ItemStack output = Slots.getSlot(tile, Slots.OUTPUT);
        if (recipe != null && (output.isEmpty() || output.isItemEqual(recipe.getOutput()))) {
            if (output.getCount() < output.getMaxStackSize()) {
                input.shrink(recipe.getInput().getCount());
                catalyst.shrink(recipe.getCatalyst().getCount());
                if (output.isEmpty()) {
                    ItemStack stack = recipe.getOutput().copy();
                    Slots.setSlot(tile, Slots.OUTPUT, stack);
                } else {
                    output.grow(recipe.getOutput().getCount());
                }
                return true;
            }
        }
        return false;
    }

    public static ImmutableList<KilnRecipe> getRecipes() {
        return ImmutableList.copyOf(KILN_RECIPES);
    }

    public static class KilnRecipe {

        private final ItemStack input, catalyst, output;

        public KilnRecipe(ItemStack input, ItemStack catalyst, ItemStack output) {
            if (!input.isEmpty() && !output.isEmpty()) {
                this.input = input;
                this.catalyst = catalyst;
                this.output = output;
            } else throw new IllegalArgumentException("Kiln recipe cannot have an empty ingredient!");
        }

        public ItemStack getOutput() {
            return output;
        }

        public ItemStack getInput() {
            return input;
        }

        public ItemStack getCatalyst() {
            return catalyst;
        }

        public boolean canSmelt(ItemStack input, ItemStack catalyst) {
            boolean inputOverflow = this.input.getCount() > input.getCount();
            boolean catalystOverflow = this.catalyst.getCount() > catalyst.getCount();
            boolean validInput = this.input.isItemEqual(input);
            boolean validCatalyst = this.catalyst.isItemEqual(catalyst);
            return !(inputOverflow || catalystOverflow) && validInput && validCatalyst;
        }

    }

}
