package dev.craigmc08.techmod.recipes;

import dev.craigmc08.techmod.TechMod;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class RegisterRecipes {
  private static final String MODID = TechMod.MODID;

  public static RecipeType<ArcSmeltingRecipe> ARC_SMELTING;

  public static RecipeSerializer<ArcSmeltingRecipe> ARC_SMELTING_SERIALIZER;

  public static void register() {
    ARC_SMELTING = Registry.register(Registry.RECIPE_TYPE, new Identifier(MODID, "arc_smelting"), new RecipeType<ArcSmeltingRecipe>(){
      public String toString() {
        return "arc_smelting";
      }
    });

    ARC_SMELTING_SERIALIZER = Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(MODID, "arc_smelting"), ArcSmeltingRecipeSerializer.INSTANCE);
  }
}
