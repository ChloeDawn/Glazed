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
import net.insomniakitten.glazed.client.ClientWrapper;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ItemBlockGlass extends ItemBlock {

    public ItemBlockGlass(Block block) {
        super(block);
        assert block.getRegistryName() != null;
        setRegistryName(block.getRegistryName());
        setHasSubtypes(true);
    }

    @Override @SideOnly(Side.CLIENT)
    public String getUnlocalizedName(ItemStack stack) {
        int meta = stack.getMetadata() % GlassBlockType.values().length;
        String type = GlassBlockType.values()[meta].getName();
        return this.getBlock().getUnlocalizedName() + "." + type;
    }

    @Override @SideOnly(Side.CLIENT)
    public String getItemStackDisplayName(ItemStack stack) {
        int meta = stack.getMetadata() % GlassBlockType.values().length;
        String type = GlassBlockType.values()[meta].getName();
        Set<Pair<UUID, String>> keys = ClientWrapper.SPECIALS.keySet();
        Pair match = Pair.of(Glazed.proxy.getUUID(), type);
        if (keys.contains(match)) {
            return ClientWrapper.SPECIALS.get(match);
        } else {
            return super.getItemStackDisplayName(stack);
        }
    }

    @Override @SideOnly(Side.CLIENT)
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
