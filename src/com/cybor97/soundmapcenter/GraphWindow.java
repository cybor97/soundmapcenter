package com.cybor97.soundmapcenter;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class GraphWindow extends JWindow {
    private Graphics graphics;
    private static Color[] colors = {Color.RED, Color.GREEN, Color.BLUE};

    public GraphWindow() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        setSize(255, 255);
        this.setLocation(screenSize.width / 2 - 127, screenSize.height / 2 - 127);
        setVisible(true);
        setAlwaysOnTop(true);
        this.graphics = getGraphics();
    }

    public void drawSoundFrame(byte[] buffer) {
        int[] amplitudes = new int[buffer.length];
        for (int i = 0; i < buffer.length; i++) {
            amplitudes[i] = Math.abs(buffer[i]) * 2;
        }

        SwingUtilities.invokeLater(() -> {
            graphics.clearRect(0, 0, 255, 255);

            for (int i = 0; i < amplitudes.length; i++) {
                int amplitude = amplitudes[i];
                graphics.setColor(colors[i]);
                graphics.drawOval(127 - amplitude / 2, 127 - amplitude / 2, amplitude, amplitude);
            }
        });
    }
}
