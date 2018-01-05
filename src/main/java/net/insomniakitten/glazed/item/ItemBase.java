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

import net.insomniakitten.glazed.Glazed;
import net.insomniakitten.glazed.client.model.IModelProvider;
import net.insomniakitten.glazed.client.model.WrappedModel;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Set;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;

public class ItemBase extends Item implements IModelProvider {

    public ItemBase(String name) {
        String loc = LOWER_UNDERSCORE.to(LOWER_CAMEL, name);
        setRegistryName(name);
        setUnlocalizedName(Glazed.ID + "." + loc);
        setCreativeTab(Glazed.CTAB);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        String key = stack.getUnlocalizedName() + ".tooltip";
        if (I18n.hasKey(key)) tooltip.add(I18n.format(key));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addModels(Set<WrappedModel> models) {
        models.add(new WrappedModel.Builder(this).build());
    }

}
