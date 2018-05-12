package net.insomniakitten.glazed.compat.jei

import mezz.jei.api.IJeiRuntime
import mezz.jei.api.IModPlugin
import mezz.jei.api.IModRegistry
import mezz.jei.api.ISubtypeRegistry
import mezz.jei.api.JEIPlugin
import mezz.jei.api.ingredients.IModIngredientRegistration
import mezz.jei.api.recipe.IRecipeCategoryRegistration

@JEIPlugin
class GlazedJEIPlugin : IModPlugin {
    override fun register(registry: IModRegistry) = Unit
    override fun onRuntimeAvailable(runtime: IJeiRuntime) = Unit
    override fun registerItemSubtypes(registry: ISubtypeRegistry) = Unit
    override fun registerIngredients(registry: IModIngredientRegistration) = Unit
    override fun registerCategories(registry: IRecipeCategoryRegistration) = Unit
}
