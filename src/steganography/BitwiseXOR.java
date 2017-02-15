/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package steganography;

import java.awt.image.BufferedImage;
import javax.swing.JLabel;
import utility.ImageUtility;

/**
 *
 * @author varun
 */
public class BitwiseXOR {
    
    /**
     * Let source bit = s and target bit = t
     * Let MSB of hidden image is h
     * then t = s ^ h and h = s ^ t
     * format (i){s, t} where i is index, s is source bit and t is target bit
     * (1){7, 0} (2){7, 1} (3){7, 2} (4){7, 3}
     * (5){6, 0} (6){6, 1} (7){6, 2} (8){6, 3}
     * (9){5, 0} (10){5, 1} (11){5, 2} (12){5, 3}
     * (13){4, 0} (14){4, 1} (15){4, 2} (16){4, 3}
     * (17){3, 0} (18){3, 1} (19){3, 2}
     * (20){2, 0} (21){2, 1} (22){2, 3}
     * (23){1, 0} (24){1, 2} (25){1, 3}
     * (26){0, 1} (27){0, 2} (28){0, 3}
     * @param index value from [1, 28]
     * @return an array of size 2. first value is source bit 
     * and second value is target bit (where data is XORed using source bit)
     */
    private int[] mapping(int index) {
        
        // mapping[0] is source bit and mapping[1] is target bit
        int mapping[] = new int[2];
        
        if (index <= 4) {
            mapping[0] = 7;
            mapping[1] = index - 1;
        } else if (index <= 8) {
            mapping[0] = 6;
            mapping[1] = index - 4 - 1;
        } else if (index <= 12) {
            mapping[0] = 5;
            mapping[1] = index - 8 - 1;
        } else if (index <= 16) {
            mapping[0] = 4;
            mapping[1] = index - 12 - 1;
        } else if (index <= 19) {
            mapping[0] = 3;
            mapping[1] = index - 16 - 1;
        } else if (index <= 22) {
            mapping[0] = 2;
            switch (index) {
                case 20: {
                    mapping[1] = 0;
                    break;
                }
                case 21: {
                    mapping[1] = 1;
                    break;
                }
                case 22: {
                    mapping[1] = 3;
                    break;
                }
            }
        } else if (index <= 25) {
            mapping[0] = 1;
            switch (index) {
                case 23: {
                    mapping[1] = 0;
                    break;
                }
                case 24: {
                    mapping[1] = 2;
                    break;
                }
                case 25: {
                    mapping[1] = 3;
                    break;
                }
            }
        } else if (index <= 28) {
            mapping[0] = 0;
            switch (index) {
                case 26: {
                    mapping[1] = 1;
                    break;
                }
                case 27: {
                    mapping[1] = 2;
                    break;
                }
                case 28: {
                    mapping[1] = 3;
                    break;
                }
            }
        }
        return mapping;
    }
    
    public void xor(BufferedImage userSpaceImage, int bitwiseXORIndex,
            int pixelSize, JLabel nameLabel) {
        int mapping[] = mapping(bitwiseXORIndex);
        int sourceBit = mapping[0];
        int targetBit = mapping[1];
        ImageUtility imageUtility = new ImageUtility();
        byte image[] = imageUtility.getByteData(userSpaceImage);
        int bytesPerPixel = pixelSize / 8;
        for (int i = 0; i < image.length; i += bytesPerPixel) {
            for (int j = 0; j < bytesPerPixel; j++) {
                int sourceBitValue = (image[i + j] >> sourceBit) & 1;
                int targetBitValue = (image[i + j] >> targetBit) & 1;
                int singleBit = sourceBitValue ^ targetBitValue;
                if (singleBit == 0) {
                    image[i + j] = 0x00;
                } else {
                    image[i + j] = (byte) 128;
                }
            }
        }
        nameLabel.setText("Source bit: " + sourceBit + " Target bit: " +
                targetBit);
    }
}
