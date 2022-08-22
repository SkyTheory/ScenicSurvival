package skytheory.scenicsurvival;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import skytheory.scenicsurvival.event.EntityEvent;
import skytheory.scenicsurvival.event.ExplosionEvent;
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
	public static final String MOD_VERSION = "1.0.0";
	public static final String VERSION = MC_VERSION + "-" + MOD_VERSION;

	@Mod.Instance
	public static ScenicSurvival INSTANCE;

	public static final String PROXY_CLIENT = "skytheory.scenicsurvival.init.proxy.CommonProxy";
	public static final String PROXY_SERVER = "skytheory.scenicsurvival.init.proxy.CommonProxy";

	@SidedProxy(clientSide = PROXY_CLIENT, serverSide = PROXY_SERVER)
	public static CommonProxy proxy;

	@Mod.EventHandler
	public void construct(FMLConstructionEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(EntityEvent.class);
		MinecraftForge.EVENT_BUS.register(ExplosionEvent.class);
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
