package skytheory.scenicsurvival.event;

import java.util.Arrays;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import skytheory.scenicsurvival.config.ScenicSurvivalConfig;
import skytheory.scenicsurvival.config.ScenicSurvivalProperties;

public class ProtectBlockEvent {

	// ブロックを保護する高度
	public static int PRESERVE = 60;
	// ディメンション指定
	public static List<Integer> DIM_IDS = Arrays.asList(0);

	@SubscribeEvent
	public static void onExplosion(net.minecraftforge.event.world.ExplosionEvent.Detonate event) {
		World world = event.getWorld();
		int dim = world.provider.getDimension();
		ScenicSurvivalProperties prop = ScenicSurvivalConfig.PROPERTIES.get(dim);
		if (prop.protectBlockEnabled.getBoolean()) {
			Explosion explosion = event.getExplosion();
			Entity cause = explosion.getExplosivePlacedBy();
			if (cause != null && !(cause instanceof EntityPlayer)) {
				event.getAffectedBlocks().removeIf(b ->
				b.getY() >= prop.protectBlockMinHeight.getInt() &&
				b.getY() <= prop.protectBlockMaxHeight.getInt()
				);
			}
		}
	}
}
