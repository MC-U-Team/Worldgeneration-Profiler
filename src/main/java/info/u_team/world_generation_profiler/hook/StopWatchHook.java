package info.u_team.world_generation_profiler.hook;

import java.util.*;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.*;

import com.google.common.base.Stopwatch;

import info.u_team.world_generation_profiler.config.CommonConfig;
import net.minecraft.world.chunk.*;

public class StopWatchHook {
	
	private static final Logger LOGGER = LogManager.getLogger("World Generation Profiler");
	
	private static Map<ChunkStatus, Integer> THRESHOLD_MAP;
	
	public static void doGenerationWorkHook(ChunkStatus status, IChunk chunk, Stopwatch stopWatch) {
		if (THRESHOLD_MAP == null) {
			THRESHOLD_MAP = new HashMap<ChunkStatus, Integer>();
			CommonConfig.getInstance().getChunkStatusThresholdMap().forEach((chunkStatus, intValue) -> THRESHOLD_MAP.put(chunkStatus, intValue.get()));
		}
		final long codeExecutionTime = stopWatch.elapsed(TimeUnit.MILLISECONDS);
		if (THRESHOLD_MAP.get(status).intValue() <= codeExecutionTime) {
			LOGGER.warn("Chunkstatus " + status.getRegistryName() + " took " + codeExecutionTime + " milliseconds to generate at chunk " + chunk.getPos());
		}
	}
	
}
