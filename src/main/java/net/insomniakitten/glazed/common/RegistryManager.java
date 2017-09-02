package net.insomniakitten.glazed.common;

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
import net.insomniakitten.glazed.common.kiln.RecipesKiln;
import net.insomniakitten.glazed.common.kiln.TileKiln;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.HashSet;
import java.util.Set;

@Mod.EventBusSubscriber(modid = Glazed.MOD_ID)
public class RegistryManager {

    private static final Set<ItemBlock> ITEM_BLOCKS = new HashSet<>();

    public static void registerItemBlock(ItemBlock iblock) {
        ITEM_BLOCKS.add(iblock);
    }

    @SubscribeEvent
    public static void onBlockRegistry(RegistryEvent.Register<Block> event) {
        GameRegistry.registerTileEntity(TileKiln.class, TileKiln.getKey());
        if (ConfigManager.glassConfig.enableFunctionalGlass) {
            event.getRegistry().register(Glazed.GLASS);
        }
        event.getRegistry().registerAll(Glazed.KILN, Glazed.MATERIAL);
    }

    @SubscribeEvent
    public static void onItemRegistry(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(ITEM_BLOCKS.toArray(new ItemBlock[0]));
    }

    @SubscribeEvent
    public static void onRecipeRegistry(RegistryEvent.Register<IRecipe> event) {
        registerBasicGlassRecipe();
        if (ConfigManager.kilnConfig.dyedGlassRecipes) {
            RegistryManager.registerDyedGlassRecipes();
        }
    }

    private static void registerBasicGlassRecipe() {
        ItemStack sand = new ItemStack(Blocks.SAND, 16);
        ItemStack glass = new ItemStack(Blocks.GLASS, 16);
        RecipesKiln.addKilnRecipe(sand, ItemStack.EMPTY, glass);
    }

    private static void registerDyedGlassRecipes() {
        ItemStack sand = new ItemStack(Blocks.SAND, 16);
        for (EnumDyeColor color : EnumDyeColor.values()) {
            ItemStack dye = new ItemStack(Items.DYE, 1, color.getMetadata());
            ItemStack glass = new ItemStack(Blocks.STAINED_GLASS, 16, color.getDyeDamage());
            RecipesKiln.addKilnRecipe(sand, dye, glass);
        }
    }

}
