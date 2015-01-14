/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sandeshvahak.account;


import java.util.Hashtable;
import sandeshbase.StatusConstant;


/**
 *
 * @author Vijay
 */
public class UserManager {
    private Hashtable userList;

        public UserManager()
        {
            this.userList = new Hashtable();
        }

        //public void AddUser(User user)
        //{
        //    //if(user == null || user.)
        //    //if(this.userList.ContainsKey())
        //}

        public User createUser(String jid, String nickname )
        {
            if (this.userList.containsKey(jid))
                return (User)this.userList.get(jid);

            String server = StatusConstant.whatsAppServer;
            if (jid.indexOf('-')!=-1)
                server = StatusConstant.whatsGroupChat;

            User tmpUser = new User(jid, server, nickname);
            this.userList.put(jid, tmpUser);
            return tmpUser;
        }
}
