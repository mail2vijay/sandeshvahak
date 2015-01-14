/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sandeshvahak.helper;

/**
 *
 * @author Vijay
 */
public class ByRef<T> {
    T value;
    ByRef(T ob)
    {
        value=ob;
    }
    public T get()
    {
        return this.value;
    }
    public void set(T v)
    {
        this.value=v;
    }
    
}
