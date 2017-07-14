package net.insomniakitten.glazed.compat.jei.recipe;

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

import com.google.common.collect.Lists;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.insomniakitten.glazed.compat.jei.JEICompatPlugin;
import net.insomniakitten.glazed.kiln.RecipeHandlerKiln;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public class KilnJEIRecipe implements IRecipeWrapper {

    private final RecipeHandlerKiln.KilnRecipe recipe;

    public KilnJEIRecipe(RecipeHandlerKiln.KilnRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        List<List<ItemStack>> inputs = JEICompatPlugin.helpers.getStackHelper().expandRecipeItemStackInputs(Lists.newArrayList(recipe.getInput(), recipe.getCatalyst()));
        ingredients.setInputLists(ItemStack.class, inputs);
        ingredients.setOutput(ItemStack.class, recipe.getOutput());
    }
}
