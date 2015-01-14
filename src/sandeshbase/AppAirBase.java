/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sandeshbase;

import java.util.ArrayList;
import java.util.List;
import sandesh.encoding.utility.Base64;
import sandeshvahak.account.AccountInfo;
import sandeshvahak.helper.BinaryTreeNodeReader;
import sandeshvahak.helper.BinaryTreeNodeWriter;
import sandeshvahak.helper.KeyStream;
import sandeshvahak.helper.ProtocolTreeNode;

/**
 *
 * @author Vijay
 */
public class AppAirBase {

    protected ProtocolTreeNode uploadResponse;
    protected AccountInfo accountinfo;
    public static boolean DEBUG;
    protected String password;
    protected boolean hidden;
    protected int loginStatus;
    public int connectionStatus;
    protected KeyStream outputKey;
    protected List messageQueue;
    protected String name;
    protected String phoneNumber;
    protected BinaryTreeNodeReader reader;
    protected int timeout = 300000;
    protected AirUtility airUtility;
    public static String ENCODING = "UTF-8";
    protected byte[] _challengeBytes;
    protected BinaryTreeNodeWriter BinWriter;
    public static AirSendBase airSendBase;

    protected AppAirBase(String phoneNum, String imei, String nick, boolean debug, boolean hidden) {
        this.messageQueue = new ArrayList();
        this.phoneNumber = phoneNum;
        this.password = imei;
        this.name = nick;
        this.hidden = false;
        AppAirBase.DEBUG = debug;
        this.reader = new BinaryTreeNodeReader();
        this.loginStatus = ConnectionStatus.DISCONNECTED;
        this.BinWriter = new BinaryTreeNodeWriter();
        this.airUtility = AirUtility.getInstance();
    }

    public void connect() {
        try {
            this.airUtility.connectToServer();
            this.loginStatus = ConnectionStatus.CONNECTED;
            airSendBase.onConnectSuccess();
            //success
            /**
             * Notify other object that connection successful
             */
        } catch (Exception e) {
            /**
             * Notify other object about connection failed
             */
        }
    }

    public void disconnect() {
        this.airUtility.disconnectFromServer();
        this.loginStatus = ConnectionStatus.DISCONNECTED;
        /**
         * notify object about disconnection
         */
    }

    public byte[] encryptPassword() {
        return Base64.decode(this.password);
    }

    public AccountInfo getAccountInfo() {
        return this.accountinfo;
    }

    public Object[] getAllMessages() {
        Object[] tmpReturn;
        synchronized (AppAirBase.class) {
            tmpReturn = this.messageQueue.toArray();
            this.messageQueue.clear();
        }
        return tmpReturn;
    }

    public void addMessage(ProtocolTreeNode node) {
        synchronized (AppAirBase.class) {
            this.messageQueue.add(node);
        }
    }

    public boolean hasMessages() {
        if (this.messageQueue == null) {
            return false;
        }
        return this.messageQueue.size() > 0;
    }

    public void sendData(byte[] data) {
        try {
            this.airUtility.sendData(data);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            this.disconnect();
        }
    }

    public void sendNode(ProtocolTreeNode node) {
        this.sendData(this.BinWriter.write(node, true));
    }
}
