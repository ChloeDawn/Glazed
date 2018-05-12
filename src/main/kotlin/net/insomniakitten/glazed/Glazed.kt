package net.insomniakitten.glazed

import net.insomniakitten.glazed.gui.GlassKilnContainer
import net.insomniakitten.glazed.gui.GlassKilnGui
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.network.IGuiHandler
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Mod(
        modid = Glazed.ID,
        name = Glazed.NAME,
        version = Glazed.VERSION,
        dependencies = Glazed.DEPENDENCIES,
        modLanguageAdapter = Glazed.LANG_ADAPTER,
        acceptedMinecraftVersions = Glazed.MC_VERSIONS
)
object Glazed {
    const val ID = "glazed"
    const val NAME = "Glazed"
    const val VERSION = "%VERSION%"
    const val DEPENDENCIES = "required-after:forgelin@[1.6.0,);" +
                             "required-after-client:ctm@[MC1.12-0.3.0.14,);" +
                             "required-after:forge@[1.12.2-14.23.3.2678,);"
    const val MC_VERSIONS = "[1.12,1.13)"
    const val LANG_ADAPTER = "net.shadowfacts.forgelin.KotlinAdapter"

    val LOGGER: Logger = LogManager.getLogger(ID)

    val TAB = object : CreativeTabs(ID) {
        @SideOnly(Side.CLIENT)
        override fun getTranslatedTabLabel() = "item_group.$ID.label"

        @SideOnly(Side.CLIENT)
        override fun getTabIconItem() = ItemStack(GlazedRegistry.GLASS_KILN_ITEM)

        @SideOnly(Side.CLIENT)
        override fun displayAllRelevantItems(items: NonNullList<ItemStack>) {
            items += ItemStack(GlazedRegistry.GLASS_KILN_ITEM)
            items += ItemStack(GlazedRegistry.KILN_BRICKS_ITEM)
            GlazedVariant.VARIANTS.forEach {
                items += ItemStack(GlazedRegistry.GLASS_BLOCK_ITEM, 1, it.ordinal)
                items += ItemStack(GlazedRegistry.GLASS_PANE_ITEM, 1, it.ordinal)
            }
        }
    }

    val GUI_HANDLER = object : IGuiHandler {
        override fun getServerGuiElement(
                id: Int,
                player: EntityPlayer,
                world: World,
                x: Int,
                y: Int,
                z: Int
        ): Any? {
            return if (id == 0) {
                val te = world.getTileEntity(BlockPos(x, y, z))
                GlassKilnContainer(te ?: return null, player)
            } else null
        }

        override fun getClientGuiElement(
                id: Int,
                player: EntityPlayer,
                world: World,
                x: Int,
                y: Int,
                z: Int
        ): Any? {
            return if (id == 0) {
                val te = world.getTileEntity(BlockPos(x, y, z))
                GlassKilnGui(te ?: return null, player)
            } else null
        }
    }

    @EventHandler
    fun onPreInitialization(event: FMLPreInitializationEvent) {
        NetworkRegistry.INSTANCE.registerGuiHandler(ID, GUI_HANDLER)
        MinecraftForge.EVENT_BUS.register(GlazedRegistry)
        GlazedProxy.instance.registerEventHandler()
    }

    @EventHandler
    fun onInitialization(event: FMLInitializationEvent) {
        GlazedProxy.instance.registerReloadListener()
    }
}
