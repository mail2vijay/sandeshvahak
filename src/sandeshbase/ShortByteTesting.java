/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sandeshbase;

/**
 *
 * @author Vijay
 */
public class ShortByteTesting {
    public static void  main(String...s)
    {
        short ss=256;
        int is=256;
        byte b=(byte)ss;
        byte c=(byte)is;
        System.out.println("byte:"+(b&255)+":"+(c&255));
        
        
    }
}
