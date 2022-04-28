package dev.craigmc08.techmod.blocks;

import java.util.function.ToIntFunction;

import dev.craigmc08.techmod.TechMod;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class RegisterBlocks {
  private static ToIntFunction<BlockState> createLightLevelFromState(int litLevel) {
    return (state) -> {
      return (Boolean) state.get(Properties.LIT) ? litLevel : 0;
    };
  }

  private static final String MODID = TechMod.MODID;

  public static final Block COPPER_BLOCK = new Block(FabricBlockSettings.of(Material.METAL).hardness(6.0F));
  public static final Block COPPER_ORE = new Block(FabricBlockSettings.of(Material.STONE).hardness(3.0F));

  public static final Block ARC_FURNACE = new ArcFurnaceBlock(
      FabricBlockSettings.of(Material.METAL).requiresTool().hardness(6.0F).luminance(createLightLevelFromState(13)));
  public static BlockEntityType<ArcFurnaceBlockEntity> ARC_FURNACE_BLOCK_ENTITY;

  public static final Block HEATED_GENERATOR = new HeatedGeneratorBlock(
    FabricBlockSettings.of(Material.METAL).requiresTool().hardness(6.0F).luminance(createLightLevelFromState(13)));
  public static BlockEntityType<HeatedGeneratorBlockEntity> HEATED_GENERATOR_BLOCK_ENTITY;

  // TODO: different tiers of wire
  public static final Block WIRE = new WireBlock(FabricBlockSettings.of(Material.METAL).hardness(1.0F));

  public static void register() {
    Registry.register(Registry.BLOCK, new Identifier(MODID, "copper_block"), COPPER_BLOCK);
    Registry.register(Registry.BLOCK, new Identifier(MODID, "copper_ore"), COPPER_ORE);

    Registry.register(Registry.BLOCK, new Identifier(MODID, "arc_furnace"), ARC_FURNACE);
    ARC_FURNACE_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, "arc_furnace"),
        BlockEntityType.Builder.create(ArcFurnaceBlockEntity::new, ARC_FURNACE).build(null));

    Registry.register(Registry.BLOCK, new Identifier(MODID, "heated_generator"), HEATED_GENERATOR);
    HEATED_GENERATOR_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, "heated_generator"),
        BlockEntityType.Builder.create(HeatedGeneratorBlockEntity::new, HEATED_GENERATOR).build(null));

    Registry.register(Registry.BLOCK, new Identifier(MODID, "wire"), WIRE);
  }
}
