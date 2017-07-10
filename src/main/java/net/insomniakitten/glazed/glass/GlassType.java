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
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum GlassType implements IStringSerializable {

    FROSTED(0.3f, 1.5f, 0, SoundType.GLASS, false),
    RADIANT(0.3f, 1.5f, 10, SoundType.GLASS, false),
    IRIDESCENT(0.3f, 1.5f, 8, SoundType.GLASS, false),
    ENERGETIC(0.3f, 1.5f, 0, SoundType.GLASS, false),
    SHADOWED(0.3f, 10.0f, 0, SoundType.GLASS, true),
    VOIDED(0.3f, 10.0f, 5, SoundType.GLASS, false),
    REINFORCED(2.0f, 3000.0f, 0, SoundType.METAL, false),
    AURORIC(0.3f, 10.0f, 8, SoundType.GLASS, false);

    private final float HARDNESS;
    private final float RESISTANCE;
    private final int LIGHT_LEVEL;
    private final SoundType SOUND;
    private final boolean IS_OPAQUE;

    GlassType(float hardness, float resistance, int lightLevel, SoundType sound, boolean isOpaque) {
        HARDNESS = hardness;
        RESISTANCE = resistance;
        LIGHT_LEVEL = lightLevel;
        SOUND = sound;
        IS_OPAQUE = isOpaque;
    }

    public String getName() { return name().toLowerCase(Locale.ENGLISH); }
    public int getMetadata() { return ordinal(); }

    public float getHardness() { return HARDNESS; }
    public float getResistance() { return RESISTANCE; }
    public int getLightLevel() { return LIGHT_LEVEL; }
    public SoundType getSound() { return SOUND; }
    public boolean isOpaque() { return IS_OPAQUE; }

}
