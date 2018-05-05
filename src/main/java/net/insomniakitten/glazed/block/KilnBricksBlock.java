package net.insomniakitten.glazed.block;

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

import net.insomniakitten.glazed.Glazed;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public final class KilnBricksBlock extends Block {
    public KilnBricksBlock() {
        super(Material.ROCK, MapColor.ADOBE);
        setUnlocalizedName(Glazed.ID + ".kiln_bricks");
        setCreativeTab(Glazed.TAB);
        setSoundType(SoundType.STONE);
        setHardness(2.0F);
        setResistance(30.0F);
    }
}
