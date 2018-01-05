package net.insomniakitten.glazed.client.model;

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

import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

@SideOnly(Side.CLIENT)
public interface ICustomMeshDefinition {

    static void registerMeshDefinition(Item item, ICustomMeshDefinition iface) {
        Set<ModelResourceLocation> variants = new HashSet<>();
        iface.addModelVariants(variants);
        variants.forEach(variant -> ModelBakery.registerItemVariants(item, variant));
        final Function<ItemStack, ModelResourceLocation> func = iface::getMeshDefinition;
        ModelLoader.setCustomMeshDefinition(item, func::apply);
    }

    ModelResourceLocation getMeshDefinition(ItemStack stack);

    void addModelVariants(Set<ModelResourceLocation> variants);

}
