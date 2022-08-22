package skytheory.scenicsurvival.config;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.config.GuiConfig;
import skytheory.scenicsurvival.ScenicSurvival;

public class ScenicSurvivalConfigScreen extends GuiConfig {
	public ScenicSurvivalConfigScreen(GuiScreen parent) {
		super(parent, ScenicSurvivalConfig.getConfigElements(), ScenicSurvival.MOD_ID, false, false, I18n.format("scenicsurvival.tip.config"));
	}

	@Override
	public void onGuiClosed() {
		if (ScenicSurvivalConfig.CONFIG.hasChanged()) {
			ScenicSurvivalConfig.save();
			ScenicSurvivalConfig.read();
		}
		super.onGuiClosed();
	}
}
