package net.insomniakitten.glazed.compat.waila

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

import mcp.mobius.waila.api.IWailaConfigHandler
import mcp.mobius.waila.api.IWailaDataAccessor
import mcp.mobius.waila.api.IWailaDataProvider
import mcp.mobius.waila.api.IWailaPlugin
import mcp.mobius.waila.api.IWailaRegistrar
import mcp.mobius.waila.api.WailaPlugin
import net.insomniakitten.glazed.block.GlassBlock
import net.insomniakitten.glazed.block.GlassPaneBlock
import net.insomniakitten.glazed.extensions.description
import net.minecraft.item.ItemStack

@WailaPlugin
class GlazedWailaPlugin : IWailaPlugin, IWailaDataProvider {
    override fun register(registrar: IWailaRegistrar) = registrar.let {
        it.registerBodyProvider(this, GlassBlock::class.java)
        it.registerBodyProvider(this, GlassPaneBlock::class.java)
    }

    override fun getWailaBody(
            stack: ItemStack,
            tooltip: MutableList<String>,
            accessor: IWailaDataAccessor,
            config: IWailaConfigHandler
    ) = tooltip.apply { add(stack.description) }
}
