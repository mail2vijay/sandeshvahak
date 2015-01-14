/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sandeshvahak.helper;

/**
 *
 * @author Vijay
 */
public class KeyValue {
     private String key ;
     private String value;

        public KeyValue(String key, String value)throws NullPointerException
        {
            if ((value == null) || (key == null))
            {
                throw new NullPointerException("either key or value is null");
            }
            this.key = key;
            this.value = value;
        }
        public String getKey()
        {
            return this.key;
        }
        public String getValue()
        {
            return this.value;
        }
      
}
