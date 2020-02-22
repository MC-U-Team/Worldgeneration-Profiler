package info.u_team.world_generation_profiler.hook;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;

public class StopWatchHook {
	
	public static void decorateHook(Biome biome, GenerationStage.Decoration stage, BlockPos pos, Stopwatch stopWatch) {
		final long codeExecutionTime = stopWatch.elapsed(TimeUnit.MILLISECONDS);
		System.out.println("Decoration stage " + stage + " took " + codeExecutionTime + " milliseconds to complete in biome " + biome.getRegistryName() + " at chunk pos " + pos);
	}
	
}
