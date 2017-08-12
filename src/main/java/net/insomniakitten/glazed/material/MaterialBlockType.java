package net.insomniakitten.glazed.material;

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

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum MaterialBlockType implements IStringSerializable {

    BRICKS(2.0f, 30f, 0, Material.ROCK, SoundType.STONE, BlockRenderLayer.SOLID, true),
    SLAG(0.5f, 2.5f, 0, Material.GROUND, SoundType.SLIME, BlockRenderLayer.SOLID, false),
    CRYSTAL(1.5f, 30f, 8, Material.GLASS, SoundType.GLASS, BlockRenderLayer.TRANSLUCENT, false);

    private final float hardness;
    private final float resistance;
    private final int lightLevel;
    private final Material material;
    private final SoundType soundType;
    private final BlockRenderLayer layer;
    private final boolean isSolid;

    MaterialBlockType(
            float hardness,
            float resistance,
            int lightLevel,
            Material material,
            SoundType soundType,
            BlockRenderLayer layer,
            boolean isSolid) {
        this.hardness = hardness;
        this.resistance = resistance;
        this.lightLevel = lightLevel;
        this.material = material;
        this.soundType = soundType;
        this.layer = layer;
        this.isSolid = isSolid;
    }

    private static final PropertyEnum<MaterialBlockType> PROPERTY = PropertyEnum.create("type", MaterialBlockType.class);

    public static PropertyEnum<MaterialBlockType> getProperty() {
        return PROPERTY;
    }

    public static MaterialBlockType getType(IBlockState state) {
        return state.getValue(PROPERTY);
    }

    public static MaterialBlockType getType(int meta) {
        if (meta >= values().length || meta < 0) return values()[0];
        return values()[meta];
    }

    public String getName() {
        return name().toLowerCase(Locale.ENGLISH);
    }

    public int getMetadata() {
        return ordinal();
    }

    public float getHardness() {
        return hardness;
    }

    public float getResistance() {
        return resistance;
    }

    public int getLightLevel() {
        return lightLevel;
    }

    public Material getMaterial() {
        return material;
    }

    public SoundType getSoundType() {
        return soundType;
    }

    public BlockRenderLayer getRenderLayer() {
        return layer;
    }

    public boolean isSolid() {
        return isSolid;
    }

}
