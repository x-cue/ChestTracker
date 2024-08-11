package com.xcue.lib;

import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdventureChest {
    private final Long blockPos;
    private final List<ChestStatistics> stats;
    private ZonedDateTime lastLootedTS;
    public static final int RESPAWN_MINUTES = 7;

    public AdventureChest(@NotNull Long blockPos) {
        this.lastLootedTS = null;
        this.blockPos = blockPos;
        this.stats = new ArrayList<>();
    }

    @NotNull
    public BlockPos getBlockPos() {
        return BlockPos.fromLong(blockPos);
    }

    @NotNull
    public Long getBlockPosAsLong() {
        return blockPos;
    }

    /**
     * @return read-only List of stat history
     */
    @NotNull
    public List<ChestStatistics> getStats() {
        return Collections.unmodifiableList(this.stats);
    }

    /**
     *
     * @return Amount of times the chest was looted
     */
    public int getTimesLooted() {
        return stats.size();
    }

    public void loot(ChestStatistics statistics) {
        this.lastLootedTS = statistics.getLootedTS();
        this.stats.add(statistics);
    }

    @Nullable
    public ZonedDateTime getLastLootedTS() {
        return this.lastLootedTS;
    }

    /**
     * @param now Current time
     * @return Whether the chest could have respawned since it was last looted
     */
    public boolean couldHaveRespawned(ZonedDateTime now) {
        ZonedDateTime lastLootedTS = getLastLootedTS();
        return lastLootedTS == null || now.compareTo(getLastLootedTS().plusMinutes(RESPAWN_MINUTES)) > 0;
    }
}
