package com.setycz.chickens.registry;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by setyc on 14.02.2016.
 */
public final class LiquidEggRegistry {
    private static final Map<ResourceLocation, LiquidEggRegistryItem> ITEMS = new HashMap<ResourceLocation, LiquidEggRegistryItem>();
    private static final Map<String, LiquidEggRegistryItem> STRING_TO_ITEM = new HashMap<String, LiquidEggRegistryItem>();

    public static void register(LiquidEggRegistryItem liquidEgg) {
        ITEMS.put(liquidEgg.getResourceLocation(), liquidEgg);
        STRING_TO_ITEM.put(liquidEgg.getRegistryName(), liquidEgg);
    }

    public static Collection<LiquidEggRegistryItem> getAll() {
        return ITEMS.values();
    }

    @Nullable
    public static LiquidEggRegistryItem getByResourceLocation(ResourceLocation type) {
        LiquidEggRegistryItem liquidEgg = ITEMS.get(type);
        return liquidEgg != null ? ITEMS.get(type) : getByRegistryName(type.toString());
    }

    @Nullable
    public static LiquidEggRegistryItem getByRegistryName(String type)
    {
        return STRING_TO_ITEM.get(type);
    }
}
