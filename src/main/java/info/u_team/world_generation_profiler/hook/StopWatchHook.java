package info.u_team.world_generation_profiler.hook;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.ConfiguredFeature;

public class StopWatchHook {
	
	public static void decorateHook(Biome biome, ConfiguredFeature<?, ?> configuredFeature, BlockPos pos, Stopwatch stopWatch) {
		final long codeExecutionTime = stopWatch.elapsed(TimeUnit.MILLISECONDS);
		
		new RuntimeException().printStackTrace();
		
		System.out.println("Feature " + configuredFeature.feature.getRegistryName() + " took " + codeExecutionTime + " milliseconds to complete in biome " + biome.getRegistryName() + " at pos " + pos);
	}
	
}
