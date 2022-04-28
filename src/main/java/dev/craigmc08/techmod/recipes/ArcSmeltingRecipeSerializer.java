package dev.craigmc08.techmod.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;

public class ArcSmeltingRecipeSerializer implements RecipeSerializer<ArcSmeltingRecipe> {
  public static final int DEFAULT_COOKING_TIME = 200;

  public static final ArcSmeltingRecipeSerializer INSTANCE = new ArcSmeltingRecipeSerializer();

  @Override
  public ArcSmeltingRecipe read(Identifier id, JsonObject json) {
    String group = JsonHelper.getString(json, "group", "");
    JsonElement ingredientEl = JsonHelper.hasArray(json, "ingredient") ? JsonHelper.getArray(json, "ingredient") : JsonHelper.getObject(json, "ingredient");
    Ingredient ingredient = Ingredient.fromJson(ingredientEl);

    JsonObject resultObj = JsonHelper.getObject(json, "result");
    Identifier resultIdent = new Identifier(JsonHelper.getString(resultObj, "item"));
    ItemStack resultItem = new ItemStack(Registry.ITEM.getOrEmpty(resultIdent).orElseThrow(() -> {
      return new IllegalStateException("Item: " + resultIdent.toString() + " does not exist");
    }));
    int resultCount = JsonHelper.getInt(resultObj, "count");
    resultItem.setCount(resultCount);

    int cookingTime = JsonHelper.getInt(json, "cookingtime", DEFAULT_COOKING_TIME);
    
    return new ArcSmeltingRecipe(id, group, ingredient, resultItem, cookingTime);
  }

  @Override
  public ArcSmeltingRecipe read(Identifier id, PacketByteBuf buf) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void write(PacketByteBuf buf, ArcSmeltingRecipe recipe) {
    // TODO Auto-generated method stub

  }
  
}
