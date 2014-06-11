package com.phyous.asciicam;

import java.awt.*;

public class TerminalPixel {
    private static final int TERMINAL_COLOR_RED = 31;
    private static final int TERMINAL_COLOR_BLUE = 34;
    private static final int TERMINAL_COLOR_GREEN = 32;
    private static final int TERMINAL_COLOR_WHITE = 37;
    private static final int MAX_INTENSITY = 256 * 3;

    private Color mRealPixelColor;
    public TerminalPixel(int rgbInt) {
        mRealPixelColor = new Color(rgbInt);
    }

    public static TerminalPixel getPixel(int rgbInt) {
        return new TerminalPixel(rgbInt);
    }

    public String getTerminalStr() {
        return String.format("\033[1;%dm%s", toTerminalColor(mRealPixelColor), toTerminalCharacter(mRealPixelColor));
    }

    private int toTerminalColor(Color color) {
        if (isLeading(color.getRed(), color.getBlue(), color.getGreen())) {
            return TERMINAL_COLOR_RED;
        } else if (isLeading(color.getBlue(), color.getRed(), color.getGreen())) {
            return TERMINAL_COLOR_BLUE;
        } else if (isLeading(color.getGreen(), color.getRed(), color.getBlue())) {
            return TERMINAL_COLOR_GREEN;
        } else {
            return TERMINAL_COLOR_WHITE;
        }
    }

    private String toTerminalCharacter(Color rgb) {
        int intensity = rgb.getRed() + rgb.getGreen() + rgb.getBlue();
        String character;

        if (intensity < MAX_INTENSITY / 4) {
            character = ".";
        } else if (intensity < (MAX_INTENSITY / 4) * 2) {
            character = "i";
        } else if (intensity < (MAX_INTENSITY / 4) * 3) {
            character = "e";
        } else {
            character = "@";
        }
        return character + character;
    }

    private boolean isLeading(int c1, int c2, int c3) {
        return c1 > c2 && c1 > c3 && c1 * 3 > (c2 + c3) * 2;
    }

}
