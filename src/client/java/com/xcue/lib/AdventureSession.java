package com.xcue.lib;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.text.Text;
import org.joml.Matrix4f;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;

public final class AdventureSession {
    private AdventureSession() {

    }

    static {
        sessionStart = null;
    }

    private static boolean isOnCosmicSky;
    private static ZonedDateTime sessionStart;

    public static void setServer(ServerInfo info) {
        isOnCosmicSky = info.address.equalsIgnoreCase("cosmicsky.net") && info.name.equalsIgnoreCase("cosmic sky");
    }

    public static boolean isOnCosmicSky() {
        return isOnCosmicSky;
    }

    /**
     * Starts an adventure session, enabling the rest of the mod
     */
    public static void startSession() {
        if (isOnCosmicSky() && sessionStart == null) {
            sessionStart = ZonedDateTime.now(ZoneId.systemDefault());

            MinecraftClient.getInstance().player.sendMessage(Text.literal("Adventure session started"));
        }
    }

    /**
     * Terminates the adventure session immediately
     */
    public static void stopSession() {
        stopSession(false);
    }

    /**
     * Ends the adventure session, disabling the rest of the mod
     *
     * @param logStatistics Whether to log the statistics
     */
    public static void stopSession(boolean logStatistics) {
        if (!isStarted()) return;

        // Log all statistics here.
        ZonedDateTime sessionStartCopy = sessionStart;
        sessionStart = null;
        ZonedDateTime sessionEnd = ZonedDateTime.now(ZoneId.systemDefault());
        Map<Long, AdventureChest> chests = ChestTracker.getLootedChests();

        // TODO Get days/hours/minutes formatted time different between start and end (make it a function)
        // TODO Log the session along with all of the chests in a json file and present the data to the player
        // TODO clear ChestTracker AFTERWARDS
        MinecraftClient.getInstance().player.sendMessage(Text.literal("Adventure session stopped"));

    }

    /**
     * @return Whether an adventure session is in progress
     */
    public static boolean isStarted() {
        return sessionStart != null;
    }

    public static ZonedDateTime getSessionStart() {
        return sessionStart;
    }

    public static Map<Long, AdventureChest> getLootedChests() {
        return ChestTracker.getLootedChests();
    }
}
