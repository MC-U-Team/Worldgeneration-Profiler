package info.u_team.world_generation_profiler;

import org.apache.logging.log4j.*;
import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.core.Filter.Result;
import org.apache.logging.log4j.core.appender.*;
import org.apache.logging.log4j.core.appender.rolling.*;
import org.apache.logging.log4j.core.config.*;
import org.apache.logging.log4j.core.filter.*;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.script.Script;

import info.u_team.u_team_core.util.verify.JarSignVerifier;
import info.u_team.world_generation_profiler.config.CommonConfig;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;

@Mod(WorldGenerationProfilerMod.MODID)
public class WorldGenerationProfilerMod {
	
	public static final String MODID = "worldgenerationprofiler";
	
	public static final Logger LOGGER = (Logger) LogManager.getLogger("World Generation Profiler");
	public static final Marker MARKER = MarkerManager.getMarker("World-Generation-Profiler");
	
	public WorldGenerationProfilerMod() {
		JarSignVerifier.checkSigned(MODID);
		ModLoadingContext.get().registerConfig(Type.COMMON, CommonConfig.CONFIG);
		setupLogger();
	}
	
	private void setupLogger() {
		System.out.println("_____________________________________________XX");
		LoggerContext context = LoggerContext.getContext(false);
		Configuration configuration = context.getConfiguration();
		
		final String script = //
				"var result = false \r\n" + //
						"if (typeof logEvent === 'undefined') { \r\n" + //
						"   result = false \r\n" + //
						"} else { \r\n" + //
						"   result = logEvent.getLoggerName().equals('" + LOGGER.getName() + "')\r\n" + //
						"} \r\n" + //
						"result;";
		
		configuration.addLoggerFilter((Logger) LogManager.getRootLogger(), ScriptFilter.createFilter(Script.createScript("script", "javascript", script), Result.DENY, Result.NEUTRAL, configuration));
		
		RollingRandomAccessFileAppender appender = RollingRandomAccessFileAppender.newBuilder() //
				.setName("World Generation Profiler") //
				.withFileName("logs/world-generation-profiler.log") //
				.withFilePattern("logs/world-generation-profiler-%i.log.gz") //
				.setLayout( //
						PatternLayout.newBuilder() //
								.withPattern("[%d{ddMMMyyyy HH:mm:ss.SSS}] [%t/%level] [%logger/%markerSimpleName]: %minecraftFormatting{%msg}{strip}%n%xEx").build() //
				) //
				.withPolicy(CompositeTriggeringPolicy.createPolicy( //
						OnStartupTriggeringPolicy.createPolicy(1), //
						SizeBasedTriggeringPolicy.createPolicy("200MB")))
				.withStrategy( //
						DefaultRolloverStrategy.newBuilder() //
								.withMax("5") //
								.withFileIndex("min") //
								.build() //
				).build();
		appender.start();
		
		configuration.addLoggerAppender(LOGGER, appender);
		
		context.updateLoggers();
		
		LOGGER.info(MARKER, "YAAASDihzgdsiizjfhsdjkm,lfhsdkfksdjrhfgresdlukghzesruitlhjesdfuilhergjkldfhgjdflh");
		LOGGER.info("GOGOGOGOOGOGOGOGOGOG,lfhsdkfksdjrhfgresdlukghzesruitlhjesdfuilhergjkldfhgjdflh");
	}
	
}
