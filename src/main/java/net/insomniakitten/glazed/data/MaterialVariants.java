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
import net.minecraft.util.BlockRenderLayer;

import java.util.stream.Stream;

public enum MaterialVariants implements IPropertyProvider<MaterialVariants> {

    KILN_BRICKS("bricksKiln"),

/*    COMPRESSED_SLIME(Material.SPONGE, SoundType.SLIME, BlockRenderLayer.TRANSLUCENT, "blockSlimeCompressed") {
        @Override
        public void onCollidedWithBlock(Entity entity, World world, BlockPos pos) {
            if (!entity.isSneaking() && entity.motionY < 0.0D) {
                entity.motionY = -entity.motionY;
                if (!(entity instanceof EntityLivingBase)) {
                    entity.motionY *= 0.8D;
                }
            }
        }
    },

    OBSIDIAN_SLAG(Material.SPONGE, SoundType.SLIME, "blockObsidianSlag") {
        @Override
        public boolean canCollideWithBlock(Entity entity, World world, BlockPos pos) {
            return false;
        }

        @Override
        public void onCollidedWithBlock(Entity entity, World world, BlockPos pos) {
            if (entity.motionY < 0.0D) {
                entity.motionY *= -0.5D;
            }
            entity.motionX *= 0.5;
            entity.motionZ *= 0.5;
        }

        @Override
        public boolean isFullCube() {
            return false;
        }
    }*/;

    private final Material material;
    private final SoundType soundType;
    private final BlockRenderLayer layer;
    private final String oreDict;

    MaterialVariants(Material material, SoundType soundType, BlockRenderLayer layer, String oreDict) {
        this.material = material;
        this.soundType = soundType;
        this.layer = layer;
        this.oreDict = oreDict;
    }

    MaterialVariants(Material material, SoundType sound, String oreDict) {
        this(material, sound, BlockRenderLayer.SOLID, oreDict);
    }

    MaterialVariants(String oreDict) {
        this(Material.ROCK, SoundType.STONE, oreDict);
    }

    public static Stream<MaterialVariants> stream() {
        return Stream.of(values());
    }

    @Override
    public MaterialVariants getProvider() {
        return this;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public SoundType getSoundType() {
        return soundType;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return layer;
    }

    @Override
    public String getOrePrefix() {
        return null; // No parent data, all constants in this enum are unique
    }

    @Override
    public String getOreDict() {
        return oreDict;
    }

    @Override
    public boolean isFullCube() {
        return true;
    }

}
