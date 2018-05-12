package net.insomniakitten.glazed

import net.insomniakitten.glazed.GlazedVariant.VARIANTS
import net.insomniakitten.glazed.block.GlassKilnBlock
import net.insomniakitten.glazed.extensions.get
import net.insomniakitten.glazed.extensions.getPosition
import net.insomniakitten.glazed.extensions.key
import net.insomniakitten.glazed.extensions.variant
import net.minecraft.block.Block
import net.minecraft.block.BlockPane.EAST
import net.minecraft.block.BlockPane.NORTH
import net.minecraft.block.BlockPane.SOUTH
import net.minecraft.block.BlockPane.WEST
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.GlStateManager.DestFactor
import net.minecraft.client.renderer.GlStateManager.SourceFactor
import net.minecraft.client.renderer.GlStateManager.alphaFunc
import net.minecraft.client.renderer.GlStateManager.color
import net.minecraft.client.renderer.GlStateManager.depthMask
import net.minecraft.client.renderer.GlStateManager.disablePolygonOffset
import net.minecraft.client.renderer.GlStateManager.doPolygonOffset
import net.minecraft.client.renderer.GlStateManager.enableAlpha
import net.minecraft.client.renderer.GlStateManager.enableBlend
import net.minecraft.client.renderer.GlStateManager.enablePolygonOffset
import net.minecraft.client.renderer.GlStateManager.tryBlendFuncSeparate
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.client.renderer.block.statemap.IStateMapper
import net.minecraft.client.renderer.block.statemap.StateMapperBase
import net.minecraft.client.renderer.color.IBlockColor
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.client.resources.IResourceManager
import net.minecraft.client.resources.IResourceManagerReloadListener
import net.minecraft.item.Item
import net.minecraft.util.IStringSerializable
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.ColorHandlerEvent
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.client.FMLClientHandler
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.lwjgl.opengl.GL11

@SideOnly(Side.CLIENT)
object GlazedClient : IResourceManagerReloadListener {
    private val BLOCK_STATE_MAPPER = object : StateMapperBase() {
        override fun getModelResourceLocation(state: IBlockState) = state.run {
            ModelResourceLocation("${Glazed.ID}:${key}_glass_block", "normal")
        }
    }

    private val PANE_STATE_MAPPER = object : StateMapperBase() {
        override fun getModelResourceLocation(state: IBlockState) = state.let {
            ModelResourceLocation(
                    "${Glazed.ID}:${it.key}_glass_pane",
                    "east=${it[EAST]},north=${it[NORTH]}," +
                    "south=${it[SOUTH]},west=${it[WEST]}"
            )
        }
    }

    private val BLOCK_COLOR = IBlockColor { state, access, pos, tint ->
        if (tint == 0 && access != null && pos != null) {
            state.variant.color(access, pos)
        }
        -1
    }

    private val ITEM_COLOR = IItemColor { stack, tint ->
        if (tint == 0 && stack in GlazedVariant) {
            client.player?.run { stack.variant?.run { color(world, position) } }
        }
        -1
    }

    private val client get() = FMLClientHandler.instance().client
    private val textureMap get() = client.textureMapBlocks
    private val textureManager get() = client.renderEngine
    private val blockRenderer get() = client.blockRendererDispatcher

    private var destroyStageSprites = emptyArray<TextureAtlasSprite>()

    override fun onResourceManagerReload(rm: IResourceManager) {
        Glazed.LOGGER.debug("Reloading destroy stage sprite cache")
        destroyStageSprites = Array(10) {
            textureMap.getAtlasSprite("minecraft:blocks/destroy_stage_$it")
        }
    }

    @SubscribeEvent
    fun onModelRegistry(event: ModelRegistryEvent) {
        register(GlazedRegistry.GLASS_BLOCK, BLOCK_STATE_MAPPER)
        register(GlazedRegistry.GLASS_PANE, PANE_STATE_MAPPER)
        register(GlazedRegistry.KILN_BRICKS_ITEM, "normal")
        register(GlazedRegistry.GLASS_KILN_ITEM, "inventory")
        register(GlazedRegistry.GLASS_BLOCK_ITEM, "normal", VARIANTS)
        register(GlazedRegistry.GLASS_PANE_ITEM, "inventory", VARIANTS)
    }

