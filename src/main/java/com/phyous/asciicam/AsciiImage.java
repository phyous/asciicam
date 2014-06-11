package com.phyous.asciicam;

import javax.imageio.ImageIO;
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
    private List<String> processedImage;

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
        processedImage = IntStream.range(0, height).mapToObj(y -> {
            StringBuilder sb = new StringBuilder();
            IntStream.range(0, width).forEach(x -> {
                sb.append(
                        TerminalPixel.getPixel(
                                mBufferedImage.getRGB(
                                        x * widthScaleFactor(width),
                                        y * heightScaleFactor(height)))
                                .getTerminalStr());
            }
            );
            return sb.toString();
        }).collect(Collectors.toCollection(ArrayList<String>::new));
        return processedImage;
    }

    private int widthScaleFactor(int width) {
        return mBufferedImage.getWidth() / width;
    }

    private int heightScaleFactor(int height) {
        return mBufferedImage.getHeight() / height;
    }
}
