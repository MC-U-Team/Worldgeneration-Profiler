package info.u_team.world_generation_profiler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import info.u_team.u_team_core.util.verify.JarSignVerifier;
import net.minecraftforge.fml.common.Mod;

@Mod(WorldGenerationProfilerMod.MODID)
public class WorldGenerationProfilerMod {
	
	public static final String MODID = "worldgenerationprofiler";
	
	public static final Logger LOGGER = LogManager.getLogger("World Generation Profiler");
	
	public WorldGenerationProfilerMod() {
		JarSignVerifier.checkSigned(MODID);
	}
}
