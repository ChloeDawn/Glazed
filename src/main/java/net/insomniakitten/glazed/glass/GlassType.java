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

    private final float hardness;
    private final float resistance;
    private final int lightLevel;
    private final SoundType soundType;
    private final boolean isOpaque;

    GlassType(float hardness, float resistance, int lightLevel, SoundType soundType, boolean isOpaque) {
        this.hardness = hardness;
        this.resistance = resistance;
        this.lightLevel = lightLevel;
        this.soundType = soundType;
        this.isOpaque = isOpaque;
    }

    public String getName() { return name().toLowerCase(Locale.ENGLISH); }
    public int getMetadata() { return ordinal(); }

    public float getHardness() { return hardness; }
    public float getResistance() { return resistance; }
    public int getLightLevel() { return lightLevel; }
    public SoundType getSound() { return soundType; }
    public boolean isOpaque() { return isOpaque; }

}
