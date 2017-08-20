package net.insomniakitten.glazed.shard;

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

import com.google.common.base.Equivalence.Wrapper;
import net.insomniakitten.glazed.Glazed;
import net.insomniakitten.glazed.client.model.ModelRegistry;
import net.insomniakitten.glazed.client.model.WrappedModel.ModelBuilder;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemGlassShard extends Item {

    public ItemGlassShard() {
        setRegistryName("shard");
        setUnlocalizedName(Glazed.MOD_ID + ".shard");
        setCreativeTab(Glazed.CTAB);
        setHasSubtypes(true);
        ModelRegistry.registerModel(new ModelBuilder(this).build());
    }

    @Override @SuppressWarnings("ConstantConditions")
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            for (Wrapper<ItemStack> shard : ShardRegistry.SHARDS.keySet()) {
                items.add(shard.get());
            }
        }
    }

    @Override @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        ItemStack glassBlock = ShardRegistry.getGlassFromShard(stack);
        if (!glassBlock.isEmpty()) {
            tooltip.add(glassBlock.getDisplayName());
        }
    }

}
