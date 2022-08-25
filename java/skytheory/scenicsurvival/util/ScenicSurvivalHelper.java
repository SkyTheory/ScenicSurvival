package skytheory.scenicsurvival.util;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ScenicSurvivalHelper {

	public static String[] getBiomeBlocks() {
		Set<String> blocks = new HashSet<>();
		ForgeRegistries.BIOMES.getValuesCollection().stream().forEach(biome -> {
			blocks.add(biome.topBlock.getBlock().getRegistryName().toString());
			blocks.add(biome.fillerBlock.getBlock().getRegistryName().toString());
		});
		return blocks.toArray(new String[0]);
	}

	public static String[] getAllBlocks() {
		return ForgeRegistries.BLOCKS.getValuesCollection().stream()
				.filter(b -> b != Blocks.AIR)
				.map(b -> b.getRegistryName().toString())
				.collect(Collectors.toList())
				.toArray(new String[0]);
	}
}