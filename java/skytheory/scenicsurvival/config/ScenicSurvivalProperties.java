package skytheory.scenicsurvival.config;

import java.util.List;

import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.config.Property.Type;
import net.minecraftforge.fml.client.config.IConfigElement;
import skytheory.scenicsurvival.util.ScenicSurvivalHelper;

/*
 * コンフィグGUIで利用できるよう、値はProperty型で保持する
 * また、Gsonでの読み書きのために、プリミティブ型で値を保持するObjectへ変換できるようにする
 * もしかしたら、Gsonのために普段は通常のオブジェクトとして値を保存して、
 * GUIを開く・閉じる時だけ都度Propertyオブジェクトを用意する方が楽だったかもしれない
 * ……と思ったら、List<IConfigElement>から特定の値を再取得するのが若干面倒そう？
 */
public class ScenicSurvivalProperties {

	private final ConfigCategory dimensionCategory;
	public final Property protectBlockEnabled;
	public final Property protectBlockMinHeight;
	public final Property protectBlockMaxHeight;
	public final Property protectVillagerEnabled;
	public final Property suppressSpawnEnabled;
	public final Property suppressSpawnExcept;

	public static ScenicSurvivalProperties getDefault(int id, String name) {
		if (id == 0) {
			return new ScenicSurvivalProperties(name, true, 60, 255, true, true, ScenicSurvivalHelper.getBiomeBlocks());
		}
		return new ScenicSurvivalProperties(name, false, 0, 255, false, false, new String[0]);
	}

	public static ScenicSurvivalProperties fromAdapter(int id, String name, ScenicSurvivalProperties.JsonAdapter adapter) {
		ScenicSurvivalProperties prop = getDefault(id, name);
		prop.protectBlockEnabled.set(adapter.protectBlock.enable);
		prop.protectBlockMinHeight.set(adapter.protectBlock.minHeight);
		prop.protectBlockMaxHeight.set(adapter.protectBlock.maxHeight);
		prop.protectVillagerEnabled.set(adapter.protectVillager.enable);
		prop.suppressSpawnEnabled.set(adapter.suppressSpawn.enable);
		prop.suppressSpawnExcept.set(adapter.suppressSpawn.allowed);
		return prop;
	}

	ScenicSurvivalProperties(String name, boolean bflag, int bmin, int bmax, boolean vflag, boolean mflag, String[] mblocks) {
		// 内容は何でもいいけれど、要素の順序の保持に使用
		int id = 0;
		this.dimensionCategory = new ConfigCategory(name);
		this.dimensionCategory.setComment("Settings for dimensions.");

		// Block protection
		ConfigCategory protectBlockCategory = new ConfigCategory("Protect block from explosion", this.dimensionCategory);
		protectBlockCategory.setComment("Prevent blocks from being destroyed by explosions caused by mobs.");

		this.protectBlockEnabled = new Property("Enable", Boolean.toString(bflag), Property.Type.BOOLEAN);
		protectBlockEnabled.setComment("Prevent blocks from being destroyed by explosions caused by mobs.");
		protectBlockCategory.put(Integer.toString(id++), protectBlockEnabled);

		int blockMin = MathHelper.clamp(bmin, 0, 255);
		this.protectBlockMinHeight = new Property("Protect Min Y", Integer.toString(blockMin), Property.Type.INTEGER)
				.setMinValue(0)
				.setMaxValue(255);
		protectBlockMinHeight.setComment("Minimum height of protection");
		protectBlockCategory.put(Integer.toString(id++), protectBlockMinHeight);

		int blockMax = MathHelper.clamp(bmax, 0, 255);
		this.protectBlockMaxHeight = new Property("Protect Max Y", Integer.toString(blockMax), Property.Type.INTEGER)
				.setMinValue(0)
				.setMaxValue(255);
		protectBlockMaxHeight.setComment("Maximum height of protection");
		protectBlockCategory.put(Integer.toString(id++), protectBlockMaxHeight);

		// Villager protection
		ConfigCategory protectVillagerCategory = new ConfigCategory("Protect villager from mobs", this.dimensionCategory);
		protectVillagerCategory.setComment("Prevent villagers from being killed by mobs.");

		this.protectVillagerEnabled = new Property("Enable", Boolean.toString(vflag), Property.Type.BOOLEAN);
		protectVillagerEnabled.setComment("Prevent villagers from being killed by mobs.");
		protectVillagerCategory.put(Integer.toString(id++), protectVillagerEnabled);

		ConfigCategory suppressSpawningCategory = new ConfigCategory("Suppress mob spawning", this.dimensionCategory);
		suppressSpawningCategory.setComment("Prevent mobs spawning except specific blocks.");

		this.suppressSpawnEnabled = new Property("Enable", Boolean.toString(mflag), Property.Type.BOOLEAN);
		suppressSpawningCategory.put(Integer.toString(id++), suppressSpawnEnabled);

		this.suppressSpawnExcept = new Property("Allowed blocks", mblocks, Type.STRING);
		suppressSpawnExcept.setValidValues(ScenicSurvivalHelper.getAllBlocks());
		suppressSpawnExcept.setComment("What type of blocks can spawn mobs.");
		suppressSpawningCategory.put(Integer.toString(id++), suppressSpawnExcept);
	}

	public ScenicSurvivalProperties.JsonAdapter toAdapter() {
		return new JsonAdapter(this);
	}

	public void append(List<IConfigElement> list) {
		list.add(new ConfigElement(dimensionCategory));
	}

	static class JsonAdapter {
		final ProtectBlock protectBlock;
		final ProtectVillager protectVillager;
		final SuppressSpawn suppressSpawn;

		JsonAdapter(ScenicSurvivalProperties prop){
			this.protectBlock = new ProtectBlock(
					prop.protectBlockEnabled.getBoolean(),
					prop.protectBlockMinHeight.getInt(),
					prop.protectBlockMaxHeight.getInt());
			this.protectVillager = new ProtectVillager(
					prop.protectVillagerEnabled.getBoolean());
			this.suppressSpawn = new SuppressSpawn(
					prop.suppressSpawnEnabled.getBoolean(),
					prop.suppressSpawnExcept.getStringList());
		}

		static class ProtectBlock {
			boolean enable;
			int minHeight;
			int maxHeight;

			ProtectBlock(boolean enable, int minHeight, int maxHeight) {
				this.enable = enable;
				this.minHeight = minHeight;
				this.maxHeight = maxHeight;
			}
		}

		static class ProtectVillager {
			boolean enable;

			ProtectVillager(boolean enable) {
				this.enable = enable;
			}
		}

		static class SuppressSpawn {
			boolean enable;
			String[] allowed;

			SuppressSpawn(boolean enable, String[] allowed) {
				this.enable = enable;
				this.allowed = allowed;
			}
		}
	}
}
