package net.insomniakitten.glazed.compat.waila.provider;

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

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.insomniakitten.glazed.kiln.BlockKiln;
import net.insomniakitten.glazed.kiln.TileKiln;
import net.insomniakitten.glazed.kiln.TileKiln.Slots;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class KilnDataProvider implements IWailaDataProvider {

    @Override @SideOnly(Side.CLIENT)
    public List<String> getWailaBody(
            ItemStack stack, List<String> tooltip,
            IWailaDataAccessor accessor, IWailaConfigHandler config) {
        boolean isUpper = BlockKiln.isUpper(accessor.getBlockState());
        BlockPos tilePos = isUpper ? accessor.getPosition().down() : accessor.getPosition();
        TileEntity tile = accessor.getWorld().getTileEntity(tilePos);

        if (tile != null) {
            for (Slots slot : Slots.values()) {
                ItemStack item = Slots.getSlot((TileKiln) tile, slot);
                if (!item.isEmpty()) {
                    String label = I18n.format("waila.glazed.kiln." + slot.getName()) + ": ";
                    String multiplier = " x " + item.getCount();
                    tooltip.add(label + item.getDisplayName() + multiplier);
                }
            }
        }

        return tooltip;
    }
}
