package info.u_team.world_generation_profiler.init;

import org.apache.logging.log4j.LogManager;

import info.u_team.u_team_core.api.construct.Construct;
import info.u_team.u_team_core.api.construct.IModConstruct;
import info.u_team.world_generation_profiler.WorldGenerationProfilerMod;
import info.u_team.world_generation_profiler.config.CommonConfig;
import info.u_team.world_generation_profiler.logger.ExtraLoggerFile;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;

@Construct(modid = WorldGenerationProfilerMod.MODID)
public class WorldGenerationProfilerCommonConstruct implements IModConstruct {
	
	@Override
	public void construct() {
		ModLoadingContext.get().registerConfig(Type.COMMON, CommonConfig.CONFIG);
		
		ExtraLoggerFile.setupILogger(WorldGenerationProfilerMod.LOGGER, LogManager.getRootLogger(), !CommonConfig.getInstance().logInDefaultLog.get());
	}
	
}
