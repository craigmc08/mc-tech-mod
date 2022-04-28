package dev.craigmc08.techmod.blocks;

import org.jetbrains.annotations.Nullable;

import dev.craigmc08.techmod.container.ArcFurnaceScreenHandler;
import dev.craigmc08.techmod.recipes.ArcSmeltingRecipe;
import dev.craigmc08.techmod.recipes.RegisterRecipes;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.recipe.RecipeUnlocker;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import team.reborn.energy.EnergySide;
import team.reborn.energy.EnergyStorage;
import team.reborn.energy.EnergyTier;

public class ArcFurnaceBlockEntity extends BlockEntity
    implements SidedInventory, RecipeUnlocker, RecipeInputProvider, Tickable, NamedScreenHandlerFactory, EnergyStorage {
  private static final int[] BOTTOM_SLOTS = new int[] { 1 };
  private static final int[] SIDE_AND_TOP_SLOTS = new int[] { 0 };
  // Ores take 200 ticks to smelt. At 5/t, this is 1k per ore. So each coal
  // (with Energy API reference value of 4000) can smelt 4 ore -> 8 ingots.
  // This feels most balanced with vanilla.
  private static final double POWER_PER_TICK = 5;
  private static final double MAX_POWER = 10000;
  private static final EnergyTier ENERGY_TIER = EnergyTier.LOW;

  private final DefaultedList<ItemStack> items = DefaultedList.ofSize(2, ItemStack.EMPTY);
  private final Object2IntOpenHashMap<Identifier> recipesUsed;

  private int cookTime = 0;
  private int totalCookTime = 0;
  private boolean isCooking = false;
  private double power = 0;

  private final PropertyDelegate propertyDelegate = new PropertyDelegate() {
    @Override
    public int get(int index) {
      switch (index) {
        case 0:
          return cookTime;
        case 1:
          return totalCookTime;
        case 2:
          return (int)power;
        case 3:
          return (int)MAX_POWER;
        default:
          return 0;
      }
    }

    @Override
    public void set(int index, int value) {
      switch (index) {
        case 0:
          cookTime = value;
          break;
        case 1:
          totalCookTime = value;
        case 2:
          setStored((double)value);
          break;
      }
    }

    @Override
    public int size() {
      return 4;
    }
  };

  public ArcFurnaceBlockEntity() {
    super(RegisterBlocks.ARC_FURNACE_BLOCK_ENTITY);

    recipesUsed = new Object2IntOpenHashMap<>();
  }

  @Override
  public void clear() {
    items.clear();
  }

  @Override
  public int size() {
    return items.size();
  }

  @Override
  public boolean isEmpty() {
    for (ItemStack itemStack : items) {
      if (!itemStack.isEmpty())
        return false;
    }
    return true;
  }

  @Override
  public ItemStack getStack(int slot) {
    return items.get(slot);
  }

  @Override
  public ItemStack removeStack(int slot, int amount) {
    ItemStack result = Inventories.splitStack(items, slot, amount);
    if (!result.isEmpty()) {
      if (slot == 0) {
        cookTime = 0;
        isCooking = false;
      }
      markDirty();
    }
    return result;
  }

  @Override
  public ItemStack removeStack(int slot) {
    ItemStack result = Inventories.removeStack(items, slot);
    if (slot == 0) {
      cookTime = 0;
      isCooking = false;
    }
    markDirty();
    return result;
  }

  @Override
  public void setStack(int slot, ItemStack stack) {
    ItemStack old = items.get(slot);
    boolean sameItem = !stack.isEmpty() && old.isItemEqualIgnoreDamage(old) && ItemStack.areTagsEqual(stack, old);

    items.set(slot, stack);
    if (stack.getCount() > getMaxCountPerStack()) {
      stack.setCount(getMaxCountPerStack());
    }

    if (slot == 0 && !sameItem) {
      totalCookTime = getCookTime();
      cookTime = 0;
      isCooking = false;
      markDirty();
    }
  }

  @Override
  public void markDirty() {
    super.markDirty();
  }

  @Override
  public boolean canPlayerUse(PlayerEntity player) {
    return true;
  }

  @Override
  public void fromTag(BlockState state, CompoundTag tag) {
    super.fromTag(state, tag);
    Inventories.fromTag(tag, items);
    cookTime = tag.getShort("CookTime");
    totalCookTime = tag.getShort("TotalCookTime");
    isCooking = tag.getBoolean("IsCooking");
    setStored(tag.getDouble("Power"));
    CompoundTag recipesUsedTag = tag.getCompound("RecipesUsed");

    for (String ident : recipesUsedTag.getKeys()) {
      this.recipesUsed.put(new Identifier(ident), recipesUsedTag.getInt(ident));
    }
  }

  @Override
  public CompoundTag toTag(CompoundTag tag) {
    tag.putShort("CookTime", (short) cookTime);
    tag.putShort("TotalCookTime", (short) totalCookTime);
    tag.putBoolean("IsCooking", isCooking);
    tag.putDouble("Power", power);
    CompoundTag recipesUsedTag = new CompoundTag();
    recipesUsed.forEach((ident, times) -> {
      recipesUsedTag.putInt(ident.toString(), times);
    });
    tag.put("RecipesUsed", recipesUsedTag);
    Inventories.toTag(tag, items);
    return super.toTag(tag);
  }

  @Override
  public int[] getAvailableSlots(Direction side) {
    switch (side) {
      case DOWN:
        return BOTTOM_SLOTS;
      default:
        return SIDE_AND_TOP_SLOTS;
    }
  }

  @Override
  public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
    if (slot == 1) {
      // Cannot insert into output slot
      return false;
    } else {
      // Can insert into input slot (the only other slot)
      return true;
    }
  }

  @Override
  public boolean canExtract(int slot, ItemStack stack, Direction dir) {
    if (slot == 0) {
      // Cannot extract from input slot
      return false;
    } else {
      // Can extract from output slot
      return true;
    }
  }

  private boolean wasCooking = false;

  @Override
  public void tick() {
    boolean dirtied = false;

    if (!world.isClient) {
      ArcSmeltingRecipe recipe = world.getRecipeManager().getFirstMatch(RegisterRecipes.ARC_SMELTING, this, world)
          .orElse(null);

      if (hasPower() && canAcceptRecipeOutput(recipe)) {
        isCooking = true;
        cookTime++;
        power -= POWER_PER_TICK;
        if (cookTime == totalCookTime) {
          cookTime = 0;
          totalCookTime = getCookTime();
          craft(recipe);
          dirtied = true;
        }
      } else {
        isCooking = false;
      }
    }

    if (wasCooking != isCooking) {
      world.setBlockState(pos, world.getBlockState(pos).with(ArcFurnaceBlock.LIT, isCooking), 3);
      wasCooking = isCooking;
      dirtied = true;
    }

    if (dirtied) {
      markDirty();
    }
  }

  private boolean hasPower() {
    return power > POWER_PER_TICK;
  }

  private boolean canAcceptRecipeOutput(@Nullable ArcSmeltingRecipe recipe) {
    if (!getStack(0).isEmpty() && recipe != null) {
      ItemStack recipeOutput = recipe.getOutput();

      if (recipeOutput.isEmpty())
        return false;
      else {
        ItemStack output = items.get(1);
        int count = recipeOutput.getCount();
        if (output.isEmpty())
          return true;
        else if (!output.isItemEqualIgnoreDamage(recipeOutput))
          return false;
        else if (output.getCount() + count <= getMaxCountPerStack()
            && output.getCount() + count <= output.getMaxCount())
          return true;
        else
          return output.getCount() + count <= recipeOutput.getMaxCount();
      }
    } else {
      return false;
    }
  }

  private void craft(@Nullable ArcSmeltingRecipe recipe) {
    if (recipe == null || !canAcceptRecipeOutput(recipe))
      return;

    ItemStack input = items.get(0);
    ItemStack output = items.get(1);
    ItemStack recipeOutput = recipe.getOutput();

    if (output.isEmpty()) {
      items.set(1, recipeOutput.copy());
    } else if (output.getItem() == recipeOutput.getItem()) {
      output.setCount(output.getCount() + recipeOutput.getCount());
    }

    if (!world.isClient)
      setLastRecipe(recipe);

    input.decrement(1);
  }

  private int getCookTime() {
    return (Integer) this.world.getRecipeManager().getFirstMatch(RegisterRecipes.ARC_SMELTING, this, this.world)
        .map(ArcSmeltingRecipe::getCookTime).orElse(200);
  }

  @Override
  public void provideRecipeInputs(RecipeFinder finder) {
    for (ItemStack itemStack : items) {
      finder.addItem(itemStack);
    }
  }

  @Override
  public void setLastRecipe(Recipe<?> recipe) {
    if (recipe != null) {
      Identifier ident = recipe.getId();
      this.recipesUsed.addTo(ident, 1);
    }
  }

  @Override
  public Recipe<?> getLastRecipe() {
    return null;
  }

  @Override
  public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
    return new ArcFurnaceScreenHandler(syncId, inv, this, propertyDelegate);
  }

  @Override
  public Text getDisplayName() {
    return new TranslatableText(getCachedState().getBlock().getTranslationKey());
  }

  @Override
  public double getMaxStoredPower() {
    return MAX_POWER;
  }

  @Override
  public EnergyTier getTier() {
    // 32/t in/out
    return ENERGY_TIER;
  }

  @Override
  public double getStored(EnergySide face) {
    return power;
  }

  @Override
  public void setStored(double amount) {
    power = Math.min(amount, MAX_POWER);
  }
}
