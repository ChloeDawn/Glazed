package net.insomniakitten.glazed.compat.chisel;

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

import net.insomniakitten.glazed.GlazedConfig;
import net.minecraftforge.fml.common.Loader;
import team.chisel.common.config.Configurations;

public final class ChiselHelper {

    private ChiselHelper() {}

    public static boolean isChiselLoaded() {
        return Loader.isModLoaded("chisel");
    }

    public static boolean isGlassChiselEnabled() {
        return isChiselLoaded() && getConfig().enableGlassChisel;
    }

    public static boolean isGlassChiselDamageable() {
        int durability = getConfig().glassChiselDurability;
        return Configurations.allowChiselDamage && durability > 0;
    }

    public static GlazedConfig.Compat.Chisel getConfig() {
        return GlazedConfig.Compat.CHISEL;
    }

}
