/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sandeshbase;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import sandesh.encoding.utility.Base64;
import sandeshvahak.helper.KeyValue;
import sandeshvahak.helper.ProtocolTreeNode;
import sandeshvahak.helper.TicketCounter;
import sandeshvahak.parser.FMessage;

/**
 *
 * @author victory
 */
public class AirBase extends AirSendBase {

    public AirBase(String phoneNum, String imei, String nick, boolean debug, boolean hidden) {
        super(phoneNum, imei, nick, debug, hidden);
    }

    public String sendMessage(String to, String txt) {
        FMessage tmpMessage = new FMessage(getJID(to), true);
        tmpMessage.data = txt;
        this.sendMessage(tmpMessage, this.hidden);
        return tmpMessage.identifier_key.ToString();
    }

    public void sendMessageVcard(String to, String name, String vcard_data) {
        FMessage tmpMessage = new FMessage(getJID(to), true);
        tmpMessage.data = vcard_data;
        tmpMessage.media_wa_type = FMessage.Type.Contact;
        tmpMessage.media_name = name;
        this.sendMessage(tmpMessage, this.hidden);
    }
    /*
     public void sendSync(String[] numbers, SyncMode mode, SyncContext context, int index, boolean last )
     {
     ArrayList<ProtocolTreeNode> users = new ArrayList<>();
     for(String number : numbers)
     {
     String _number = number;
     if (!_number.startsWith("+"))
     _number = "+"+number;
     users.add(new ProtocolTreeNode("user", null, _number.getBytes("UTF-8")));
     }
     ProtocolTreeNode node = new ProtocolTreeNode("iq", new KeyValue[]{
     new KeyValue("to", getJID(this.phoneNumber)),
     new KeyValue("type", "get"),
     new KeyValue("id", TicketCounter.getInstance().makeId("sendsync_")),
     new KeyValue("xmlns", "urn:xmpp:whatsapp:sync")
     }, new ProtocolTreeNode("sync", new KeyValue[]
     {
     new KeyValue("mode", mode.toString().toLowerCase()),
     new KeyValue("context", context.toString().toLowerCase()),
     new KeyValue("sid", new Date().toString()),
     new KeyValue("index", index.toString()),
     new KeyValue("last", last.toString())
     },
     users.toArray()
     )
     );
     this.SendNode(node);
     }

     /*  public void SendMessageImage(string to, byte[] ImageData, ImageType imgtype)
     {
     FMessage msg = this.getFmessageImage(to, ImageData, imgtype);
     if (msg != null)
     {
     this.SendMessage(msg);
     }
     }

     /*  protected FMessage getFmessageImage(string to, byte[] ImageData, ImageType imgtype)
     {
     string type = string.Empty;
     string extension = string.Empty;
     switch (imgtype)
     {
     case ImageType.PNG:
     type = "image/png";
     extension = "png";
     break;
     case ImageType.GIF:
     type = "image/gif";
     extension = "gif";
     break;
     default:
     type = "image/jpeg";
     extension = "jpg";
     break;
     }
            
     //create hash
     string filehash = string.Empty;
     using(HashAlgorithm sha = HashAlgorithm.Create("sha256"))
     {
     byte[] raw = sha.ComputeHash(ImageData);
     filehash = Convert.ToBase64String(raw);
     }

     //request upload
     WaUploadResponse response = this.UploadFile(filehash, "image", ImageData.Length, ImageData, to, type, extension);

     if (response != null && !String.IsNullOrEmpty(response.url))
     {
     //send message
     FMessage msg = new FMessage(to, true)
     {
     media_wa_type = FMessage.Type.Image,
     media_mime_type = response.mimetype,
     media_name = response.url.Split('/').Last(),
     media_size = response.size,
     media_url = response.url,
     binary_data = this.CreateThumbnail(ImageData)
     };
     return msg;
     }
     return null;
     }

     /*     public void SendMessageVideo(string to, byte[] videoData, VideoType vidtype)
     {
     FMessage msg = this.getFmessageVideo(to, videoData, vidtype);
     if (msg != null)
     {
     this.SendMessage(msg);
     }
     }

     /*  protected FMessage getFmessageVideo(string to, byte[] videoData, VideoType vidtype)
     {
     to = GetJID(to);
     string type = string.Empty;
     string extension = string.Empty;
     switch (vidtype)
     {
     case VideoType.MOV:
     type = "video/quicktime";
     extension = "mov";
     break;
     case VideoType.AVI:
     type = "video/x-msvideo";
     extension = "avi";
     break;
     default:
     type = "video/mp4";
     extension = "mp4";
     break;
     }

     //create hash
     string filehash = string.Empty;
     using (HashAlgorithm sha = HashAlgorithm.Create("sha256"))
     {
     byte[] raw = sha.ComputeHash(videoData);
     filehash = Convert.ToBase64String(raw);
     }

     //request upload
     WaUploadResponse response = this.UploadFile(filehash, "video", videoData.Length, videoData, to, type, extension);

     if (response != null && !String.IsNullOrEmpty(response.url))
     {
     //send message
     FMessage msg = new FMessage(to, true) { 
     media_wa_type = FMessage.Type.Video, 
     media_mime_type = response.mimetype, 
     media_name = response.url.Split('/').Last(), 
     media_size = response.size, 
     media_url = response.url, 
     media_duration_seconds = response.duration 
     };
     return msg;
     }
     return null;
     }

     /* public void SendMessageAudio(string to, byte[] audioData, AudioType audtype)
     {
     FMessage msg = this.getFmessageAudio(to, audioData, audtype);
     if (msg != null)
     {
     this.SendMessage(msg);
     }
     }

     /*   protected FMessage getFmessageAudio(string to, byte[] audioData, AudioType audtype)
     {
     to = GetJID(to);
     string type = string.Empty;
     string extension = string.Empty;
     switch (audtype)
     {
     case AudioType.WAV:
     type = "audio/wav";
     extension = "wav";
     break;
     case AudioType.OGG:
     type = "audio/ogg";
     extension = "ogg";
     break;
     default:
     type = "audio/mpeg";
     extension = "mp3";
     break;
     }

     //create hash
     string filehash = string.Empty;
     using (HashAlgorithm sha = HashAlgorithm.Create("sha256"))
     {
     byte[] raw = sha.ComputeHash(audioData);
     filehash = Convert.ToBase64String(raw);
     }

     //request upload
     WaUploadResponse response = this.UploadFile(filehash, "audio", audioData.Length, audioData, to, type, extension);

     if (response != null && !String.IsNullOrEmpty(response.url))
     {
     //send message
     FMessage msg = new FMessage(to, true) { 
     media_wa_type = FMessage.Type.Audio, 
     media_mime_type = response.mimetype, 
     media_name = response.url.Split('/').Last(), 
     media_size = response.size, 
     media_url = response.url, 
     media_duration_seconds = response.duration 
     };
     return msg;
     }
     return null;
     }

     /*protected Response UploadFile(String b64hash, String type, long size, byte[] fileData, String to, String contenttype, String extension)
     {
     ProtocolTreeNode media = new ProtocolTreeNode("media", new KeyValue[] {
     new KeyValue("hash", b64hash),
     new KeyValue("type", type),
     new KeyValue("size", size.ToString())
     });
     string id = TicketManager.GenerateId();
     ProtocolTreeNode node = new ProtocolTreeNode("iq", new KeyValue[] {
     new KeyValue("id", id),
     new KeyValue("to", WhatsConstants.WhatsAppServer),
     new KeyValue("type", "set"),
     new KeyValue("xmlns", "w:m")
     }, media);
     this.uploadResponse = null;
     this.SendNode(node);
     int i = 0;
     while (this.uploadResponse == null && i <= 10)
     {
     i++;
     this.pollMessage();
     }
     if (this.uploadResponse != null && this.uploadResponse.GetChild("duplicate") != null)
     {
     WaUploadResponse res = new WaUploadResponse(this.uploadResponse);
     this.uploadResponse = null;
     return res;
     }
     else
     {
     try
     {
     string uploadUrl = this.uploadResponse.GetChild("media").GetAttribute("url");
     this.uploadResponse = null;

     Uri uri = new Uri(uploadUrl);

     string hashname = string.Empty;
     byte[] buff = MD5.Create().ComputeHash(System.Text.Encoding.Default.GetBytes(b64hash));
     StringBuilder sb = new StringBuilder();
     foreach (byte b in buff)
     {
     sb.Append(b.ToString("X2"));
     }
     hashname = String.Format("{0}.{1}", sb.ToString(), extension);

     string boundary = "zzXXzzYYzzXXzzQQ";

     sb = new StringBuilder();

     sb.AppendFormat("--{0}\r\n", boundary);
     sb.Append("Content-Disposition: form-data; name=\"to\"\r\n\r\n");
     sb.AppendFormat("{0}\r\n", to);
     sb.AppendFormat("--{0}\r\n", boundary);
     sb.Append("Content-Disposition: form-data; name=\"from\"\r\n\r\n");
     sb.AppendFormat("{0}\r\n", this.phoneNumber);
     sb.AppendFormat("--{0}\r\n", boundary);
     sb.AppendFormat("Content-Disposition: form-data; name=\"file\"; filename=\"{0}\"\r\n", hashname);
     sb.AppendFormat("Content-Type: {0}\r\n\r\n", contenttype);
     string header = sb.ToString();

     sb = new StringBuilder();
     sb.AppendFormat("\r\n--{0}--\r\n", boundary);
     string footer = sb.ToString();

     long clength = size + header.Length + footer.Length;

     sb = new StringBuilder();
     sb.AppendFormat("POST {0}\r\n", uploadUrl);
     sb.AppendFormat("Content-Type: multipart/form-data; boundary={0}\r\n", boundary);
     sb.AppendFormat("Host: {0}\r\n", uri.Host);
     sb.AppendFormat("User-Agent: {0}\r\n", WhatsConstants.UserAgent);
     sb.AppendFormat("Content-Length: {0}\r\n\r\n", clength);
     string post = sb.ToString();

     TcpClient tc = new TcpClient(uri.Host, 443);
     SslStream ssl = new SslStream(tc.GetStream());
     try
     {
     ssl.AuthenticateAsClient(uri.Host);
     }
     catch (Exception e)
     {
     throw e;
     }

     List<byte> buf = new List<byte>();
     buf.AddRange(Encoding.UTF8.GetBytes(post));
     buf.AddRange(Encoding.UTF8.GetBytes(header));
     buf.AddRange(fileData);
     buf.AddRange(Encoding.UTF8.GetBytes(footer));

     ssl.Write(buf.ToArray(), 0, buf.ToArray().Length);

     //moment of truth...
     buff = new byte[1024];
     ssl.Read(buff, 0, 1024);

     string result = Encoding.UTF8.GetString(buff);
     foreach (string line in result.Split(new string[] { "\n" }, StringSplitOptions.RemoveEmptyEntries))
     {
     if (line.StartsWith("{"))
     {
     string fooo = line.TrimEnd(new char[] { (char)0 });
     JavaScriptSerializer jss = new JavaScriptSerializer();
     WaUploadResponse resp = jss.Deserialize<WaUploadResponse>(fooo);
     if (!String.IsNullOrEmpty(resp.url))
     {
     return resp;
     }
     }
     }
     }
     catch (Exception)
     { }
     }
     return null;
     }

     /*   protected void SendQrSync(byte[] qrkey, byte[] token = null)
     {
     string id = TicketCounter.MakeId("qrsync_");
     List<ProtocolTreeNode> children = new List<ProtocolTreeNode>();
     children.Add(new ProtocolTreeNode("sync", null, qrkey));
     if (token != null)
     {
     children.Add(new ProtocolTreeNode("code", null, token));
     }
     ProtocolTreeNode node = new ProtocolTreeNode("iq", new[] {
     new KeyValue("type", "set"),
     new KeyValue("id", id),
     new KeyValue("xmlns", "w:web")
     }, children.ToArray());
     this.SendNode(node);
     }
     */

