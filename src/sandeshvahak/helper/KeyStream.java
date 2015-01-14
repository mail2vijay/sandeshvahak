/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sandeshvahak.helper;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Vijay
 */
public class KeyStream {

    public final static String AUTHMETHOD = "WAUTH-2";
    private final int DROP = 768;
    private final RC4 rc4;
    private Mac mac;
    private int seq;

    public KeyStream(byte[] key, byte[] macKey) {
        this.rc4 = new RC4(key, DROP);
        try {
            SecretKeySpec signingKey = new SecretKeySpec(macKey, "HmacSHA1");
            //Get an hmac_sha1 Mac instance and initialize with the signing key
            this.mac = Mac.getInstance("HmacSHA1");
            this.mac.init(signingKey);
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
        }
    }

    public static byte[][] generateKeys(byte[] password, byte[] nonce) {
        byte[][] array = new byte[4][];
        byte[][] array2 = array;
        byte[] array3 = new byte[]{
            1,
            2,
            3,
            4
        };
        byte[] array4 = new byte[nonce.length + 1];
        try {

            System.arraycopy(nonce, 0, array4, 0, nonce.length);
            nonce = array4;
            for (int j = 0; j < array2.length; j++) {
                nonce[nonce.length - 1] = array3[j];
                Rfc2898DeriveBytes rfc2898DeriveBytes = new Rfc2898DeriveBytes(password, nonce, 2);
                array2[j] = rfc2898DeriveBytes.getBytes(20);

            }

        } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
            Logger.getLogger(KeyStream.class.getName()).log(Level.SEVERE, null, ex);
        }
        return array2;
    }

    public void decodeMessage(byte[] buffer, int macOffset, int offset, int length) throws Exception {
        byte[] array = this.computeMac(buffer, offset, length);
        for (int i = 0; i < 4; i++) {
            if (buffer[macOffset + i] != array[i]) {
                throw new Exception("MAC mismatch on index");
            }
        }
        this.rc4.cipher(buffer, offset, length);
    }

    public void encodeMessage(byte[] buffer, int macOffset, int offset, int length) {
        this.rc4.cipher(buffer, offset, length);
        byte[] array = this.computeMac(buffer, offset, length);
        System.arraycopy(array, 0, buffer, macOffset, 4);
    }

    private byte[] computeMac(byte[] buffer, int offset, int length) {
        this.mac.update(buffer, offset, length);
        byte[] array = new byte[]{
            (byte) (this.seq >> 24),
            (byte) (this.seq >> 16),
            (byte) (this.seq >> 8),
            (byte) this.seq
        };
        this.seq += 1;
        return this.mac.doFinal(array);
    }

}
