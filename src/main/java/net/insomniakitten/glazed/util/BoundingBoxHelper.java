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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.Arrays;
import java.util.function.Function;

public final class BoundingBoxHelper {

    private BoundingBoxHelper() {}

    public static ImmutableMap<EnumFacing, AxisAlignedBB> computeAABBsForFacing(AxisAlignedBB axisAlignedBB) {
        double minX = axisAlignedBB.minX, minY = axisAlignedBB.minY, minZ = axisAlignedBB.minZ;
        double maxX = axisAlignedBB.maxX, maxY = axisAlignedBB.maxY, maxZ = axisAlignedBB.maxZ;
        return Arrays.stream(EnumFacing.HORIZONTALS).collect(ImmutableMap.toImmutableMap(Function.identity(), facing -> {
            if (facing != EnumFacing.NORTH) switch (facing) {
                case SOUTH:
                    return new AxisAlignedBB(1.0D - maxX, minY, 1.0D - maxZ, 1.0D - minX, maxY, 1.0D - minZ);
                case WEST:
                    return new AxisAlignedBB(minZ, minY, 1.0D - minX, maxZ, maxY, 1.0D - maxX);
                case EAST:
                    return new AxisAlignedBB(1.0D - maxZ, minY, maxX, 1.0D - minZ, maxY, minX);
            }
            return axisAlignedBB;
        }));
    }

    public static ImmutableMap<EnumFacing, AxisAlignedBB> computeAABBsForFacing(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return computeAABBsForFacing(new AxisAlignedBB(minX / 16.0, minY / 16.0, minZ / 16.0, maxX / 16.0, maxY / 16.0, maxZ / 16.0));
    }

    public static ImmutableMap<EnumFacing, AxisAlignedBB> computeAABBsForFacing(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        return computeAABBsForFacing((double) minX, (double) minY, (double) minZ, (double) maxX, (double) maxY, (double) maxZ);
    }

}
