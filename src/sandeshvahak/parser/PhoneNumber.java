/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sandeshvahak.parser;

/**
 *
 * @author Vijay
 */
public class PhoneNumber {
     private String country;
        private String CC;
        public String number;
        public String ISO3166;
        public String ISO639;
        protected String _mcc;
        protected String _mnc;
    
     public PhoneNumber(String number)
        {
           
            
        }
     public String getFullNumber()
     {
         return CC+number;
     }
     public String getMCC()
     {
         // left padd 
       return (" "+" "+" "+this._mcc).replace(' ', '0');
     }
     public String getMNC()
     {
          return (" "+" "+" "+this._mnc).replace(' ', '0');
     }
     public String getCountryCode()
     {
         return this.CC;
     }
     public String getCountry()
     {
         return this.country;
     }
     
     public String getNumber()
     {
         return this.number;
     }
     public String getISO3166()
     {
         return this.ISO3166;
     }
     public String getISO639()
     {
         return this.ISO639;
     }
     
}
