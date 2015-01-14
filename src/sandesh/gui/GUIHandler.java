/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sandesh.gui;

import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import sandeshbase.AirBase;
import sandeshbase.AirSendBase;
import sandeshbase.MessageHandler;
import sandeshbase.VisibilityCategory;
import sandeshbase.VisibilitySetting;
import sandeshvahak.helper.ProtocolTreeNode;
import sandeshvahak.response.GroupInformation;

/**
 *
 * @author Vijay
 */
public class GUIHandler implements MessageHandler, Runnable {

    private HashMap<String, Chat> chatList = new HashMap<>();
    private AirBase instance;
    private static GUIHandler obj;
    private SandeshVahak mainGui;

    private GUIHandler() {

    }

    public static GUIHandler getInstance() {
        if (obj == null) {
            obj = new GUIHandler();
        }
        JFrame.setDefaultLookAndFeelDecorated(true);
        return obj;
    }

    public void start() {
        
        AirSendBase.handleMessage = this;
        mainGui=SandeshVahak.getInstance();
        mainGui.txtUserID.setText(secretpass.SecretPass.MOBILENUMBER);
        mainGui.pswText.setText(secretpass.SecretPass.PASSWORD);
    }

    public void connect(String mobileNo, String pass, boolean debug, boolean hidden) {
        /*
         * Connect to whats app server
         * Login to whats app account
         */
        System.out.println("Connect called");
        instance = new AirBase(mobileNo, pass, "sandeshVahak", true, true);
        new Thread(() -> {
            instance.connect();
        }).start();
    }

    private void login() {
        try {
            instance.login(null);
        } catch (Exception ex) {
            Logger.getLogger(GUIHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        while (true) {
            instance.pollMessages(true);
        }
    }

    @Override
    public void onConnectSuccess() {
        System.out.println("Connected Successfully");
        this.login();
    }

    @Override
    public void onDisconnect(Exception e) {
        //onDisconnect(e);
    }

    @Override
    public void onConnectFailed(Exception e) {
        //onConnectFailed(e);
    }

    @Override
    public void onLoginSuccess(String phoneNumber, byte[] data) {
        System.out.println("Login Successfully");
       mainGui.pnlbeforeLogin.setVisible(false);
        mainGui.pnlAfterLogin.setVisible(true);
        /*
         * Step first to start for listening on message
         */
        new Thread(GUIHandler.getInstance()).start();
        /**
         * send keep alive at every 15 seconds
         */
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(150000L);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GUIHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
                instance.sendActive();
            }
        }).start();

    }

    @Override
    public void onLoginFailed(String data) {
        //onLoginFailed(data);
    }

    @Override
    public void onGetMessage(ProtocolTreeNode messageNode, String from, String id, String name, String message, boolean receipt_sent) {
        System.out.println("from" + from + ":Message:" + message);
        //onGetMessage(messageNode, from, id, name, message, receipt_sent);
    }

    @Override
    public void onGetMessageImage(ProtocolTreeNode mediaNode, String from, String id, String fileName, int fileSize, String url, byte[] preview) {
        System.out.println("on getImage Successfully");
//onGetMessageImage(mediaNode, from, id, fileName, fileSize, url, preview);
    }

    @Override
    public void onGetMessageVideo(ProtocolTreeNode mediaNode, String from, String id, String fileName, int fileSize, String url, byte[] preview) {
        System.out.println("on get Video Successfully");
//onGetMessageVideo(mediaNode, from, id, fileName, fileSize, url, preview);
    }

    @Override
    public void onGetMessageAudio(ProtocolTreeNode mediaNode, String from, String id, String fileName, int fileSize, String url, byte[] preview) {
        System.out.println("on getAudio Successfully");
//onGetMessageAudio(mediaNode, from, id, fileName, fileSize, url, preview);
    }

    @Override
    public void onGetMessageLocation(ProtocolTreeNode locationNode, String from, String id, double lon, double lat, String url, String name, byte[] preview) {
        System.out.println("get Message location Successfully");
        //onGetMessageLocation(locationNode, from, id, lon, lat, url, name, preview);
    }

    @Override
    public void onGetMessageVcard(ProtocolTreeNode vcardNode, String from, String id, String name, byte[] data) {
        System.out.println("on get V card");
//onGetMessageVcard(vcardNode, from, id, name, data);
    }

    @Override
    public void onError(String id, String from, int code, String text) {
        System.out.println("on error Successfully");
//onError(id, from, code, text);
    }

    @Override
    public void onNotificationPicture(String type, String jid, String id) {
        System.out.println("on notification Successfully");
    }

    @Override
    public void onGetMessageReceivedServer(String from, String id) {
        System.out.println("on message receievd  server");  //onGetMessageReceivedServer(from, id);
    }

    @Override
    public void onGetMessageReceivedClient(String from, String id) {
        //onGetMessageReceivedClient(from, id);
        System.out.println("on message received client");
    }

    @Override
    public void onGetPresence(String from, String type) {
        System.out.println("from" + from + "Connected Successfully" + "type" + type);
    }

    @Override
    public void onGetGroupParticipants(String gjid, String[] jids) {
        //onGetGroupParticipants(gjid, jids);
        System.out.println("On group partition" + "Connected Successfully");
    }

    @Override
    public void onGetLastSeen(String from, Date lastSeen) {
        System.out.println("on get last seen" + from);
//onGetLastSeen(from, lastSeen);
    }

    @Override
    public void onGetTyping(String from) {
        System.out.println("Connected Successfully");
//onGetTyping(from);
    }

    @Override
    public void onGetPaused(String from) {
 System.out.println("get paused"+from);        
//onGetPaused(from);
    }

    @Override
    public void onGetPhoto(String from, String id, byte[] preview) {
        System.out.println("on getphoto"+from);
        //onGetPhoto(from, id, preview);
    }

    @Override
    public void onGetPhotoPreview(String from, String id, byte[] data) {
 System.out.println("on get photo preview"+from);        
//onGetPhotoPreview(from, id, data);
    }

    @Override
    public void onGetGroups(GroupInformation[] groups) {
        System.out.println("on get group Connected Successfully");
        //onGetGroups(groups);
    }

    @Override
    public void onGetContactName(String from, String contactName) {
 System.out.println("on get contact name Connected Successfully");        
//onGetContactName(from, contactName);
    }

    @Override
    public void onGetStatus(String from, String type, String name, String status) {
 System.out.println("on get status Connected Successfully"+from);        
//onGetStatus(from, type, name, status);
    }

    @Override
    public void onGetSyncResult(int index, String sid, HashMap<String, String> existingUsers, String[] failedNumbers) {
        //onGetSyncResult(index, sid, existingUsers, failedNumbers);
         System.out.println("on get sync result");
    }

    @Override
    public void onGetPrivacySettings(HashMap<VisibilityCategory, VisibilitySetting> settings) {

    }

    @Override
    public void onGetParticipantAdded(String gjid, String jid, Date time) {
 System.out.println("on get participant added");
    }

    @Override
    public void onGetParticipantRemoved(String gjid, String jid, String author, Date time) {
 System.out.println("on getParticipant removed");
    }

    @Override
    public void onGetParticipantRenamed(String gjid, String oldJid, String newJid, Date time) {
 System.out.println("on get participant renamed");
    }

    @Override
    public void onGetGroupSubject(String gjid, String jid, String username, String subject, Date time) {
 System.out.println("on get group subject");
    }
}
