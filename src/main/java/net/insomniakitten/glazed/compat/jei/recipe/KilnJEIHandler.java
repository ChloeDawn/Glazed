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

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class KilnJEIHandler implements IRecipeHandler<KilnJEIRecipe> {

    @Override
    public Class<KilnJEIRecipe> getRecipeClass() {
        return null;
    }

    @Override
    public String getRecipeCategoryUid(KilnJEIRecipe recipe) {
        return null;
    }

    @Override
    public IRecipeWrapper getRecipeWrapper(KilnJEIRecipe recipe) {
        return null;
    }

    @Override
    public boolean isRecipeValid(KilnJEIRecipe recipe) {
        return false;
    }

}
