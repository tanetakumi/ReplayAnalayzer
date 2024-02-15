package net.serveron.hane;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {
        File dir = new File("replay-directory");
        if(!dir.exists()){
            boolean _a = dir.mkdirs();
        }
        File[] files = dir.listFiles();
        for(File f : files){
            String filename = f.getName();
            Pattern pattern = Pattern.compile("^(.+)(\\.[^.]+)$");
            Matcher matcher = pattern.matcher(filename);

            if(matcher.find()){
                String name = matcher.group(1);
                String extension = matcher.group(2);
                if(extension.equals(".mcpr")){
                    File extractDir = new File(dir, name);
                    if(!extractDir.exists()){
                        boolean _a = extractDir.mkdirs();
                        extractReplay(f.getAbsolutePath(), extractDir);
                        System.out.println("解凍しました。");
                    } else {
                        analyzeDir(extractDir);
                    }
                }
            }
        }
    }

    private static void analyzeDir(File extractDir){
        Pattern pattern = Pattern.compile("^(.+)(\\.[^.]+)$");
        File[] files = extractDir.listFiles();
        for(File f : files){
            Matcher matcher = pattern.matcher(f.getName());
            if(matcher.find()){
                String extension = matcher.group(2);
                if(extension.equals(".tmcpr")){
                    analyze(f.getAbsolutePath());
                }
            }
        }
    }

    public static void analyze(String filepath) {
        Protocol pro = new Protocol();
        try (FileInputStream fis = new FileInputStream(filepath);
             DataInputStream dis = new DataInputStream(fis);
        ) {
            while (dis.available() > 0) {
                int timestamp = readInteger(dis);
                int size = readInteger(dis);
                int id = dis.readByte();
                byte[] packet = new byte[size-1];
                dis.readFully(packet);
                System.out.printf("Timestamp: %5d, Size: %6d, ID: %s, Name: %s\n", timestamp, size, String.format("0x%02X", id), pro.getProtocolName(id));
            }
        } catch (IOException e) {
            e.fillInStackTrace();
        }
    }

    public static int readInteger(DataInputStream dis) throws IOException {
        byte[] intBytes = new byte[4];
        dis.readFully(intBytes);
        ByteBuffer buffer = ByteBuffer.wrap(intBytes);
        buffer.order(ByteOrder.BIG_ENDIAN);
        return buffer.getInt();
    }

    public static void extractReplay(String zipFilePath, File extractDir) {
        try (FileInputStream fis = new FileInputStream(zipFilePath);
             ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis))) {

            ZipEntry entry;
            byte[] buffer = new byte[4096];

            while ((entry = zis.getNextEntry()) != null) {
                File file = new File(extractDir, entry.getName());

                if (entry.isDirectory()) {
                    boolean _a = file.mkdirs();
                } else {
                    try (FileOutputStream fos = new FileOutputStream(file);
                         BufferedOutputStream bos = new BufferedOutputStream(fos, buffer.length)) {

                        int bytesRead;
                        while ((bytesRead = zis.read(buffer, 0, buffer.length)) != -1) {
                            bos.write(buffer, 0, bytesRead);
                        }
                    }
                }
                zis.closeEntry();
            }

        } catch (IOException e) {
            e.fillInStackTrace();
        }
    }

    public static void compressReplay(String filepath, File extractDir){
        try (FileOutputStream fos = new FileOutputStream(filepath);
             ZipOutputStream zos = new ZipOutputStream(fos)
        ){
            for(File f : Objects.requireNonNull(extractDir.listFiles(File::isFile))){
                zos.putNextEntry(new ZipEntry(f.getName()));
                byte[] buffer = new byte[4096];
                int bytesRead;
                try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f))) {
                    while ((bytesRead = bis.read(buffer)) != -1) {
                        zos.write(buffer, 0, bytesRead);
                    }
                }
                zos.closeEntry();
            }
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }
}