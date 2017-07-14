package net.insomniakitten.glazed.glass;

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
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum GlassType implements IStringSerializable {

    GAIA(0.3f, 1.5f, 0, SoundType.GLASS, BlockRenderLayer.TRANSLUCENT, false, false, false),
    RADIANT(0.3f, 1.5f, 15, SoundType.GLASS, BlockRenderLayer.TRANSLUCENT, false, false, false),
    IRIDESCENT(0.3f, 1.5f, 8, SoundType.GLASS, BlockRenderLayer.CUTOUT, false, false, false),
    ENERGETIC(0.3f, 1.5f, 0, SoundType.GLASS, BlockRenderLayer.TRANSLUCENT, false, true, false),
    SHADOWED(0.3f, 10.0f, 0, SoundType.GLASS, BlockRenderLayer.TRANSLUCENT, true, false, false),
    VOIDIC(0.3f, 10.0f, 8, SoundType.GLASS, BlockRenderLayer.TRANSLUCENT, false, false, false),
    QUILTED(0.3f, 10.0f, 0, SoundType.CLOTH, BlockRenderLayer.CUTOUT, false, false, true),
    REINFORCED(2.0f, 3000.0f, 0, SoundType.METAL, BlockRenderLayer.CUTOUT, false, false, true),
    AURORIC(0.3f, 10.0f, 8, SoundType.GLASS, BlockRenderLayer.TRANSLUCENT, false, false, false);

    private final float hardness;
    private final float resistance;
    private final int lightLevel;
    private final SoundType soundType;
    private final BlockRenderLayer layer;
    private final boolean isOpaque;
    private final boolean isPowered;
    private final boolean dropsItem;

    GlassType(
            float hardness,
            float resistance,
            int lightLevel,
            SoundType soundType,
            BlockRenderLayer layer,
            boolean isOpaque,
            boolean isPowered,
            boolean dropsItem) {
        this.hardness = hardness;
        this.resistance = resistance;
        this.lightLevel = lightLevel;
        this.soundType = soundType;
        this.layer = layer;
        this.isOpaque = isOpaque;
        this.isPowered = isPowered;
        this.dropsItem = dropsItem;
    }

    private static final PropertyEnum<GlassType> PROPERTY = PropertyEnum.create("type", GlassType.class);
    public static PropertyEnum<GlassType> getProperty() { return PROPERTY; }

    public static GlassType getType(IBlockState state) { return state.getValue(PROPERTY); }
    public static GlassType getType(int meta) {
        if (meta >= values().length || meta < 0) return values()[0];
        return values()[meta];
    }

    public String getName() { return name().toLowerCase(Locale.ENGLISH); }
    public int getMetadata() { return ordinal(); }

    public float getHardness() { return hardness; }
    public float getResistance() { return resistance; }
    public int getLightLevel() { return lightLevel; }
    public SoundType getSoundType() { return soundType; }
    public BlockRenderLayer getRenderLayer() { return layer; }
    public boolean isOpaque() { return isOpaque; }
    public boolean isPowered() { return isPowered; }
    public boolean dropsItem() { return dropsItem; }
    public boolean isHeavy() { return resistance >= 3000; }

}
