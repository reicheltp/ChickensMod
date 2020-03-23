package itemgroups;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import java.util.function.Supplier;


public class ChickensItemGroup extends ItemGroup {
    private Supplier<Item> _supplier;

    public ChickensItemGroup(String label, Supplier<Item> supplier){
        super(label);
        _supplier = supplier;
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(_supplier.get());
    }
}
