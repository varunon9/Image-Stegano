/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package steganography;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import javax.swing.JLabel;

/**
 *
 * @author varun
 */
public class ColourMap {
    
    public BufferedImage changeColourMap(BufferedImage userSpaceImage, 
            int colourMapIndex, JLabel nameLabel) {
        ColorModel originalCM = userSpaceImage.getColorModel();
        if (originalCM instanceof IndexColorModel) {
            IndexColorModel cm = getIndexColorModel(colourMapIndex, 
                    (IndexColorModel) originalCM);
            BufferedImage newImage = new BufferedImage(userSpaceImage.getWidth(),
                    userSpaceImage.getHeight(),
                    BufferedImage.TYPE_BYTE_INDEXED, cm);
            Graphics g = newImage.getGraphics();
            g.drawImage(userSpaceImage, 0, 0, null);
            g.dispose();
            nameLabel.setText("Colour Map: " + colourMapIndex);
            return newImage;
        } else {
            return userSpaceImage;
        }
    }
    
    private IndexColorModel getIndexColorModel(int index, 
            IndexColorModel originalCM) {
        int colourCount = originalCM.getMapSize();
        byte[] reds = new byte[colourCount];
        byte[] greens = new byte[colourCount];
        byte[] blues = new byte[colourCount];
        originalCM.getReds(reds);
        originalCM.getBlues(blues);
        originalCM.getGreens(greens);
        for (int i = 0; i < colourCount; i++) {
            reds[i] = (byte) (55 + reds[i]);
            //greens[i] = 0;
            //blues[i] = 0;
        }
        IndexColorModel cm = new IndexColorModel(8, colourCount, reds, greens, blues);
        return cm;
    }
}
