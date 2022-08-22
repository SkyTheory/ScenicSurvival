package skytheory.scenicsurvival.event;

import java.util.Arrays;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import skytheory.scenicsurvival.config.ScenicSurvivalConfig;

public class ExplosionEvent {

	// ブロックを保護する高度
	public static int PRESERVE = 60;
	// ディメンション指定
	public static List<Integer> DIM_IDS = Arrays.asList(0);

	@SubscribeEvent
	public static void onExplosion(net.minecraftforge.event.world.ExplosionEvent.Detonate event) {
		World world = event.getWorld();
		int dim = world.provider.getDimension();
		ScenicSurvivalConfig.ProtectRangeEntry entry = ScenicSurvivalConfig.PROTECT_MAP.get(dim);
		if (entry != null) {
			Explosion explosion = event.getExplosion();
			Entity cause = explosion.getExplosivePlacedBy();
			if (cause != null && !(cause instanceof EntityPlayer)) {
				event.getAffectedBlocks().removeIf(b -> b.getY() >= entry.min && b.getY() <= entry.max);
			}
		}
	}
}
