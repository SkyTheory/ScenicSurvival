package skytheory.scenicsurvival.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import skytheory.scenicsurvival.config.ScenicSurvivalConfig;

public class ProtectVillagerEvent {

	@SubscribeEvent
	public static void onVillagerHurt(LivingDamageEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof EntityVillager) {
			World world = entity.getEntityWorld();
			int dim = world.provider.getDimension();
			if (ScenicSurvivalConfig.PROPERTIES.get(dim).protectVillagerEnabled.getBoolean()) {
				if (!(event.getSource().getTrueSource() instanceof EntityPlayer)) {
					event.setCanceled(true);
				}
			}
		}
	}
}
