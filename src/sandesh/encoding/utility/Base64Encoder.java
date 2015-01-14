package sandesh.encoding.utility;

import sandesh.encoding.utility.Encoder;
import java.io.IOException;
import java.io.OutputStream;

public class Base64Encoder implements Encoder {

   protected final byte[] encodingTable = new byte[]{(byte)65, (byte)66, (byte)67, (byte)68, (byte)69, (byte)70, (byte)71, (byte)72, (byte)73, (byte)74, (byte)75, (byte)76, (byte)77, (byte)78, (byte)79, (byte)80, (byte)81, (byte)82, (byte)83, (byte)84, (byte)85, (byte)86, (byte)87, (byte)88, (byte)89, (byte)90, (byte)97, (byte)98, (byte)99, (byte)100, (byte)101, (byte)102, (byte)103, (byte)104, (byte)105, (byte)106, (byte)107, (byte)108, (byte)109, (byte)110, (byte)111, (byte)112, (byte)113, (byte)114, (byte)115, (byte)116, (byte)117, (byte)118, (byte)119, (byte)120, (byte)121, (byte)122, (byte)48, (byte)49, (byte)50, (byte)51, (byte)52, (byte)53, (byte)54, (byte)55, (byte)56, (byte)57, (byte)43, (byte)47};
   protected byte padding = 61;
   protected final byte[] decodingTable = new byte[128];


   protected void initialiseDecodingTable() {
      for(int var1 = 0; var1 < this.encodingTable.length; ++var1) {
         this.decodingTable[this.encodingTable[var1]] = (byte)var1;
      }

   }

   public Base64Encoder() {
      this.initialiseDecodingTable();
   }

   public int encode(byte[] var1, int var2, int var3, OutputStream var4) throws IOException {
      int var5 = var3 % 3;
      int var6 = var3 - var5;

      int var10;
      for(var10 = var2; var10 < var2 + var6; var10 += 3) {
         int var7 = var1[var10] & 255;
         int var8 = var1[var10 + 1] & 255;
         int var9 = var1[var10 + 2] & 255;
         var4.write(this.encodingTable[var7 >>> 2 & 63]);
         var4.write(this.encodingTable[(var7 << 4 | var8 >>> 4) & 63]);
         var4.write(this.encodingTable[(var8 << 2 | var9 >>> 6) & 63]);
         var4.write(this.encodingTable[var9 & 63]);
      }

      int var11;
      int var13;
      switch(var5) {
      case 0:
      default:
         break;
      case 1:
         var13 = var1[var2 + var6] & 255;
         var10 = var13 >>> 2 & 63;
         var11 = var13 << 4 & 63;
         var4.write(this.encodingTable[var10]);
         var4.write(this.encodingTable[var11]);
         var4.write(this.padding);
         var4.write(this.padding);
         break;
      case 2:
         var13 = var1[var2 + var6] & 255;
         int var14 = var1[var2 + var6 + 1] & 255;
         var10 = var13 >>> 2 & 63;
         var11 = (var13 << 4 | var14 >>> 4) & 63;
         int var12 = var14 << 2 & 63;
         var4.write(this.encodingTable[var10]);
         var4.write(this.encodingTable[var11]);
         var4.write(this.encodingTable[var12]);
         var4.write(this.padding);
      }

      return var6 / 3 * 4 + (var5 == 0?0:4);
   }

   private boolean ignore(char var1) {
      return var1 == 10 || var1 == 13 || var1 == 9 || var1 == 32;
   }

   public int decode(byte[] var1, int var2, int var3, OutputStream var4) throws IOException {
      int var9 = 0;

      int var10;
      for(var10 = var2 + var3; var10 > var2 && this.ignore((char)var1[var10 - 1]); --var10) {
         ;
      }

      int var12 = var10 - 4;

      for(int var11 = this.nextI(var1, var2, var12); var11 < var12; var11 = this.nextI(var1, var11, var12)) {
         byte var5 = this.decodingTable[var1[var11++]];
         var11 = this.nextI(var1, var11, var12);
         byte var6 = this.decodingTable[var1[var11++]];
         var11 = this.nextI(var1, var11, var12);
         byte var7 = this.decodingTable[var1[var11++]];
         var11 = this.nextI(var1, var11, var12);
         byte var8 = this.decodingTable[var1[var11++]];
         var4.write(var5 << 2 | var6 >> 4);
         var4.write(var6 << 4 | var7 >> 2);
         var4.write(var7 << 6 | var8);
         var9 += 3;
      }

      var9 += this.decodeLastBlock(var4, (char)var1[var10 - 4], (char)var1[var10 - 3], (char)var1[var10 - 2], (char)var1[var10 - 1]);
      return var9;
   }

   private int nextI(byte[] var1, int var2, int var3) {
      while(var2 < var3 && this.ignore((char)var1[var2])) {
         ++var2;
      }

      return var2;
   }

   public int decode(String var1, OutputStream var2) throws IOException {
      int var7 = 0;

      int var8;
      for(var8 = var1.length(); var8 > 0 && this.ignore(var1.charAt(var8 - 1)); --var8) {
         ;
      }

      byte var9 = 0;
      int var10 = var8 - 4;

      for(int var11 = this.nextI(var1, var9, var10); var11 < var10; var11 = this.nextI(var1, var11, var10)) {
         byte var3 = this.decodingTable[var1.charAt(var11++)];
         var11 = this.nextI(var1, var11, var10);
         byte var4 = this.decodingTable[var1.charAt(var11++)];
         var11 = this.nextI(var1, var11, var10);
         byte var5 = this.decodingTable[var1.charAt(var11++)];
         var11 = this.nextI(var1, var11, var10);
         byte var6 = this.decodingTable[var1.charAt(var11++)];
         var2.write(var3 << 2 | var4 >> 4);
         var2.write(var4 << 4 | var5 >> 2);
         var2.write(var5 << 6 | var6);
         var7 += 3;
      }

      var7 += this.decodeLastBlock(var2, var1.charAt(var8 - 4), var1.charAt(var8 - 3), var1.charAt(var8 - 2), var1.charAt(var8 - 1));
      return var7;
   }

   private int decodeLastBlock(OutputStream var1, char var2, char var3, char var4, char var5) throws IOException {
      byte var6;
      byte var7;
      if(var4 == this.padding) {
         var6 = this.decodingTable[var2];
         var7 = this.decodingTable[var3];
         var1.write(var6 << 2 | var7 >> 4);
         return 1;
      } else {
         byte var8;
         if(var5 == this.padding) {
            var6 = this.decodingTable[var2];
            var7 = this.decodingTable[var3];
            var8 = this.decodingTable[var4];
            var1.write(var6 << 2 | var7 >> 4);
            var1.write(var7 << 4 | var8 >> 2);
            return 2;
         } else {
            var6 = this.decodingTable[var2];
            var7 = this.decodingTable[var3];
            var8 = this.decodingTable[var4];
            byte var9 = this.decodingTable[var5];
            var1.write(var6 << 2 | var7 >> 4);
            var1.write(var7 << 4 | var8 >> 2);
            var1.write(var8 << 6 | var9);
            return 3;
         }
      }
   }

   private int nextI(String var1, int var2, int var3) {
      while(var2 < var3 && this.ignore(var1.charAt(var2))) {
         ++var2;
      }

      return var2;
   }
}