    public void sendActive() {
        ProtocolTreeNode node = new ProtocolTreeNode("presence", new KeyValue[]{new KeyValue("type", "active")});
        this.sendNode(node);
    }

    /*public void SendAddParticipants(string gjid, IEnumerable<string> participants)
     {
     string id = TicketCounter.MakeId("add_group_participants_");
     this.SendVerbParticipants(gjid, participants, id, "add");
     }
     */
    public void sendUnavailable() {
        ProtocolTreeNode node = new ProtocolTreeNode("presence", new KeyValue[]{new KeyValue("type", "unavailable")});
        this.sendNode(node);
    }
    /*
     public void SendClientConfig(string platform, string lg, string lc)
     {
     string v = TicketCounter.MakeId("config_");
     var child = new ProtocolTreeNode("config", new[] { new KeyValue("xmlns", "urn:xmpp:whatsapp:push"), new KeyValue("platform", platform), new KeyValue("lg", lg), new KeyValue("lc", lc) });
     var node = new ProtocolTreeNode("iq", new[] { new KeyValue("id", v), new KeyValue("type", "set"), new KeyValue("to", WhatsConstants.WhatsAppRealm) }, new ProtocolTreeNode[] { child });
     this.SendNode(node);
     }

     public void SendClientConfig(String platform, String lg, String lc, URI pushUri, boolean preview, bool defaultSetting, bool groupsSetting, IEnumerable<GroupSetting> groups, Action onCompleted, Action<int> onError)
     {
     string id = TicketCounter.MakeId("config_");
     var node = new ProtocolTreeNode("iq",
     new[]
     {
     new KeyValue("id", id), new KeyValue("type", "set"),
     new KeyValue("to", "") //this.Login.Domain)
     },
     new ProtocolTreeNode[]
     {
     new ProtocolTreeNode("config",
     new[]
     {
     new KeyValue("xmlns","urn:xmpp:whatsapp:push"),
     new KeyValue("platform", platform),
     new KeyValue("lg", lg),
     new KeyValue("lc", lc),
     new KeyValue("clear", "0"),
     new KeyValue("id", pushUri.ToString()),
     new KeyValue("preview",preview ? "1" : "0"),
     new KeyValue("default",defaultSetting ? "1" : "0"),
     new KeyValue("groups",groupsSetting ? "1" : "0")
     },
     this.ProcessGroupSettings(groups))
     });
     this.sendNode(node);
     }

     public void sendClose()
     {
     ProtocolTreeNode node = new ProtocolTreeNode("presence", new KeyValue[] { new KeyValue("type", "unavailable") });
     this.sendNode(node);
     }

     public void sendComposing(String to)
     {
     this.sendChatState(to, "composing");
     }

       
     public void sendGetGroups()
     {
     string id = TicketCounter.MakeId("get_groups_");
     this.SendGetGroups(id, "participating");
     }

     public void sendGetOwningGroups()
     {
     String id = TicketCounter.getInstance().makeId("get_owning_groups_");
     this.sendGetGroups(id, "owning");
     }

     public void sendGetParticipants(String gjid)
     {
     String id = TicketCounter.getInstance().makeId("get_participants_");
     ProtocolTreeNode child = new ProtocolTreeNode("list", null);
     ProtocolTreeNode node = new ProtocolTreeNode("iq", new KeyValue[]{ new KeyValue("id", id), new KeyValue("type", "get"), new KeyValue("xmlns", "w:g"), new KeyValue("to", getJID(gjid)) }, new ProtocolTreeNode[]{child});
     this.sendNode(node);
     }

     public String sendGetPhoto(String jid, String expectedPhotoId, boolean largeFormat)
     {
     String id = TicketCounter.getInstance().makeId("get_photo_");
     ArrayList<KeyValue> attrList = new ArrayList<KeyValue>();
     if (!largeFormat)
     {
     attrList.add(new KeyValue("type", "preview"));
     }
     if (expectedPhotoId != null)
     {
     attrList.add(new KeyValue("id", expectedPhotoId));
     }
     ProtocolTreeNode child = new ProtocolTreeNode("picture", toKeyValueArray(attrList));
     ProtocolTreeNode node = new ProtocolTreeNode("iq", new KeyValue[] { new KeyValue("id", id), new KeyValue("type", "get"), new KeyValue("xmlns", "w:profile:picture"), new KeyValue("to", getJID(jid)) }, new ProtocolTreeNode[]{child});
     this.sendNode(node);
     return id;
     }

     public void sendGetPhotoIds(String[] jids)
     {
     String id = TicketCounter.getInstance().makeId("get_photo_id_");
     ProtocolTreeNode node = new ProtocolTreeNode("iq", new KeyValue[] { new KeyValue("id", id), new KeyValue("type", "get"), new KeyValue("to", getJID(this.phoneNumber))},
     new ProtocolTreeNode[]{new ProtocolTreeNode("list", new KeyValue[] { new KeyValue("xmlns", "w:profile:picture") },
     new ProtocolTreeNode[]{new ProtocolTreeNode("user", new KeyValue[]{ new KeyValue("jid", getJID(this.phoneNumber))})})});
     this.sendNode(node);
     }

     public void sendGetPrivacyList()
     {
     String id = TicketCounter.getInstance().makeId("privacylist_");
     ProtocolTreeNode innerChild = new ProtocolTreeNode("list", new KeyValue[] { new KeyValue("name", "default") });
     ProtocolTreeNode child = new ProtocolTreeNode("query", new KeyValue[] { new KeyValue("xmlns", "jabber:iq:privacy") }, new ProtocolTreeNode[]{innerChild});
     ProtocolTreeNode node = new ProtocolTreeNode("iq", new KeyValue[] { new KeyValue("id", id), new KeyValue("type", "get") }, new ProtocolTreeNode[]{child});
     this.sendNode(node);
     */

