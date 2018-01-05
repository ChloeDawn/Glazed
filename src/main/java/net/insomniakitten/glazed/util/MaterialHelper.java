package net.insomniakitten.glazed.util;

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

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public final class MaterialHelper {

    private static final MaterialData DEFAULT = new MaterialData(SoundType.STONE, 00.00F, 00.00F);

    private static final ImmutableMap<Material, MaterialData> MATERIAL_DATA = ImmutableMap.<Material, MaterialData>
            builder()
            .put(Material.GROUND, new MaterialData(SoundType.GROUND, 00.50F, 02.50F))
            .put(Material.WOOD, new MaterialData(SoundType.WOOD, 02.00F, 15.00F))
            .put(Material.ROCK, new MaterialData(SoundType.STONE, 01.50F, 30.00F))
            .put(Material.IRON, new MaterialData(SoundType.METAL, 05.00F, 30.00F))
            .put(Material.LEAVES, new MaterialData(SoundType.PLANT, 00.20F, 01.00F))
            .put(Material.PLANTS, new MaterialData(SoundType.PLANT, 00.00F, 00.00F))
            .put(Material.VINE, new MaterialData(SoundType.PLANT, 00.20F, 01.00F))
            .put(Material.CLOTH, new MaterialData(SoundType.CLOTH, 00.80F, 04.00F))
            .put(Material.SAND, new MaterialData(SoundType.SAND, 00.50F, 02.50F))
            .put(Material.CIRCUITS, new MaterialData(SoundType.STONE, 00.00F, 00.00F))
            .put(Material.CARPET, new MaterialData(SoundType.CLOTH, 00.10F, 00.50F))
            .put(Material.GLASS, new MaterialData(SoundType.GLASS, 00.30F, 01.50F))
            .put(Material.ICE, new MaterialData(SoundType.GLASS, 00.50F, 02.50F))
            .put(Material.SPONGE, new MaterialData(SoundType.PLANT, 00.60F, 03.00F))
            .build();

    private MaterialHelper() {}

    public static float getHardness(Material material) {
        return MATERIAL_DATA.getOrDefault(material, DEFAULT).hardness;
    }

    public static float getResistance(Material material) {
        return MATERIAL_DATA.getOrDefault(material, DEFAULT).resistance;
    }

    public static SoundType getSoundType(Material material) {
        return MATERIAL_DATA.getOrDefault(material, DEFAULT).sound;
    }

    private static final class MaterialData {

        private final SoundType sound;
        private final float hardness;
        private final float resistance;

        private MaterialData(SoundType sound, float hardness, float resistance) {
            this.sound = sound;
            this.hardness = hardness;
            this.resistance = resistance;
        }

    }

}
