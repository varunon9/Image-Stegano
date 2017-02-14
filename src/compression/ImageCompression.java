/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compression;

/**
 *
 * @author KULDEEP KUMAR
 */
import java.io.*;
import java.util.*;
import java.awt.image.*;

import javax.imageio.*;
import javax.imageio.stream.ImageOutputStream;
public class ImageCompression {
   public static void main(String[] args) throws IOException {
   
      File input = new File("images/image3.jpg");
      BufferedImage image = ImageIO.read(input);

      File compressedImageFile = new File("images/compress.jpg");
      OutputStream os =new FileOutputStream(compressedImageFile);

      Iterator<ImageWriter>writers =  ImageIO.getImageWritersByFormatName("jpg");
      ImageWriter writer = (ImageWriter) writers.next();

      ImageOutputStream ios = ImageIO.createImageOutputStream(os);
      writer.setOutput(ios);

      ImageWriteParam param = writer.getDefaultWriteParam();
      
      param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
      param.setCompressionQuality(0.5f);
      writer.write(null, new IIOImage(image, null, null), param); 
      System.out.println("size of input image in bytes "+input.length()/1024);
      System.out.println("size of output image in bytes "+compressedImageFile.length()/1024);
      os.close();
      ios.close();
      writer.dispose();
   }
}