package info.u_team.world_generation_profiler;

import info.u_team.u_team_core.util.verify.JarSignVerifier;
import info.u_team.world_generation_profiler.config.CommonConfig;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;

@Mod(WorldGenerationProfilerMod.MODID)
public class WorldGenerationProfilerMod {
	
	public static final String MODID = "worldgenerationprofiler";
	
	public WorldGenerationProfilerMod() {
		JarSignVerifier.checkSigned(MODID);
		ModLoadingContext.get().registerConfig(Type.COMMON, CommonConfig.CONFIG);
	}
}
