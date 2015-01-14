package sandesh.encoding.utility;



import java.util.Calendar;
import java.util.Date;



public class DateTimeUtilities {

   public static final int ONESECOND = 1000;
   public static final long ONEMINUTE = 60000L;
   public static final long ONEHOUR = 3600000L;
   public static final long ONEDAY = 86400000L;
   public static final long ONEWEEK = 604800000L;
   public static final long ONEYEAR = 31536000000L;
   public static final int[] MONTH_LABELS = new int[]{28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39};
   public static final int[] WEEKDAY_LABELS = new int[]{40, 41, 42, 43, 44, 45, 46};


   public static boolean isSameDate(long var0, long var2) {
      return isSameDate(var0, var2, 5);
   }

   public static boolean isSameDate(long var0, long var2, int var4) {
      Calendar var5 = Calendar.getInstance();
      Calendar var6 = Calendar.getInstance();
      var5.setTime(new Date(var0));
      var6.setTime(new Date(var2));
      return var5.get(1) == var6.get(1) && (var4 == 1 || var5.get(2) == var6.get(2) && (var4 == 2 || var5.get(5) == var6.get(5)));
   }

/*   public static String formatTimestamp(long var0, Font var2, int var3) {
      long var4 = System.currentTimeMillis();
      long var6 = var4 - var0;
      if(var6 / 60000L == 0L) {
         return Res.getString(52);
      } else {
         if(var6 / 3600000L == 0L) {
            int var8 = (int)(var6 / 60000L);
            String var9 = Res.getString(50, var8);
            if(var2.stringWidth(var9) < var3) {
               return var9;
            }

            var9 = Res.getString(51, var8);
            if(var2.stringWidth(var9) < var3) {
               return var9;
            }
         }

         return var6 / 86400000L == 0L?shortTimeFormat(var0):(var6 / 604800000L == 0L?weekdayTimeFormat(var0) + " @ " + shortTimeFormat(var0):formatDate(var0, var6 / 31536000000L > 0L) + " @ " + shortTimeFormat(var0));
      }
   }*/

   public static String dayOfYear(long var0) {
      Calendar var2 = Calendar.getInstance();
      var2.setTime(new Date(var0));
      return "";
      //return Res.getString(MONTH_LABELS[var2.get(2)]) + ' ' + var2.get(5) + ' ' + var2.get(1);
   }

   public static String simpleDayFormat(long var0) {
      Calendar var2 = Calendar.getInstance();
      var2.setTime(new Date(var0));
      //return Res.getString(MONTH_LABELS[var2.get(2)]) + ' ' + var2.get(5);
       return "";
   }

   public static String shortTimeFormat(long var0) {
      Calendar var2 = Calendar.getInstance();
      var2.setTime(new Date(var0));
      int var3 = var2.get(12);
      String var4 = var3 < 10?"0":"";
      int var5 = var2.get(11);
      String var6 = var5 == 0?"0":"";
      return var6 + var5 + ":" + var4 + var3;
   }

   public static String timeFormatMS(long var0) {
      long var2 = var0 / 60000L % 60L;
      long var4 = var0 / 1000L % 60L;
      String var6 = Long.toString(var2);
      String var7 = Long.toString(var4);
      if(var4 < 10L) {
         var7 = '0' + var7;
      }

      return var6 + ':' + var7;
   }

   public static String formatDate(long var0, boolean var2) {
      Calendar var3 = Calendar.getInstance();
      var3.setTime(new Date(var0));
      long var4 = (long)(var3.get(2) + 1);
      long var6 = (long)var3.get(5);
      String var8 = "" + var4 + '/' + Long.toString(var6);
      if(var2) {
         int var9 = var3.get(1);
         return "" + var9 + '/' + var8;
      } else {
         return var8;
      }
   }

   public static String logTimeFormat(long var0) {
      Calendar var2 = Calendar.getInstance();
      var2.setTime(new Date(var0));
      int var3 = var2.get(2) + 1;
      int var4 = var2.get(5);
      int var5 = var2.get(11);
      String var6 = var5 == 0?"0":"";
      int var7 = var2.get(12);
      String var8 = var7 < 10?"0":"";
      int var9 = var2.get(13);
      String var10 = var9 < 10?"0":"";
      int var11 = var2.get(14);
      String var12;
      if(var11 < 10) {
         var12 = "00";
      } else if(var11 < 100) {
         var12 = "0";
      } else {
         var12 = "";
      }

      return var3 + "/" + var4 + " " + var6 + var5 + ":" + var8 + var7 + ":" + var10 + var9 + "." + var12 + var11;
   }

   public static String weekdayTimeFormat(long var0) {
      Calendar var2 = Calendar.getInstance();
      var2.setTime(new Date(var0));
      int var3 = var2.get(7);
       return "";//return Res.getString(WEEKDAY_LABELS[var3 - 1]);
   }

   public static String readableElapsedTime(long var0) {
      StringBuffer var2 = new StringBuffer();
      long var3 = var0 / 3600000L;
      if(var0 >= 3600000L) {
         var2.append(var3);
         var2.append(':');
         var0 %= 3600000L;
      }

      long var5;
      if(var0 >= 60000L) {
         var5 = var0 / 60000L;
         if(var3 > 0L && var5 < 10L) {
            var2.append('0');
         }

         var2.append(var5);
         var0 %= 60000L;
      }

      var2.append(':');
      if(var0 >= 1000L) {
         var5 = var0 / 1000L;
         if(var5 < 10L) {
            var2.append("0");
         }

         var2.append(var5);
      } else {
         var2.append("00");
      }

      return var2.toString();
   }

   public static String mmsFileDate(long var0) {
      Calendar var2 = Calendar.getInstance();
      var2.setTime(new Date(var0));
      long var3 = (long)var2.get(1);
      long var5 = (long)(var2.get(2) + 1);
      String var7 = var5 < 10L?"0": Constants.STRING_EMPTY_STRING;
      long var8 = (long)var2.get(5);
      String var10 = var8 < 10L?"0": Constants.STRING_EMPTY_STRING;
      return var3 + var7 + var5 + var10 + var8;
   }

}
