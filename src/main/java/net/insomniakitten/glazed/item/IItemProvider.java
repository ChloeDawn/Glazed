package net.insomniakitten.glazed.item;

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

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public interface IItemProvider {

    static Collection<Supplier<Item>> collectItemBlocks(ImmutableList<Block> blocks) {
        return blocks.stream()
                .filter(IItemProvider.class::isInstance)
                .map(block -> ((IItemProvider) block).getItemBlock())
                .filter(Objects::nonNull)
                .<Supplier<Item>>map(Suppliers::ofInstance)
                .collect(Collectors.toSet());
    }

    @Nullable
    ItemBlock getItemBlock();

}

