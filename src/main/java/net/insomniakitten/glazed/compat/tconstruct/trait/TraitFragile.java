package net.insomniakitten.glazed.compat.tconstruct.trait;

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

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.traits.AbstractTrait;

public final class TraitFragile extends AbstractTrait {

    private final String unlocName = "tconstruct.glazed.trait.fragile.name";
    private final String unlocDesc = "tconstruct.glazed.trait.fragile.desc";

    public TraitFragile() {
        super("fragile", TextFormatting.GOLD);
    }

    @Override
    public String getLocalizedName() {
        return Util.translate(unlocName);
    }

    @Override
    public String getLocalizedDesc() {
        return Util.translate(unlocDesc); // TODO Parse break chance config for description
    }

    @Override
    public void afterBlockBreak(ItemStack tool, World world, IBlockState state, BlockPos pos, EntityLivingBase player, boolean wasEffective) {
        if (world.rand.nextInt(100) == 0) { // TODO Config for break chance
            tool.setItemDamage(tool.getMaxDamage() - 1);
            if (world.isRemote && player instanceof EntityPlayer) {
                world.playSound((EntityPlayer) player, pos, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 1.5F, 0.8F);
            }
        }
    }

}
