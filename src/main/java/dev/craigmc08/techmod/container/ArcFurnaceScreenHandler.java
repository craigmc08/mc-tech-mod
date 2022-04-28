package dev.craigmc08.techmod.container;

import dev.craigmc08.techmod.container.slot.OutputSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class ArcFurnaceScreenHandler extends ScreenHandler {
  private final Inventory inventory;
  private final PropertyDelegate propertyDelegate;

  public ArcFurnaceScreenHandler(int syncId, PlayerInventory playerInventory) {
    this(syncId, playerInventory, new SimpleInventory(2), new ArrayPropertyDelegate(4));
  }

  public ArcFurnaceScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
    super(RegisterScreens.ARC_FURNACE_SCREEN_HANDLER, syncId);
    checkSize(inventory, 2);
    this.inventory = inventory;
    this.propertyDelegate = propertyDelegate;
    inventory.onOpen(playerInventory.player);

    addProperties(propertyDelegate);

    // Furnace slots
    addSlot(new Slot(inventory, 0, 56, 35));
    addSlot(new OutputSlot(playerInventory.player, inventory, 1, 116, 35));

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

  public int getCookProgress() {
    int cookTime = propertyDelegate.get(0);
    int totalCookTime = propertyDelegate.get(1);
    return totalCookTime == 0 ? 0 : cookTime * 22 / totalCookTime;
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
