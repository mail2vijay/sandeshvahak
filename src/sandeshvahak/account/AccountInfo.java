/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sandeshvahak.account;

/**
 *
 * @author Vijay
 */
public class AccountInfo {
        public String status;
        public String kind;
        public String creation;
        public String expiration;

        public AccountInfo(String status, String kind, String creation, String expiration)
        {
            this.status = status;
            this.kind = kind;
            this.creation = creation;
            this.expiration = expiration;
        }

        public String toString()
        {
            return "Status: "+this.status+", Kind: "+this.kind+", Creation: "+this.creation+", Expiration: "+this.expiration+"";
                      
        }
}
