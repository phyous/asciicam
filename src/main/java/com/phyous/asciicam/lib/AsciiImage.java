package com.phyous.asciicam.lib;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AsciiImage {
    private static final int DEFAULT_IMAGE_OUTPUT_WIDTH = 80;
    private BufferedImage mBufferedImage;
    private List<List<TerminalPixel>> rawImage;

    public AsciiImage(String filePath) {
        try {
            mBufferedImage = ImageIO.read(Files.newInputStream(Paths.get(filePath)));
        } catch (IOException e) {
            System.err.println("Error opening input file");
        }
    }

    public AsciiImage(BufferedImage bufferedImage) {
        mBufferedImage = bufferedImage;
    }

    public static AsciiImage getAsciiImage(String filePath) {
        return new AsciiImage(filePath);
    }

    public static AsciiImage getAsciiImage(BufferedImage bufferedImage) {
        return new AsciiImage(bufferedImage);
    }

    public List<String> getProcessedImage() {
        return getProcessedImage(DEFAULT_IMAGE_OUTPUT_WIDTH);
    }

    public List<String> getProcessedImage(int width) {
        int scaledHeight = (mBufferedImage.getHeight() * width) / mBufferedImage.getWidth();
        return getProcessedImage(width, scaledHeight);
    }

    public List<String> getProcessedImage(int width, int height) {
        return this.getRawImage(width, height, null).stream().map(row -> {
            StringBuilder sb = new StringBuilder();
            row.forEach(pixel -> {
                if(pixel == null) sb.append(" ");
                else sb.append(pixel.getTerminalStr());
            });
            return sb.toString();
        }).collect(Collectors.toList());
    }

    public List<List<TerminalPixel>> getRawImage(int width, Color mask) {
        int scaledHeight = (mBufferedImage.getHeight() * width) / mBufferedImage.getWidth();
        return getRawImage(width, scaledHeight, mask);
    }

    public List<List<TerminalPixel>> getRawImage(int width, int height, Color mask) {
        rawImage = IntStream.range(0, height).mapToObj((int y) ->
                IntStream.range(0, width).mapToObj((int x) -> {
                    TerminalPixel p = TerminalPixel.getPixel(mBufferedImage.getRGB(x * widthScaleFactor(width),
                            y * heightScaleFactor(height)));
                    if (mask == null) {
                        return p;
                    } else {
                        if(p.getRealColor().equals(mask)) {
                            p.setMasked(true);
                            return p;
                        }
                        else return null;
                    }
                }).collect(Collectors.toCollection(ArrayList<TerminalPixel>::new)))
                .collect(Collectors.toList());
        return rawImage;
    }

    private int widthScaleFactor(int width) {
        return mBufferedImage.getWidth() / width;
    }

    private int heightScaleFactor(int height) {
        return mBufferedImage.getHeight() / height;
    }
}
