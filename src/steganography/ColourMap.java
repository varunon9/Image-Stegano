/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package steganography;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;

/**
 *
 * @author varun
 */
public class ColourMap {
    
    public BufferedImage changeColourMap(BufferedImage userSpaceImage, 
            IndexColorModel customIndexColorModel) {
        ColorModel originalCM = userSpaceImage.getColorModel();
        if (originalCM instanceof IndexColorModel) {
            boolean isAlphaPremultiplied = userSpaceImage.isAlphaPremultiplied();
            WritableRaster raster = userSpaceImage.copyData(null);
            BufferedImage newImage = new BufferedImage(customIndexColorModel, 
                    raster, isAlphaPremultiplied, null);
            return newImage;
        } else {
            return null;
        }
    }
}
