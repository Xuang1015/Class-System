package util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PasswordHelper {
    private static final char[] ALL_CHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final char[] HEX_CHAR = "0123456789ABCDEF".toCharArray();
    private static final int FIFTEEN = 0x0f;

    public static String encrypt(String plaintext) {
        StringBuilder ciphertext = new StringBuilder();
        try {
            StringBuilder ciphertextOriginal = new StringBuilder();
            byte[] ciphertextByte = MessageDigest.getInstance("MD5").digest(plaintext.getBytes("UTF-8"));
            for (byte b : ciphertextByte) {
                ciphertextOriginal.append(HEX_CHAR[(b >> 4) & FIFTEEN]);
                ciphertextOriginal.append(HEX_CHAR[b & FIFTEEN]);
            }
            ciphertext.append(ciphertextOriginal.toString());
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ciphertext.insert(5, saltGenerate(8));
        ciphertext.insert(18, saltGenerate(4));
        return ciphertext.toString();
    }

    private static String saltGenerate(int size) {
        StringBuilder salt = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < size; i++) {
            salt.append(ALL_CHAR[(random.nextInt(ALL_CHAR.length))]);
        }
        return salt.toString();
    }
}
