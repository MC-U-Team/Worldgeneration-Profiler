package info.u_team.world_generation_profiler.hook;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

import net.minecraft.world.chunk.*;

public class StopWatchHook {
	
	public static void doGenerationWorkHook(ChunkStatus status, IChunk chunk, Stopwatch stopWatch) {
		final long codeExecutionTime = stopWatch.elapsed(TimeUnit.MILLISECONDS);
		
		System.out.println("Chunkstatus " + status + " took " + codeExecutionTime + " milliseconds in chunk " + chunk.getPos());
		
		// System.out.println(chunks);
		
		// System.out.println("Decoration stage " + stage + " took " + codeExecutionTime + " milliseconds to complete in biome "
		// + biome.getRegistryName() + " at chunk pos " + pos);
	}
	
}
