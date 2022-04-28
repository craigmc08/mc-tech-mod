package dev.craigmc08.techmod;

import dev.craigmc08.techmod.container.RegisterScreens;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class TechModClient implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    RegisterScreens.registerClient();
  }
}
