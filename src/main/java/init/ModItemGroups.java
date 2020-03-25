package init;

import com.setycz.chickens.itemgroup.ChickensItemGroup;
import net.minecraft.item.ItemGroup;

public class ModItemGroups {
    public static final ItemGroup DEFAULT = new itemgroups.ChickensItemGroup("default", () -> ModItems.ITEMSPAWNEGG);

    public static final ItemGroup CHICKENS_TAB = new ChickensItemGroup();
}