    public void sendGetServerProperties() {
        String id = TicketCounter.getInstance().makeId("get_server_properties_");
        ProtocolTreeNode node = new ProtocolTreeNode("iq", new KeyValue[]{new KeyValue("id", id), new KeyValue("type", "get"), new KeyValue("xmlns", "w"), new KeyValue("to", "s.whatsapp.net")},
                new ProtocolTreeNode[]{new ProtocolTreeNode("props", null)});
        this.sendNode(node);
    }

    public void sendGetStatuses(String[] jids) {
        ArrayList<ProtocolTreeNode> targets = new ArrayList<ProtocolTreeNode>();
        for (String jid : jids) {
            targets.add(new ProtocolTreeNode("user", new KeyValue[]{new KeyValue("jid", getJID(jid))}, null, null));
        }

        ProtocolTreeNode node = new ProtocolTreeNode("iq", new KeyValue[]{
            new KeyValue("to", "s.whatsapp.net"),
            new KeyValue("type", "get"),
            new KeyValue("xmlns", "status"),
            new KeyValue("id", TicketCounter.getInstance().makeId("getstatus"))
        }, new ProtocolTreeNode[]{
            new ProtocolTreeNode("status", null, toArrayProtocol(targets), null)
        }, null);

        this.sendNode(node);
    }

