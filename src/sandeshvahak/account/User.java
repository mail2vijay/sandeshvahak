/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sandeshvahak.account;




/**
 *
 * @author Vijay
 */
public class User {
    private String serverUrl;
        private String nickName;
        private String jID;
        public User(String jid, String srvUrl, String nickname )
        {
            this.jID = jid;
            this.nickName = nickname;
            this.serverUrl = srvUrl;
        }

        public String getFullJid()
        {
            //return Application.getJID(this.jID); // fix it
            return null;
        }

        public void setServerUrl(String srvUrl)
        {
            this.serverUrl = srvUrl;
        }

        public String toString()
        {
            return this.getFullJid();
        }
}
