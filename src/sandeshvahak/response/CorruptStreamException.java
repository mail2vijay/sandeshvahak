/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sandeshvahak.response;

/**
 *
 * @author Vijay
 */
public class CorruptStreamException {
 private String message;
 public String getMessage()
 {
     return this.message;
 }
        public CorruptStreamException(String pMessage)
        {
            
            this.message = pMessage;
        }   
}
