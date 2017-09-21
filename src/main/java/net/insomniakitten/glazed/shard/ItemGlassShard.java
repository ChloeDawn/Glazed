package net.insomniakitten.glazed.shard;

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

import com.google.common.base.Equivalence.Wrapper;
import net.insomniakitten.glazed.Glazed;
import net.insomniakitten.glazed.client.model.ModelRegistry;
import net.insomniakitten.glazed.client.model.WrappedModel.ModelBuilder;
import net.insomniakitten.glazed.common.ConfigManager;
import net.insomniakitten.glazed.shard.ShardManager.Data;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mojang.realmsclient.gui.ChatFormatting.DARK_GRAY;
import static com.mojang.realmsclient.gui.ChatFormatting.GRAY;
import static com.mojang.realmsclient.gui.ChatFormatting.RED;
import static com.mojang.realmsclient.gui.ChatFormatting.YELLOW;

public class ItemGlassShard extends Item {

    public static final CreativeTabs CTAB_SHARDS = new CreativeTabs(Glazed.MOD_ID + ".shards") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(Glazed.GLASS_SHARD.get());
        }
    };

    public ItemGlassShard() {
        setRegistryName("shard");
        setUnlocalizedName(Glazed.MOD_ID + ".shard");
        setCreativeTab(ItemGlassShard.CTAB_SHARDS);
        setHasSubtypes(true);
        ModelRegistry.registerModel(new ModelBuilder(this).build());
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("ConstantConditions")
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        ItemStack glassBlock = Data.getGlassFromShard(stack);

        if (glassBlock.isEmpty()) {
            tooltip.add(RED + I18n.format("tooltip.glazed.shard.invalid"));
            String modid = stack.getTagCompound().getString("name").split(":")[ 0 ];
            if (!Loader.isModLoaded(modid)) {
                tooltip.add(I18n.format("tooltip.glazed.shard.invalid.mod", modid));
            } else if (!ConfigManager.shardsConfig.anarchyMode) {
                tooltip.add(I18n.format("tooltip.glazed.shard.invalid.config"));
            }
            return;
        }

        tooltip.add(glassBlock.getDisplayName());

        if (canShowDescription()) {
            String descKey = "tooltip.glazed.shard.desc";
            getStackTooltip(glassBlock, world, flag).stream().map(string -> DARK_GRAY + I18n.format(descKey, string))
                    .forEach(tooltip::add);
            String modKey = "tooltip.glazed.shard.mod";
            String domain = glassBlock.getItem().getRegistryName().getResourceDomain();
            Optional<ModContainer> container = Loader.instance().getActiveModList().stream()
                    .filter(containerPredicate -> containerPredicate.getModId().equals(domain)).findFirst();
            container.ifPresent(mod -> tooltip.add(YELLOW + I18n.format(modKey, GRAY + mod.getName())));
        }
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            for (Wrapper<ItemStack> key : Data.getShards().keySet()) {
                items.add(key.get());
            }
        }
    }

    protected boolean canShowDescription() {
        return !ConfigManager.shardsConfig.requireShift || ConfigManager.shardsConfig.requireShift &&
                (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT));
    }

    @SideOnly(Side.CLIENT)
    protected List<String> getStackTooltip(ItemStack stack, World world, ITooltipFlag flag) {
        List<String> list = new ArrayList<>();
        stack.getItem().addInformation(stack, world, list, flag);
        return list;
    }

}
