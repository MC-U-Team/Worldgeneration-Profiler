package info.u_team.world_generation_profiler.config;

import java.util.*;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.world.chunk.ChunkStatus;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.*;
import net.minecraftforge.registries.ForgeRegistries;

public class CommonConfig {
	
	public static final ForgeConfigSpec CONFIG;
	private static final CommonConfig INSTANCE;
	
	static {
		final Pair<CommonConfig, ForgeConfigSpec> pair = new Builder().configure(CommonConfig::new);
		CONFIG = pair.getRight();
		INSTANCE = pair.getLeft();
	}
	
	public static CommonConfig getInstance() {
		return INSTANCE;
	}
	
	public final BooleanValue logInDefaultLog;
	
	private final Map<ChunkStatus, IntValue> chunkStatusThresholdMap = new HashMap<>();
	
	public Map<ChunkStatus, IntValue> getChunkStatusThresholdMap() {
		return Collections.unmodifiableMap(chunkStatusThresholdMap);
	}
	
	private CommonConfig(Builder builder) {
		logInDefaultLog = builder.comment("If true the log messages will also be logged to the console and the log files. If false all messages will only be logged to the world-generation-profiler.log file").define("logInDefaultLog", true);
		builder.comment("Chunk status threshold warn settings").push("chunkStatusThreshold");
		ForgeRegistries.CHUNK_STATUS.forEach(chunkStatus -> {
			chunkStatusThresholdMap.put(chunkStatus, builder.defineInRange(chunkStatus.getRegistryName().toString(), 50, 0, 1000));
		});
		builder.pop();
	}
	
}
