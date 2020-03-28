package com.setycz.chickens.client.render;

import com.setycz.chickens.entity.EntityChickensChicken;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Created by setyc on 12.02.2016.
 */
@OnlyIn(Dist.CLIENT)
public class RenderChickensChicken extends MobRenderer<EntityChickensChicken> {

    public RenderChickensChicken(EntityRendererManager renderManagerIn, ModelBase modelBaseIn) {
        super(renderManagerIn, modelBaseIn, 0.3F);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityChickensChicken entity) {
        return entity.getTexture();
    }

    @Override
    protected float handleRotationFloat(EntityChickensChicken livingBase, float partialTicks) {
        float f = livingBase.oFlap + (livingBase.wingRotation - livingBase.oFlap) * partialTicks;
        float f1 = livingBase.oFlapSpeed + (livingBase.destPos - livingBase.oFlapSpeed) * partialTicks;
        return (MathHelper.sin(f) + 1.0F) * f1;
    }
}
