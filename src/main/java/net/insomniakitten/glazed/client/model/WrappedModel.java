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

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public final class WrappedModel {

    private final Item item;
    private final int meta;
    private final ResourceLocation resource;
    private final String variants;
    private final ModelResourceLocation mrl;

    private WrappedModel(ModelBuilder model) {
        this.item = model.item;
        this.meta = model.meta;
        this.resource = model.resource;
        this.variants = model.variants.stream().collect(Collectors.joining(","));
        this.mrl = new ModelResourceLocation(this.resource, this.variants);
    }

    public Item getItem() {
        return item;
    }

    public int getMetadata() {
        return meta;
    }

    public ResourceLocation getResourceLocation() {
        return resource;
    }

    public String getVariants() {
        return variants;
    }

    public ModelResourceLocation getModelResourceLocation() {
        return mrl;
    }

    public static class ModelBuilder {

        private Item item;
        private int meta;
        private ResourceLocation resource;
        private Set<String> variants = new HashSet<>();

        public ModelBuilder(Item item, int meta) {
            this.item = item;
            this.meta = meta;
            this.resource = item.getRegistryName();
        }

        public ModelBuilder(Item item) {
            this(item, 0);
        }

        public ModelBuilder setResourceLocation(ResourceLocation resource) {
            this.resource = resource;
            return this;
        }

        public ModelBuilder addVariant(String variant) {
            this.variants.add(variant);
            return this;
        }

        public WrappedModel build() {
            if (variants.isEmpty()) {
                variants.add("inventory");
            }
            return new WrappedModel(this);
        }

    }

}
