package sandesh.encoding.utility;

import sandesh.encoding.utility.Base64Encoder;
import sandesh.encoding.utility.Encoder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Base64 {

   private static final Encoder encoder = new Base64Encoder();


   public static byte[] encode(byte[] var0) {
      int var1 = (var0.length + 2) / 3 * 4;
      ByteArrayOutputStream var2 = new ByteArrayOutputStream(var1);

      try {
         encoder.encode(var0, 0, var0.length, var2);
      } catch (Exception var4) {
         throw new RuntimeException("exception encoding base64 string: " + var4);
      }

      return var2.toByteArray();
   }

   public static int encode(byte[] var0, OutputStream var1) throws IOException {
      return encoder.encode(var0, 0, var0.length, var1);
   }

   public static int encode(byte[] var0, int var1, int var2, OutputStream var3) throws IOException {
      return encoder.encode(var0, var1, var2, var3);
   }

   public static byte[] decode(byte[] var0) {
      int var1 = var0.length / 4 * 3;
      ByteArrayOutputStream var2 = new ByteArrayOutputStream(var1);

      try {
         encoder.decode(var0, 0, var0.length, var2);
      } catch (IOException var4) {
         throw new RuntimeException("exception decoding base64 string: " + var4);
      }

      return var2.toByteArray();
   }

   public static byte[] decode(String var0) {
      int var1 = var0.length() / 4 * 3;
      ByteArrayOutputStream var2 = new ByteArrayOutputStream(var1);

      try {
         encoder.decode(var0, var2);
      } catch (IOException var4) {
         throw new RuntimeException("exception decoding base64 string: " + var4);
      }

      return var2.toByteArray();
   }

   public static int decode(String var0, OutputStream var1) throws IOException {
      return encoder.decode(var0, var1);
   }

}
