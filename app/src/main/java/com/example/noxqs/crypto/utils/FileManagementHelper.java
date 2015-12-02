package com.example.noxqs.crypto.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

    public static void writeSecretKeyToFile(String key){
        File secretKeyFile = new File(DEFAULT_FILE_LOCATION, "tajni_kljuc.txt");
        try {
            secretKeyFile.createNewFile();
            FileOutputStream f = new FileOutputStream(secretKeyFile);
            PrintWriter pw = new PrintWriter(f);
            pw.write(key);
            pw.flush();
            pw.close();
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writePLainTextToFile(String plainText){
        File plainTextFile = new File(DEFAULT_FILE_LOCATION, "plain_text.txt");
        try {
            plainTextFile.createNewFile();
            FileOutputStream f = new FileOutputStream(plainTextFile);
            PrintWriter pw = new PrintWriter(f);
            pw.write(plainText);
            pw.flush();
            pw.close();
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeEncryptedTextToFile(String encryptedText){
        File encryptedTextFile = new File(DEFAULT_FILE_LOCATION, "encrypted_text.txt");
        try {
            encryptedTextFile.createNewFile();
            FileOutputStream f = new FileOutputStream(encryptedTextFile);
            PrintWriter pw = new PrintWriter(f);
            pw.write(encryptedText);
            pw.flush();
            pw.close();
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object readObject(String path) throws IOException, ClassNotFoundException {
        ObjectInputStream inputStream = null;
        inputStream = new ObjectInputStream(new FileInputStream(path));
        Object retval = inputStream.readObject();
        inputStream.close();
        return retval;
    }

    public static void saveObject(Object obj, String path) throws IOException {
        File file = new File(path);
        ObjectOutputStream publicKeyOS = new ObjectOutputStream(
                new FileOutputStream(path));
        publicKeyOS.writeObject(obj);
        publicKeyOS.close();
    }
}
