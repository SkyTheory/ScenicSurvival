package skytheory.scenicsurvival;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import skytheory.scenicsurvival.event.ProtectBlockEvent;
import skytheory.scenicsurvival.event.ProtectVillagerEvent;
import skytheory.scenicsurvival.event.SuppressSpawnEvent;
import skytheory.scenicsurvival.init.proxy.CommonProxy;

@Mod(
		modid=ScenicSurvival.MOD_ID,
		name=ScenicSurvival.MOD_NAME,
		version=ScenicSurvival.VERSION,
		guiFactory = "skytheory.scenicsurvival.config.ScenicSurvivalConfigGuiFactory"
		)

public class ScenicSurvival {

	public static final String MOD_ID = "scenic_survival";
	public static final String MOD_NAME = "Scenic Survival";
	public static final String MC_VERSION = "1.12.2";
	public static final String MOD_VERSION = "1.1.0";
	public static final String VERSION = MC_VERSION + "-" + MOD_VERSION;

	@Mod.Instance
	public static ScenicSurvival INSTANCE;

	public static final String PROXY_CLIENT = "skytheory.scenicsurvival.init.proxy.CommonProxy";
	public static final String PROXY_SERVER = "skytheory.scenicsurvival.init.proxy.CommonProxy";

	@SidedProxy(clientSide = PROXY_CLIENT, serverSide = PROXY_SERVER)
	public static CommonProxy proxy;

	public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

	@Mod.EventHandler
	public void construct(FMLConstructionEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(ProtectBlockEvent.class);
		MinecraftForge.EVENT_BUS.register(ProtectVillagerEvent.class);
		MinecraftForge.EVENT_BUS.register(SuppressSpawnEvent.class);
	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}
}
