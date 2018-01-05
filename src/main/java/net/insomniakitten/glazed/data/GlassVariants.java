package net.insomniakitten.glazed.data;

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

import net.insomniakitten.glazed.block.base.IPropertyProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.stream.Stream;

public enum GlassVariants implements IPropertyProvider<GlassVariants> {

    GAIA(0.3f, 1.5f, 0, BlockRenderLayer.TRANSLUCENT) {
        @Override
        public boolean hasTintIndex() {
            return true;
        }
    },

    RADIANT(0.3f, 1.5f, 15, BlockRenderLayer.TRANSLUCENT),

    IRIDESCENT(0.3f, 1.5f, 8, BlockRenderLayer.CUTOUT),

    ENERGETIC(0.3f, 1.5f, 0, BlockRenderLayer.TRANSLUCENT) {
        @Override
        public int getRedstonePower() {
            return 15;
        }
    },

    SHADOWED(0.3f, 10.0f, 0, BlockRenderLayer.TRANSLUCENT) {
        @Override
        public int getLightOpacity() {
            return 255;
        }
    },

    VOIDIC(0.3f, 10.0f, 8, BlockRenderLayer.TRANSLUCENT) {
        @Override
        public void onCollidedWithBlock(Entity entity, World world, BlockPos pos) {
            if (!entity.isSneaking()) {
                if (entity.motionY <= 0.0F) entity.motionY *= 0.4D;
                entity.motionX *= 0.4D;
                entity.motionZ *= 0.4D;
                entity.fallDistance = 0.0F;
            }
        }

        @Override
        public boolean canCollideWithBlock(Entity entity, World world, BlockPos pos) {
            return entity != null && entity.isSneaking();
        }
    },

    QUILTED(0.3f, 10.0f, 0, BlockRenderLayer.CUTOUT) {
        @Override
        public SoundType getSoundType() {
            return new GlassSoundType(SoundType.CLOTH);
        }

        @Override
        public boolean requiresSilkTouch() {
            return false;
        }
    },

    REINFORCED(2.0f, 3000.0f, 0, BlockRenderLayer.CUTOUT) {
        @Override
        public SoundType getSoundType() {
            return new GlassSoundType(SoundType.METAL);
        }

        @Override
        public boolean requiresSilkTouch() {
            return false;
        }

        @Override
        public boolean isToolEffective(String tool) {
            return "pickaxe".equals(tool);
        }
    },

    SLIMY(0.3f, -1.0f, 0, BlockRenderLayer.TRANSLUCENT) {
        @Override
        public SoundType getSoundType() {
            return new GlassSoundType(SoundType.SLIME);
        }

        @Override
        public void onEntityWalk(World world, BlockPos pos, Entity entity) {
            if (entity != null && Math.abs(entity.motionY) < 0.1D && !entity.isSneaking()) {
                double multiplier = 1.2D + Math.abs(entity.motionY) * 0.2D;
                entity.motionX *= multiplier;
                entity.motionZ *= multiplier;
            }
        }
    },

    AURORIC(0.3f, 10.0f, 8, BlockRenderLayer.TRANSLUCENT);

    private final float hardness;
    private final float resistance;
    private final int lightLevel;
    private final BlockRenderLayer layer;

    GlassVariants(float hardness, float resistance, int lightLevel, BlockRenderLayer layer) {
        this.hardness = hardness;
        this.resistance = resistance;
        this.lightLevel = lightLevel;
        this.layer = layer;
    }

    public static Stream<GlassVariants> stream() {
        return Stream.of(values());
    }

    @Override
    public GlassVariants getProvider() {
        return this;
    }

    @Override
    public Material getMaterial() {
        return Material.GLASS;
    }

    @Override
    public float getHardness() {
        return hardness;
    }

    @Override
    public float getResistance() {
        return resistance;
    }

    @Override
    public SoundType getSoundType() {
        return SoundType.GLASS;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return layer;
    }

    @Override
    public String getOrePrefix() {
        return "blockGlass";
    }

    @Override
    public int getLightLevel() {
        return lightLevel;
    }

    @Override
    public int getLightOpacity() {
        return 0;
    }

    @Override
    public boolean requiresSilkTouch() {
        return true;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    protected static class GlassSoundType extends SoundType {

        public GlassSoundType(SoundType type) {
            super(1.0F, 1.0F, SoundEvents.BLOCK_GLASS_BREAK,
                    type.getStepSound(), type.getPlaceSound(),
                    type.getHitSound(), type.getFallSound());
        }

    }

}
