package dev.craigmc08.techmod.container;

import dev.craigmc08.techmod.TechMod;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class RegisterScreens {
  private static final String MODID = TechMod.MODID;

  public static ScreenHandlerType<ArcFurnaceScreenHandler> ARC_FURNACE_SCREEN_HANDLER;
  public static ScreenHandlerType<HeatedGeneratorScreenHandler> HEATED_GENERATOR_SCREEN_HANDLER;

  public static void register() {
    ARC_FURNACE_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier(MODID, "arc_furnace"), ArcFurnaceScreenHandler::new);
    HEATED_GENERATOR_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier(MODID, "heated_generator"), HeatedGeneratorScreenHandler::new);
  }

  public static void registerClient() {
    ScreenRegistry.register(ARC_FURNACE_SCREEN_HANDLER, ArcFurnaceScreen::new);
    ScreenRegistry.register(HEATED_GENERATOR_SCREEN_HANDLER, HeatedGeneratorScreen::new);
  }
}
