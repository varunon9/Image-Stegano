/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import javax.imageio.ImageIO;

/**
 *
 * @author varun
 */
public class ImageUtility {
    
    /**
     * @param coverImage original carrier/cover image
     * @return a copy of supplied image
     */
    public BufferedImage copyImage(BufferedImage coverImage) {
        ColorModel colorModel = coverImage.getColorModel();
        boolean isAlphaPremultiplied = coverImage.isAlphaPremultiplied();
        WritableRaster raster = coverImage.copyData(null);
        BufferedImage newImage = new BufferedImage(colorModel, raster,
                isAlphaPremultiplied, null);
        return newImage;
    }
    
    /**
     * @param userSpaceImage
     * @return byte array of supplied image
     */
    public byte[] getByteData(BufferedImage userSpaceImage) {
        WritableRaster raster = userSpaceImage.getRaster();
        DataBufferByte buffer = (DataBufferByte) raster.getDataBuffer();
        return buffer.getData();
    }
    
    public String getNewFileName(String name) {
        int dotIndex = name.lastIndexOf('.');
        String extension = name.substring(dotIndex + 1);
        int endIndex = dotIndex - 1;
        String nameWithoutExtension = name.substring(0, endIndex);
        String newName = nameWithoutExtension + "-steg." + extension;
        return newName;
    }
    
    public boolean isImage(String name) {
        String extension = name.substring(name.lastIndexOf('.') + 1);
        extension = extension.toLowerCase();
        if (extension.equals("png") || extension.equals("gif")
                || extension.equals("jpeg") || extension.equals("jpg")
                || extension.equals("bmp") || extension.equals("tif")
                || extension.equals("tiff")) {
            return true;
        }
        return false;
    }
    
    /**
     * @param image image-file to save
     * @param file to save image to
     */
    public void saveImage(BufferedImage image, File file) {
        String name = file.getName();
        String extension = name.substring(name.lastIndexOf('.') + 1);
        try {
            ImageIO.write(image, extension, file);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
