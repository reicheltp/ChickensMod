package com.setycz.chickens.item;

import com.setycz.chickens.ChickensMod;
import com.setycz.chickens.entity.EntityChickensChicken;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

/**
 * Created by setyc on 21.12.2016.
 */
public class ItemAnalyzer extends Item {
    public ItemAnalyzer() {
        super(new Properties()
                .group(ChickensMod.chickensTab)
                .maxStackSize(1)
                .maxDamage(238)
        );
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent("item.analyzer.tooltip1"));
        tooltip.add(new TranslationTextComponent("item.analyzer.tooltip2"));
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
        if (target.world.isRemote || !(target instanceof EntityChickensChicken)) {
            return false;
        }

        EntityChickensChicken chicken = (EntityChickensChicken) target;
        chicken.setStatsAnalyzed(true);

        StringTextComponent chickenName = new StringTextComponent(chicken.getName());
        chickenName.getStyle().setBold(true).setColor(TextFormatting.GOLD);
        playerIn.sendMessage(chickenName);

        playerIn.sendMessage(new TranslationTextComponent("entity.ChickensChicken.tier", chicken.getTier()));

        playerIn.sendMessage(new TranslationTextComponent("entity.ChickensChicken.growth", chicken.getGrowth()));
        playerIn.sendMessage(new TranslationTextComponent("entity.ChickensChicken.gain", chicken.getGain()));
        playerIn.sendMessage(new TranslationTextComponent("entity.ChickensChicken.strength", chicken.getStrength()));

        if (!chicken.isChild()) {
            int layProgress = chicken.getLayProgress();
            if (layProgress <= 0) {
                playerIn.sendMessage(new TranslationTextComponent("entity.ChickensChicken.nextEggSoon"));
            } else {
                playerIn.sendMessage(new TranslationTextComponent("entity.ChickensChicken.layProgress", layProgress));
            }
        }

        stack.damageItem(1, target);
        return true;
    }
}
