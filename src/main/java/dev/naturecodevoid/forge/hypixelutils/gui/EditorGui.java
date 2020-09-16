package dev.naturecodevoid.forge.hypixelutils.gui;

import dev.naturecodevoid.forge.hypixelutils.HypixelUtils;
import dev.naturecodevoid.forge.hypixelutils.features.CoinTracker;
import dev.naturecodevoid.forge.hypixelutils.util.Coordinate2D;
import dev.naturecodevoid.forge.hypixelutils.util.Util;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class EditorGui extends GuiScreen {
    private boolean isDragging = false;
    private int lastX = 0;
    private int lastY = 0;

    public void initGui() {
        this.buttonList.add(new GuiButton(1,
                this.width / 2 - 75,
                this.height - 20,
                150,
                20,
                "Save"
        ));
    }

    public void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 1:
                this.mc.displayGuiScreen(null);
                return;
        }
    }

    @Override
    public void drawScreen(int x, int y, float partialTicks) {
        super.drawDefaultBackground();

        CoinTracker.instance.render(null);

        // TODO: make features draggable

        super.drawScreen(x, y, partialTicks);
    }

    @Override
    public void mouseClicked(int x, int y, int time) throws IOException {
        if ((y != lastY) || (x != lastX)) {
            this.isDragging = true;
            this.lastX = x;
            this.lastY = y;
        }
        super.mouseClicked(x, y, time);
    }

    @Override
    public void mouseClickMove(int x, int y, int lastButtonClicked, long timeSinceClick) {
        if (this.isDragging) {
            Coordinate2D percent = Util.getPercentFromPos(x, y);
            HypixelUtils.config.coinTrackerX = percent.x;
            HypixelUtils.config.coinTrackerY = percent.y;
            if (HypixelUtils.config.coinTrackerX == 99) HypixelUtils.config.coinTrackerX++;
            if (HypixelUtils.config.coinTrackerY == 99) HypixelUtils.config.coinTrackerY++;

            this.lastX = x;
            this.lastY = y;
        }
        super.mouseClickMove(x, y, lastButtonClicked, timeSinceClick);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        HypixelUtils.config.markDirty();
        HypixelUtils.config.writeData();
        Keyboard.enableRepeatEvents(false);
    }
}
