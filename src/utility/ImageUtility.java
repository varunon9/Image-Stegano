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
     * this method convert supplied image to suitable type
     * it is needed because we need bytes of array so TYPE_INT images must be
     * converted to BYTE_BGR or so
     * @param originalImage loaded from file-chooser
     * @return 
     */
    
    public BufferedImage convertImage(BufferedImage originalImage) {
        int newImageType = originalImage.getType();
        
        /**
         * Converting int to byte since byte array is needed later to modify 
         * the image
         */
        if (newImageType == BufferedImage.TYPE_INT_RGB
                || newImageType == BufferedImage.TYPE_INT_BGR) {
            newImageType = BufferedImage.TYPE_3BYTE_BGR;
        } else if (newImageType == BufferedImage.TYPE_INT_ARGB) {
            newImageType = BufferedImage.TYPE_4BYTE_ABGR;
        } else if (newImageType == BufferedImage.TYPE_INT_ARGB_PRE) {
            newImageType = BufferedImage.TYPE_4BYTE_ABGR_PRE;
        } else {
            // no need to convert original image
            return null;
        }
        BufferedImage newImage = new BufferedImage(originalImage.getWidth(), 
                originalImage.getHeight(), newImageType);
        Graphics g = newImage.getGraphics();
        g.drawImage(originalImage, 0, 0, null);
        g.dispose();
        return newImage;
    }
    
    /**
     * we don't want to alter original image (actually converted image)
     * since we need this each time for reference
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
