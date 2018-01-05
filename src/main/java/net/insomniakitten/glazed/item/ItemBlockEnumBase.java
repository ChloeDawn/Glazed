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

import net.insomniakitten.glazed.block.base.BlockEnumBase;
import net.insomniakitten.glazed.block.base.IPropertyProvider;
import net.insomniakitten.glazed.client.model.IModelProvider;
import net.insomniakitten.glazed.client.model.WrappedModel;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Set;

public class ItemBlockEnumBase<E extends Enum<E> & IPropertyProvider<E>> extends ItemBlock implements IModelProvider {

    protected final E[] values;

    public ItemBlockEnumBase(BlockEnumBase<E> block) {
        super(block);
        //noinspection ConstantConditions
        setRegistryName(block.getRegistryName());
        values = block.getValues();
        setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int meta = stack.getMetadata() % values.length;
        return this.getUnlocalizedName() + "." + values[meta].getUnlocalizedName();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        String key = stack.getUnlocalizedName() + ".tooltip";
        if (I18n.hasKey(key)) tooltip.add(I18n.format(key));
        else {
            if (I18n.hasKey(key + "0")) tooltip.add(I18n.format(key + "0"));
            if (I18n.hasKey(key + "1")) tooltip.add(I18n.format(key + "1"));
            if (I18n.hasKey(key + "2")) tooltip.add(I18n.format(key + "2"));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addModels(Set<WrappedModel> models) {
        for (E value : values) {
            models.add(new WrappedModel.Builder(this, value.getMetadata())
                    .addVariant("variant=" + value.getName())
                    .build()
            );
        }
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

}

