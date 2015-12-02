package com.example.noxqs.crypto.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by noxqs on 28.11.15..
 */
public class HashUtil {

    public static String hash(String text, String algorithm) throws NoSuchAlgorithmException {
        String password = text;
        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.update(password.getBytes());
        byte byteData[] = md.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public static String hashSafe(String text, String algorithm) {
        try {
            return hash(text, algorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
