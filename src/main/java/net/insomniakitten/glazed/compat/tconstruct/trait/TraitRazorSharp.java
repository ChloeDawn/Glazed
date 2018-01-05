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

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.traits.AbstractTrait;

public final class TraitRazorSharp extends AbstractTrait {

    private final String unlocName = "tconstruct.glazed.trait.razorSharp.name";
    private final String unlocDesc = "tconstruct.glazed.trait.razorSharp.desc";

    public TraitRazorSharp() {
        super("razor_sharp", TextFormatting.AQUA);
    }

    @Override
    public String getLocalizedName() {
        return Util.translate(unlocName);
    }

    @Override
    public String getLocalizedDesc() {
        return Util.translate(unlocDesc); // TODO Parse crit bonus chance config for description
    }

    @Override
    public void onHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damage, boolean isCritical) {
        if (isCritical && player.world.rand.nextInt(3) == 0) { // TODO Config for crit bonus chance
            target.attackEntityFrom(DamageSource.GENERIC, damage / 2.0F);
        }
    }

}
