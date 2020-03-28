package com.setycz.chickens.client.model;

import net.minecraft.client.renderer.entity.model.ChickenModel;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Created by setyc on 12.02.2016.
 */
@OnlyIn(Dist.CLIENT)
public class ModelChickensChicken<T extends Entity> extends ChickenModel<T> {
}
