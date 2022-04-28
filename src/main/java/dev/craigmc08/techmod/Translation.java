package dev.craigmc08.techmod;

public class Translation {
  public static String getKey(String category, String path) {
    return category + "." + TechMod.MODID + "." + path;
  }
}
