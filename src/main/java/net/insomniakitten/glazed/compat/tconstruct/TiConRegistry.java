package net.insomniakitten.glazed.compat.tconstruct;

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

import net.insomniakitten.glazed.compat.tconstruct.material.MaterialGlass;
import net.insomniakitten.glazed.compat.tconstruct.trait.TraitFragile;
import net.insomniakitten.glazed.compat.tconstruct.trait.TraitRazorSharp;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.shared.TinkerFluids;

public final class TiConRegistry {

    private static final MaterialGlass GLASS = new MaterialGlass();

    private TiConRegistry() {}

    public static void onPreInit(FMLPreInitializationEvent event) {
        TinkerRegistry.addMaterialStats(GLASS, GLASS.getHeadStats(), GLASS.getHandleStats());
        GLASS.setFluid(TinkerFluids.glass);
        GLASS.addTrait(new TraitFragile());
        GLASS.addTrait(new TraitRazorSharp());
        GLASS.setCastable(true);
        TinkerRegistry.integrate(GLASS, "blockGlass").preInit();
    }

    @SideOnly(Side.CLIENT)
    public static void onClientInit(FMLInitializationEvent event) {
        GLASS.setRenderInfo(0xC0F5FE);
    }

}
