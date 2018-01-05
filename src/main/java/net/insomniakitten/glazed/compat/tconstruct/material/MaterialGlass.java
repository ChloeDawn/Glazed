package net.insomniakitten.glazed.compat.tconstruct.material;

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

import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.utils.HarvestLevels;

public final class MaterialGlass extends Material {

    private final String unlocName = "tconstruct.glazed.material.glass.name";
    private final HeadMaterialStats headMaterialStats = new HeadMaterialStats(25, 2.00F, 2.00F, HarvestLevels.STONE);
    private final HandleMaterialStats handleMaterialStats = new HandleMaterialStats(0.90F, 0);

    public MaterialGlass() {
        super("glass", 0xC0F5FE);
    }

    @Override
    public String getLocalizedName() {
        return Util.translate(unlocName, getIdentifier());
    }

    public HeadMaterialStats getHeadStats() {
        return headMaterialStats;
    }

    public HandleMaterialStats getHandleStats() {
        return handleMaterialStats;
    }

}
