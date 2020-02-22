package info.u_team.world_generation_profiler.hook;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

import net.minecraft.world.biome.Biome;

public class TestHook {
	
	public static void hook(Biome biome) {
		Stopwatch stopwatch = Stopwatch.createStarted();
		
		stopwatch.stop();
		stopwatch.elapsed(TimeUnit.MILLISECONDS);
		
		
		System.out.println("CALLED DO GENERATE FEATURE: " + biome.getRegistryName());
	}
	
}
