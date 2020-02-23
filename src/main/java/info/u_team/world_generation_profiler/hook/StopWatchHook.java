package info.u_team.world_generation_profiler.hook;

import java.util.*;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.*;
import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.Filter.Result;
import org.apache.logging.log4j.core.LifeCycle.State;
import org.apache.logging.log4j.core.appender.*;
import org.apache.logging.log4j.core.appender.rolling.*;
import org.apache.logging.log4j.core.config.*;
import org.apache.logging.log4j.core.filter.MarkerFilter;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.message.Message;

import com.google.common.base.Stopwatch;

import info.u_team.world_generation_profiler.config.CommonConfig;
import net.minecraft.world.chunk.*;

public class StopWatchHook {
	
	private static final Logger LOGGER = (Logger) LogManager.getLogger("World Generation Profiler");
	
	/*static {
		LoggerContext context = LoggerContext.getContext(false);
		Configuration configuration = context.getConfiguration();
		LoggerConfig loggerConfig = configuration.getLoggerConfig("World Generation Profiler");
		
		System.out.println("______________________________________________________________________________________");
		// loggerConfig.getAppenders().forEach((s, a) -> {
		// System.out.println(s + " - " + a);
		// });
		//
		// loggerConfig.getAppenderRefs().forEach(a -> {
		// System.out.println(a);
		// });
		
		FileAppender a = ((FileAppender) loggerConfig.getAppenders().get("DebugFile"));
		System.out.println(a.getFileName());
		System.out.println(a.getManager());
		
//		context.addFilter(new Filter);
		
		if (loggerConfig.getName().equals("loggerName")) {
			loggerConfig.removeAppender("appenderName");
		} else {
			throw new RuntimeException("There was no logger " + "loggerName");
		}
		context.updateLoggers();
		
		MarkerFilter.createFilter("yay", Result.ACCEPT, Result.DENY);
		
		RollingRandomAccessFileAppender.newBuilder() //
				.setName("World Generation Profiler") //
				.withFileName("logs/world-generation-profiler.log") //
				.withFilePattern("logs/world-generation-profiler-%i.log.gz") //
				.setLayout( //
						PatternLayout.newBuilder() //
								.withPattern("[%d{ddMMMyyyy HH:mm:ss.SSS}] [%t/%level] [%logger/%markerSimpleName]: %minecraftFormatting{%msg}{strip}%n%xEx").build() //
				) //
				.withPolicy(OnStartupTriggeringPolicy.createPolicy(1)) //
				.withPolicy(SizeBasedTriggeringPolicy.createPolicy("200MB")) //
				.withStrategy( //
						DefaultRolloverStrategy.newBuilder() //
								.withMax("5") //
								.withFileIndex("min") //
								.build() //
				);
				
	}*/
	
	private static Map<ChunkStatus, Integer> THRESHOLD_MAP;
	
	public static void doGenerationWorkHook(ChunkStatus status, IChunk chunk, Stopwatch stopWatch) {
		LOGGER.info("TEST YOLO");
		if (THRESHOLD_MAP == null) {
			THRESHOLD_MAP = new HashMap<ChunkStatus, Integer>();
			CommonConfig.getInstance().getChunkStatusThresholdMap().forEach((chunkStatus, intValue) -> THRESHOLD_MAP.put(chunkStatus, intValue.get()));
		}
		final long codeExecutionTime = stopWatch.elapsed(TimeUnit.MILLISECONDS);
		if (THRESHOLD_MAP.get(status).intValue() <= codeExecutionTime) {
			LOGGER.warn("Chunkstatus " + status.getRegistryName() + " took " + codeExecutionTime + " milliseconds to generate at chunk " + chunk.getPos());
		}
	}
	
}
