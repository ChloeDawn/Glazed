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

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.gui.IDrawableAnimated.StartDirection;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.insomniakitten.glazed.Glazed;
import net.insomniakitten.glazed.compat.jei.JEICompatPlugin;
import net.insomniakitten.glazed.common.kiln.TileKiln.Slots;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class KilnJEICategory implements IRecipeCategory<KilnJEIRecipe> {

    private static final ResourceLocation KILN_GUI = new ResourceLocation(Glazed.MOD_ID, "textures/gui/kiln.png");

    private final IDrawable background;
    private final IDrawableAnimated flame;
    private final IDrawableAnimated arrow;
    private final String displayName;

    public KilnJEICategory(IGuiHelper guiHelper) {
        final IDrawableStatic flameDrawable = guiHelper.createDrawable(KILN_GUI, 176, 0, 14, 14);
        final IDrawableStatic arrowDrawable = guiHelper.createDrawable(KILN_GUI, 176, 14, 24, 17);

        this.background = guiHelper.createDrawable(KILN_GUI, 33, 16, 110, 54);
        this.flame = guiHelper.createAnimatedDrawable(flameDrawable, 300, StartDirection.TOP, true);
        this.arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, StartDirection.LEFT, false);
        this.displayName = getName().getUnformattedText();
    }

    @Override
    public String getUid() {
        return JEICompatPlugin.ID;
    }

    @Override
    public String getTitle() {
        return displayName;
    }

    @Override
    public String getModName() {
        return Glazed.MOD_NAME;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, KilnJEIRecipe recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        guiItemStacks.init(Slots.INPUT.ordinal(), true, 0, 0);
        guiItemStacks.init(Slots.CATALYST.ordinal(), true, 22, 0);
        guiItemStacks.init(Slots.OUTPUT.ordinal(), false, 82, 18);
        guiItemStacks.set(ingredients);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        flame.draw(minecraft, 12, 20);
        arrow.draw(minecraft, 46, 18);
    }

    public ITextComponent getName() {
        String name = Glazed.KILN.getUnlocalizedName() + ".name";
        return new TextComponentTranslation(name);
    }

}
