package ru.sbt.course.CyptFile;
/**
 * Вспомогательная программа для шифрования (дешифрования) файлов.
 * Алгоритм шифрования: сдвигом влево каждого байта на key бит. Дешифрование - сдвиг вправо.
 *
 * @author Hin7
 * @version 1.0 27/03/2020
 */


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;

import static java.nio.file.StandardOpenOption.*;

public class CryptFile {
    static public void main(String[] args) {
        int key = 3;
        String sourceFilesDir = "c:\\Java\\pluginRootDirectory\\pluginName\\";
        String cryptFilesDir = "c:\\Java\\pluginRootDirectory\\cryptPluginName\\";
        String backFilesDir = "c:\\Java\\pluginRootDirectory\\uncryptPluginName\\";
        String[] files = {"Print123Plugin.class", "PrintHelloPlugin.class", "PrintGoodbyePlugin.class"};

        CryptFile cf = new CryptFile();
        for (String file : files)
            cf.crypt(sourceFilesDir + file, cryptFilesDir + file, key);

        for (String file : files)
            cf.uncrypt(cryptFilesDir + file, backFilesDir + file, key);


    }

    void crypt(String inFileName, String outFileName, int key) {
        try {
            byte[] data = Files.readAllBytes(Paths.get(inFileName));
            byte[] cdata = new byte[data.length];
            int i = 0;
            //собственно шифрование
            for (byte b : data) {
                int ib = b & 0xFF;
                cdata[i++] = (byte)((ib << key)| ((ib >> (8 - key))));
            }
            OpenOption[] openOptions = new OpenOption[]{WRITE, CREATE, TRUNCATE_EXISTING};
            Files.write(Paths.get(outFileName), cdata, openOptions);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void uncrypt(String inFileName, String outFileName, int key){
        try{
            byte[] data = Files.readAllBytes(Paths.get(inFileName));
            byte[] cdata = new byte[data.length];
            int i = 0;
            //собственно дешифрование
            for (byte b : data) {
                int ib = b & 0xFF;
                cdata[i++] = (byte)((ib >> key)| ((ib << (8 - key))));
            }
            OpenOption[] openOptions = new OpenOption[]{WRITE, CREATE, TRUNCATE_EXISTING};
            Files.write(Paths.get(outFileName), cdata, openOptions);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
