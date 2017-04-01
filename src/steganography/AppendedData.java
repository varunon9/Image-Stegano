/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package steganography;

import java.io.DataInputStream;

/**
 *
 * @author varun
 */
public class AppendedData {
    
    public String getPNGAppendedData(DataInputStream in) {
        StringBuilder message = new StringBuilder();
        try {
            // reading signature
            in.readLong();
            
            boolean moreData = true;
            byte[] data;
            String chunkType;
            
            // reading upto end of PNG file i.e IEND chunk
            while (true) {
                try {
                    // reading length of chunk
                    int length = in.readInt();

                    // reading type of chunk
                    byte[] typeBytes = new byte[4];
                    in.readFully(typeBytes);

                    // reading data
                    data = new byte[length];
                    in.readFully(data);

                    // Read the CRC
                    int crc = in.readInt();
                    
                    chunkType = new String(typeBytes, "UTF-8");
                    if (chunkType.equals("IEND")) {
                        break;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            // reading appended data (byte by byte) if any
            while (moreData) {
                try {
                    byte b = in.readByte();
                    message.append(String.format("%02X", b));
                } catch(Exception e) {
                    moreData = false;
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        return message.toString();
    }
    
    /**
     * Reference: https://en.wikipedia.org/wiki/JPEG
     * @param in data input stream of opened file
     * @return appended data
     */
    public String getJPEGAppendedData(DataInputStream in) {
        String message = "";
        return message;
    }
    
    public String getBMPAppendedData(DataInputStream in) {
        StringBuilder message = new StringBuilder();
        try {
            // reading first two bytes
            in.readFully(new byte[2]);
            
            // reading size in little endian
            byte buffer[] = new byte[4];
            in.readFully(buffer);
            int size = (buffer[0] & 0xFF) | (buffer[1] & 0xFF) << 8 ;
            size = size | (buffer[2] & 0xFF) << 16 | (buffer[3] & 0xFF) << 24;
            
            // skipping bytes equal to size (2 + 4 bytes deducted)
            for (int i = 1; i <= size - 6; i++) {
                in.readByte();
            }
            
            // reading appended data (byte by byte) if any
            boolean moreData = true;
            while (moreData) {
                try {
                    byte b = in.readByte();
                    message.append(String.format("%02X", b));
                } catch (Exception e) {
                    moreData = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message.toString();
    }
}
