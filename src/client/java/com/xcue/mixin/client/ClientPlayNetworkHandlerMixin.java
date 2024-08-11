package com.xcue.mixin.client;

import com.xcue.lib.AdventureSession;
import com.xcue.lib.ChestTracker;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

// Mixins are top-bottom in order of when they are called
@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Inject(at = @At("TAIL"), method = "onOpenScreen")
    public void getSyncIdForChestScreen(OpenScreenS2CPacket packet, CallbackInfo info) {
        if (!AdventureSession.isStarted()) return;
        // Return if not tracking a chest
        if (ChestTracker.getLastOpenedChest() == null) return;

        if (Objects.equals(packet.getName().getString(), "Chest")) {
            ChestTracker.setSyncId(packet.getSyncId());
        }
    }

    @Unique
    private static final String advRegEx = "(?:(?:Abandoned Ruins)|(?:Lost Wasteland)|(?:Demonic Realm))";

    @Inject(at = @At("TAIL"), method = "onGameMessage")
    public void cancelChestIfLooted(GameMessageS2CPacket packet, CallbackInfo callbackInfo) {
        String msg = packet.content().getString().trim();

        if (!AdventureSession.isStarted() && msg.matches(String.format("^(?i)\\(!\\) Traveling to the %s Adventure!$", advRegEx))) {
            AdventureSession.startSession();
        } else if (msg.matches("^(?i)\\(!\\) This Chest was previously looted .+ ago$")) {
            ChestTracker.resetState();
        }
    }

    @Inject(at = @At("TAIL"), method = "onInventory")
    public void lootChest(InventoryS2CPacket packet, CallbackInfo info) {
        if (!AdventureSession.isStarted()) return;
        // Return if not tracking a chest
        if (ChestTracker.getLastOpenedChest() == null) return;

        if (packet.getSyncId() == ChestTracker.getSyncId() && packet.getRevision() == 1) {
            ChestTracker.lootChest(packet);
        }

        ChestTracker.resetState();
    }
}