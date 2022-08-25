package skytheory.scenicsurvival.config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.client.config.IConfigElement;
import skytheory.scenicsurvival.ScenicSurvival;

public class ScenicSurvivalConfig {

	public static final Map<Integer, String> DIMENSIONS = new HashMap<>();
	public static final Map<Integer, ScenicSurvivalProperties> PROPERTIES = new HashMap<>();
	public static final TypeToken<Map<Integer, ScenicSurvivalProperties.JsonAdapter>> TYPE_TOKEN = new TypeToken<Map<Integer, ScenicSurvivalProperties.JsonAdapter>>() {};
	public static final Gson GSON = new GsonBuilder()
			.setPrettyPrinting()
			.create();

	public static File FILE;

	public static void init(File file) {
		FILE = file;
		DIMENSIONS.clear();
		PROPERTIES.clear();
		DimensionManager.getRegisteredDimensions().forEach((type, set) -> {
			set.forEach(i -> {
				DIMENSIONS.put(i, type.getName());
			});
		});
		read();
		save();
	}

	public static List<IConfigElement> getConfigElements() {
		List<IConfigElement> elements = new ArrayList<>();
		DIMENSIONS.forEach((id, name) -> {
			ScenicSurvivalProperties prop = PROPERTIES.get(id);
			prop.append(elements);
		});

		return elements;
	}

	public static void read() {
		try {
			Map<Integer, ScenicSurvivalProperties.JsonAdapter> readedProperties = GSON.fromJson(new FileReader(FILE), TYPE_TOKEN.getType());
			if (readedProperties != null) {
				readedProperties.forEach((id, adapter) ->
				PROPERTIES.put(id, ScenicSurvivalProperties.fromAdapter(id, DIMENSIONS.get(id), adapter))
				);
			}
		} catch (IOException | IllegalStateException | JsonSyntaxException e) {
			ScenicSurvival.LOGGER.error("Error reading configuration.", e);
		}
		DIMENSIONS.forEach((id, name) -> {
			if (!PROPERTIES.containsKey(id)) {
				ScenicSurvivalProperties prop = ScenicSurvivalProperties.getDefault(id, name);
				PROPERTIES.put(id, prop);
			}
		});
	}

	public static void save() {
		try {
			FileWriter writer = new FileWriter(FILE);
			Map<Integer, ScenicSurvivalProperties.JsonAdapter> jsonMap = new HashMap<>();
			PROPERTIES.forEach((id, prop) -> jsonMap.put(id, prop.toAdapter()));
			writer.write(GSON.toJson(jsonMap, TYPE_TOKEN.getType()));
			writer.close();
		} catch (IOException e) {
			ScenicSurvival.LOGGER.error("Error writing configuration.", e);
		}
	}
}