    public void sendInactive() {
        ProtocolTreeNode node = new ProtocolTreeNode("presence", new KeyValue[]{new KeyValue("type", "inactive")});
        this.sendNode(node);
    }

    /*   public void sendLeaveGroup(String gjid)
     {
     this.SendLeaveGroups(new string[] { gjid });
     }

     /*    public void sendLeaveGroups(String[] gjids)
     {
     String id = TicketCounter.getInstance().makeId("leave_group_");
     ProtocolTreeNode[] innerChilds = from gjid in gjids select new ProtocolTreeNode("group", new[] { new KeyValue("id", gjid) });
     var child = new ProtocolTreeNode("leave", null, innerChilds);
     var node = new ProtocolTreeNode("iq", new KeyValue[] { new KeyValue("id", id), new KeyValue("type", "set"), new KeyValue("xmlns", "w:g"), new KeyValue("to", "g.us") }, child);
     this.SendNode(node);
     }
     */
    public void sendMessage(FMessage message, boolean hidden) {
        if (message.media_wa_type != FMessage.Type.Undefined) {
            this.sendMessageWithMedia(message);
        } else {
            this.sendMessageWithBody(message, hidden);
        }
    }

    public void sendMessageBroadcast(String[] to, String message) {
        FMessage msg = new FMessage("", true);
        msg.data = message;
        msg.media_wa_type = FMessage.Type.Undefined;
        this.sendMessageBroadcast(to, msg);
    }
    /*
     public void sendMessageBroadcastImage(String[] recipients, byte[] ImageData, ImageType imgtype)
     {
     String to="";
     ArrayList<String> foo = new ArrayList<String>();
     for (String s :recipients)
     foo.add(getJID(s));
            
     for(String ss:foo)
     to+= ","+ss;
     FMessage msg = this.getFmessageImage(to, ImageData, imgtype);
     if (msg != null)
     {
     this.sendMessage(msg);
     }
     }

     public void sendMessageBroadcastAudio(String[] recipients, byte[] AudioData, AudioType audtype)
     {
     String to="";
     ArrayList<String> foo = new ArrayList<String>();
     for (String s :recipients)
     foo.add(getJID(s));
            
     for(String ss:foo)
     to+= ","+ss;
     FMessage msg = this.getFmessageAudio(to, AudioData, audtype);
     if (msg != null)
     {
     this.sendMessage(msg);
     }
     }

     public void sendMessageBroadcastVideo(String[] recipients, byte[] VideoData, VideoType vidtype)
     {
     String to="";
     ArrayList<String> foo = new ArrayList<>();
     for(String s : recipients)
            
     foo.add(getJID(s));
            
     for(String ss:foo)
     to+=","+ss;
     FMessage msg = this.getFmessageVideo(to, VideoData, vidtype);
     if (msg != null)
     {
     this.sendMessage(msg);
     }
     }

     public void sendMessageBroadcast(String[] to, FMessage message)
     {
     if (to != null && to.length > 0 && message != null && !(message.data==null))
     {
     ProtocolTreeNode child = null;
     if (message.media_wa_type == FMessage.Type.Undefined)
     {
     try {
     //text broadcast
     child = new ProtocolTreeNode("body", null, null, message.data.getBytes("UTF-8"));
     } catch (UnsupportedEncodingException ex) {
     Logger.getLogger(AirBase.class.getName()).log(Level.SEVERE, null, ex);
     }
     }
     else
     {
     throw new UnsupportedOperationException();
     }

     //compose broadcast envelope
     ProtocolTreeNode xnode = new ProtocolTreeNode("x", new KeyValue[] {
     new KeyValue("xmlns", "jabber:x:event")
     }, new ProtocolTreeNode[]{new ProtocolTreeNode("server", null)});
     ArrayList<ProtocolTreeNode> toNodes = new ArrayList<ProtocolTreeNode>();
     for(String target : to)
     {
     toNodes.add(new ProtocolTreeNode("to", new KeyValue[] { new KeyValue("jid",getJID(target)) }));
     }

     ProtocolTreeNode broadcastNode = new ProtocolTreeNode("broadcast", null, toArrayProtocol(toNodes));
     ProtocolTreeNode messageNode = new ProtocolTreeNode("message", new KeyValue[] {
     new KeyValue("to", "broadcast"),
     new KeyValue("type", message.media_wa_type == FMessage.Type.Undefined?"text":"media"),
     new KeyValue("id", message.identifier_key.id)
     }, new ProtocolTreeNode[] {
     broadcastNode,
     xnode,
     child
     });
     this.sendNode(messageNode);
     }
     }*/

