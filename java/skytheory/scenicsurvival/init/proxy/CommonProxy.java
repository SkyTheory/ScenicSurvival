package skytheory.scenicsurvival.init.proxy;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import skytheory.scenicsurvival.config.ScenicSurvivalConfig;

public class CommonProxy {

	public Configuration config;

	public void preInit(FMLPreInitializationEvent event) {
		File directory = event.getModConfigurationDirectory();
		Configuration config = new Configuration(new File(directory.getPath(), "ScenicSurvival.cfg"));
		ScenicSurvivalConfig.init(config);
	}

	public void init(FMLInitializationEvent event) {
	}

	public void postInit(FMLPostInitializationEvent event) {
	}

}
