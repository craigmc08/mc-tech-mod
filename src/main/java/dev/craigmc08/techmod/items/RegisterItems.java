package dev.craigmc08.techmod.items;

import dev.craigmc08.techmod.materials.*;
import dev.craigmc08.techmod.TechMod;
import dev.craigmc08.techmod.blocks.RegisterBlocks;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class RegisterItems {
  private final static String MODID = TechMod.MODID;

  public static final ItemGroup ITEM_GROUP_TECHMOD = FabricItemGroupBuilder.build(
		new Identifier("techmod", "tech_mod"),
		() -> new ItemStack(Blocks.IRON_ORE)
	);

	public static final Item COPPER_INGOT = new Item(new FabricItemSettings().group(ITEM_GROUP_TECHMOD));
  public static final Item COPPER_NUGGET = new Item(new FabricItemSettings().group(ITEM_GROUP_TECHMOD));

  public static final ArmorMaterial copperArmorMaterial = new CopperArmorMaterial();
  public static final Item COPPER_HELMET = new ArmorItem(copperArmorMaterial, EquipmentSlot.HEAD, new Item.Settings().group(ITEM_GROUP_TECHMOD));
  public static final Item COPPER_CHESTPLATE = new ArmorItem(copperArmorMaterial, EquipmentSlot.CHEST, new Item.Settings().group(ITEM_GROUP_TECHMOD));
  public static final Item COPPER_LEGGINGS = new ArmorItem(copperArmorMaterial, EquipmentSlot.LEGS, new Item.Settings().group(ITEM_GROUP_TECHMOD));
  public static final Item COPPER_BOOTS = new ArmorItem(copperArmorMaterial, EquipmentSlot.FEET, new Item.Settings().group(ITEM_GROUP_TECHMOD));

  public static final Item COPPER_SWORD = new SwordItem(CopperToolMaterial.INSTANCE, 3, -3.0F, new Item.Settings().group(ITEM_GROUP_TECHMOD));
  public static final Item COPPER_SHOVEL = new ShovelItem(CopperToolMaterial.INSTANCE, 1, -2.4F, new Item.Settings().group(ITEM_GROUP_TECHMOD));
  public static final Item COPPER_PICKAXE = new CustomPickaxeItem(CopperToolMaterial.INSTANCE, 1, -2.8F, new Item.Settings().group(ITEM_GROUP_TECHMOD));
  public static final Item COPPER_AXE = new CustomAxeItem(CopperToolMaterial.INSTANCE, 5, -3.2F, new Item.Settings().group(ITEM_GROUP_TECHMOD));
  public static final Item COPPER_HOE = new CustomHoeItem(CopperToolMaterial.INSTANCE, 1, -3.2F, new Item.Settings().group(ITEM_GROUP_TECHMOD));

  public static final Item COPPER_COIL = new Item(new FabricItemSettings().group(ITEM_GROUP_TECHMOD));
  public static final Item HEATING_ELEMENT = new Item(new FabricItemSettings().group(ITEM_GROUP_TECHMOD));

  public static void register() {
    Registry.register(Registry.ITEM, new Identifier(MODID, "copper_ingot"), COPPER_INGOT);
    Registry.register(Registry.ITEM, new Identifier(MODID, "copper_nugget"), COPPER_NUGGET);

    Registry.register(Registry.ITEM, new Identifier(MODID, "copper_block"), new BlockItem(RegisterBlocks.COPPER_BLOCK, new Item.Settings().group(ITEM_GROUP_TECHMOD)));
    Registry.register(Registry.ITEM, new Identifier(MODID, "copper_ore"), new BlockItem(RegisterBlocks.COPPER_ORE, new Item.Settings().group(ITEM_GROUP_TECHMOD)));

    Registry.register(Registry.ITEM, new Identifier(MODID, "copper_helmet"), COPPER_HELMET);
    Registry.register(Registry.ITEM, new Identifier(MODID, "copper_chestplate"), COPPER_CHESTPLATE);
    Registry.register(Registry.ITEM, new Identifier(MODID, "copper_leggings"), COPPER_LEGGINGS);
    Registry.register(Registry.ITEM, new Identifier(MODID, "copper_boots"), COPPER_BOOTS);

    Registry.register(Registry.ITEM, new Identifier(MODID, "copper_sword"), COPPER_SWORD);
    Registry.register(Registry.ITEM, new Identifier(MODID, "copper_shovel"), COPPER_SHOVEL);
    Registry.register(Registry.ITEM, new Identifier(MODID, "copper_pickaxe"), COPPER_PICKAXE);
    Registry.register(Registry.ITEM, new Identifier(MODID, "copper_axe"), COPPER_AXE);
    Registry.register(Registry.ITEM, new Identifier(MODID, "copper_hoe"), COPPER_HOE);

    Registry.register(Registry.ITEM, new Identifier(MODID, "copper_coil"), COPPER_COIL);
    Registry.register(Registry.ITEM, new Identifier(MODID, "heating_element"), HEATING_ELEMENT);

    Registry.register(Registry.ITEM, new Identifier(MODID, "arc_furnace"), new BlockItem(RegisterBlocks.ARC_FURNACE, new Item.Settings().group(ITEM_GROUP_TECHMOD)));
    Registry.register(Registry.ITEM, new Identifier(MODID, "heated_generator"), new BlockItem(RegisterBlocks.HEATED_GENERATOR, new Item.Settings().group(ITEM_GROUP_TECHMOD)));

    Registry.register(Registry.ITEM, new Identifier(MODID, "wire"), new BlockItem(RegisterBlocks.WIRE, new Item.Settings().group(ITEM_GROUP_TECHMOD)));
  }
}
