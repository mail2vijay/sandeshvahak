/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sandesh.encoding.utility;

/**
 *
 * @author Vijay
 */
public class MessageDigest {
    java.security.MessageDigest _md5;


   public MessageDigest() {
      try {
         this._md5 = java.security.MessageDigest.getInstance("md5");
      } catch (Exception var2) {
         ;
      }

   }

   public void reset() {
      this._md5.reset();
   }

   public void update(byte[] var1) {
      this._md5.update(var1, 0, var1.length);
   }

   public byte[] digest() {
      byte[] var1 = new byte[128];

      int var2;
      try {
         var2 = this._md5.digest(var1, 0, 128);
      } catch (Exception var4) {
         return null;
      }

      byte[] var3 = new byte[var2];
      System.arraycopy(var1, 0, var3, 0, var2);
      return var3;
   }
}
