package info.u_team.world_generation_profiler;

import org.apache.logging.log4j.*;

import info.u_team.u_team_core.util.verify.JarSignVerifier;
import info.u_team.world_generation_profiler.config.CommonConfig;
import info.u_team.world_generation_profiler.logger.ExtraLoggerFile;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(WorldGenerationProfilerMod.MODID)
@EventBusSubscriber(bus = Bus.MOD)
public class WorldGenerationProfilerMod {
	
	public static final String MODID = "worldgenerationprofiler";
	
	public static final Logger LOGGER = LogManager.getLogger("World Generation Profiler");
	
	public WorldGenerationProfilerMod() {
		JarSignVerifier.checkSigned(MODID);
		ModLoadingContext.get().registerConfig(Type.COMMON, CommonConfig.CONFIG);
	}
	
	@SubscribeEvent
	public static void on(FMLCommonSetupEvent event) {
		ExtraLoggerFile.setupILogger(LOGGER, LogManager.getRootLogger(), !CommonConfig.getInstance().logInDefaultLog.get());
	}
}
