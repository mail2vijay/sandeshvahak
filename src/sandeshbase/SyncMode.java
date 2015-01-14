/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sandeshbase;

/**
 *
 * @author Vijay
 */
public interface SyncMode {

    int Full = 0;
    int Delta = 1;
    int Query = 2;
    int Chunked = 3;
}
