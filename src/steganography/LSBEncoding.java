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
public class LSBEncoding {
    
    ImageUtility imageUtility;
    
    public LSBEncoding() {
        imageUtility = new ImageUtility();
    }
    
    public void encodeText(BufferedImage coverImage, 
            String message, int bitArray[]) {
        byte image[] = imageUtility.getByteData(coverImage);
        byte payload[] = message.getBytes();
        int offset = 0;
        int imageLength = image.length;
        boolean data[] = convertToBits(payload);
        int dataLength = data.length;
        int dataOverFlag = 0;
        for (int i = 0; i < imageLength && dataOverFlag == 0; i++) {
            for (int j = 7; j >= 0  && dataOverFlag == 0; j--) {
                if (bitArray[j] == 1) {
                    int mask = returnMask(j);
                    image[i] = (byte) ((image[i] & mask));
                    if (data[offset++]) {
                        image[i] = (byte) (image[i] | ~mask);
                    }
                    if (offset >= dataLength) {
                        dataOverFlag = 1;
                    }
                }
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
                int singleBit = (b >> i) & 1;
                if (singleBit == 1) {
                    result[offset++] = true;
                } else {
                    result[offset++] = false;
                }
            }
        }
        return result;
    }
    
    public String decodeText(BufferedImage coverImage, int bitArray[]) {
        byte image[] = imageUtility.getByteData(coverImage);
        int offset = 0;
        int imageLength = image.length;
        
        // counting how many bits are modified per byte
        int count = 0;
        for (int i = 0; i < bitArray.length; i++) {
            if (bitArray[i] == 1) {
                count++;
            }
        }
        
        boolean data[] = new boolean[imageLength * count];
        for (int i = 0; i < imageLength; i++) {
            for (int j = 7; j >= 0; j--) {
                if (bitArray[j] == 1) {
                    int singleBit = (image[i] >> j) & 1;
                    if (singleBit == 1) {
                        data[offset++] = true;
                    } else {
                        data[offset++] = false;
                    }
                }
            }
        }
        
        // converting boolean array to byte array
        int secretMessageLength = (imageLength * count) / 8;
        byte secretMessage[] = new byte[secretMessageLength];
        for (int i = 0; i < secretMessageLength; i++) {
            for (int bit = 0; bit < 8; bit++) {
                if (data[i * 8 + bit]) {
                    secretMessage[i] |= (128 >> bit);
                }
            }
        }
        try {
            return new String(secretMessage, "ASCII");
        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
