package net.insomniakitten.glazed.compat.jei;

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

import mezz.jei.api.*;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.insomniakitten.glazed.Glazed;
import net.insomniakitten.glazed.compat.jei.recipe.KilnJEICategory;
import net.insomniakitten.glazed.compat.jei.recipe.KilnJEIRecipe;
import net.insomniakitten.glazed.kiln.GuiKiln;
import net.insomniakitten.glazed.kiln.RecipesKiln;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

@JEIPlugin
public class JEICompatPlugin implements IModPlugin {

    public static IJeiHelpers helpers;

    @Override
    public void register(@Nonnull IModRegistry registry) {
        helpers = registry.getJeiHelpers();

        registry.addRecipes(
                RecipesKiln.getRecipes(),
                KilnJEICategory.ID);

        registry.handleRecipes(
                RecipesKiln.KilnRecipe.class,
                KilnJEIRecipe::new,
                KilnJEICategory.ID);

        registry.addRecipeCatalyst(
                new ItemStack(Glazed.Objects.BKILN),
                KilnJEICategory.ID);

        registry.addRecipeClickArea(
                GuiKiln.class,
                80, 35, 21, 14,
                KilnJEICategory.ID);
    }

    @Override
    public void registerCategories(@Nonnull IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new KilnJEICategory(
                registry.getJeiHelpers().getGuiHelper()));
    }
}
