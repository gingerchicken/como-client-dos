package net.como.client.utils;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.util.Random;

public class CrashUtils {
    private final static Random random = new Random();
    public static BlockPos randomPosition(int height) {
        int k = 16777215;

        return new BlockPos(
                random.nextInt(k),
                height,
                random.nextInt(k)
        );
    }

    public static ChunkPos randomChunkPos() {
        int k = 16777215;

        return new ChunkPos(
                random.nextInt(k),
                random.nextInt(k)
        );
    }
}
