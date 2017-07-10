package net.insomniakitten.glazed.glass;

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

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ItemBlockGlass extends ItemBlock {

    public ItemBlockGlass(Block block) {
        super(block);
        assert block.getRegistryName() != null;
        setRegistryName(block.getRegistryName());
        setHasSubtypes(true);
    }

    @Override @Nonnull
    public String getUnlocalizedName(ItemStack stack) {
        int meta = stack.getMetadata() % GlassType.values().length;
        String type = GlassType.values()[meta].getName();
        return this.getBlock().getUnlocalizedName() + "." + type;
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

}
