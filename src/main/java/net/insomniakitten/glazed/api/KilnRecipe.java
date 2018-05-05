package net.insomniakitten.glazed.api;

/*
 *  Copyright 2018 InsomniaKitten
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

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Set;

public final class KilnRecipe implements IForgeRegistryEntry<KilnRecipe> {
    private final ItemStack input;
    private final ItemStack ouput;
    private final Set<ItemStack> catalysts;

    private ResourceLocation registryName;

    private KilnRecipe(ItemStack input, ItemStack output, ItemStack... catalysts) {
        final Builder<ItemStack> builder = ImmutableSet.builder();
        for (ItemStack catalyst : catalysts) {
            builder.add(catalyst.copy());
        }
        this.input = input.copy();
        this.ouput = output.copy();
        this.catalysts = builder.build();
    }

    public static KilnRecipe create(ItemStack input, ItemStack output, ItemStack... catalysts) {
        return new KilnRecipe(input, output, catalysts);
    }

    @Override
    public final KilnRecipe setRegistryName(ResourceLocation name) {
        registryName = Objects.requireNonNull(name);
        return this;
    }

    @Override
    @Nullable
    public final ResourceLocation getRegistryName() {
        return registryName;
    }

    @Override
    public final Class<KilnRecipe> getRegistryType() {
        return KilnRecipe.class;
    }
}
