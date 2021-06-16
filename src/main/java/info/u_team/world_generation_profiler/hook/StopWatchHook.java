package info.u_team.world_generation_profiler.hook;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

import info.u_team.world_generation_profiler.WorldGenerationProfilerMod;
import info.u_team.world_generation_profiler.config.CommonConfig;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.registries.ForgeRegistries;

public class StopWatchHook {
	
	private static Map<ChunkStatus, Integer> THRESHOLD_MAP;
	
	public static void doGenerationWorkHook(ChunkStatus status, IChunk chunk, Stopwatch stopWatch) {
		if (THRESHOLD_MAP == null) {
			THRESHOLD_MAP = new HashMap<ChunkStatus, Integer>();
			ForgeRegistries.CHUNK_STATUS.forEach(chunkStatus -> THRESHOLD_MAP.put(chunkStatus, 50)); // Add all currently registried chunk status entries with a default threshold of 50ms
			CommonConfig.getInstance().getChunkStatusThresholdMap().forEach((chunkStatus, intValue) -> THRESHOLD_MAP.put(chunkStatus, intValue.get())); // Replace values with config values
		}
		
		final long codeExecutionTime = stopWatch.elapsed(TimeUnit.MILLISECONDS);
		
		final Integer threshold = THRESHOLD_MAP.get(status);
		if (threshold == null) {
			WorldGenerationProfilerMod.LOGGER.error("Chunkstatus " + status.getRegistryName() + " is not contained in the threshold config. Value not registered at the right time?");
			return;
		}
		
		if (threshold.intValue() <= codeExecutionTime) {
			WorldGenerationProfilerMod.LOGGER.warn("Chunkstatus " + status.getRegistryName() + " took " + codeExecutionTime + " milliseconds to generate at chunk " + chunk.getPos());
		}
	}
	
}
