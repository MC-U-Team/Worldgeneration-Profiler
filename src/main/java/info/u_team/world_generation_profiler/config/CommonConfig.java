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
	
	private final Map<ChunkStatus, IntValue> chunkStatusThresholdMap = new HashMap<>();
	
	public Map<ChunkStatus, IntValue> getChunkStatusThresholdMap() {
		return Collections.unmodifiableMap(chunkStatusThresholdMap);
	}
	
	private CommonConfig(Builder builder) {
		builder.comment("Chunk status threshold warn settings").push("chunkStatusThreshold");
		ForgeRegistries.CHUNK_STATUS.forEach(chunkStatus -> {
			chunkStatusThresholdMap.put(chunkStatus, builder.defineInRange(chunkStatus.getRegistryName().toString(), 50, 0, 1000));
		});
		builder.pop();
	}
	
}
