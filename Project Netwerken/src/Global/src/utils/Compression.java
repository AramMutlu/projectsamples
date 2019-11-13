package utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class Compression {
    public static byte[] compress(byte[] data) {
        try {
            Deflater deflater = new Deflater(9);
            deflater.setInput(data);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
            deflater.finish();

            byte[] buffer = new byte[1024];
            while (!deflater.finished()) {
                int count = deflater.deflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] decompress(byte[] data) {
        try {
            Inflater inflater = new Inflater();
            inflater.setInput(data);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);

            byte[] buffer = new byte[1024];
            while (!inflater.finished()) {
                int count = 0;
                count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
            return outputStream.toByteArray();
        } catch(DataFormatException e){
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
