package net.insomniakitten.glazed.common.item;

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

import net.insomniakitten.glazed.client.model.ModelRegistry;
import net.insomniakitten.glazed.client.model.WrappedModel;
import net.insomniakitten.glazed.client.model.WrappedModel.ModelBuilder;
import net.insomniakitten.glazed.common.block.BlockBase;
import net.insomniakitten.glazed.common.util.IStatePropertyHolder;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemBlockBase <E extends Enum<E> & IStatePropertyHolder<E>> extends ItemBlock {

    private final E[] values;

    public ItemBlockBase(BlockBase<E> block) {
        super(block);
        this.values = block.getValues();
        assert block.getRegistryName() != null;
        setRegistryName(block.getRegistryName());
        setHasSubtypes(true);
        registerModels();
    }

    private void registerModels() {
        for (E value : values) {
            WrappedModel model = new ModelBuilder(this, value.getMetadata()).addVariant("type=" + value.getName())
                    .build();
            ModelRegistry.registerModel(model);
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int meta = stack.getMetadata() % values.length;
        return this.block.getUnlocalizedName() + "." + values[ meta ].getName();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        String key = stack.getUnlocalizedName() + ".tooltip";
        if (I18n.hasKey(key)) {
            tooltip.add(I18n.format(key));
        }
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

}
