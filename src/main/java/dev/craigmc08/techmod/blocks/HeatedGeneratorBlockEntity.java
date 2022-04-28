package dev.craigmc08.techmod.blocks;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import dev.craigmc08.techmod.container.HeatedGeneratorScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergyHandler;
import team.reborn.energy.EnergySide;
import team.reborn.energy.EnergyStorage;
import team.reborn.energy.EnergyTier;

public class HeatedGeneratorBlockEntity extends BlockEntity
    implements Inventory, Tickable, NamedScreenHandlerFactory, EnergyStorage {

  // TODO: implement burning lava buckets

  private static final double MAX_POWER = 10000;
  // Enough to power 2 arc furnaces simultaneously
  private static final double POWER_PER_TICK = 10;
  private static final EnergyTier ENERGY_TIER = EnergyTier.LOW;
  private static final Map<Item, Integer> FUELTIME_MAP = createFuelTimeMap();

  private DefaultedList<ItemStack> items = DefaultedList.ofSize(1, ItemStack.EMPTY);
  private int burnTime = 0;
  private int totalBurnTime = 0;
  private double power = 0;
  private boolean isActive = false;

  private PropertyDelegate propertyDelegate = new PropertyDelegate() {
    @Override
    public int get(int index) {
      switch (index) {
        case 0:
          return burnTime;
        case 1:
          return totalBurnTime;
        case 2:
          return (int)power;
        case 3:
          return (int)MAX_POWER;
        default:
          return 0;
      }
    }

    @Override
    public void set(int index, int value) { }

    @Override
    public int size() { return 4; }
  };

  public HeatedGeneratorBlockEntity() {
    super(RegisterBlocks.HEATED_GENERATOR_BLOCK_ENTITY);
  }

  @Override
  public void clear() {
    items.clear();
  }

  @Override
  public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
    return new HeatedGeneratorScreenHandler(syncId, inv, this, propertyDelegate);
  }

  @Override
  public double getMaxStoredPower() {
    return MAX_POWER;
  }

  @Override
  public EnergyTier getTier() {
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

  @Override
  public Text getDisplayName() {
    return new TranslatableText(getCachedState().getBlock().getTranslationKey());
  }

  private boolean wasActive = false;
  @Override
  public void tick() {
    boolean dirtied = false;

    if (!world.isClient) {
      ItemStack fuel = items.get(0);

      if (isBurning()) {
        if (power + POWER_PER_TICK <= MAX_POWER) {
          isActive = true;
          burnTime--;
          power += POWER_PER_TICK;
        } else {
          isActive = false;
        }
      }

      if (!isBurning()) {
        // Don't consume item unless there is room for more power
        if (getBurnTime() > 0 && power + POWER_PER_TICK <= MAX_POWER) {
          totalBurnTime = burnTime = getBurnTime();
          isActive = true;
          fuel.decrement(1);
          dirtied = true;
        }
      }
    }

    if (wasActive != isActive) {
      world.setBlockState(pos, world.getBlockState(pos).with(HeatedGeneratorBlock.LIT, isActive), 3);
      wasActive = isActive;
      dirtied = true;
    }

    // Transfer power outwards
    List<EnergyHandler> energyTargets = Lists.newArrayList();

    for (Direction dir : Direction.values()) {
      BlockPos neighborPos = getPos().offset(dir);
      BlockEntity neighborEntity = world.getBlockEntity(neighborPos);

      if (Energy.valid(neighborEntity)) {
        Direction neighborDir = dir.getOpposite();

        energyTargets.add(Energy.of(neighborEntity).side(neighborDir));
      }
    }

    // TODO: abstract energy transfering to a parent class
    // Transfer first to neighbors that can accept the smallest amount of energy
    // Transfer at most 1/nth of the remaining output budget to each neighbor

    energyTargets.sort(Comparator.comparingDouble((handler) -> {
      // Amount of energy that can be transferred into this neighbor this tick
      return Math.min(handler.getMaxInput(), handler.getMaxStored() - handler.getEnergy());
    }));
    double energyTransfered = 0;
    double maxOutput = getMaxOutput(null);
    EnergyHandler input = Energy.of(this);
    for (int i = 0; i < energyTargets.size(); i++) {
      EnergyHandler output = energyTargets.get(i);
      double maxInput = Math.min(output.getMaxInput(), output.getMaxStored() - output.getEnergy());
      double maxOutputActual = Math.min(maxOutput - energyTransfered, power);
      double amount = Math.min(maxOutputActual / (energyTargets.size() - i), maxInput);
      energyTransfered += input.into(output).move(amount);
    }

    if (dirtied) {
      markDirty();
    }
  }

  private int getBurnTime() {
    if (items.get(0).isEmpty()) return 0;
    return FUELTIME_MAP.get(items.get(0).getItem());
  }

  private boolean isBurning() {
    return isActive && burnTime > 0;
  }

  @Override
  public int size() {
    return items.size();
  }

  @Override
  public boolean isEmpty() {
    for (ItemStack itemStack : items) {
      if (!itemStack.isEmpty()) return false;
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
      markDirty();
    }
    return result;
  }

  @Override
  public ItemStack removeStack(int slot) {
    ItemStack result = Inventories.removeStack(items, slot);
    markDirty();
    return result;
  }

  @Override
  public void setStack(int slot, ItemStack stack) {
    items.set(slot, stack);
    markDirty();
  }

  @Override
  public boolean canPlayerUse(PlayerEntity player) {
    return true;
  }
  
  @Override
  public void fromTag(BlockState state, CompoundTag tag) {
    super.fromTag(state, tag);
    Inventories.fromTag(tag, items);
    burnTime = tag.getShort("BurnTime");
    totalBurnTime = tag.getShort("TotalBurnTime");
    isActive = tag.getBoolean("IsActive");
    setStored(tag.getDouble("Power"));
  }

  @Override
  public CompoundTag toTag(CompoundTag tag) {
    Inventories.toTag(tag, items);
    tag.putShort("BurnTime", (short)burnTime);
    tag.putShort("TotalBurnTime", (short)totalBurnTime);
    tag.putBoolean("IsActive", isActive);
    tag.putDouble("Power", power);
    return super.toTag(tag);
  }

  public static Map<Item, Integer> createFuelTimeMap() {
      Map<Item, Integer> map = AbstractFurnaceBlockEntity.createFuelTimeMap();
      // Coal burns for 1600 in a furnace, but needs to produce 4000 = 10 * 400 = 10 * (1600 / 4) power
      // So burn time needs to be divided by 4
      map.forEach((item, time) -> {
        map.put(item, time / 4);
      });
      return map;
   }
}
