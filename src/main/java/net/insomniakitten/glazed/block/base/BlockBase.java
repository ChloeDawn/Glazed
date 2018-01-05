package net.insomniakitten.glazed.block.base;

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

import com.google.common.base.CaseFormat;
import net.insomniakitten.glazed.Glazed;
import net.insomniakitten.glazed.item.IItemProvider;
import net.insomniakitten.glazed.item.ItemBlockBase;
import net.insomniakitten.glazed.util.MaterialHelper;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;

public class BlockBase extends Block implements IItemProvider {

    private final BlockStateContainer container;

    public BlockBase(String name, Material material, SoundType sound, float hardness, float resistance) {
        super(material);
        String loc = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name);
        setRegistryName(name);
        setUnlocalizedName(Glazed.ID + "." + loc);
        setCreativeTab(Glazed.CTAB);
        setSoundType(sound);
        setHardness(hardness);
        setResistance(resistance);
        container = createStateContainer().build();
        setDefaultState(getBlockState().getBaseState());
    }

    public BlockBase(String name, Material material, SoundType sound) {
        this(name, material, sound, MaterialHelper.getHardness(material), MaterialHelper.getResistance(material));
    }

    public BlockBase(String name, Material material, float hardness, float resistance) {
        this(name, material, MaterialHelper.getSoundType(material), hardness, resistance);
    }

    public BlockBase(String name, Material material) {
        this(name, material, MaterialHelper.getSoundType(material), MaterialHelper.getHardness(material), MaterialHelper.getResistance(material));

    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    protected final BlockStateContainer createBlockState() {
        return new BlockStateContainer.Builder(this).build();
    }

    @Override
    public final BlockStateContainer getBlockState() {
        return container;
    }

    @Override
    public ItemBlock getItemBlock() {
        return new ItemBlockBase(this);
    }

    protected BlockStateContainer.Builder createStateContainer() {
        return new BlockStateContainer.Builder(this);
    }

}