package business.util;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncrypterMD5 implements PasswordEncrypter {

    private MessageDigest messageDigest;

    public PasswordEncrypterMD5() {
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public String encrypt(String password) {
        if (password == null || password.length() == 0) {
            throw new IllegalArgumentException("String to encrypt cannot be null or have zero length!");
        }

        messageDigest.update(password.getBytes());
        byte[] hash = messageDigest.digest();
        StringBuffer hexString = new StringBuffer();
        String myHash = DatatypeConverter.printHexBinary(hash).toUpperCase();
        return myHash;

    }

}
