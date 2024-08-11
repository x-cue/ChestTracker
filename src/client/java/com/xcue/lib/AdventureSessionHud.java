package com.xcue.lib;

import com.xcue.lib.util.DateTimeUtils;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;

public class AdventureSessionHud implements HudRenderCallback {
    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {
        if (AdventureSession.isStarted()) {
            TextRenderer tr = MinecraftClient.getInstance().textRenderer;

            render(drawContext, tr);
        }
    }

    private void render(DrawContext drawContext, TextRenderer tr) {
        int rgb = 65535; // Aqua
        String sessionTime = DateTimeUtils.getTimeSince(AdventureSession.getSessionStart(), DateTimeUtils.now());
        int chestsLooted = AdventureSession.getLootedChests().values().stream().mapToInt(AdventureChest::getTimesLooted).sum();
        List<String> text = new ArrayList<>() {{
            add("Session Time: " + sessionTime);
            add("Chests Looted: " + chestsLooted);
        }};
        drawText(drawContext, tr, text, rgb);
    }

    private void drawText(DrawContext drawContext, TextRenderer tr, List<String> text, int rgb) {
        int padding = 5;
        int posx = 30;
        int posy = 30;
        int color = 0x80000000;

        // 20
        int boxx = posx - padding;
        // 20
        int boxy = posy - padding + 1;

        int boxx2 = padding + posx;

        int boxy2 = posy + padding - 2;

        int longestTxtWidth = 0;

        for (int i = 0; i < text.size(); i++) {
            String line = text.get(i);
            int linePadding = i != 0 ? 2 : 0;
            longestTxtWidth = Math.max(tr.getWidth(line), longestTxtWidth);

            drawContext.drawText(tr, line, posx, posy + linePadding, rgb, false);
            int height = tr.getWrappedLinesHeight(line, boxx2 - 1 + longestTxtWidth);

            boxy2 += height;
            posy += height;
        }

        boxx2 += longestTxtWidth;

        drawContext.fill(boxx, boxy, boxx2, boxy2, color);
        drawContext.draw();
    }
}
