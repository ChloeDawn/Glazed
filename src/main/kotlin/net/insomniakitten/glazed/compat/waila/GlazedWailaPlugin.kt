package net.insomniakitten.glazed.compat.waila

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
