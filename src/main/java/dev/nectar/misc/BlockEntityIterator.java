package dev.nectar.misc;

import dev.nectar.mixins.accessor.ChunkAccessor;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

public class BlockEntityIterator implements Iterator<BlockEntity> {
    private final Iterator<Chunk> chunks;
    private Iterator<BlockEntity> blockEntities;

    public BlockEntityIterator() {
        this.chunks = new ChunkIterator(false);
        nextChunk();
    }

    @Override
    public boolean hasNext() {
        if (blockEntities == null) return false;
        if (blockEntities.hasNext()) return true;

        nextChunk();

        return blockEntities.hasNext();
    }

    @Override
    public BlockEntity next() {
        return blockEntities.next();
    }

    @Override
    public void remove() {
        Iterator.super.remove();
    }

    @Override
    public void forEachRemaining(Consumer<? super BlockEntity> action) {
        Iterator.super.forEachRemaining(action);
    }

    private void nextChunk() {
        while (true) {
            if (!chunks.hasNext()) break;

            Map<BlockPos, BlockEntity> blockEntityMap = ((ChunkAccessor) chunks.next()).getBlockEntities();

            if (!blockEntityMap.isEmpty()) {
                blockEntities = blockEntityMap.values().iterator();
                break;
            }
        }
    }
}
