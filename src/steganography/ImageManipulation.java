/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package steganography;

import java.awt.image.BufferedImage;
import utility.ImageUtility;

/**
 *
 * @author varun   
 */
public class ImageManipulation {
    
    public void convertToGrayscale(BufferedImage userSpaceImage) {
        int height = userSpaceImage.getHeight();
        int width = userSpaceImage.getWidth();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = userSpaceImage.getRGB(x, y);
                int a = (pixel >> 24) & 0xff;
                int r = (pixel >> 16) & 0xff;
                int g = (pixel >> 8) & 0xff;
                int b = pixel & 0xff;
                int avg = (r + g + b) / 3;
                pixel = (a << 24) | (avg << 16) | (avg << 8) | avg;
                userSpaceImage.setRGB(x, y, pixel);
            }
        }
    }
    
    public void invertImage(BufferedImage userSpaceImage, int pixelSize) {
        ImageUtility imageUtility = new ImageUtility();
        byte image[] = imageUtility.getByteData(userSpaceImage);
        int bytesPerPixel = pixelSize / 8;
        for (int i = 0; i < image.length; i += bytesPerPixel) {
            for (int j = 0; j < bytesPerPixel; j++) {
                image[i + j] = (byte) (~image[i + j]);
            }
        }
    }
}
