package init;

import itemgroups.ChickensItemGroup;
import net.minecraft.item.ItemGroup;

public class ModItemGroups {
    public static final ItemGroup Default = new ChickensItemGroup("default", () -> ModItems.ITEMSPAWNEGG);
}
