/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagestegano;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author varun
 */
public class ImageFileFilter extends FileFilter {

    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        String name = f.getName();
        return isImage(name);
    }

    @Override
    public String getDescription() {
        return "Images Only";
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
    
    public boolean isPNGOrBMPImage(String name) {
        String extension = name.substring(name.lastIndexOf('.') + 1);
        extension = extension.toLowerCase();
        if (extension.equals("png") || extension.equals("bmp")) {
            return true;
        }
        return false;
    }

}
