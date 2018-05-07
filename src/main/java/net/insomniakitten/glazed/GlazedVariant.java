package net.insomniakitten.glazed;

/*
 *  Copyright 2018 InsomniaKitten
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
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Locale;

import static net.minecraft.util.BlockRenderLayer.CUTOUT;
import static net.minecraft.util.BlockRenderLayer.TRANSLUCENT;

public enum GlazedVariant implements IStringSerializable {
    GAIA(TRANSLUCENT, 0.3F, 1.5F) {
        @Override
        protected int getColor(IBlockAccess access, BlockPos pos) {
            return access.getBiome(pos).getFoliageColorAtPos(pos);
        }
    },
    RADIANT(TRANSLUCENT, 0.3F, 1.5F, 15),
    IRIDESCENT(CUTOUT, 0.3F, 1.5F, 8),
    ENERGETIC(TRANSLUCENT, 0.3F, 1.5F),
    SHADOWED(TRANSLUCENT, 0.3F, 1.5F),
    VOIDIC(TRANSLUCENT, 0.3F, 1.5F),
    QUILTED(CUTOUT, 0.3F, 1.5F) {
        private final SoundType soundTypeQuilted = new SoundType(
                1.0F, 1.0F,
                SoundEvents.BLOCK_GLASS_BREAK,
                SoundEvents.BLOCK_CLOTH_STEP,
                SoundEvents.BLOCK_CLOTH_PLACE,
                SoundEvents.BLOCK_CLOTH_HIT,
                SoundEvents.BLOCK_CLOTH_FALL
        );

        @Override
        public SoundType getSoundType() {
            return soundTypeQuilted;
        }
    },
    REINFORCED(CUTOUT, 0.3F, 1.5F) {
        @Override
        public SoundType getSoundType() {
            return SoundType.METAL;
        }
    },
    SLIMY(TRANSLUCENT, 0.3F, 1.5F) {
        private final SoundType soundTypeSlimy = new SoundType(
                1.0F, 1.0F,
                SoundEvents.BLOCK_GLASS_BREAK,
                SoundEvents.BLOCK_SLIME_STEP,
                SoundEvents.BLOCK_GLASS_PLACE,
                SoundEvents.BLOCK_SLIME_HIT,
                SoundEvents.BLOCK_SLIME_FALL
        );

        @Override
        public SoundType getSoundType() {
            return soundTypeSlimy;
        }
    },
    AURORIC(TRANSLUCENT, 0.3F, 1.5F, 10);

    public static final GlazedVariant[] VARIANTS = values();

    public static final PropertyEnum<GlazedVariant> PROPERTY = PropertyEnum.create(
            "variant", GlazedVariant.class
    );

    private final BlockRenderLayer layer;
    private final float hardness;
    private final float resistance;
    private final int lightLevel;

    GlazedVariant(BlockRenderLayer layer, float hardness, float resistance, int lightLevel) {
        this.layer = layer;
        this.hardness = hardness;
        this.resistance = resistance;
        this.lightLevel = lightLevel;
    }

    GlazedVariant(BlockRenderLayer layer, float hardness, float resistance) {
        this(layer, hardness, resistance, 0);
    }

    public static boolean isValid(int ordinal) {
        return ordinal >= 0 && ordinal < VARIANTS.length;
    }

    public static String getName(ItemStack stack) {
        final int meta = stack.getMetadata();
        return isValid(meta) ? VARIANTS[meta].getName() : "unknown";
    }

    @SideOnly(Side.CLIENT)
    public static String getDescription(ItemStack stack) {
        return I18n.format("tooltip.glazed.variant." + getName(stack));
    }

    public BlockRenderLayer getRenderLayer() {
        return layer;
    }

    public float getHardness() {
        return hardness;
    }

    public float getResistance() {
        return resistance;
    }

    public int getLightLevel() {
        return lightLevel;
    }

    public SoundType getSoundType() {
        return SoundType.GLASS;
    }

    protected int getColor(IBlockAccess access, BlockPos pos) {
        return 0xFFFFFFFF;
    }

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }

    public void onEntityWalk(IBlockAccess access, BlockPos pos, Entity entity) {

    }

    public void onEntityCollide(IBlockAccess access, BlockPos pos, Entity entity) {

    }

    public boolean onPlayerInteract(IBlockAccess access, BlockPos pos, EntityPlayer player, EnumFacing side) {
        return false;
    }
}
