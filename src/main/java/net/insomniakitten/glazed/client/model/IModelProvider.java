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

import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashSet;
import java.util.Set;

@SideOnly(Side.CLIENT)
public interface IModelProvider {

    static Set<WrappedModel> getModels(IModelProvider provider) {
        Set<WrappedModel> models = new HashSet<>();
        provider.addModels(models);
        return models;
    }

    static void registerModels(Item item, IModelProvider provider) {
        for (WrappedModel model : getModels(provider)) {
            ModelLoader.setCustomModelResourceLocation(
                    item, model.getMetadata(), model.getMRL());
        }
    }

    void addModels(Set<WrappedModel> models);

}

