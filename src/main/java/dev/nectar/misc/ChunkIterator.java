package dev.nectar.misc;

import dev.nectar.Nectar;
import dev.nectar.mixins.accessor.ClientChunkManagerAccessor;
import dev.nectar.mixins.accessor.ClientChunkMapAccessor;
import net.minecraft.world.chunk.Chunk;

import java.util.Iterator;
import java.util.function.Consumer;

public class ChunkIterator implements Iterator<Chunk> {
    private final ClientChunkMapAccessor map = (ClientChunkMapAccessor) (Object) ((ClientChunkManagerAccessor) Nectar.mc.world.getChunkManager()).sedna$getChunks();
    private final boolean onlyWithLoadedNeighbours;

    private int i = 0;
    private Chunk chunk;

    public ChunkIterator(boolean onlyWithLoadedNeighbours) {
        this.onlyWithLoadedNeighbours = onlyWithLoadedNeighbours;

        getNext();
    }

    private Chunk getNext() {
        Chunk prev = chunk;
        chunk = null;

        while (i < map.sedna$getChunks().length()) {
            chunk = map.sedna$getChunks().get(i++);
            if (chunk != null && (!onlyWithLoadedNeighbours || isInRadius(chunk))) break;
        }

        return prev;
    }

    private boolean isInRadius(Chunk chunk) {
        int x = chunk.getPos().x;
        int z = chunk.getPos().z;

        return Nectar.mc.world.getChunkManager().isChunkLoaded(x + 1, z) && Nectar.mc.world.getChunkManager().isChunkLoaded(x - 1, z) && Nectar.mc.world.getChunkManager().isChunkLoaded(x, z + 1) && Nectar.mc.world.getChunkManager().isChunkLoaded(x, z - 1);
    }

    @Override
    public boolean hasNext() {
        return chunk != null;
    }

    @Override
    public Chunk next() {
        return getNext();
    }

    @Override
    public void remove() {
        Iterator.super.remove();
    }

    @Override
    public void forEachRemaining(Consumer<? super Chunk> action) {
        Iterator.super.forEachRemaining(action);
    }
}
