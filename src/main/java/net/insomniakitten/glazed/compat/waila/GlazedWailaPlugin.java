package net.insomniakitten.glazed.compat.waila;

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

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;
import net.insomniakitten.glazed.GlazedVariant;
import net.insomniakitten.glazed.block.GlassBlock;
import net.insomniakitten.glazed.block.GlassPaneBlock;
import net.minecraft.item.ItemStack;

import java.util.List;

@WailaPlugin
public final class GlazedWailaPlugin implements IWailaPlugin, IWailaDataProvider {
    @Override
    public void register(IWailaRegistrar registrar) {
        registrar.registerBodyProvider(this, GlassBlock.class);
        registrar.registerBodyProvider(this, GlassPaneBlock.class);
    }

    @Override
    public List<String> getWailaBody(ItemStack stack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        tooltip.add(GlazedVariant.getDescription(stack));
        return tooltip;
    }
}
