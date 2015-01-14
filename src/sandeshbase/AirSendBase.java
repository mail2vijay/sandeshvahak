/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sandeshbase;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import sandeshvahak.account.AccountInfo;
import sandeshvahak.helper.Func;
import sandeshvahak.helper.KeyStream;
import sandeshvahak.helper.KeyValue;
import sandeshvahak.helper.ProtocolTreeNode;
import sandeshvahak.helper.TicketCounter;
import sandeshvahak.parser.FMessage;
import sandeshvahak.parser.PhoneNumber;
import sandeshvahak.response.GroupInformation;

/**
 *
 * @author Vijay
 */
public class AirSendBase extends AppAirBase implements MessageHandler{

    private static AirSendBase airSendBase;
    public static MessageHandler handleMessage;
    public static AirSendBase getInstance(String phoneNum, String imei, String nick, boolean debug, boolean hidden) {
        if (airSendBase == null) {
            airSendBase = new AirSendBase(phoneNum, imei, nick, debug, hidden);
        }
        return airSendBase;
    }

    public AirSendBase(String phoneNum, String imei, String nick, boolean debug, boolean hidden) {
        super(phoneNum, imei, nick, debug, hidden);
        AppAirBase.airSendBase=this;
    }

    public void login(byte[] nextChallenge) throws Exception {
        //reset stuff
        System.out.println("Entering login");
        this.reader.Key = null;
        this.BinWriter.Key = null;
        this._challengeBytes = null;
        if (nextChallenge != null) {
            this._challengeBytes = nextChallenge;
        }
        String resource = StatusConstant.device + "-" + StatusConstant.whatsAppVer + "-" + StatusConstant.whatsPort;
        byte[] data = this.BinWriter.startStream(StatusConstant.whatsAppServer, resource);
        ProtocolTreeNode feat = this.addFeatures();
        ProtocolTreeNode auth = this.addAuth();
        this.sendData(data);
        this.sendData(this.BinWriter.write(feat, false));
        this.sendData(this.BinWriter.write(auth, false));
        this.pollMessage(true);//stream start
        this.pollMessage(true);//features
        this.pollMessage(true);//challenge or success
        if (this.loginStatus != ConnectionStatus.LOGGEDIN) {
            //oneshot failed
            ProtocolTreeNode authResp = this.addAuthResponse();
            this.sendData(this.BinWriter.write(authResp, false));
            this.pollMessage(true);
        }
        this.sendAvailableForChat(this.name, this.hidden);
    }

    @SuppressWarnings("empty-statement")
    public void pollMessages(boolean autoReceipt) {
        while (pollMessage(autoReceipt)) ;
    }

    /**
     *
     *
     */
    public boolean pollMessage(boolean autoReceipt) {
        if (this.loginStatus == ConnectionStatus.CONNECTED || this.loginStatus == ConnectionStatus.LOGGEDIN) {
            byte[] nodeData;
            try {
                nodeData = this.airUtility.readNextNode();
                if (nodeData != null) {
                    return this.processInboundData(nodeData, autoReceipt);
                }
            } catch (Exception e) {
                this.disconnect();
            }
        }
        return false;
    }

    protected ProtocolTreeNode addFeatures() {
        return new ProtocolTreeNode("stream:features", null);
    }

    protected ProtocolTreeNode addAuth() {

        KeyValue[] attr = new KeyValue[3];
        attr[0] = new KeyValue("mechanism", KeyStream.AUTHMETHOD);
        attr[1] = new KeyValue("user", this.phoneNumber);
        if (this.hidden) {
            attr[2] = new KeyValue("passive", "true");
        } else {
            attr = new KeyValue[2];
            attr[0] = new KeyValue("mechanism", KeyStream.AUTHMETHOD);
            attr[1] = new KeyValue("user", this.phoneNumber);
        }
        ProtocolTreeNode node = new ProtocolTreeNode("auth", attr, null, this.getAuthBlob());
        return node;
    }

