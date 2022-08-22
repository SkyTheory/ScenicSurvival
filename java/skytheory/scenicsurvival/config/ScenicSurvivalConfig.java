package skytheory.scenicsurvival.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.unimi.dsi.fastutil.ints.IntSortedSet;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.IConfigElement;

public class ScenicSurvivalConfig {

	public static final String CATEGORY_BLOCK = "block";
	public static final String CATEGORY_ENTITY = "entity";

	public static Configuration CONFIG;

	public static final String DESC_ENABLE = "Enable prevent breaking block by explosion in this dimension.";
	public static final String DESC_HEIGHT_MIN = "Minimum height to prevent breaking block by explosion.";
	public static final String DESC_HEIGHT_MAX = "Maximum height to prevent breaking block by explosion.";
	public static final String DESC_VILLAGER = "Enable prevent killing villager by mobs.";

	public static final Map<Integer, ProtectRangeEntry> PROTECT_MAP = new HashMap<>();
	public static boolean protectVillager;

	public static List<IConfigElement> getConfigElements() {
		List<IConfigElement> elements = new ArrayList<>();
		Map<DimensionType, IntSortedSet> dimIdMap = DimensionManager.getRegisteredDimensions();
		Map<Integer, String> dimNameMap = new HashMap<>();
		dimIdMap.forEach((type, set) -> {
			set.forEach(i -> {
				dimNameMap.put(i, type.getName());
			});
		});
		dimNameMap.forEach((id, name) -> {
			// LangKeyを実装させたかったけれど、可変個数要素を動的に翻訳する方法が見つからなかったので取りやめ
			elements.add(new ConfigElement(CONFIG.get(CATEGORY_BLOCK, String.format("Dim%d_Protect", id), id == 0 ? true : false, String.format("%s Dimension ID:%d, Name: %s", DESC_ENABLE, id, name))));
			elements.add(new ConfigElement(CONFIG.get(CATEGORY_BLOCK, String.format("Dim%d_MinHeight", id), id == 0 ? 60 : 0, String.format("%s Dimension ID:%d, Name: %s", DESC_HEIGHT_MIN, id, name), 0, 255)));
			elements.add(new ConfigElement(CONFIG.get(CATEGORY_BLOCK, String.format("Dim%d_MaxHeight", id), 255, String.format("%s Dimension ID:%d, Name: %s", DESC_HEIGHT_MAX, id, name), 0, 255)));
		});

		elements.add(new ConfigElement(CONFIG.get(CATEGORY_ENTITY, "ProtectVillager", true, DESC_VILLAGER)));

		return elements;
	}

	public static void init(Configuration cfg) {
		CONFIG = cfg;
		CONFIG.load();
		read();
		if (cfg.hasChanged()) {
			save();
		}
	}

	public static void read() {
		Map<DimensionType, IntSortedSet> dimIdMap = DimensionManager.getRegisteredDimensions();
		Map<Integer, String> dimNameMap = new HashMap<>();
		dimIdMap.forEach((type, set) -> {
			set.forEach(i -> {
				dimNameMap.put(i, type.getName());
			});
		});
		PROTECT_MAP.clear();
		dimNameMap.forEach((id, name) -> {
			boolean isEnabled = CONFIG.getBoolean(String.format("Dim%d_Protect", id), CATEGORY_BLOCK, id == 0 ? true : false, String.format("%s Dimension ID:%d, Name: %s", DESC_ENABLE, id, name));
			int min = CONFIG.getInt(String.format("Dim%d_MinHeight", id), CATEGORY_BLOCK, id == 0 ? 60 : 0, 0, 255, String.format("%s Dimension ID:%d, Name: %s", DESC_HEIGHT_MIN, id, name));
			int max = CONFIG.getInt(String.format("Dim%d_MaxHeight", id), CATEGORY_BLOCK, 255, 0, 255, String.format("%s Dimension ID:%d, Name: %s", DESC_HEIGHT_MAX, id, name));
			if (isEnabled) {
				PROTECT_MAP.put(id, new ProtectRangeEntry(min, max));
			}
		});
		protectVillager = CONFIG.getBoolean("ProtectVillager", CATEGORY_ENTITY, true, DESC_VILLAGER);
	}

	public static void save() {
		CONFIG.save();
	}


	public static class ProtectRangeEntry {

		public final int min;
		public final int max;

		private ProtectRangeEntry(int min, int max) {
			this.min = min;
			this.max = max;
		}
	}
}