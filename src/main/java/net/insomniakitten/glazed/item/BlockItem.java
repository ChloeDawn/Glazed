package net.insomniakitten.glazed.item;

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

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.function.Function;

public final class BlockItem extends ItemBlock {
    @Nullable
    private final Function<ItemStack, String> suffixFunction;

    public BlockItem(Block block, @Nullable Function<ItemStack, String> suffixFunction) {
        super(block);
        this.suffixFunction = suffixFunction;
        setHasSubtypes(suffixFunction != null);
    }

    public BlockItem(Block block) {
        this(block, null);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        String name = block.getUnlocalizedName();
        if (suffixFunction != null) {
            name += "." + suffixFunction.apply(stack);
        }
        return name;
    }

    @Override
    public int getMetadata(int damage) {
        return hasSubtypes ? damage : 0;
    }
}
