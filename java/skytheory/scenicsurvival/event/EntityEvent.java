package skytheory.scenicsurvival.event;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import skytheory.scenicsurvival.config.ScenicSurvivalConfig;

public class EntityEvent {

	@SubscribeEvent
	public static void onVillagerHurt(LivingDamageEvent event) {
		if (ScenicSurvivalConfig.protectVillager) {
			if (event.getEntity() instanceof EntityVillager && !(event.getSource().getTrueSource() instanceof EntityPlayer)) {
				event.setCanceled(true);
			}
		}
	}
}
