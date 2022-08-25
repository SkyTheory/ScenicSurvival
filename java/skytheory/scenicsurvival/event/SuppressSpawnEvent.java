package skytheory.scenicsurvival.event;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import skytheory.scenicsurvival.config.ScenicSurvivalConfig;
import skytheory.scenicsurvival.config.ScenicSurvivalProperties;

public class SuppressSpawnEvent {

	@SubscribeEvent
	public static void onCheckSpawn(LivingSpawnEvent.CheckSpawn event) {
		if (!event.isSpawner()) {
			EntityLivingBase entity = event.getEntityLiving();
			if (entity instanceof IMob) {
				World world = event.getWorld();
				int dim = world.provider.getDimension();
				ScenicSurvivalProperties prop = ScenicSurvivalConfig.PROPERTIES.get(dim);
				if (prop.suppressSpawnEnabled.getBoolean()) {
					IBlockState iblockstate = world.getBlockState((new BlockPos(entity)).down());
					String blockName = iblockstate.getBlock().getRegistryName().toString();
					List<String> allowedBlocks = Arrays.asList(prop.suppressSpawnExcept.getStringList());
					if (!allowedBlocks.contains(blockName)) {
						event.setResult(Result.DENY);
					}
				}
			}
		}
	}
}
