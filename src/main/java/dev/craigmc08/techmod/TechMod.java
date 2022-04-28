package dev.craigmc08.techmod;

import dev.craigmc08.techmod.blocks.RegisterBlocks;
import dev.craigmc08.techmod.container.RegisterScreens;
import dev.craigmc08.techmod.items.RegisterItems;
import dev.craigmc08.techmod.recipes.RegisterRecipes;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;

public class TechMod implements ModInitializer {
	public static final String MODID = "techmod";

	public static final ConfiguredFeature<?, ?> ORE_COPPER_OVERWORLD = Feature.ORE
		.configure(new OreFeatureConfig(
			OreFeatureConfig.Rules.BASE_STONE_OVERWORLD,
			RegisterBlocks.COPPER_ORE.getDefaultState(),
			8
		))
		.decorate(Decorator.RANGE.configure(new RangeDecoratorConfig(0, 0, 64)))
		.spreadHorizontally()
		.repeat(20);

	@Override
	public void onInitialize() {
		RegisterItems.register();
		RegisterBlocks.register();
		RegisterRecipes.register();
		RegisterScreens.register();

		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier("techmod", "ore_copper_overworld"), ORE_COPPER_OVERWORLD);
	}
}
