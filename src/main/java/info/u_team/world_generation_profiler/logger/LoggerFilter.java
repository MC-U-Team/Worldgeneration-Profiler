package info.u_team.world_generation_profiler.logger;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;

@Plugin(name = "LoggerFilter", category = Node.CATEGORY, elementType = Filter.ELEMENT_TYPE, printObject = true)
public final class LoggerFilter extends AbstractFilter {
	
	public static final String ATTR_LOGGER = "logger";
	private final String name;
	
	private LoggerFilter(final String name, final Result onMatch, final Result onMismatch) {
		super(onMatch, onMismatch);
		this.name = name;
	}
	
	private Result filter(final Logger logger) {
		return filter(logger.getName());
	}
	
	private Result filter(String logger) {
		return name != null && name.equals(logger) ? onMatch : onMismatch;
	}
	
	@Override
	public Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object... params) {
		return filter(logger);
	}
	
	@Override
	public Result filter(final Logger logger, final Level level, final Marker marker, final Object msg, final Throwable t) {
		return filter(logger);
	}
	
	@Override
	public Result filter(final Logger logger, final Level level, final Marker marker, final Message msg, final Throwable t) {
		return filter(logger);
	}
	
	@Override
	public Result filter(final LogEvent event) {
		return filter(event.getLoggerName());
	}
	
	@Override
	public Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0) {
		return filter(logger);
	}
	
	@Override
	public Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1) {
		return filter(logger);
	}
	
	@Override
	public Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2) {
		return filter(logger);
	}
	
	@Override
	public Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3) {
		return filter(logger);
	}
	
	@Override
	public Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
		return filter(logger);
	}
	
	@Override
	public Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
		return filter(logger);
	}
	
	@Override
	public Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6) {
		return filter(logger);
	}
	
	@Override
	public Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7) {
		return filter(logger);
	}
	
	@Override
	public Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8) {
		return filter(logger);
	}
	
	@Override
	public Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9) {
		return filter(logger);
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@PluginFactory
	public static LoggerFilter createFilter(@PluginAttribute(ATTR_LOGGER) final String logger, @PluginAttribute(AbstractFilterBuilder.ATTR_ON_MATCH) final Result match, @PluginAttribute(AbstractFilterBuilder.ATTR_ON_MISMATCH) final Result mismatch) {
		if (logger == null) {
			LOGGER.error("A logger must be provided for LoggerFilter");
			return null;
		}
		return new LoggerFilter(logger, match, mismatch);
	}
	
}
