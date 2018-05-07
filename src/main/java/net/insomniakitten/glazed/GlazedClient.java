package net.insomniakitten.glazed;

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

import net.insomniakitten.glazed.block.GlassKilnBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPane;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;

import static java.util.Objects.requireNonNull;
import static net.minecraft.client.renderer.GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA;
import static net.minecraft.client.renderer.GlStateManager.DestFactor.SRC_COLOR;
import static net.minecraft.client.renderer.GlStateManager.DestFactor.ZERO;
import static net.minecraft.client.renderer.GlStateManager.SourceFactor.DST_COLOR;
import static net.minecraft.client.renderer.GlStateManager.SourceFactor.ONE;
import static net.minecraft.client.renderer.GlStateManager.SourceFactor.SRC_ALPHA;

@SideOnly(Side.CLIENT)
enum GlazedClient implements IResourceManagerReloadListener {
    INSTANCE;

    private static final IStateMapper STATE_MAPPER_BLOCK = new StateMapperBase() {
        @Override
        protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
            final String name = state.getValue(GlazedVariant.PROPERTY).getName() + "_glass_block";
            return new ModelResourceLocation(new ResourceLocation(Glazed.ID, name), "normal");
        }
    };

    private static final IStateMapper STATE_MAPPER_PANE = new StateMapperBase() {
        @Override
        protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
            final String name = state.getValue(GlazedVariant.PROPERTY).getName() + "_glass_pane";
            return new ModelResourceLocation(new ResourceLocation(Glazed.ID, name), String.join(",",
                    "east=" + state.getValue(BlockPane.EAST),
                    "north=" + state.getValue(BlockPane.NORTH),
                    "south=" + state.getValue(BlockPane.SOUTH),
                    "west=" + state.getValue(BlockPane.WEST)
            ));
        }
    };

    private static final IBlockColor BLOCK_COLOR = (state, access, pos, tintIndex) -> {
        if (tintIndex == 0 && access != null && pos != null) {
            return state.getValue(GlazedVariant.PROPERTY).getColor(access, pos);
        }
        return 0xFFFFFFFF;
    };

    private static final IItemColor ITEM_COLOR = (stack, tintIndex) -> {
        final int meta = stack.getMetadata();
        if (tintIndex == 0 && GlazedVariant.isValid(meta)) {
            final Minecraft mc = FMLClientHandler.instance().getClient();
            if (mc.player != null && mc.player.world != null) {
                final World world = mc.player.world;
                final BlockPos pos = mc.player.getPosition();
                return GlazedVariant.VARIANTS[meta].getColor(world, pos);
            }
        }
        return 0xFFFFFFFF;
    };

    private final TextureAtlasSprite[] destroyStageSprites = new TextureAtlasSprite[10];

    @Override
    public void onResourceManagerReload(IResourceManager rm) {
        Glazed.LOGGER.debug("Reloading destroy stage sprite cache");
        Arrays.setAll(destroyStageSprites, i -> ModelLoader.defaultTextureGetter()
                .apply(new ResourceLocation("minecraft:blocks/destroy_stage_" + i))
        );
    }

    @SubscribeEvent
    protected void onModelRegistry(ModelRegistryEvent event) {
        register(GlazedRegistry.GLASS_BLOCK, STATE_MAPPER_BLOCK);
        register(GlazedRegistry.GLASS_PANE, STATE_MAPPER_PANE);
        register(GlazedRegistry.KILN_BRICKS_ITEM, "normal");
        register(GlazedRegistry.GLASS_KILN_ITEM, "inventory");
        register(GlazedRegistry.GLASS_BLOCK_ITEM, "normal", GlazedVariant.VARIANTS);
        register(GlazedRegistry.GLASS_PANE_ITEM, "inventory", GlazedVariant.VARIANTS);
    }

    @SubscribeEvent
    protected void onBlockColors(ColorHandlerEvent.Block event) {
        register(event, GlazedRegistry.GLASS_BLOCK, BLOCK_COLOR);
        register(event, GlazedRegistry.GLASS_PANE, BLOCK_COLOR);
    }

    @SubscribeEvent
    protected void onItemColors(ColorHandlerEvent.Item event) {
        register(event, GlazedRegistry.GLASS_BLOCK_ITEM, ITEM_COLOR);
        register(event, GlazedRegistry.GLASS_PANE_ITEM, ITEM_COLOR);
    }

    @SubscribeEvent
    protected void onRenderWorldLast(RenderWorldLastEvent event) {
        final Minecraft mc = FMLClientHandler.instance().getClient();
        final PlayerControllerMP controller = mc.playerController;

        if (controller == null || !controller.getIsHittingBlock()) return;

        final BlockPos curr = controller.currentBlock;
        final IBlockState state = mc.world.getBlockState(curr);

        if (state.getBlock() != GlazedRegistry.GLASS_KILN) return;

        final int destroyStage = (int) (controller.curBlockDamageMP * 10.0F) - 1;

        if (destroyStage < 0 || destroyStage > 10) return;

        try {
            GlStateManager.tryBlendFuncSeparate(DST_COLOR, SRC_COLOR, ONE, ZERO);
            GlStateManager.enableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);
            GlStateManager.enablePolygonOffset();
            GlStateManager.doPolygonOffset(-3.0F, -3.0F);
            GlStateManager.enableAlpha();
            GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);

            final Tessellator tessellator = Tessellator.getInstance();
            final BufferBuilder buffer = tessellator.getBuffer();
            final Vec3d vec = getPositionVector(mc.player, event.getPartialTicks());
            final TextureAtlasSprite sprite = destroyStageSprites[destroyStage];
            final BlockPos pos = state.getValue(GlassKilnBlock.HALF).offsetToOtherHalf(curr);

            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
            buffer.setTranslation(-vec.x, -vec.y, -vec.z);
            buffer.noColor();

            mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            mc.getBlockRendererDispatcher().renderBlockDamage(state, pos, sprite, mc.world);

            buffer.setTranslation(0.0D, 0.0D, 0.0D);
            tessellator.draw();
        } catch (Exception e) {
            throw new RuntimeException("Encountered exception whilst rendering destroy stage quads", e);
        } finally {
            GlStateManager.tryBlendFuncSeparate(SRC_ALPHA, ONE_MINUS_SRC_ALPHA, ONE, ZERO);
            GlStateManager.doPolygonOffset(0.0F, 0.0F);
            GlStateManager.disablePolygonOffset();
            GlStateManager.depthMask(true);
        }
    }

    private Vec3d getPositionVector(Entity entity, float partialTicks) {
        final double lastX = entity.lastTickPosX;
        final double lastY = entity.lastTickPosY;
        final double lastZ = entity.lastTickPosZ;
        final double posX = lastX + ((entity.posX - lastX) * partialTicks);
        final double posY = lastY + ((entity.posY - lastY) * partialTicks);
        final double posZ = lastZ + ((entity.posZ - lastZ) * partialTicks);
        return new Vec3d(posX, posY, posZ);
    }

    private void register(Block block, IStateMapper stateMapper) {
        final ResourceLocation id = requireNonNull(block.getRegistryName());
        Glazed.LOGGER.debug("Registering state mapper for {}", id);
        ModelLoader.setCustomStateMapper(block, stateMapper);
    }

    private void register(Item item, String variant) {
        final ResourceLocation id = requireNonNull(item.getRegistryName());
        Glazed.LOGGER.debug("Registering item model for {}#0 to {}#{}", id, id, variant);
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(id, variant));
    }

    private void register(Item item, String variant, IStringSerializable... variants) {
        final ResourceLocation id = requireNonNull(item.getRegistryName());
        for (int meta = 0; meta < variants.length; meta++) {
            final String path = variants[meta].getName() + "_" + id.getResourcePath();
            final ResourceLocation model = new ResourceLocation(Glazed.ID, path);
            Glazed.LOGGER.debug("Registering item model for {}#{} to {}#{}", id, meta, model, variant);
            ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(model, variant));
        }
    }

    private void register(ColorHandlerEvent.Item event, Item item, IItemColor color) {
        final String cls = requireNonNull(color).getClass().getSimpleName();
        final ResourceLocation id = requireNonNull(item.getRegistryName());
        Glazed.LOGGER.debug("Registering item color instance {} for {}", cls, id);
        event.getItemColors().registerItemColorHandler(color, item);
    }

    private void register(ColorHandlerEvent.Block event, Block block, IBlockColor color) {
        final String cls = requireNonNull(color).getClass().getSimpleName();
        final ResourceLocation id = requireNonNull(block.getRegistryName());
        Glazed.LOGGER.debug("Registering block color instance {} for {}", cls, id);
        event.getBlockColors().registerBlockColorHandler(color, block);
    }
}
