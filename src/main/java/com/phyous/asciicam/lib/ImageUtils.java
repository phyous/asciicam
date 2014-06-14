package com.phyous.asciicam.lib;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ImageUtils {
    public static List<String> addOverlay(AsciiImage backgorund, AsciiImage overlay,
                                                Color overlayMask, int width) {
        List<List<TerminalPixel>> backgroundImage = backgorund.getRawImage(width, null);
        List<List<TerminalPixel>> maskedImage = overlay.getRawImage(width, overlayMask);

        int height = backgroundImage.get(0).size();

        return IntStream.range(0, height - 1).mapToObj((int y) -> {
                StringBuilder sb = new StringBuilder();
                IntStream.range(0, width - 1).forEach((int x) -> {
                        String s = maskedImage.get(y).get(x) == null ?
                                backgroundImage.get(y).get(x).getTerminalStr() :
                                maskedImage.get(y).get(x).getTerminalStr();
                    sb.append(s);
                });
                return sb.toString();
        }).collect(Collectors.toList());
    }
}
