package com.xcue.lib;

import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import net.minecraft.text.Text;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ChestTracker {
    private ChestTracker() {

    }

    static {
        chestMap = new HashMap<>();
        syncId = -1;
    }

    private static int syncId;
    private static Map<Long, AdventureChest> chestMap;
    private static ZonedDateTime chestOpenedTS;
    private static ChestBlockEntity chest;

    //#region Chest Tracking
    public static void setSyncId(int syncId) {
        ChestTracker.syncId = syncId;
    }

    public static int getSyncId() {
        return syncId;
    }

    public static ZonedDateTime getLastOpenedChestTS() {
        return chestOpenedTS;
    }

    public static ChestBlockEntity getLastOpenedChest() {
        return chest;
    }

    public static void setLastOpenedChest(ChestBlockEntity chest) {
        if (chest != null) chestOpenedTS = ZonedDateTime.now(ZoneId.systemDefault());
        ChestTracker.chest = chest;
    }

    public static void resetState() {
        syncId = -1;
        chest = null;
    }

    //#endregion

    //#region Looted Chests
    public static Map<Long, AdventureChest> getLootedChests() {
        return chestMap;
    }

    public static void resetLootedChests() {
        chestMap.clear();
    }

    private static AdventureChest getOrCreateAdventureChest() {
        Long pos = getLastOpenedChest().getPos().asLong();
        return chestMap.getOrDefault(pos, new AdventureChest(pos));
    }

    public static void lootChest(InventoryS2CPacket packet) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
        List<ItemStack> stacks = packet.getContents().stream().limit(27).filter(x -> !x.isOf(Items.AIR)).toList();

        // Chest not empty
        if (stacks.size() != 0) {
            AdventureChest aChest = getOrCreateAdventureChest();

            // Enough time passed since last loot
            if (aChest.couldHaveRespawned(now)) {
                // Create/log chest statistics!
                aChest.loot(new ChestStatistics(now, stacks));
            }
        }
    }
    //#endregion
}
