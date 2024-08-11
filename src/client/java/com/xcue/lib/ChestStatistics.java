package com.xcue.lib;

import net.minecraft.item.ItemStack;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Data representation of looting a chest
 */
public class ChestStatistics {
    private final ZonedDateTime lootedTS;
    private final List<ItemStack> items;

    public ChestStatistics(ZonedDateTime lootedTS, List<ItemStack> items) {
        this.lootedTS = lootedTS;
        this.items = items;
    }

    /**
     * @return ZonedDateTime the chest was looted
     */
    public ZonedDateTime getLootedTS() {
        return this.lootedTS;
    }

    /**
     * @return Unmodifiable List of items that were in the chest
     */
    public List<ItemStack> getItems() {
        return Collections.unmodifiableList(this.items);
    }
}
