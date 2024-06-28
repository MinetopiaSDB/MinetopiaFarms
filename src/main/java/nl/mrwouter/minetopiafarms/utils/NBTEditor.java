package nl.mrwouter.minetopiafarms.utils;

import com.saicone.rtag.RtagItem;
import org.bukkit.inventory.ItemStack;

public class NBTEditor {

    public static ItemStack set(ItemStack object, Object value, Object... keys) {
        RtagItem tag = new RtagItem(object);
        tag.set(value, keys);
        return tag.loadCopy();
    }

    public static boolean contains(ItemStack object, Object... keys) {
        RtagItem tag = new RtagItem(object);
        return !tag.getOptional(keys).isEmpty();
    }

    public static String getString(ItemStack object, Object... keys) {
        RtagItem tag = new RtagItem(object);
        return tag.get(keys);
    }
}