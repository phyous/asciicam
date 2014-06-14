package com.phyous.asciicam;

import com.phyous.asciicam.lib.AsciiImage;
import com.phyous.asciicam.lib.ImageUtils;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.FrameGrabber;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.bytedeco.javacpp.opencv_highgui.cvLoadImage;

public class Main {
    private static final int FRAME_DELAY = 50;

    public static void main(String[] args) throws Exception {
	    //AsciiImage.getAsciiImage(args[0]).getProcessedImage(Integer.parseInt(args[1])).forEach(System.out::println);
        IplImage grabbedImage = null;

        AsciiImage overlayAscii = AsciiImage.getAsciiImage(args[0]);
        double gamma = args.length == 2 ? Double.parseDouble(args[0]) : 2.2;

        FrameGrabber grabber = FrameGrabber.createDefault(0);
        grabber.setImageWidth(500);
        grabber.setImageHeight(500);
        grabber.start();
        grabbedImage = grabber.grab();

        while ((grabbedImage = grabber.grab()) != null) {
            BufferedImage bi = grabbedImage.getBufferedImage(2.2/grabber.getGamma());
            //AsciiImage.getAsciiImage(bi).getProcessedImage(80).forEach(System.out::println);

            ImageUtils.addOverlay(AsciiImage.getAsciiImage(bi), overlayAscii, Color.black, 80).
                    forEach(System.out::println);;
            Thread.sleep(FRAME_DELAY);
            Runtime.getRuntime().exec("clear");
        }

    }
}
