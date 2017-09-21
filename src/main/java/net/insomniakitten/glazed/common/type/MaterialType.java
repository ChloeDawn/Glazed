package net.insomniakitten.glazed.common.type;

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
import net.insomniakitten.glazed.common.util.IOverlayProvider;
import net.insomniakitten.glazed.common.util.IStateEventHolder;
import net.insomniakitten.glazed.common.util.IStatePropertyHolder;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public enum MaterialType implements IStatePropertyHolder<MaterialType>, IStateEventHolder<MaterialType>, IOverlayProvider {

    KILN_BRICKS(2.0f, 30.0f, "bricksKiln"),

    COMPRESSED_SLIME(2.0f, 0.0f, Material.SPONGE, SoundType.SLIME, BlockRenderLayer.TRANSLUCENT,
            "blockSlimeCompressed") {
        @Override
        public void onCollidedWithBlock(Entity entity, World world, BlockPos pos) {
            if (entity.motionY < 0.0D) {
                entity.motionY = -entity.motionY;
                if (!(entity instanceof EntityLivingBase)) {
                    entity.motionY *= 0.8D;
                }
            }
        }
    },

    OBSIDIAN_SLAG(2.0f, 30.0f, Material.SPONGE, SoundType.SLIME, "blockObsidianSlag") {
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
        public ResourceLocation getOverlayTexture(IBlockState state) {
            return new ResourceLocation(Glazed.MOD_ID, "textures/overlay/slag.png");
        }

        @Override
        public boolean isFullCube() {
            return false;
        }
    },

    VOID_CRYSTAL(2.0f, 30.0f, Material.GLASS, SoundType.GLASS, "blockCrystalVoid"),;

    private final float hardness;
    private final float resistance;
    private final Material material;
    private final SoundType soundType;
    private final BlockRenderLayer layer;
    private final String oreDict;

    MaterialType(
            float hardness, float resistance, Material material, SoundType soundType, BlockRenderLayer layer,
            String oreDict) {
        this.hardness = hardness;
        this.resistance = resistance;
        this.material = material;
        this.soundType = soundType;
        this.layer = layer;
        this.oreDict = oreDict;
    }

    MaterialType(float hardness, float resistance, Material material, SoundType sound, String oreDict) {
        this(hardness, resistance, material, sound, BlockRenderLayer.SOLID, oreDict);
    }

    MaterialType(float hardness, float resistance, String oreDict) {
        this(hardness, resistance, Material.ROCK, SoundType.STONE, oreDict);
    }

    @Override
    public MaterialType getEnum() {
        return this;
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
        return null; // No parent type, all constants in this enum are unique
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
