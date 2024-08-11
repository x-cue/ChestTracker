package com.xcue;

import com.xcue.lib.AdventureSession;
import com.xcue.lib.AdventureSessionHud;
import com.xcue.lib.ChestTracker;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

public class IslandAutoSwingClient implements ClientModInitializer {
    final int[] i = {0};

    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.

        ClientPlayConnectionEvents.JOIN.register(((handler, sender, client) -> {
            AdventureSession.setServer(Objects.requireNonNull(handler.getServerInfo()));
            AdventureSession.stopSession();
        }));

        UseBlockCallback.EVENT.register(((player, world, hand, hitResult) -> {
            if (AdventureSession.isStarted()) {

                BlockPos pos = hitResult.getBlockPos();
                if (world.getBlockEntity(pos) instanceof ChestBlockEntity chest) {
                    // Set last opened chest here. Very next packet received should be SetContainer
                    ChestTracker.setLastOpenedChest(chest);
                }
            }

            return ActionResult.PASS;
        }));

        // Register Adventure HUD
        HudRenderCallback.EVENT.register(new AdventureSessionHud());
    }
}