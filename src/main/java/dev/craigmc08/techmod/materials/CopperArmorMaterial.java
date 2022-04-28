package dev.craigmc08.techmod.materials;

import dev.craigmc08.techmod.items.RegisterItems;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public class CopperArmorMaterial implements ArmorMaterial {
  private static final int[] BASE_DURABILITY = new int[] { 13, 15, 16, 11 };
  private static final int[] PROTECTION_VALUES = new int[] { 2, 5, 6, 2 };

  @Override
  public int getDurability(EquipmentSlot slot) {
    return BASE_DURABILITY[slot.getEntitySlotId()] * 20;
  }

  @Override
  public int getProtectionAmount(EquipmentSlot slot) {
    return PROTECTION_VALUES[slot.getEntitySlotId()];
  }

  @Override
  public int getEnchantability() {
    return 10;
  }

  @Override
  public SoundEvent getEquipSound() {
    return SoundEvents.ITEM_ARMOR_EQUIP_IRON;
  }

  @Override
  public Ingredient getRepairIngredient() {
    return Ingredient.ofItems(RegisterItems.COPPER_INGOT);
  }

  @Override
  public String getName() {
    return "copper";
  }

  @Override
  public float getToughness() {
    return 0;
  }

  @Override
  public float getKnockbackResistance() {
    return 0;
  }
}
