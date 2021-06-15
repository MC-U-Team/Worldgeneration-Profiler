package info.u_team.world_generation_profiler.hook;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

import info.u_team.world_generation_profiler.WorldGenerationProfilerMod;
import info.u_team.world_generation_profiler.config.CommonConfig;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.IChunk;

public class StopWatchHook {
	
	private static Map<ChunkStatus, Integer> THRESHOLD_MAP;
	
	public static void doGenerationWorkHook(ChunkStatus status, IChunk chunk, Stopwatch stopWatch) {
		if (THRESHOLD_MAP == null) {
			THRESHOLD_MAP = new HashMap<ChunkStatus, Integer>();
			CommonConfig.getInstance().getChunkStatusThresholdMap().forEach((chunkStatus, intValue) -> THRESHOLD_MAP.put(chunkStatus, intValue.get()));
		}
		final long codeExecutionTime = stopWatch.elapsed(TimeUnit.MILLISECONDS);
		if (THRESHOLD_MAP.get(status).intValue() <= codeExecutionTime) {
			WorldGenerationProfilerMod.LOGGER.warn("Chunkstatus " + status.getRegistryName() + " took " + codeExecutionTime + " milliseconds to generate at chunk " + chunk.getPos());
		}
	}
	
}
