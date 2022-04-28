package dev.craigmc08.techmod.recipes;

import dev.craigmc08.techmod.blocks.ArcFurnaceBlockEntity;
import dev.craigmc08.techmod.blocks.RegisterBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class ArcSmeltingRecipe implements Recipe<ArcFurnaceBlockEntity> {
   protected final Identifier id;
   protected final String group;
   protected final Ingredient input;
   protected final ItemStack output;
   protected final int cookTime;

   public ArcSmeltingRecipe(Identifier id, String group, Ingredient input, ItemStack output, int cookTime) {
      this.id = id;
      this.group = group;
      this.input = input;
      this.output = output;
      this.cookTime = cookTime;
   }

   public boolean matches(ArcFurnaceBlockEntity inv, World world) {
      return this.input.test(inv.getStack(0));
   }

   public ItemStack craft(ArcFurnaceBlockEntity inv) {
      return this.output.copy();
   }

   @Environment(EnvType.CLIENT)
   public boolean fits(int width, int height) {
      return true;
   }

   public DefaultedList<Ingredient> getPreviewInputs() {
      DefaultedList<Ingredient> defaultedList = DefaultedList.of();
      defaultedList.add(this.input);
      return defaultedList;
   }

   public ItemStack getOutput() {
      return this.output;
   }

   @Environment(EnvType.CLIENT)
   public String getGroup() {
      return this.group;
   }

   public int getCookTime() {
      return this.cookTime;
   }

   public Identifier getId() {
      return this.id;
   }

   public RecipeType<?> getType() {
      return RegisterRecipes.ARC_SMELTING;
   }

   public RecipeSerializer<?> getSerializer() {
     return ArcSmeltingRecipeSerializer.INSTANCE;
   }

   @Environment(EnvType.CLIENT)
   public ItemStack getRecipeKindIcon() {
      return new ItemStack(RegisterBlocks.ARC_FURNACE);
   }
}

