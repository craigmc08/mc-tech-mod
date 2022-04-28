package dev.craigmc08.techmod.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class HeatedGeneratorScreenHandler extends ScreenHandler {
  private final Inventory inventory;
  private final PropertyDelegate propertyDelegate;

  public HeatedGeneratorScreenHandler(int syncId, PlayerInventory playerInventory) {
    this(syncId, playerInventory, new SimpleInventory(1), new ArrayPropertyDelegate(4));
  }

  public HeatedGeneratorScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
    super(RegisterScreens.HEATED_GENERATOR_SCREEN_HANDLER, syncId);
    checkSize(inventory, 1);
    this.inventory = inventory;
    this.propertyDelegate = propertyDelegate;
    inventory.onOpen(playerInventory.player);

    addProperties(propertyDelegate);

    // Fuel slot
    addSlot(new Slot(inventory, 0, 80, 27));

    // Player inventory slots
    int m;
    int l;
    for (m = 0; m < 3; ++m) {
      for (l = 0; l < 9; ++l) {
        addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
      }
    }
    // Player hotbar slots
    for (m = 0; m < 9; ++m) {
      addSlot(new Slot(playerInventory, m, 8 + m * 18, 142));
    }
  }

  public int getBurnProgress() {
    int burnTime = propertyDelegate.get(0);
    int totalBurnTime = propertyDelegate.get(1);
    return totalBurnTime == 0 ? 0 : burnTime * 12 / totalBurnTime;
  }

  public boolean isBurning() {
    return propertyDelegate.get(0) > 0;
  }

  public int getPower() {
    return propertyDelegate.get(2);
  }

  public int getMaxPower() {
    return propertyDelegate.get(3);
  }

  public int getPowerProgress() {
    return getPower() * 59 / getMaxPower();
  }

  @Override
  public boolean canUse(PlayerEntity player) {
    return inventory.canPlayerUse(player);
  }

  // Shift + left click on inventory slot
  @Override
  public ItemStack transferSlot(PlayerEntity player, int invSlot) {
    ItemStack newStack = ItemStack.EMPTY;
    Slot slot = slots.get(invSlot);
    if (slot != null && slot.hasStack()) {
      ItemStack originalStack = slot.getStack();
      newStack = originalStack.copy();
      if (invSlot < inventory.size()) {
        if (!insertItem(originalStack, inventory.size(), slots.size(), true)) {
          return ItemStack.EMPTY;
        }
      } else if (!insertItem(originalStack, 0, 1, false)) {
        return ItemStack.EMPTY;
      }

      if (originalStack.isEmpty()) {
        slot.setStack(ItemStack.EMPTY);
      } else {
        slot.markDirty();
      }
    }

    return newStack;
  }
}
