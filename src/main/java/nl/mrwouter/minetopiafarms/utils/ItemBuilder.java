package nl.mrwouter.minetopiafarms.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import nl.mrwouter.minetopiafarms.utils.NBTEditor;

/**
 * Easily create itemstacks, without messing your hands. <i>Note that if you do
 * use this in one of your projects, leave this notice.</i> <i>Please do credit
 * me if you do use this in one of your projects.</i>
 * 
 * @author NonameSL
 */

public class ItemBuilder {
	private ItemStack is;

	/**
	 * Create a new ItemBuilder from scratch.
	 * 
	 * @param m
	 *            The material to create the ItemBuilder with.
	 */
	public ItemBuilder(Material m) {
		this(m, 1);
	}

	/**
	 * Create a new ItemBuilder over an existing itemstack.
	 * 
	 * @param is
	 *            The itemstack to create the ItemBuilder over.
	 */
	public ItemBuilder(ItemStack is) {
		this.is = is;
	}

	/**
	 * Create a new ItemBuilder from scratch.
	 * 
	 * @param m
	 *            The material of the item.
	 * @param amount
	 *            The amount of the item.
	 */
	public ItemBuilder(Material m, int amount) {
		is = new ItemStack(m, amount);
	}

	/**
	 * Create a new ItemBuilder from scratch.
	 * 
	 * @param m
	 *            The material of the item.
	 * @param amount
	 *            The amount of the item.
	 * @param durability
	 *            The durability of the item.
	 */
	@SuppressWarnings("deprecation")
	public ItemBuilder(Material m, int amount, byte durability) {
		is = new ItemStack(m, amount, durability);
	}

	/**
	 * Clone the ItemBuilder into a new one.
	 * 
	 * @return The cloned instance.
	 */
	public ItemBuilder clone() {
		return new ItemBuilder(is);
	}

	public ItemBuilder addNBTTag(Object value, Object... keys) {
		is = NBTEditor.set(is, value, keys);
		return this;
	}
	
	/**
	 * Change the durability of the item.
	 * 
	 * @param dur
	 *            The durability to set it to.
	 */
	@SuppressWarnings("deprecation")
	public ItemBuilder setDurability(short dur) {
		is.setDurability(dur);
		return this;
	}

	/**
	 * Set the displayname of the item.
	 * 
	 * @param name
	 *            The name to change it to.
	 */
	public ItemBuilder setName(String name) {
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(Utils.color(name));
		is.setItemMeta(im);
		return this;
	}

	/**
	 * Add an unsafe enchantment.
	 * 
	 * @param ench
	 *            The enchantment to add.
	 * @param level
	 *            The level to put the enchant on.
	 */
	public ItemBuilder addUnsafeEnchantment(Enchantment ench, int level) {
		is.addUnsafeEnchantment(ench, level);
		return this;
	}

	/**
	 * Remove a certain enchant from the item.
	 * 
	 * @param ench
	 *            The enchantment to remove
	 */
	public ItemBuilder removeEnchantment(Enchantment ench) {
		is.removeEnchantment(ench);
		return this;
	}

	/**
	 * Set the skull owner for the item. Works on skulls only.
	 * 
	 * @param owner
	 *            The name of the skull's owner.
	 */
	@SuppressWarnings("deprecation")
	public ItemBuilder setSkullOwner(String owner) {
		try {
			SkullMeta im = (SkullMeta) is.getItemMeta();
			im.setOwner(owner);
			is.setItemMeta(im);
		} catch (ClassCastException expected) {
		}
		return this;
	}

	/**
	 * Add an enchant to the item.
	 * 
	 * @param ench
	 *            The enchant to add
	 * @param level
	 *            The level
	 */
	public ItemBuilder addEnchant(Enchantment ench, int level) {
		ItemMeta im = is.getItemMeta();
		im.addEnchant(ench, level, true);
		is.setItemMeta(im);
		return this;
	}

