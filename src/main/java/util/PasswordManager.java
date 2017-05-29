package util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by ivanmolera on 27/5/17.
 */
public class PasswordManager {

    public static String encrypt(String password) {
        String hash = "";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(password.getBytes());
            hash = byteToHexString(digest.digest(password.getBytes("UTF-8")));
        }
        catch(NoSuchAlgorithmException nsae) {

        } catch (UnsupportedEncodingException e) {

        }
        return hash;
    }

    private static String byteToHexString(byte[] input) {
        String output = "";
        for (int i=0; i<input.length; ++i) {
            output += String.format("%02X", input[i]);
        }
        return output;
    }
}
