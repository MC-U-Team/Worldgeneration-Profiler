package info.u_team.world_generation_profiler.hook;

import net.minecraft.world.biome.Biome;

public class TestHook {
	
	public static void hook(Biome biome) {
		System.out.println("CALLED DO GENERATE FEATURE: " + biome.getRegistryName());
	}
	
}