	/**
	 * Add multiple enchants at once.
	 * 
	 * @param enchantments
	 *            The enchants to add.
	 */
	public ItemBuilder addEnchantments(Map<Enchantment, Integer> enchantments) {
		is.addEnchantments(enchantments);
		return this;
	}

	/**
	 * Sets infinity durability on the item by setting the durability to
	 * Short.MAX_VALUE.
	 */
	@SuppressWarnings("deprecation")
	public ItemBuilder setInfinityDurability() {
		is.setDurability(Short.MAX_VALUE);
		return this;
	}

	/**
	 * Re-sets the lore.
	 * 
	 * @param lore
	 *            The lore to set it to.
	 */
	public ItemBuilder setLore(String... lore) {
		ItemMeta im = is.getItemMeta();
		im.setLore(Arrays.asList(lore));
		is.setItemMeta(im);
		return this;
	}
	
	/**
	 * Sets the lore.
	 * 
	 * @param lore
	 *            The lore to set it to.
	 */
	public ItemBuilder setLore(String lore) {
		ItemMeta im = is.getItemMeta();
		im.setLore(Arrays.asList(lore.split("\n")));
		is.setItemMeta(im);
		return this;
	}

	/**
	 * Re-sets the lore.
	 * 
	 * @param lore
	 *            The lore to set it to.
	 */
	public ItemBuilder setLore(List<String> lore) {
		ItemMeta im = is.getItemMeta();
		im.setLore(lore);
		is.setItemMeta(im);
		return this;
	}

	/**
	 * Remove a lore line.
	 * 
	 * @param line
	 *            The line to remove.
	 */
	public ItemBuilder removeLoreLine(String line) {
		ItemMeta im = is.getItemMeta();
		List<String> lore = new ArrayList<>(im.getLore());
		if (!lore.contains(line))
			return this;
		lore.remove(line);
		im.setLore(lore);
		is.setItemMeta(im);
		return this;
	}

	/**
	 * Remove a lore line.
	 * 
	 * @param index
	 *            The index of the lore line to remove.
	 */
	public ItemBuilder removeLoreLine(int index) {
		ItemMeta im = is.getItemMeta();
		List<String> lore = new ArrayList<>(im.getLore());
		if (index < 0 || index > lore.size())
			return this;
		lore.remove(index);
		im.setLore(lore);
		is.setItemMeta(im);
		return this;
	}

	/**
	 * Add a lore line.
	 * 
	 * @param line
	 *            The lore line to add.
	 */
	public ItemBuilder addLoreLine(String line) {
		ItemMeta im = is.getItemMeta();
		List<String> lore = new ArrayList<>();
		if (im.hasLore())
			lore = new ArrayList<>(im.getLore());
		lore.add(Utils.color(line));
		im.setLore(lore);
		is.setItemMeta(im);
		return this;
	}

	/**
	 * Add a lore line.
	 * 
	 * @param line
	 *            The lore line to add.
	 * @param pos
	 *            The index of where to put it.
	 */
	public ItemBuilder addLoreLine(String line, int pos) {
		ItemMeta im = is.getItemMeta();
		List<String> lore = new ArrayList<>(im.getLore());
		lore.set(pos, line);
		im.setLore(lore);
		is.setItemMeta(im);
		return this;
	}

	/**
	 * Sets the armor color of a leather armor piece. Works only on leather armor
	 * pieces.
	 * 
	 * @param color
	 *            The color to set it to.
	 */
	public ItemBuilder setLeatherArmorColor(Color color) {
		try {
			LeatherArmorMeta im = (LeatherArmorMeta) is.getItemMeta();
			im.setColor(color);
			is.setItemMeta(im);
		} catch (ClassCastException expected) {
		}
		return this;
	}

	/**
	 * Retrieves the itemstack from the ItemBuilder.
	 * 
	 * @return The itemstack created/modified by the ItemBuilder instance.
	 */
	public ItemStack toItemStack() {
		return is;
	}
}