    public void sendMessageBroadcast(String[] to, FMessage message) {
        if (to != null && to.length > 0 && message != null && !(message.data == null)) {
            ProtocolTreeNode child = null;
            if (message.media_wa_type == FMessage.Type.Undefined) {
                try {
                    //text broadcast
                    child = new ProtocolTreeNode("body", null, null, message.data.getBytes("UTF-8"));
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(AirBase.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                throw new UnsupportedOperationException();
            }

            //compose broadcast envelope
            ProtocolTreeNode xnode = new ProtocolTreeNode("x", new KeyValue[]{
                new KeyValue("xmlns", "jabber:x:event")
            }, new ProtocolTreeNode[]{new ProtocolTreeNode("server", null)});
            ArrayList<ProtocolTreeNode> toNodes = new ArrayList<ProtocolTreeNode>();
            for (String target : to) {
                toNodes.add(new ProtocolTreeNode("to", new KeyValue[]{new KeyValue("jid", getJID(target))}));
            }

            ProtocolTreeNode broadcastNode = new ProtocolTreeNode("broadcast", null, toArrayProtocol(toNodes));
            ProtocolTreeNode messageNode = new ProtocolTreeNode("message", new KeyValue[]{
                new KeyValue("to", "broadcast"),
                new KeyValue("type", message.media_wa_type == FMessage.Type.Undefined ? "text" : "media"),
                new KeyValue("id", message.identifier_key.id)
            }, new ProtocolTreeNode[]{
                broadcastNode,
                xnode,
                child
            });
            this.sendNode(messageNode);
        }
    }

    protected void sendChatState(String to, String type) {
        ProtocolTreeNode node = new ProtocolTreeNode("chatstate", new KeyValue[]{new KeyValue("to", getJID(to))}, new ProtocolTreeNode[]{
            new ProtocolTreeNode(type, null)
        });
        this.sendNode(node);
    }

    public void sendNop() {
        this.sendNode(null);
    }

    public void sendPaused(String to) {
        this.sendChatState(to, "paused");
    }

    public void sendPing() {
        String id = TicketCounter.getInstance().makeId("ping_");
        ProtocolTreeNode child = new ProtocolTreeNode("ping", new KeyValue[]{new KeyValue("xmlns", "w:p")});
        ProtocolTreeNode node = new ProtocolTreeNode("iq", new KeyValue[]{new KeyValue("id", id), new KeyValue("type", "get")}, new ProtocolTreeNode[]{child});
        this.sendNode(node);
    }

    public void sendPresenceSubscriptionRequest(String to) {
        ProtocolTreeNode node = new ProtocolTreeNode("presence", new KeyValue[]{new KeyValue("type", "subscribe"), new KeyValue("to", getJID(to))});
        this.sendNode(node);
    }

    public void sendQueryLastOnline(String jid) {
        String id = TicketCounter.getInstance().makeId("last_");
        ProtocolTreeNode child = new ProtocolTreeNode("query", null);
        ProtocolTreeNode node = new ProtocolTreeNode("iq", new KeyValue[]{new KeyValue("id", id), new KeyValue("type", "get"), new KeyValue("to", getJID(jid)), new KeyValue("xmlns", "jabber:iq:last")}, new ProtocolTreeNode[]{child});
        this.sendNode(node);
    }

    public void sendRemoveParticipants(String gjid, ArrayList<String> participants) {
        String id = TicketCounter.getInstance().makeId("remove_group_participants_");
        //this.SendVerbParticipants(gjid, participants, id, "remove");
    }

    public void sendSetGroupSubject(String gjid, String subject) {
        String id = TicketCounter.getInstance().makeId("set_group_subject_");
        ProtocolTreeNode child = new ProtocolTreeNode("subject", new KeyValue[]{new KeyValue("value", subject)});
        ProtocolTreeNode node = new ProtocolTreeNode("iq", new KeyValue[]{new KeyValue("id", id), new KeyValue("type", "set"), new KeyValue("xmlns", "w:g"), new KeyValue("to", gjid)}, new ProtocolTreeNode[]{child});
        this.sendNode(node);
    }

    /*   public void sendSetPhoto(byte[] bytes, byte[] thumbnailBytes)
     {
     String id = TicketCounter.getInstance().makeId("set_photo_");
     bytes = this.processProfilePicture(bytes);

     ArrayList<ProtocolTreeNode>list = new ArrayList<>();
     list.add(new ProtocolTreeNode("picture", null, null, bytes));

     if (thumbnailBytes == null)
     {
     //auto generate
     thumbnailBytes = this.createThumbnail(bytes);
     }

     try {
     //debug
     new FileOutputStream("pic.jpg").write(bytes);
     new FileOutputStream("picthumb.jpg").write(thumbnailBytes);
     } catch (FileNotFoundException ex) {
     Logger.getLogger(AirBase.class.getName()).log(Level.SEVERE, null, ex);
     } catch (IOException ex) {
     Logger.getLogger(AirBase.class.getName()).log(Level.SEVERE, null, ex);
     }
          

     if (thumbnailBytes != null)
     {
     list.add(new ProtocolTreeNode("picture", new KeyValue[] { new KeyValue("type", "preview") }, null, thumbnailBytes));
     }
     ProtocolTreeNode node = new ProtocolTreeNode("iq", new KeyValue[] { new KeyValue("id", id), new KeyValue("type", "set"), new KeyValue("xmlns", "w:profile:picture"), new KeyValue("to", getJID(this.phoneNumber)) }, toArrayProtocol(list));
     this.sendNode(node);
     }

     /*    public void sendSetPrivacyBlockedList(String[] jidSet)
     {
     String id = TicketCounter.getInstance().makeId("privacy_");
     ProtocolTreeNode[] nodeArray =  new ProtocolTreeNode[]{new ProtocolTreeNode(
     "item", 
     new KeyValue[]{ new KeyValue("type", "jid"), new KeyValue("value", jid), new KeyValue("action", "deny"), new KeyValue("order", String.valueOf(index))})
     };
     ProtocolTreeNode child = new ProtocolTreeNode("list", new KeyValue[] { new KeyValue("name", "default") }, (nodeArray.Length == 0) ? null : nodeArray);
     ProtocolTreeNode node2 = new ProtocolTreeNode("query", new KeyValue[] { new KeyValue("xmlns", "jabber:iq:privacy") }, child);
     ProtocolTreeNode node3 = new ProtocolTreeNode("iq", new KeyValue[] { new KeyValue("id", id), new KeyValue("type", "set") }, node2);
     this.sendNode(node3);
     }
     */
    public void sendStatusUpdate(String status) {
        try {
            String id = TicketCounter.getInstance().makeId("sendstatus_");

            ProtocolTreeNode node = new ProtocolTreeNode("iq", new KeyValue[]{
                new KeyValue("to", "s.whatsapp.net"),
                new KeyValue("type", "set"),
                new KeyValue("id", id),
                new KeyValue("xmlns", "status")
            },
                    new ProtocolTreeNode[]{
                        new ProtocolTreeNode("status", null, status.getBytes("UTF-8"))
                    });

            this.sendNode(node);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(AirBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendSubjectReceived(String to, String id) {
        ProtocolTreeNode child = new ProtocolTreeNode("received", new KeyValue[]{new KeyValue("xmlns", "urn:xmpp:receipts")});
        ProtocolTreeNode node = getSubjectMessage(to, id, child);
        this.sendNode(node);
    }

    public void sendUnsubscribeHim(String jid) {
        ProtocolTreeNode node = new ProtocolTreeNode("presence", new KeyValue[]{new KeyValue("type", "unsubscribed"), new KeyValue("to", jid)});
        this.sendNode(node);
    }

    public void SendUnsubscribeMe(String jid) {
        ProtocolTreeNode node = new ProtocolTreeNode("presence", new KeyValue[]{new KeyValue("type", "unsubscribe"), new KeyValue("to", jid)});
        this.sendNode(node);
    }

    public void sendGetGroups(String id, String type) {
        ProtocolTreeNode child = new ProtocolTreeNode("list", new KeyValue[]{new KeyValue("type", type)});
        ProtocolTreeNode node = new ProtocolTreeNode("iq", new KeyValue[]{new KeyValue("id", id), new KeyValue("type", "get"), new KeyValue("xmlns", "w:g"), new KeyValue("to", "g.us")}, new ProtocolTreeNode[]{child});
        this.sendNode(node);
    }

    protected void sendMessageWithBody(FMessage message, boolean hidden) {
        ProtocolTreeNode child = null;
        try {
            child = new ProtocolTreeNode("body", null, null, message.data.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(AirBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.sendNode(getMessageNode(message, child, hidden));
    }

    protected void sendMessageWithMedia(FMessage message) {
        ProtocolTreeNode node = null;
        if (FMessage.Type.System == message.media_wa_type) {
            throw new UnsupportedOperationException("Cannot send system message over the network");
        }

        ArrayList<KeyValue> list = new ArrayList<>();
        list.add(new KeyValue("xmlns", "urn:xmpp:whatsapp:mms"));
        list.add(new KeyValue("type", FMessage.GetMessage_WA_Type_StrValue(message.media_wa_type)));
        if (FMessage.Type.Location == message.media_wa_type) {
            list.add(new KeyValue("latitude", String.valueOf(message.latitude)));
            list.add(new KeyValue("longitude", String.valueOf(message.longitude)));
            if (message.location_details != null) {
                list.add(new KeyValue("name", message.location_details));
            }
            if (message.location_url != null) {
                list.add(new KeyValue("url", message.location_url));
            }
        } else if (((FMessage.Type.Contact != message.media_wa_type) && (message.media_name != null)) && ((message.media_url != null) && (message.media_size > 0L))) {
            list.add(new KeyValue("file", message.media_name));
            list.add(new KeyValue("size", String.valueOf(message.media_size)));
            list.add(new KeyValue("url", message.media_url));
            if (message.media_duration_seconds > 0) {
                list.add(new KeyValue("seconds", String.valueOf(message.media_duration_seconds)));
            }
        }
        if ((FMessage.Type.Contact == message.media_wa_type) && (message.media_name != null)) {
            try {
                node = new ProtocolTreeNode("media", toKeyValueArray(list), new ProtocolTreeNode[]{new ProtocolTreeNode("vcard", new KeyValue[]{new KeyValue("name", message.media_name)}, message.data.getBytes("UTF-8"))});
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(AirBase.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            byte[] data = message.binary_data;
            if ((data == null) && !(message.data.length() > 0)) {
                try {
                    data = Base64.decode(message.data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (data != null) {
                list.add(new KeyValue("encoding", "raw"));
            }
            node = new ProtocolTreeNode("media", toKeyValueArray(list), null, data);
        }
        this.sendNode(getMessageNode(message, node, false));
    }

    private KeyValue[] toKeyValueArray(ArrayList<KeyValue> value) {
        int length = value.size();
        KeyValue[] array = new KeyValue[length];
        for (int i = 0; i < length; i++) {
            array[i] = value.get(i);
        }
        return array;
    }

    private ProtocolTreeNode[] toArrayProtocol(ArrayList<ProtocolTreeNode> value) {
        int length = value.size();
        ProtocolTreeNode[] array = new ProtocolTreeNode[length];
        for (int i = 0; i < length; i++) {
            array[i] = value.get(i);
        }
        return array;
    }
    /*
     protected void sendVerbParticipants(String gjid, String[] participants, String id, String inner_tag)
     {
     ProtocolTreeNode[] source =  jid in participants select new ProtocolTreeNode("participant", new[] { new KeyValue("jid", getJID(jid)) });
     var child = new ProtocolTreeNode(inner_tag, null, source);
     var node = new ProtocolTreeNode("iq", new[] { new KeyValue("id", id), new KeyValue("type", "set"), new KeyValue("xmlns", "w:g"), new KeyValue("to", GetJID(gjid)) }, child);
     this.SendNode(node);
     }

     public void sendSetPrivacySetting(VisibilityCategory category, VisibilitySetting setting)
     {
     ProtocolTreeNode node = new ProtocolTreeNode("iq", new KeyValue[] { 
     new KeyValue("to", "s.whatsapp.net"),
     new KeyValue("id", TicketCounter.getInstance().makeId("setprivacy_")),
     new KeyValue("type", "set"),
     new KeyValue("xmlns", "privacy")
     }, new ProtocolTreeNode[] {
     new ProtocolTreeNode("privacy", null, new ProtocolTreeNode[] {
     new ProtocolTreeNode("category", new KeyValue [] {
     new KeyValue("name", this.privacyCategoryToString(category)),
     new KeyValue("value", this.privacySettingToString(setting))
     })
     })
     });

     this.sendNode(node);
     }
     */

    public void sendGetPrivacySettings() {
        ProtocolTreeNode node = new ProtocolTreeNode("iq", new KeyValue[]{
            new KeyValue("to", "s.whatsapp.net"),
            new KeyValue("id", TicketCounter.getInstance().makeId("getprivacy_")),
            new KeyValue("type", "get"),
            new KeyValue("xmlns", "privacy")
        }, new ProtocolTreeNode[]{
            new ProtocolTreeNode("privacy", null)
        });
        this.sendNode(node);
    }

    protected static ProtocolTreeNode getMessageNode(FMessage message, ProtocolTreeNode pNode, boolean hidden) {
        return new ProtocolTreeNode("message", new KeyValue[]{
            new KeyValue("to", message.identifier_key.remote_jid),
            new KeyValue("type", message.media_wa_type == FMessage.Type.Undefined ? "text" : "media"),
            new KeyValue("id", message.identifier_key.id)
        },
                new ProtocolTreeNode[]{
                    new ProtocolTreeNode("x", new KeyValue[]{new KeyValue("xmlns", "jabber:x:event")}, new ProtocolTreeNode[]{new ProtocolTreeNode("server", null)}),
                    pNode,
                    new ProtocolTreeNode("offline", null)
                });
    }

    protected static ProtocolTreeNode getSubjectMessage(String to, String id, ProtocolTreeNode child) {
        return new ProtocolTreeNode("message", new KeyValue[]{new KeyValue("to", to), new KeyValue("type", "subject"), new KeyValue("id", id)}, new ProtocolTreeNode[]{child});
    }

    public static String getJID(String target) {
        target = target.trim();
        if (!target.contains("@")) {
            //check if group message
            if (target.contains("-")) {
                //to group
                target += "@g.us";
            } else {
                //to normal user
                target += "@s.whatsapp.net";
            }
        }
        return target;
    }
}
