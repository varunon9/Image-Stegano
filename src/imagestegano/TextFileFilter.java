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
public class TextFileFilter extends FileFilter {

    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        String name = f.getName();
        return isText(name);
    }

    @Override
    public String getDescription() {
        return "Plain Text Only";
    }
    
    public boolean isText(String name) {
        String extension = name.substring(name.lastIndexOf('.') + 1);
        extension = extension.toLowerCase();
        if (extension.equals("text") || extension.equals("txt")) {
            return true;
        }
        return false;
    }
    
}
