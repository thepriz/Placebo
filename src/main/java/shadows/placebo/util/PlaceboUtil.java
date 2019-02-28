package shadows.placebo.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class PlaceboUtil {

	public static void sMRL(Item k, int meta, String variant) {
		ModelLoader.setCustomModelResourceLocation(k, meta, new ModelResourceLocation(k.getRegistryName(), variant));
	}

	public static void sMRL(Block k, int meta, String variant) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(k), meta, new ModelResourceLocation(k.getRegistryName(), variant));
	}

	public static void sMRL(String statePath, Block k, int meta, String variant) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(k), meta, new ModelResourceLocation(k.getRegistryName().getNamespace() + ":" + statePath, variant));
	}

	public static void sMRL(String statePath, Item k, int meta, String variant) {
		ModelLoader.setCustomModelResourceLocation(k, meta, new ModelResourceLocation(k.getRegistryName().getNamespace() + ":" + statePath, variant));
	}

	public static void sMRL(String domain, String statePath, Block k, int meta, String variant) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(k), meta, new ModelResourceLocation(domain + ":" + statePath, variant));
	}

	public static void sMRL(String domain, String statePath, Item k, int meta, String variant) {
		ModelLoader.setCustomModelResourceLocation(k, meta, new ModelResourceLocation(domain + ":" + statePath, variant));
	}

	public static boolean isOwnedBy(IForgeRegistryEntry<?> thing, String owner) {
		return thing.getRegistryName().getNamespace().equals(owner);
	}

	public static Item getItemByName(String regname) {
		return ForgeRegistries.ITEMS.getValue(new ResourceLocation(regname));
	}

	public static Item[] getItemsByNames(String... regnames) {
		Item[] items = new Item[regnames.length];
		for (int i = 0; i < regnames.length; i++) {
			items[i] = ForgeRegistries.ITEMS.getValue(new ResourceLocation(regnames[i]));
		}
		return items;
	}

	public static void setRegNameIllegally(IForgeRegistryEntry<?> entry, String name) {
		Loader l = Loader.instance();
		ModContainer k = l.activeModContainer();
		l.setActiveModContainer(l.getMinecraftModContainer());
		entry.setRegistryName(new ResourceLocation("minecraft", name));
		l.setActiveModContainer(k);
	}

	public static NBTTagCompound getStackNBT(ItemStack stack) {
		if (stack.isEmpty()) throw new RuntimeException("Tried to get tag compound from empty stack!  This is a bug!");
		if (stack.hasTagCompound()) return stack.getTagCompound();
		stack.setTagCompound(new NBTTagCompound());
		return stack.getTagCompound();
	}

	/**
	 * Returns an ArrayList (non-fixed) with the provided elements.
	 */
	@SafeVarargs
	public static <T> List<T> asList(T... objs) {
		ArrayList<T> list = new ArrayList<>();
		for (T t : objs)
			list.add(t);
		return list;
	}

	/**
	 * Creates a List<ItemStack> from things that could be ItemStacks.
	 */
	public static List<ItemStack> toStackList(Object... objs) {
		ItemStack[] stacks = new ItemStack[objs.length];
		for (int i = 0; i < objs.length; i++)
			stacks[i] = RecipeHelper.makeStack(objs[i]);
		return NonNullList.from(ItemStack.EMPTY, stacks);
	}

	/**
	 * Creates an ItemStack[] from things that could be ItemStacks.
	 */
	public static ItemStack[] toStackArray(Object... objs) {
		ItemStack[] stacks = new ItemStack[objs.length];
		for (int i = 0; i < objs.length; i++)
			stacks[i] = RecipeHelper.makeStack(objs[i]);
		return stacks;
	}

	/**
	 * Overrides a block in the registry.  Also registers a default itemblock that shows the passed modid as the owner.
	 * @param block The block to override.  Must already have a name set.
	 * @param modid The owner of this override block.
	 */
	public static void registerOverrideBlock(Block block, String modid) {
		ForgeRegistries.BLOCKS.register(block);
		ForgeRegistries.ITEMS.register(new ItemBlock(block) {
			@Override
			public String getCreatorModId(ItemStack itemStack) {
				return modid;
			}
		}.setRegistryName(block.getRegistryName()));
	}

	/**
	 * Sets the name and key of an item in preparation for registration.
	 */
	public static <T extends Item> T initItem(T item, String modid, String name) {
		item.setRegistryName(modid, name);
		item.setTranslationKey(modid + "." + name);
		return item;
	}

	/**
	 * Sets the name, key, hardness, and resistance of a block in preparation for registration.
	 */
	public static <T extends Block> T initBlock(T block, String modid, String name, float hardness, float resist) {
		block.setRegistryName(modid, name);
		block.setTranslationKey(modid + "." + name);
		block.setHardness(hardness);
		block.setResistance(resist);
		return block;
	}

}
