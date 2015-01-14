/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sandeshvahak.helper;

import java.util.Date;
import java.util.Random;

/**
 *
 * @author Vijay
 */
public class Func {
    public static boolean isShort(String value)
        {
            return value.length() < 256;
        }

        public static int strlen_wa(String str)
        {
            int len = str.length();
            if (len >= 256)
                len = len & 0xFF00 >> 8;
            return len;
        }

        public static String _hex(int val)
        {
            return (String.valueOf(val).length()%2 == 0) ? String.valueOf(val) : ("0" + String.valueOf(val));
        }

        public static String random_uuid()
        {
            Random mt_rand = new Random(0L);
            return  mt_rand.nextInt(0xffff)+" "
                    +mt_rand.nextInt(0xffff)+"-"
                    +mt_rand.nextInt(0xffff)+"-"
                    +(mt_rand.nextInt(0x0fff) | 0x4000)+
                    "-"+(mt_rand.nextInt(0x3fff) | 0x8000)+"-"+ mt_rand.nextInt(0xffff)+""+mt_rand.nextInt(0xffff)+""+ mt_rand.nextInt(0xffff);
                   
        }

        public static String strtohex(String str)
        {
            String hex = "0x";
            char[]arr=str.toCharArray();
            for (int i=0;i<arr.length;i++)
                hex += arr[i];
            return hex;
        }

        public static String hexString2Ascii(String hexString)
        {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i <= hexString.length() - 2; i += 2)
            {
                sb.append(String.valueOf(((char)Integer.parseInt(hexString.substring(i, 2),16))));
            }
            return sb.toString();
        }

        public static long getUnixTimestamp(Date value)
        {
            
            return value.getTime();
        }

        public static long getNowUnixTimestamp()
        {
            return getUnixTimestamp(new Date());
        }

        public static boolean arrayEqual(byte[] b1, byte[] b2)
        {
            int len = b1.length;
            if (b1.length != b2.length)
                return false;
            for (int i = 0; i < len; i++)
            {
                if (b1[i] != b2[i])
                    return false;
            }
            return true;
        }
}
