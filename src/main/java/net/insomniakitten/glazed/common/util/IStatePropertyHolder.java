package net.insomniakitten.glazed.common.util;

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

import com.google.common.base.CaseFormat;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public interface IStatePropertyHolder <E extends Enum<E> & IStringSerializable> extends IStringSerializable {

    E getEnum();

    float getHardness();

    float getResistance();

    Material getMaterial();

    SoundType getSoundType();

    BlockRenderLayer getRenderLayer();

    String getOrePrefix();

    default int getLightLevel() {
        return 0;
    }

    default String getEffectiveTool() {
        return null;
    }

    default String getName() {
        return this.getEnum().name().toLowerCase(Locale.ROOT);
    }

    default int getMetadata() {
        return this.getEnum().ordinal();
    }

    default String getOreDict() {
        return this.getOrePrefix() + CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, this.getName());
    }

    default boolean requiresSilkTouch() {
        return false;
    }

    default boolean isFullCube() {
        return true;
    }

}
