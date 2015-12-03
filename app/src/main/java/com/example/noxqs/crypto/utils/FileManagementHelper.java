package com.example.noxqs.crypto.utils;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.security.KeyPair;

/**
 * Created by noxqs on 25.11.15..
 */
public class FileManagementHelper {

    public static final String DEFAULT_FILE_LOCATION = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/CryptoFiles/";

    public FileManagementHelper() {
    }

    public static void writePrivateKeyToFile(KeyPair keyPair) {
        File privateKeyFile = new File(DEFAULT_FILE_LOCATION, "privatni_kljuc.txt");

        try {
            privateKeyFile.createNewFile();
            FileOutputStream f = new FileOutputStream(privateKeyFile);
            PrintWriter pw = new PrintWriter(f);
            pw.write(keyPair.getPrivate().toString());
            pw.flush();
            pw.close();
            f.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writePublicKeyToFile(KeyPair key) {
        File publicKeyFile = new File(DEFAULT_FILE_LOCATION, "javni_kljuc.txt");
        try {
            publicKeyFile.createNewFile();
            FileOutputStream f = new FileOutputStream(publicKeyFile);
            PrintWriter pw = new PrintWriter(f);
            pw.write(key.getPublic().toString());
            pw.flush();
            pw.close();
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeToFile(String data, String filename){
        File file = new File(DEFAULT_FILE_LOCATION + filename);
        try {
            file.createNewFile();
            FileOutputStream f = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(f);
            pw.write(data);
            pw.flush();
            pw.close();
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String readFromFile(String filename){
        String line;
        String text = "";

        try{
            FileReader fileReader = new FileReader(DEFAULT_FILE_LOCATION + filename);
            BufferedReader br = new BufferedReader(fileReader);

            while((line = br.readLine()) != null){
                text += line;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return text;
    }

}
