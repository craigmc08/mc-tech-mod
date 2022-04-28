package dev.craigmc08.techmod.container;

import com.mojang.blaze3d.systems.RenderSystem;

import dev.craigmc08.techmod.TechMod;
import dev.craigmc08.techmod.Translation;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class HeatedGeneratorScreen extends HandledScreen<ScreenHandler> {
  private static final Identifier TEXTURE = new Identifier(TechMod.MODID, "textures/gui/container/heated_generator.png");

  public HeatedGeneratorScreen(ScreenHandler handler, PlayerInventory inventory, Text title) {
    super(handler, inventory, title);
  }

  @Override
  protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    client.getTextureManager().bindTexture(TEXTURE);
    int x = (width - backgroundWidth) / 2;
    int y = (height - backgroundHeight) / 2;
    drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);

    HeatedGeneratorScreenHandler h = (HeatedGeneratorScreenHandler)handler;

    if (h.isBurning()) {
      int burnProgress = h.getBurnProgress();
      drawTexture(matrices, x + 81, y + 46 + 13 - burnProgress, 176, 0 + 12 - burnProgress, 14, burnProgress + 1);
    }

    int powerProgress = ((HeatedGeneratorScreenHandler)handler).getPowerProgress();
    drawTexture(matrices, x + 8, y + 8 + (59 - powerProgress), 177, 32 + (59 - powerProgress), 16, powerProgress);
  }

  @Override
  public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
    renderBackground(matrices);
    super.render(matrices, mouseX, mouseY, delta);
    drawMouseoverTooltip(matrices, mouseX, mouseY);

    // Draw energy tooltip, if mouse is in that area
    int x = (width - backgroundWidth) / 2;
    int y = (height - backgroundHeight) / 2;
    if (x + 7 <= mouseX && mouseX <= x + 24 && y + 7 <= mouseY && mouseY <= y + 67) {
      int power = ((HeatedGeneratorScreenHandler)handler).getPower();
      int maxPower = ((HeatedGeneratorScreenHandler)handler).getMaxPower();
      renderTooltip(matrices, new TranslatableText(Translation.getKey("gui", "energy_storage"), power, maxPower), mouseX, mouseY);
    }
  }

  @Override
  protected void init() {
    super.init();

    titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
  }
}
