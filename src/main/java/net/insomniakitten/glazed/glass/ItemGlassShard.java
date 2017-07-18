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

import net.insomniakitten.glazed.Glazed;
import net.insomniakitten.glazed.RegistryManager.ShardRegistry;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemGlassShard extends Item {

    public ItemGlassShard() {
        setRegistryName("shard");
        setUnlocalizedName(Glazed.MOD_ID + ".shard");
        setCreativeTab(Glazed.TAB);
        setHasSubtypes(true);
    }

    @Override @SuppressWarnings("ConstantConditions")
    public void getSubItems(
            @Nonnull CreativeTabs tab,
            @Nonnull NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab))
            items.addAll(ShardRegistry.SHARDS.keySet());
    }

    @Override @SideOnly(Side.CLIENT)
    public void addInformation(
            ItemStack stack,
            @Nullable World world,
            List<String> tooltip,
            ITooltipFlag flag) {
        // TODO: Tooltips
    }
}
