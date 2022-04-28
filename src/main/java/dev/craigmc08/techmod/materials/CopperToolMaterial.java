package dev.craigmc08.techmod.materials;

import dev.craigmc08.techmod.items.RegisterItems;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public class CopperToolMaterial implements ToolMaterial {
  public static final CopperToolMaterial INSTANCE = new CopperToolMaterial();

  @Override
  public int getDurability() {
    return 250;
  }

  @Override
  public float getMiningSpeedMultiplier() {
    return 8.0F;
  }

  @Override
  public float getAttackDamage() {
    return 3.0F;
  }

  @Override
  public int getMiningLevel() {
    return 2;
  }

  @Override
  public int getEnchantability() {
    return 10;
  }

  @Override
  public Ingredient getRepairIngredient() {
    return Ingredient.ofItems(RegisterItems.COPPER_INGOT);
  }
  
}
