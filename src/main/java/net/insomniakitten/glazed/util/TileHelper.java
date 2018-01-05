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

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import net.insomniakitten.glazed.Glazed;
import net.insomniakitten.glazed.tile.TileEntityBase;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.function.Supplier;

public final class TileHelper {

    private TileHelper() {}

    public static void registerTileEntity(Class<? extends TileEntityBase> tile) {
        Converter<String, String> toKey = CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.LOWER_UNDERSCORE);
        GameRegistry.registerTileEntity(tile, Glazed.ID + ":" + toKey.convert(tile.getSimpleName()));
    }

    public static <T extends TileEntityBase> void bindTESR(Supplier<TileEntitySpecialRenderer<T>> tesrSupplier, Class<T> tileClass) {
        Glazed.proxy.bindTESR(tesrSupplier, tileClass);
    }

}