    @SubscribeEvent
    fun onBlockColors(event: ColorHandlerEvent.Block) {
        register(event, GlazedRegistry.GLASS_BLOCK, BLOCK_COLOR)
        register(event, GlazedRegistry.GLASS_PANE, BLOCK_COLOR)
    }

    @SubscribeEvent
    fun onItemColors(event: ColorHandlerEvent.Item) {
        register(event, GlazedRegistry.GLASS_BLOCK_ITEM, ITEM_COLOR)
        register(event, GlazedRegistry.GLASS_PANE_ITEM, ITEM_COLOR)
    }

    @SubscribeEvent
    fun onRenderWorldLast(event: RenderWorldLastEvent) {
        val controller = client.playerController ?: return

        if (!controller.isHittingBlock) return

        val curr = controller.currentBlock
        val state = client.world[curr].state

        if (state.block != GlazedRegistry.GLASS_KILN) return

        val destroyStage = (controller.curBlockDamageMP * 10.0f).toInt() - 1

        if (destroyStage < 0 || destroyStage > 10) return

        try {
            tryBlendFuncSeparate(
                    SourceFactor.DST_COLOR,
                    DestFactor.SRC_COLOR,
                    SourceFactor.ONE,
                    DestFactor.ZERO
            )
            enableBlend()
            color(1.0f, 1.0f, 1.0f, 0.5f)
            enablePolygonOffset()
            doPolygonOffset(-3.0f, -3.0f)
            enableAlpha()
            alphaFunc(GL11.GL_GREATER, 0.1f)

            val buffer = Tessellator.getInstance().buffer
            val posLerp = client.player.getPosition(event.partialTicks)
            val sprite = destroyStageSprites[destroyStage]
            val pos = state[GlassKilnBlock.HALF](curr)

            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK)
            buffer.setTranslation(-posLerp.x, -posLerp.y, -posLerp.z)
            buffer.noColor()

            textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE)
            blockRenderer.renderBlockDamage(state, pos, sprite, client.world)

            buffer.setTranslation(0.0, 0.0, 0.0)
            Tessellator.getInstance().draw()
        } catch (e: Exception) {
            throw RuntimeException("Encountered exception whilst rendering destroy stage quads", e)
        } finally {
            tryBlendFuncSeparate(
                    SourceFactor.SRC_ALPHA,
                    DestFactor.ONE_MINUS_SRC_ALPHA,
                    SourceFactor.ONE,
                    DestFactor.ZERO
            )
            doPolygonOffset(0.0f, 0.0f)
            disablePolygonOffset()
            depthMask(true)
        }
    }

    private fun register(block: Block, stateMapper: IStateMapper) {
        Glazed.LOGGER.debug("Registering state mapper for {}", block.registryName!!)
        ModelLoader.setCustomStateMapper(block, stateMapper)
    }

    private fun register(item: Item, variant: String) {
        val id = item.registryName!!
        Glazed.LOGGER.debug("Registering item model for {}#0 to {}#{}", id, id, variant)
        ModelLoader.setCustomModelResourceLocation(item, 0, ModelResourceLocation(id, variant))
    }

    private fun register(item: Item, variant: String, variants: Array<out IStringSerializable>) =
            variants.forEachIndexed { meta, it ->
                val id = item.registryName!!
                val model = ResourceLocation(Glazed.ID, it.name + "_" + id.resourcePath)
                Glazed.LOGGER.debug("Registering item model for {}#{} to {}#{}", id, meta, model, variant)
                ModelLoader.setCustomModelResourceLocation(item, meta, ModelResourceLocation(model, variant))
            }

    private fun register(event: ColorHandlerEvent.Item, item: Item, color: IItemColor) {
        Glazed.LOGGER.debug(
                "Registering item color instance {} for {}",
                color::class.java.simpleName, item.registryName!!
        )
        event.itemColors.registerItemColorHandler(color, item)
    }

    private fun register(event: ColorHandlerEvent.Block, block: Block, color: IBlockColor) {
        Glazed.LOGGER.debug(
                "Registering block color instance {} for {}",
                color::class.java.simpleName, block.registryName!!
        )
        event.blockColors.registerBlockColorHandler(color, block)
    }
}
