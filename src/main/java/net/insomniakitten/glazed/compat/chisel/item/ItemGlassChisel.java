package net.insomniakitten.glazed.compat.chisel.item;

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

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.insomniakitten.glazed.Glazed;
import net.insomniakitten.glazed.client.model.ICustomMeshDefinition;
import net.insomniakitten.glazed.client.model.WrappedModel;
import net.insomniakitten.glazed.compat.chisel.ChiselHelper;
import net.insomniakitten.glazed.item.ItemBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import team.chisel.api.IChiselGuiType;
import team.chisel.api.IChiselItem;
import team.chisel.api.carving.ICarvingVariation;
import team.chisel.api.carving.IChiselMode;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import static com.google.common.base.CaseFormat.UPPER_CAMEL;
import static com.google.common.base.CaseFormat.UPPER_UNDERSCORE;

public final class ItemGlassChisel extends ItemBase implements IChiselItem, ICustomMeshDefinition {

    private final ResourceLocation modelPath = new ResourceLocation(Glazed.ID, "item_chisel");

    private final int maxDurability;
    private final float maxDamage;

    public ItemGlassChisel() {
        super("chisel");
        setMaxStackSize(1);
        maxDurability = ChiselHelper.getConfig().glassChiselDurability;
        maxDamage = ChiselHelper.getConfig().glassChiselDamage;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        return 0F;
    }

    @Override
    public boolean isDamageable() {
        return ChiselHelper.isGlassChiselDamageable();
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        stack.damageItem(1, attacker);
        return false;
    }

    @Override
    public boolean canHarvestBlock(IBlockState block) {
        return false;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        String unlocalizedName = super.getUnlocalizedName(stack);
        //noinspection ConstantConditions
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("variant")) {
            unlocalizedName += "." + stack.getTagCompound().getString("variant");
        }
        return unlocalizedName;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            for (Variants variant : Variants.values()) {
                ItemStack stack = new ItemStack(this);
                stack.setTagInfo("variant", new NBTTagString(variant.getName()));
                items.add(stack);
            }
        }
    }

    @Override
    public boolean getIsRepairable(ItemStack chisel, ItemStack repair) {
        int idGlass = OreDictionary.getOreID("blockGlass");
        for (int id : OreDictionary.getOreIDs(repair)) {
            if (id == idGlass) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = HashMultimap.create();
        if (slot == EntityEquipmentSlot.MAINHAND) {
            AttributeModifier modifier = new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Chisel Damage", maxDamage, 0);
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), modifier);
        }
        return multimap;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return ChiselHelper.isGlassChiselDamageable() ? maxDurability : 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        String key = getUnlocalizedName() + ".tooltip";
        if (I18n.hasKey(key)) tooltip.add(I18n.format(key));
    }

    @Override
    public void addModels(Set<WrappedModel> models) {
        // Models handled in ICustomMeshDefinition
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelResourceLocation getMeshDefinition(ItemStack stack) {
        String variant = "null";
        //noinspection ConstantConditions
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("variant")) {
            variant = "variant=" + stack.getTagCompound().getString("variant");
        }
        return new ModelResourceLocation(modelPath, variant);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addModelVariants(Set<ModelResourceLocation> variants) {
        for (Variants variant : Variants.values()) {
            variants.add(new ModelResourceLocation(modelPath, "variant=" + variant.getName()));
        }
    }

    @Override
    public boolean canOpenGui(World world, EntityPlayer player, EnumHand hand) {
        return false;
    }

    @Override
    public IChiselGuiType getGuiType(World world, EntityPlayer player, EnumHand hand) {
        return IChiselGuiType.ChiselGuiType.NORMAL;
    }

    @Override
    public boolean onChisel(World world, EntityPlayer player, ItemStack chisel, ICarvingVariation target) {
        return true;
    }

    @Override
    public boolean canChisel(World world, EntityPlayer player, ItemStack chisel, ICarvingVariation target) {
        return true;
    }

    @Override
    public boolean canChiselBlock(World world, EntityPlayer player, EnumHand hand, BlockPos pos, IBlockState state) {
        return world.getBlockState(pos).getMaterial() == Material.GLASS;
    }

    @Override
    public boolean supportsMode(EntityPlayer player, ItemStack chisel, IChiselMode mode) {
        return false;
    }

    public enum Variants {

        CLEAR,
        WHITE,
        ORANGE,
        MAGENTA,
        LIGHT_BLUE,
        YELLOW,
        LIME,
        PINK,
        GRAY,
        SILVER,
        CYAN,
        PURPLE,
        BLUE,
        BROWN,
        GREEN,
        RED,
        BLACK;

        public String getName() {
            return name().toLowerCase(Locale.ROOT);
        }

        public String getOreName() {
            return "chisel" + UPPER_UNDERSCORE.to(UPPER_CAMEL, name());
        }

    }

}
