/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package steganography;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import utility.ImageUtility;

/**
 *
 * @author varun
 */
public class BPCS {
    
    public void singlePlaneBPCS(BufferedImage userSpaceImage, int bpcsIndex, 
            int pixelSize) {
        ImageUtility imageUtility = new ImageUtility();
        byte image[] = imageUtility.getByteData(userSpaceImage);
        int bytesPerPixel = pixelSize / 8;
        bpcsIndex = pixelSize - bpcsIndex - 1;
        int start = bpcsIndex / 8;
        bpcsIndex = bpcsIndex % 8;
        int shift = 7 - bpcsIndex;
        for (int i = 0; i < image.length; i += bytesPerPixel) {
            for (int j = 0; j < bytesPerPixel; j++) {
                if (j != start) {
                    image[i + j] = 0x00;
                }
            }
            int singleBit = (image[i + start] >> shift) & 1;
            if (singleBit == 0) {
                image[i + start] = 0x00;
            } else {
                image[i + start] = (byte) 128;
            }
        }
    }
    
    public void allPlaneBPCS(BufferedImage userSpaceImage, int bpcsIndex, 
            int pixelSize) {
        ImageUtility imageUtility = new ImageUtility();
        byte image[] = imageUtility.getByteData(userSpaceImage);
        int bytesPerPixel = pixelSize / 8;
        int shift = 7 - bpcsIndex;
        for (int i = 0; i < image.length; i += bytesPerPixel) {
            for (int j = 0; j < bytesPerPixel; j++) {
                int singleBit = (image[i + j] >> shift) & 1;
                if (singleBit == 0) {
                    image[i + j] = 0x00;
                } else {
                    image[i + j] = (byte) 128;
                }
            }
        }
    }
}
