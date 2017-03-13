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
public class BitPlane {
    
    public void singlePlane(BufferedImage userSpaceImage, int bpcsIndex, 
            int pixelSize) {
        ImageUtility imageUtility = new ImageUtility();
        byte image[] = imageUtility.getByteData(userSpaceImage);
        int bytesPerPixel = pixelSize / 8;
        /**
         * value of bpcsIndex -
         * [0, 7] blue plane where 0 is MSB and 7 is LSB
         * [8, 15] green plane where 8 is MSB and 15 is LSB
         * [16, 23] red plane
         * if alpha channel is present then [0, 7] alpha plane, 0 is MSB and
         * all other planes is shifted by one byte
         * color model is ABGR or BGR (in byte array)
         */
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
    
    public void allPlane(BufferedImage userSpaceImage, int bpcsIndex, 
            int pixelSize) {
        ImageUtility imageUtility = new ImageUtility();
        byte image[] = imageUtility.getByteData(userSpaceImage);
        int bytesPerPixel = pixelSize / 8;
        /**
         * value of bpcsIndex
         * -8 = 7 and -1 = 0
         * [-8, -1] where -8 is MSB and -1 is LSB
         */
        bpcsIndex = (bpcsIndex * -1) - 1;
        for (int i = 0; i < image.length; i += bytesPerPixel) {
            for (int j = 0; j < bytesPerPixel; j++) {
                int singleBit = (image[i + j] >> bpcsIndex) & 1;
                if (singleBit == 0) {
                    image[i + j] = 0x00;
                } else {
                    image[i + j] = (byte) 128;
                }
            }
        }
    }
    
    public void hideImage(BufferedImage coverImage, BufferedImage targetImage,
            int lsb, boolean invertBits) {
        ImageUtility imageUtility = new ImageUtility();
        byte cover[] = imageUtility.getByteData(coverImage);
        byte target[] = imageUtility.getByteData(targetImage);
        int coverLength = cover.length;
        int targetLength = target.length;
        int i = 0;
        while (i < coverLength && i < targetLength) {
            int msbOfTarget = (target[i] >> 7) & 1;
            if (invertBits) {
                msbOfTarget = (~msbOfTarget) & 1;
            }
            switch (lsb) {
                case 0: {
                    cover[i] = (byte) ((cover[i] & 0xFE) | msbOfTarget);
                    break;
                }
                case 1: {
                    cover[i] = (byte) ((cover[i] & 0xFD) | 2 * msbOfTarget);
                    break;
                }
                case 2: {
                    cover[i] = (byte) ((cover[i] & 0xFB) | 4 * msbOfTarget);
                    break;
                }
                case 3: {
                    cover[i] = (byte) ((cover[i] & 0xF7) | 8 * msbOfTarget);
                    break;
                }
            }
            i++;
        }
    }
    
    public void hideImage(BufferedImage coverImage, BufferedImage targetImage,
            int lsb, int upperBit) {
        ImageUtility imageUtility = new ImageUtility();
        byte cover[] = imageUtility.getByteData(coverImage);
        byte target[] = imageUtility.getByteData(targetImage);
        int coverLength = cover.length;
        int targetLength = target.length;
        int i = 0;
        while (i < coverLength && i < targetLength) {
            int msbOfTarget = (target[i] >> 7) & 1;
            int upperBitValue = (cover[i] >> upperBit) & 1;
            msbOfTarget = msbOfTarget ^ upperBitValue;
            switch (lsb) {
                case 0: {
                    cover[i] = (byte) ((cover[i] & 0xFE) | msbOfTarget);
                    break;
                }
                case 1: {
                    cover[i] = (byte) ((cover[i] & 0xFD) | 2 * msbOfTarget);
                    break;
                }
                case 2: {
                    cover[i] = (byte) ((cover[i] & 0xFB) | 4 * msbOfTarget);
                    break;
                }
                case 3: {
                    cover[i] = (byte) ((cover[i] & 0xF7) | 8 * msbOfTarget);
                    break;
                }
            }
            i++;
        }
    }
}
