package info.u_team.world_generation_profiler.logger;

import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.core.Filter.Result;
import org.apache.logging.log4j.core.appender.RollingRandomAccessFileAppender;
import org.apache.logging.log4j.core.appender.rolling.*;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.layout.PatternLayout;

public class ExtraLoggerFile {
	
	public static void setupILogger(org.apache.logging.log4j.Logger logger, org.apache.logging.log4j.Logger rootLogger, boolean denyRootLogger) {
		if (logger instanceof Logger && rootLogger instanceof Logger) {
			setupLogger((Logger) logger, (Logger) rootLogger, denyRootLogger);
		} else {
			throw new RuntimeException("The logger is not in instance of Logger");
		}
	}
	
	public static void setupLogger(Logger logger, Logger rootLogger, boolean denyRootLogger) {
		final LoggerContext context = LoggerContext.getContext(false);
		final Configuration configuration = context.getConfiguration();
		
		if (denyRootLogger) {
			configuration.addLoggerFilter(rootLogger, LoggerFilter.createFilter(logger.getName(), Result.DENY, Result.NEUTRAL));
		}
		
		final RollingRandomAccessFileAppender appender = RollingRandomAccessFileAppender.newBuilder() //
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
		configuration.addLoggerAppender(logger, appender);
		context.updateLoggers();
	}
	
}