    protected byte[] getAuthBlob() {
        byte[] data = null;
        if (this._challengeBytes != null) {
            try {
                byte[][] keys = KeyStream.generateKeys(this.encryptPassword(), this._challengeBytes);
                this.reader.Key = new KeyStream(keys[2], keys[3]);
                this.outputKey = new KeyStream(keys[0], keys[1]);
                PhoneNumber pn = new PhoneNumber(this.phoneNumber);
                ArrayList<Byte> b = new ArrayList<>();
                addRange(new byte[]{0, 0, 0, 0}, b);
                addRange(this.phoneNumber.getBytes("UTF-8"), b);
                addRange(this._challengeBytes, b);
                addRange(String.valueOf(Func.getNowUnixTimestamp()).getBytes("UTF-8"), b);
                addRange((StatusConstant.UserAgent).getBytes("UTF-8"), b);
                addRange((" MccMnc/" + pn.getMCC() + "001").getBytes("UTF-8"), b);
                data = toArray(b);
                this._challengeBytes = null;
                this.outputKey.encodeMessage(data, 0, 4, data.length - 4);
                this.BinWriter.Key = this.outputKey;
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(AirSendBase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return data;
    }

    private void addRange(byte[] ret, ArrayList<Byte> v) {
        for (int i = 0; i < ret.length; i++) {
            v.add(new Byte(ret[i]));
        }
    }

    private byte[] toArray(ArrayList<Byte> v) {
        byte[] toArray = new byte[v.size()];
        for (int i = 0; i < toArray.length; i++) {
            toArray[i] = v.get(i).byteValue();
        }
        return toArray;
    }

    private ProtocolTreeNode[] toProtocolTreeNodeArray(ArrayList<ProtocolTreeNode> v) {
        ProtocolTreeNode[] toArray = new ProtocolTreeNode[v.size()];
        for (int i = 0; i < toArray.length; i++) {
            toArray[i] = v.get(i);
        }
        return toArray;
    }

    private KeyValue[] toKeyValueArray(ArrayList<KeyValue> v) {
        KeyValue[] toArray = new KeyValue[v.size()];
        for (int i = 0; i < toArray.length; i++) {
            toArray[i] = v.get(i);
        }
        return toArray;
    }

    public static void main(String... s) throws Exception {
        AirSendBase send = AirSendBase.getInstance("918005320335", secretpass.SecretPass.PASSWORD, "Victory", true, true);
        send.connect();
        send.login(null);
        while (true) {
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected ProtocolTreeNode addAuthResponse() {
        ProtocolTreeNode node = null;
        if (this._challengeBytes != null) {
            try {
                byte[][] keys = KeyStream.generateKeys(this.encryptPassword(), this._challengeBytes);
                this.reader.Key = new KeyStream(keys[2], keys[3]);
                this.BinWriter.Key = new KeyStream(keys[0], keys[1]);
                ArrayList<Byte> b = new ArrayList<>();
                addRange(new byte[]{0, 0, 0, 0}, b);
                addRange(this.phoneNumber.getBytes("UTF-8"), b);
                addRange(this._challengeBytes, b);
                byte[] data = toArray(b);
                this.BinWriter.Key.encodeMessage(data, 0, 4, data.length - 4);
                node = new ProtocolTreeNode("response",
                        new KeyValue[]{new KeyValue("xmlns", "urn:ietf:params:xml:ns:xmpp-sasl")},
                        data);
                return node;
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(AirSendBase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return node;
    }

    /**
     * server requested for password with encryption key
     *
     * @param node
     */
    protected void processChallenge(ProtocolTreeNode node) {
        _challengeBytes = node.data;
    }

    protected boolean processInboundData(byte[] msgdata, boolean autoReceipt) {
        try {
            ProtocolTreeNode node = this.reader.nextTree(msgdata, true);
            if (node != null) {
                if (ProtocolTreeNode.tagEquals(node, "challenge")) {
                    this.processChallenge(node);
                } else if (ProtocolTreeNode.tagEquals(node, "success")) {
                    this.loginStatus = ConnectionStatus.LOGGEDIN;
                    this.accountinfo = new AccountInfo(node.getAttribute("status"),
                            node.getAttribute("kind"),
                            node.getAttribute("creation"),
                            node.getAttribute("expiration"));

                   
                    onLoginSuccess(this.phoneNumber, node.getData());
                } else if (ProtocolTreeNode.tagEquals(node, "failure")) {
                    this.loginStatus = ConnectionStatus.UNAUTHORIZED;
                    System.out.println("Login failure");
                    onLoginFailed(node.children[0].tag);
                }

                if (ProtocolTreeNode.tagEquals(node, "receipt")) {
                    String from = node.getAttribute("from");
                    String id = node.getAttribute("id");
                    String type = node.getAttribute("type") == null ? "delivery" : node.getAttribute("type");
                    switch (type) {
                        case "delivery":
                            //delivered to target
                            onGetMessageReceivedClient(from, id);
                            break;
                        case "read":
                            //read by target
                            //todo
                            break;
                        case "played":
                            //played by target
                            //todo
                            break;
                    }

                    //send ack
                    sendNotificationAck(node, type);
                }

                if (ProtocolTreeNode.tagEquals(node, "message")) {
                    this.handleMessage(node, autoReceipt);
                }


                if (ProtocolTreeNode.tagEquals(node, "iq")) {
                    this.handleIq(node);
                }

                if (ProtocolTreeNode.tagEquals(node, "stream:error")) {
                    ProtocolTreeNode textNode = node.getChild("text");
                    if (textNode != null) {
                        String content = new String(textNode.getData(), "UTF-8");
                        // Helper.DebugAdapter.Instance.fireOnPrintDebug("Error : " + content);
                    }
                    this.disconnect();
                }

                if (ProtocolTreeNode.tagEquals(node, "presence")) {

                    System.out.println("presence");
                    //presence node
                    onGetPresence(node.getAttribute("from"), node.getAttribute("type"));
                }

                if ("ib".equals(node.tag)) {
                    for (ProtocolTreeNode child : node.children) {
                        switch (child.tag) {
                            case "dirty":
                                this.sendClearDirty(child.getAttribute("type"));
                                break;
                            case "offline":
                                //this.SendQrSync(null);
                                break;
                            default:
                                throw new UnsupportedOperationException("");
                        }
                    }
                }

                if ("chatstate".equals(node.tag)) {
                    String state = node.children[0].tag;
                    switch (state) {
                        case "composing":

                            System.out.println(node.getAttribute("from") + " : is typing");
                            onGetTyping(node.getAttribute("from"));
                            break;
                        case "paused":
                            System.out.println(node.getAttribute("from") + " : paused");
                            onGetPaused(node.getAttribute("from"));
                            break;
                        default:
                            throw new UnsupportedOperationException("");
                    }
                }

                if ("ack".equals(node.tag)) {
                    String cls = node.getAttribute("class");
                    if ("message".equals(cls)) {
                        //server receipt
                        System.out.println(node.getAttribute("from") + " Server " + node.getAttribute("id"));
                        onGetMessageReceivedServer(node.getAttribute("from"), node.getAttribute("id"));
                    }
                }

                if ("notification".equals(node.tag)) {
                    this.handleNotification(node);
                }

                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    protected void handleMessage(ProtocolTreeNode node, boolean autoReceipt) {
        if (!(node.getAttribute("notify") == null)) {
            String name = node.getAttribute("notify");
            System.out.println("get Contact Name from :" + node.getAttribute("from") + ":Contact name:" + name);
            //this.fireOnGetContactName(node.getAttribute("from"), name);
        }
        if ("error".equals(node.getAttribute("type"))) {
            throw new UnsupportedOperationException("Not implemented error");//NotImplementedException(node.NodeString());
        }
        if (node.getChild("body") != null) {
            try {
                //text message
                onGetMessage(node, node.getAttribute("from"), node.getAttribute("id"), node.getAttribute("notify"), new String(node.getChild("body").getData(),"UTF-8"), autoReceipt);
                System.out.println("Message Body:" + node.getAttribute("from") + " ID-" + node.getAttribute("id") + " notify-" + node.getAttribute("notify") + " Body:" + new String(node.getChild("body").getData(), "UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(AirSendBase.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (autoReceipt) {
                this.sendMessageReceived(node, "received");
            }
        }
        if (node.getChild("media") != null) {
            ProtocolTreeNode media = node.getChild("media");
            //media message

            //define variables in switch
            String file, url, from, id;
            int size;
            byte[] preview, dat;
            id = node.getAttribute("id");
            from = node.getAttribute("from");
            switch (media.getAttribute("type")) {
                case "image":
                    url = media.getAttribute("url");
                    file = media.getAttribute("file");
                    size = Integer.parseInt(media.getAttribute("size"));
                    preview = media.getData();
                    System.out.println("Image received URL is :" + url);
                    //this.fireOnGetMessageImage(node, from, id, file, size, url, preview);
                    break;
                case "audio":
                    file = media.getAttribute("file");
                    size = Integer.parseInt(media.getAttribute("size"));
                    url = media.getAttribute("url");
                    preview = media.getData();
                    System.out.println("Audio Received:" + url);
                    //this.fireOnGetMessageAudio(node, from, id, file, size, url, preview);
                    break;
                case "video":
                    file = media.getAttribute("file");
                    size = Integer.parseInt(media.getAttribute("size"));
                    url = media.getAttribute("url");
                    preview = media.getData();
                    System.out.println("Video received:" + url);
                    //this.fireOnGetMessageVideo(node, from, id, file, size, url, preview);
                    break;
                case "location":
                    double lon = Double.parseDouble(media.getAttribute("longitude"));
                    double lat = Double.parseDouble(media.getAttribute("latitude"));
                    preview = media.getData();
                    name = media.getAttribute("name");
                    url = media.getAttribute("url");
                    System.out.println("Location received: longitude " + lon);
                    //this.fireOnGetMessageLocation(node, from, id, lon, lat, url, name, preview);
                    break;
                case "vcard":
                    ProtocolTreeNode vcard = media.getChild("vcard");
                    name = vcard.getAttribute("name");
                    dat = vcard.getData();
                    System.out.println("VCard received:" + name);
                    //this.fireOnGetMessageVcard(node, from, id, name, dat);
                    break;
            }
            this.sendMessageReceived(node, "received");
        }
    }

    protected void handleIq(ProtocolTreeNode node) {
        if ("error".equals(node.getAttribute("type"))) {

            System.out.println("Error in IQ");
            // this.fireOnError(node.getAttribute("id"), node.getAttribute("from"), Int32.Parse(node.GetChild("error").getAttribute("code")), node.GetChild("error").getAttribute("text"));
        }
        if (node.getChild("sync") != null) {
            //sync result
            ProtocolTreeNode sync = node.getChild("sync");
            ProtocolTreeNode existing = sync.getChild("in");
            ProtocolTreeNode nonexisting = sync.getChild("out");
            //process existing first
            HashMap<String, String> existingUsers = new HashMap<>();
            if (existing != null) {
                for (ProtocolTreeNode child : existing.getAllChildren()) {
                    try {
                        existingUsers.put(new String(child.getData(), "UTF-8"), child.getAttribute("jid"));
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(AirSendBase.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            //now process failed numbers
            ArrayList<String> failedNumbers = new ArrayList<>();
            if (nonexisting != null) {
                for (ProtocolTreeNode child : nonexisting.getAllChildren()) {
                    try {
                        failedNumbers.add(new String(child.getData(), "UTF-8"));
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(AirSendBase.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            int index = 0;
            index = Integer.parseInt(sync.getAttribute("index"));
            System.out.println("Get sync result");
            // this.fireOnGetSyncResult(index, sync.getAttribute("sid"), existingUsers, failedNumbers.ToArray());
        }
        if (node.getAttribute("type").equalsIgnoreCase("result")
                && node.getChild("query") != null) {
            //last seen
            Date lastSeen = new Date();
            System.out.println(node.getAttribute("from") + " Last seen+");
            //this.fireOnGetLastSeen(node.getAttribute("from"), lastSeen);
        }
        if (node.getAttribute("type").equalsIgnoreCase("result")
                && (node.getChild("media") != null || node.getChild("duplicate") != null)) {
            //media upload
            this.uploadResponse = node;
        }
        if (node.getAttribute("type").equalsIgnoreCase("result")
                && node.getChild("picture") != null) {
            //profile picture
            String from = node.getAttribute("from");
            String id = node.getChild("picture").getAttribute("id");
            byte[] dat = node.getChild("picture").getData();
            String type = node.getChild("picture").getAttribute("type");
            if ("preview".equals(type)) {
                System.out.println("Get photo preview" + from);
                // this.fireOnGetPhotoPreview(from, id, dat);
            } else {
                System.out.println("Get photo:" + from);
                // this.fireOnGetPhoto(from, id, dat);
            }
        }
        if (node.getAttribute("type").equalsIgnoreCase("get")
                && node.getChild("ping") != null) {
            this.sendPong(node.getAttribute("id"));
        }
        if (node.getAttribute("type").equalsIgnoreCase("result")
                && node.getChild("group") != null) {
            //group(s) info
            ArrayList<GroupInformation> groups = new ArrayList<GroupInformation>();
            for (ProtocolTreeNode group : node.children) {
                groups.add(new GroupInformation(
                        group.getAttribute("id"),
                        group.getAttribute("owner"),
                        group.getAttribute("creation"),
                        group.getAttribute("subject"),
                        group.getAttribute("s_t"),
                        group.getAttribute("s_o")));
            }

            System.out.println("Get groups:" + groups.size());
            //onGetGroups(groups.ToArray());
        }
        if (node.getAttribute("type").equalsIgnoreCase("result")
                && node.getChild("participant") != null) {
            //group participants
            ArrayList<String> participants = new ArrayList<String>();
            for (ProtocolTreeNode part : node.getAllChildren()) {
                if ("participant".equals(part.tag) && !(part.getAttribute("jid") == null)) {
                    participants.add(part.getAttribute("jid"));
                }
            }
            System.out.println("get group participants");
            // this.fireOnGetGroupParticipants(node.getAttribute("from"), participants.ToArray());
        }
        if ("result".equals(node.getAttribute("type")) && node.getChild("status") != null) {
            for (ProtocolTreeNode status : node.getChild("status").getAllChildren()) {
                System.out.println("On get status:");

                //this.fireOnGetStatus(status.getAttribute("jid"),
                //  "result",
                //null,
                //WhatsApp.SYSEncoding.GetString(status.GetData()));
            }
        }
        if ("result".equals(node.getAttribute("type")) && node.getChild("privacy") != null) {
            HashMap<VisibilityCategory, VisibilitySetting> settings = new HashMap<VisibilityCategory, VisibilitySetting>();
            for (ProtocolTreeNode child : node.getChild("privacy").getAllChildren("category")) {
                /*settings.add(this.parsePrivacyCategory(
                 child.getAttribute("name")), 
                 this.parsePrivacySetting(child.getAttribute("value"))
                 );*/
            }
            System.out.println("get privacy settings ");
            onGetPrivacySettings(settings);
        }
    }

    protected void handleNotification(ProtocolTreeNode node) {
        if (!(node.getAttribute("notify") == null)) {
            System.out.println("on Contact Name:" + node.getAttribute("from"));
            onGetContactName(node.getAttribute("from"), node.getAttribute("notify"));
        }
        String type = node.getAttribute("type");
        switch (type) {
            case "picture":
                ProtocolTreeNode child = node.children[0];

                System.out.println("On notification picture");
                onNotificationPicture(child.tag, 
                  child.getAttribute("jid"), 
                 child.getAttribute("id"));
                break;
            case "status":
                ProtocolTreeNode child2 = node.children[0];

                System.out.println("On Get status:" + node.getAttribute("from"));
                onGetStatus(node.getAttribute("from"), 
                 child2.tag, 
                node.getAttribute("notify"), 
                new String(child2.getData()));
                break;
            case "subject":
                //fire username notify
                System.out.println("on user name notify" + node.getAttribute("participant"));
                onGetContactName(node.getAttribute("participant"),
                    node.getAttribute("notify"));
                
                System.out.println("On group subject notify:" + node.getAttribute("from"));
                onGetGroupSubject(node.getAttribute("from"),
                 node.getAttribute("participant"),
                 node.getAttribute("notify"),
                 new String(node.getChild("body").getData()),
                 new Date(Date.parse(node.getAttribute("t"))));
                break;
            case "contacts":
                //TODO
                break;
            case "participant":
                String gjid = node.getAttribute("from");
                String t = node.getAttribute("t");
                for (ProtocolTreeNode child3 : node.getAllChildren()) {
                    if (child3.tag == "add") {
                        /* this.fireOnGetParticipantAdded(gjid, 
                         child3.getAttribute("jid"), 
                         GetDateTimeFromTimestamp(t));*/
                        System.out.println("Participant added to group" + child3.getAttribute("jid"));
                    } else if (child3.tag == "remove") {
                        /* this.fireOnGetParticipantRemoved(gjid, 
                         child3.getAttribute("jid"), 
                         child3.getAttribute("author"), 
                         GetDateTimeFromTimestamp(t));*/
                        System.out.println("Participant removed");
                    } else if (child3.tag == "modify") {
                        /*this.fireOnGetParticipantRenamed(gjid,
                         child3.getAttribute("remove"),
                         child3.getAttribute("add"),
                         GetDateTimeFromTimestamp(t));*/
                        System.out.println("participant renamed" + gjid);
                    }
                }
                break;
        }
        this.sendNotificationAck(node, null);
    }

    private void sendNotificationAck(ProtocolTreeNode node, String type) {
        String from = node.getAttribute("from");
        String to = node.getAttribute("to");
        String participant = node.getAttribute("participant");
        String id = node.getAttribute("id");
        if (type == null) {
            type = node.getAttribute("type");
        }
        ArrayList<KeyValue> attributes = new ArrayList<KeyValue>();
        if (!(to == null)) {
            attributes.add(new KeyValue("from", to));
        }
        if (!(participant == null)) {
            attributes.add(new KeyValue("participant", participant));
        }
        attributes.add(new KeyValue("to", from));

        attributes.add(new KeyValue("class", node.tag));
        attributes.add(new KeyValue("id", id));
        attributes.add(new KeyValue("type", type));

        ProtocolTreeNode sendNode = new ProtocolTreeNode("ack", toKeyValueArray(attributes));
        this.sendNode(sendNode);
    }

    protected void sendMessageReceived(ProtocolTreeNode msg, String response) {
        FMessage tmpMessage = new FMessage(new FMessage.FMessageIdentifierKey(msg.getAttribute("from"), true, msg.getAttribute("id")));
        this.sendMessageReceived(tmpMessage, response);
    }

    public void sendAvailableForChat(String nickName, boolean isHidden) {
        ProtocolTreeNode node = new ProtocolTreeNode("presence", new KeyValue[]{new KeyValue("name", (!(nickName == null) ? nickName : this.name))});
        this.sendNode(node);
    }

    protected void sendClearDirty(String[] categoryNames) {
        String id = TicketCounter.getInstance().makeId("clean_dirty_");
        ArrayList<ProtocolTreeNode> children = new ArrayList<ProtocolTreeNode>();
        for (String category : categoryNames) {
            ProtocolTreeNode cat = new ProtocolTreeNode("clean", new KeyValue[]{new KeyValue("type", category)});
            children.add(cat);
        }
        ProtocolTreeNode node = new ProtocolTreeNode("iq",
                new KeyValue[]{
            new KeyValue("id", id),
            new KeyValue("type", "set"),
            new KeyValue("to", "s.whatsapp.net"),
            new KeyValue("xmlns", "urn:xmpp:whatsapp:dirty")
        }, toProtocolTreeNodeArray(children));
        this.sendNode(node);
    }

    protected void sendClearDirty(String category) {
        this.sendClearDirty(new String[]{category});
    }

    protected void sendDeliveredReceiptAck(String to, String id) {
        this.sendReceiptAck(to, id, "delivered");
    }

    protected void sendMessageReceived(FMessage message, String response) {
        ProtocolTreeNode node = new ProtocolTreeNode("receipt", new KeyValue[]{
            new KeyValue("to", message.identifier_key.remote_jid),
            new KeyValue("id", message.identifier_key.id)
        });

        this.sendNode(node);
    }

    protected void sendNotificationReceived(String jid, String id) {
        ProtocolTreeNode child = new ProtocolTreeNode("received", new KeyValue[]{new KeyValue("xmlns", "urn:xmpp:receipts")});
        ProtocolTreeNode node = new ProtocolTreeNode("message", new KeyValue[]{new KeyValue("to", jid), new KeyValue("type", "notification"), new KeyValue("id", id)}, new ProtocolTreeNode[]{child});
        this.sendNode(node);
    }

    protected void sendPong(String id) {
        ProtocolTreeNode node = new ProtocolTreeNode("iq", new KeyValue[]{new KeyValue("type", "result"), new KeyValue("to", StatusConstant.whatsAppRealm), new KeyValue("id", id)});
        this.sendNode(node);
    }

    private void sendReceiptAck(String to, String id, String receiptType) {
        ProtocolTreeNode tmpChild = new ProtocolTreeNode("ack", new KeyValue[]{new KeyValue("xmlns", "urn:xmpp:receipts"), new KeyValue("type", receiptType)});
        ProtocolTreeNode resultNode = new ProtocolTreeNode("message", new KeyValue[]{
            new KeyValue("to", to),
            new KeyValue("type", "chat"),
            new KeyValue("id", id)
        }, new ProtocolTreeNode[]{tmpChild});
        this.sendNode(resultNode);
    }

    @Override
    public void onConnectSuccess() {
        handleMessage.onConnectSuccess();
    }

    @Override
    public void onDisconnect(Exception e) {
         handleMessage.onDisconnect(e);
    }

    @Override
    public void onConnectFailed(Exception e) {
         handleMessage.onConnectFailed(e);
    }

    @Override
    public void onLoginSuccess(String phoneNumber, byte[] data) {
       handleMessage.onLoginSuccess(phoneNumber, data);
    }

    @Override
    public void onLoginFailed(String data) {
         handleMessage.onLoginFailed(data);
    }

    @Override
    public void onGetMessage(ProtocolTreeNode messageNode, String from, String id, String name, String message, boolean receipt_sent) {
         handleMessage.onGetMessage(messageNode, from, id, name, message, receipt_sent);
    }

    @Override
    public void onGetMessageImage(ProtocolTreeNode mediaNode, String from, String id, String fileName, int fileSize, String url, byte[] preview) {
          handleMessage.onGetMessageImage(mediaNode, from, id, fileName, fileSize, url, preview);
    }

    @Override
    public void onGetMessageVideo(ProtocolTreeNode mediaNode, String from, String id, String fileName, int fileSize, String url, byte[] preview) {
         handleMessage.onGetMessageVideo(mediaNode, from, id, fileName, fileSize, url, preview);
    }

    @Override
    public void onGetMessageAudio(ProtocolTreeNode mediaNode, String from, String id, String fileName, int fileSize, String url, byte[] preview) {
         handleMessage.onGetMessageAudio(mediaNode, from, id, fileName, fileSize, url, preview);
    }

    @Override
    public void onGetMessageLocation(ProtocolTreeNode locationNode, String from, String id, double lon, double lat, String url, String name, byte[] preview) {
      handleMessage.onGetMessageLocation(locationNode, from, id, lon, lat, url, name, preview);
    }

    @Override
    public void onGetMessageVcard(ProtocolTreeNode vcardNode, String from, String id, String name, byte[] data) {
         handleMessage.onGetMessageVcard(vcardNode, from, id, name, data);
    }

    @Override
    public void onError(String id, String from, int code, String text) {
         handleMessage.onError(id, from, code, text);
    }

    @Override
    public void onNotificationPicture(String type, String jid, String id) {
         handleMessage.onNotificationPicture(type, jid, id);
    }

    @Override
    public void onGetMessageReceivedServer(String from, String id) {
         handleMessage.onGetMessageReceivedServer(from, id);
    }

    @Override
    public void onGetMessageReceivedClient(String from, String id) {
        handleMessage.onGetMessageReceivedClient(from, id);
    }

    @Override
    public void onGetPresence(String from, String type) {
         handleMessage.onGetPresence(from, type);
    }

    @Override
    public void onGetGroupParticipants(String gjid, String[] jids) {
         handleMessage.onGetGroupParticipants(gjid, jids);
    }

    @Override
    public void onGetLastSeen(String from, Date lastSeen) {
         handleMessage.onGetLastSeen(from, lastSeen);
    }

    @Override
    public void onGetTyping(String from) {
         handleMessage.onGetTyping(from);
    }

    @Override
    public void onGetPaused(String from) {
         handleMessage.onGetPaused(from);
    }

    @Override
    public void onGetPhoto(String from, String id, byte[] preview) {
         handleMessage.onGetPhoto(from, id, preview);
    }

    @Override
    public void onGetPhotoPreview(String from, String id, byte[] data) {
         handleMessage.onGetPhotoPreview(from, id, data);
    }

    @Override
    public void onGetGroups(GroupInformation[] groups) {
         handleMessage.onGetGroups(groups);
    }

    @Override
    public void onGetContactName(String from, String contactName) {
         handleMessage.onGetContactName(from, contactName);
    }

    @Override
    public void onGetStatus(String from, String type, String name, String status) {
         handleMessage.onGetStatus(from, type, name, status);
    }

    @Override
    public void onGetSyncResult(int index, String sid, HashMap<String, String> existingUsers, String[] failedNumbers) {
         handleMessage.onGetSyncResult(index, sid, existingUsers, failedNumbers);
    }

    @Override
    public void onGetPrivacySettings(HashMap<VisibilityCategory, VisibilitySetting> settings) {
        
    }

    @Override
    public void onGetParticipantAdded(String gjid, String jid, Date time) {
        
    }

    @Override
    public void onGetParticipantRemoved(String gjid, String jid, String author, Date time) {
        
    }

    @Override
    public void onGetParticipantRenamed(String gjid, String oldJid, String newJid, Date time) {
        
    }

    @Override
    public void onGetGroupSubject(String gjid, String jid, String username, String subject, Date time) {
        
    }
}
