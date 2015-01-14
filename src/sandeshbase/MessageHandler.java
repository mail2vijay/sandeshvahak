/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sandeshbase;

import java.util.Date;
import java.util.HashMap;
import sandeshvahak.helper.ProtocolTreeNode;
import sandeshvahak.response.GroupInformation;

/**
 *
 * @author Vijay
 */
public interface MessageHandler {

    public void onConnectSuccess();

    public void onDisconnect(Exception e);

    public void onConnectFailed(Exception e);

    public void onLoginSuccess(String phoneNumber, byte[] data);

    public void onLoginFailed(String data);

    public void onGetMessage(ProtocolTreeNode messageNode, String from, String id, String name, String message, boolean receipt_sent);

    public void onGetMessageImage(ProtocolTreeNode mediaNode, String from, String id, String fileName, int fileSize, String url, byte[] preview);

    public void onGetMessageVideo(ProtocolTreeNode mediaNode, String from, String id, String fileName, int fileSize, String url, byte[] preview);

    public void onGetMessageAudio(ProtocolTreeNode mediaNode, String from, String id, String fileName, int fileSize, String url, byte[] preview);

    public void onGetMessageLocation(ProtocolTreeNode locationNode, String from, String id, double lon, double lat, String url, String name, byte[] preview);

    public void onGetMessageVcard(ProtocolTreeNode vcardNode, String from, String id, String name, byte[] data);

    public void onError(String id, String from, int code, String text);

    public void onNotificationPicture(String type, String jid, String id);

    public void onGetMessageReceivedServer(String from, String id);

    public void onGetMessageReceivedClient(String from, String id);

    public void onGetPresence(String from, String type);

    public void onGetGroupParticipants(String gjid, String[] jids);

    public void onGetLastSeen(String from, Date lastSeen);

    public void onGetTyping(String from);

    public void onGetPaused(String from);

    public void onGetPhoto(String from, String id, byte[] preview);

    public void onGetPhotoPreview(String from, String id, byte[] data);

    public void onGetGroups(GroupInformation[] groups);

    public void onGetContactName(String from, String contactName);

    public void onGetStatus(String from, String type, String name, String status);

    public void onGetSyncResult(int index, String sid, HashMap<String, String> existingUsers, String[] failedNumbers);

    public void onGetPrivacySettings(HashMap<VisibilityCategory, VisibilitySetting> settings);

    public void onGetParticipantAdded(String gjid, String jid, Date time);

    public void onGetParticipantRemoved(String gjid, String jid, String author, Date time);

    public void onGetParticipantRenamed(String gjid, String oldJid, String newJid, Date time);

    public void onGetGroupSubject(String gjid, String jid, String username, String subject, Date time);
}
