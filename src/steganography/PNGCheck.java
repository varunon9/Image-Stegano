/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package steganography;

import java.io.DataInputStream;
import java.util.zip.CRC32;

/**
 *
 * @author varun
 * Reference: https://www.w3.org/TR/PNG-Structure.html
 * https://www.java-tips.org/java-se-tips-100019/23-java-awt-image/2283-png-file-format-decoder-in-java.html
 */
public class PNGCheck {

    /**
     *
     * @param in DataInputStream from InputStream of PNG file
     * @return true if signature matches 0x89504e470d0a1a0aL
     */
    public boolean isPNG(DataInputStream in) {
        try {
            long signature = in.readLong();
            if (signature == 0x89504e470d0a1a0aL) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public byte[] getHiddenData(DataInputStream in) {
        byte[] data;
        boolean moreData = true;
        while (moreData) {
            try {
                // reading length of chunk
                int length = in.readInt();

                // reading type of chunk
                byte[] typeBytes = new byte[4];
                in.readFully(typeBytes);

                // reading data
                data = new byte[length];
                in.readFully(data);

                // Read the CRC (converting int to long)
                long crc = in.readInt() & 0x00000000ffffffffL;
                if (verifyCRC(typeBytes, data, crc) == false) {
                    printHiddenData(typeBytes, data, "Error in data ");
                } else if (verifyChunkType(typeBytes) == false) {
                    printHiddenData(typeBytes, data, "Unknown Chunk ");
                }

            } catch (Exception e) {
                // end of file reached
                moreData = false;
            }
        }
        return null;
    }

    boolean verifyCRC(byte[] typeBytes, byte[] data, long crc) {
        CRC32 crc32 = new CRC32();
        crc32.update(typeBytes);
        crc32.update(data);
        long calculated = crc32.getValue();
        return (calculated == crc);
    }

    /**
     *
     * @param typeBytes byte array of length 4 (chunk type in ASCII)
     * @return true if chunk type is known reference:
     * https://www.w3.org/TR/PNG-Chunks.html
     */
    boolean verifyChunkType(byte typeBytes[]) {
        try {
            String chunkType = new String(typeBytes, "UTF-8");
            switch (chunkType) {
                case "IHDR":
                    return true;
                case "PLTE":
                    return true;
                case "IDAT":
                    return true;
                case "IEND":
                    return true;
                case "cHRM":
                    return true;
                case "gAMA":
                    return true;
                case "sBIT":
                    return true;
                case "bKGD":
                    return true;
                case "hIST":
                    return true;
                case "tRNS":
                    return true;
                case "pHYs":
                    return true;
                case "tIME":
                    return true;
                case "tEXt":
                    return true;
                case "zTXt":
                    return true;
                default:
                    return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    void printHiddenData(byte typeBytes[], byte data[], String message) {
        message += new String(typeBytes);
        int ancillaryBit = (int) (typeBytes[0] >> 5) & 1;
        int privateBit = (int) (typeBytes[1] >> 5) & 1;
        int safeToCopyBit = (int) (typeBytes[3] >> 5) & 1;
        if (ancillaryBit == 1) {
            message += " Ancillary,";
        } else {
            message += " Critical,";
        }
        if (privateBit == 1) {
            message += " Private,";
        } else {
            message += " Public,";
        }
        if (safeToCopyBit == 1) {
            message += " Safe to copy";
        } else {
            message += " Unsafe To copy";
        }
        System.out.println(message);
    }
}
