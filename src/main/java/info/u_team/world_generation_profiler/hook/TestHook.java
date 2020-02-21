package info.u_team.world_generation_profiler.hook;

import net.minecraft.world.chunk.ChunkStatus;

public class TestHook {
	
	public static void hook(ChunkStatus status) {
		System.out.println("CALLED DO GENERATE: " + status.getName());
	}
	
}
