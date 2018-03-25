package business.util;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class PasswordEncrypterTest {

    private static PasswordEncrypter encrypter;

    @BeforeClass
    public static void setUpBeforeClass() {
        encrypter = new PasswordEncrypterMD5();
    }

    @Test
    public void encrypt_CorrectPassword_Success() {
        String hash = "DEB1536F480475F7D593219AA1AFD74C";
        String password = "myPassword";

        String encryptedPass = encrypter.encrypt(password);

        assertEquals(hash, encryptedPass);
    }

    @Test
    public void encrypt_WrongPassword_Failure() {
        String hash = "DEB1536F480475F7D593219AA1AFD74C";
        String password = "mypassword";

        String encryptedPass = encrypter.encrypt(password);

        assertNotEquals(hash, encryptedPass);
    }
}
