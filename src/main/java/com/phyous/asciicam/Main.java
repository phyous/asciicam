package com.phyous.asciicam;

public class Main {

    public static void main(String[] args) {
	    AsciiImage.getAsciiImage(args[0]).getProcessedImage(Integer.parseInt(args[1])).forEach(System.out::println);
    }
}
