/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package steganography;

import java.awt.Color;
import java.awt.image.BufferedImage;
import utility.ImageUtility;

/**
 *
 * @author varun
 */
public class LSBEncoding {
    
    ImageUtility imageUtility;
    
    public LSBEncoding() {
        imageUtility = new ImageUtility();
    }
    // cover image has indexColorModel
    public void encodeText(BufferedImage coverImage, 
            String message, int bitArray[]) {
        byte image[] = imageUtility.getByteData(coverImage);
        byte payload[] = message.getBytes();
        int offset = 0;
        int imageLength = image.length;
        
        // loop will terminate when either payload or image data will be over
        for (int i = 0; i < payload.length && offset < imageLength; i++, offset++) {
            // one byte payload message
            int byteData = payload[i];
            
            // j is index of bitArray i.e which bit to be modified in image[offset]
            int j = 0;
            
            // loop will run 8 times: for each bit in byteData
            int shiftBit = 7;
            while (shiftBit >= 0 && offset < imageLength) {
                // extarcting bit from byteData (each byte payload message)
                int singleBit = (byteData >>> shiftBit) & 1;
                while (true) {
                    if (j > 7) {
                        offset++;
                        j = 0;
                        break;
                    }
                    if (bitArray[j] == 1) {
                        int mask = returnMask(j);
                        if (singleBit == 0) {
                            image[offset]
                                    = (byte) ((image[offset] & mask));
                        } else {
                            image[offset]
                                    = (byte) ((image[offset] & mask) | ~mask);
                        }System.out.println(image[offset]);
                        j++;
                        shiftBit--;
                        break;
                    }
                    j++;
                }
            }
        }
    }
    
    // cover image has RGB ColorModel
    public void encodeText(BufferedImage coverImage, String message, 
            int bitPlaneArray[], int bitArray[]) {
        byte image[] = imageUtility.getByteData(coverImage);
        byte payload[] = message.getBytes();
        boolean data[] = convertToBits(payload);
        int dataLength = data.length;
        
        // index to track data
        int index = 0;
        
        int height = coverImage.getHeight();
        int width = coverImage.getWidth();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (index >= dataLength) {
                    // to break from outer loop
                    i = width;
                    break;
                }
                // ARGB format
                int pixel = coverImage.getRGB(i, j);
                System.out.println(pixel);
                int alpha = (pixel >> 24) & 0xff;
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel >> 0) & 0xff;
                
                // Alpha Plane
                if (bitPlaneArray[0] == 1) {
                    for (int k = 0; k < bitArray.length; k++) {
                        if (bitArray[k] == 1) {
                            int mask = returnMask(k);
                            alpha = alpha & mask;
                            if (data[index++] == true) {
                                alpha = alpha | ~mask;
                            }
                            if (index >= dataLength) {
                                j = height;
                                i = width;
                                break;
                            }
                        } 
                    }
                }
                
                // Red Plane
                if (bitPlaneArray[0] == 1) {
                    for (int k = 0; k < bitArray.length; k++) {
                        if (bitArray[k] == 1) {
                            int mask = returnMask(k);
                            red = red & mask;
                            if (data[index++] == true) {
                                red = red | ~mask;
                            }
                            if (index >= dataLength) {
                                j = height;
                                i = width;
                                break;
                            }
                        }
                    }
                }
                
                // Green Plane
                if (bitPlaneArray[0] == 1) {
                    for (int k = 0; k < bitArray.length; k++) {
                        if (bitArray[k] == 1) {
                            int mask = returnMask(k);
                            green = green & mask;
                            if (data[index++] == true) {
                                green = green | ~mask;
                            }
                            if (index >= dataLength) {
                                j = height;
                                i = width;
                                break;
                            }
                        }
                    }
                }
                
                // Blue Plane
                if (bitPlaneArray[0] == 1) {
                    for (int k = 0; k < bitArray.length; k++) {
                        if (bitArray[k] == 1) {
                            int mask = returnMask(k);
                            blue = blue & mask;
                            if (data[index++] == true) {
                                blue = blue | ~mask;
                            }
                            if (index >= dataLength) {
                                j = height;
                                i = width;
                                break;
                            }
                        }
                    }
                }
                
                alpha = alpha << 24;
                red = red << 16;
                green = green << 8;
                blue = blue << 0;
                pixel = alpha | red | green | blue;
                coverImage.setRGB(i, j, pixel);
                System.out.println(pixel);
            }
        }
    }
    
    private int returnMask(int bit) {
        int mask = 0xFF;
        switch (bit) {
            case 0:
                mask = 0xFE;
                break;
            case 1:
                mask = 0xFD;
                break;
            case 2:
                mask = 0xFB;
                break;
            case 3:
                mask = 0xF7;
                break;
            case 4:
                mask = 0xEF;
                break;
            case 5:
                mask = 0xDF;
                break;
            case 6:
                mask = 0xBF;
                break;
            case 7:
                mask = 0x7F;
                break;
        }
        return mask;
    }
    
    private boolean[] convertToBits(byte payload[]) {
        boolean result[] = new boolean[8 * payload.length];
        int offset = 0;
        for (byte b: payload) {
            for (int i = 7; i >= 0; i--) {
                int singleBit = (b >> i) & 0x1;
                if (singleBit == 1) {
                    result[offset++] = true;
                }
            }
        }
        return result;
    }
}
