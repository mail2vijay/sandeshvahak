/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sandeshvahak.parser;

import java.util.Date;
import sandeshvahak.account.User;
import sandeshvahak.helper.TicketManager;

/**
 *
 * @author Vijay
 */
public class FMessage {
     public boolean gap_behind;
        public FMessageIdentifierKey identifier_key;
        public double latitude;
        public String location_details;
        public String location_url;
        public double longitude;
        public int media_duration_seconds;
        public String media_mime_type;
        public String media_name;
        public long media_size;
        public String media_url;
        public int media_wa_type;
        public boolean offline;
        public String remote_resource;
        public int status;
        public Object thumb_image;
        public Date timestamp;
        public boolean wants_receipt;
       public User user;
        public byte[] binary_data;
        public String data;
        public FMessage(FMessageIdentifierKey key)
        {
            this.status = Status.Undefined;
            this.gap_behind = true;
            this.identifier_key = key;
        }

         public FMessage(User remote_user, boolean from_me)
        {
            this.status = Status.Undefined;
            this.gap_behind = true;
            this.user = remote_user;
            this.identifier_key = new FMessageIdentifierKey(remote_user.getFullJid(), from_me, TicketManager.getInstance().generateId());
        }
        public FMessage(String remote_jid, boolean from_me)
        {
            this.status = Status.Undefined;
            this.gap_behind = true;
            this.identifier_key = new FMessageIdentifierKey(remote_jid, from_me, TicketManager.getInstance().generateId());
        }

        public FMessage(String remote_jid, String data, Object image)
            
        {
            this(remote_jid, true);
            this.data = data;
            this.thumb_image = image;
            this.timestamp = new Date();
        }
        public FMessage(User remote_user, String data, Object image)
             
        {
            this(remote_user, true);
            this.data = data;
            this.thumb_image = image;
            this.timestamp = new Date();
        }

        public void AcceptVisitor(FMessageVisitor visitor)
        {
            switch (this.media_wa_type)
            {
                case Type.Image:
                    visitor.Image(this);
                    return;

                case Type.Audio:
                    visitor.Audio(this);
                    return;

                case Type.Video:
                    visitor.Video(this);
                    return;

                case Type.Contact:
                    visitor.Contact(this);
                    return;

                case Type.Location:
                    visitor.Location(this);
                    return;

                case Type.System:
                    visitor.System(this);
                    return;
            }
            visitor.Undefined(this);
        }

        public static int getMessage_WA_Type(int type)
        {
            if (type!= 0)
            {
                if (type==Type.System)
                {
                    return Type.System;
                }
                if (type==Type.Image)
                {
                    return Type.Image;
                }
                if (type==Type.Audio)
                {
                    return Type.Audio;
                }
                if (type==Type.Video)
                {
                    return Type.Video;
                }
                if (type==Type.Contact)
                {
                    return Type.Contact;
                }
                if (type==Type.Location)
                {
                    return Type.Location;
                }
            }
            return Type.Undefined;
        }

        public static String GetMessage_WA_Type_StrValue(int type)
        {
            if (type != Type.Undefined)
            {
                if (type == Type.System)
                {
                    return "system";
                }
                if (type == Type.Image)
                {
                    return "image";
                }
                if (type == Type.Audio)
                {
                    return "audio";
                }
                if (type == Type.Video)
                {
                    return "video";
                }
                if (type == Type.Contact)
                {
                    return "vcard";
                }
                if (type == Type.Location)
                {
                    return "location";
                }
            }
            return null;
        }

        

        public class Builder
        {
             byte[] binary_data;
             String data;
             boolean from_me;
             String id;
             double latitude;
             String location_details;
             String location_url;
            double longitude;
             int media_duration_seconds;
             String media_name;
             long media_size;
             String media_url;
             int media_wa_type;
            FMessage message;
             boolean offline;
             String remote_jid;
             String remote_resource;
            String thumb_image;
             Date timestamp;
             boolean wants_receipt;

            public byte[] BinaryData()
            {
                return this.binary_data;
            }

            public FMessage.Builder BinaryData(byte[] data)
            {
                this.binary_data = data;
                return this;
            }

            public FMessage Build()
            {
                if (this.message == null)
                {
                    return null;
                }
                if (((this.remote_jid != null) && this.from_me) && (this.id != null))
                {
                    this.message.identifier_key = new FMessage.FMessageIdentifierKey(this.remote_jid, this.from_me, this.id);
                }
                if (this.remote_resource != null)
                {
                    this.message.remote_resource = this.remote_resource;
                }
                if (this.wants_receipt)
                {
                    this.message.wants_receipt = this.wants_receipt;
                }
                if (this.data != null)
                {
                    this.message.data = this.data;
                }
                if (this.thumb_image != null)
                {
                    this.message.thumb_image = this.thumb_image;
                }
                if (this.timestamp!=null)
                {
                    this.message.timestamp = new Date(this.timestamp.getTime());
                }
                if (this.offline)
                {
                    this.message.offline = this.offline;
                }
                if (this.media_wa_type!=-1)
                {
                    this.message.media_wa_type = this.media_wa_type;
                }
                if (this.media_size>0)
                {
                    this.message.media_size = this.media_size;
                }
                if (this.media_duration_seconds>0)
                {
                    this.message.media_duration_seconds = this.media_duration_seconds;
                }
                if (this.media_url != null)
                {
                    this.message.media_url = this.media_url;
                }
                if (this.media_name != null)
                {
                    this.message.media_name = this.media_name;
                }
                if (this.latitude>0)
                {
                    this.message.latitude = this.latitude;
                }
                if (this.longitude>0)
                {
                    this.message.longitude = this.longitude;
                }
                if (this.location_url != null)
                {
                    this.message.location_url = this.location_url;
                }
                if (this.location_details != null)
                {
                    this.message.location_details = this.location_details;
                }
                if (this.binary_data != null)
                {
                    this.message.binary_data = this.binary_data;
                }
                return this.message;
            }

            public String Data()
            {
                return this.data;
            }

            public FMessage.Builder Data(String data)
            {
                this.data = data;
                return this;
            }

            public boolean From_me()
            {
                return this.from_me;
            }

            public FMessage.Builder From_me(boolean from_me)
            {
                this.from_me = from_me;
                return this;
            }

            public String Id()
            {
                return this.id;
            }

            public FMessage.Builder Id(String id)
            {
                this.id = id;
                return this;
            }

            public boolean Instantiated()
            {
                return (this.message != null);
            }

            public FMessage.Builder Key(FMessage.FMessageIdentifierKey key)
            {
                this.remote_jid = key.remote_jid;
                this.from_me = key.from_me;
                this.id = key.id;
                return this;
            }

            public double Latitude()
            {
                return this.latitude;
            }

            public FMessage.Builder Latitude(double latitude)
            {
                this.latitude =latitude;
                return this;
            }

            public String Location_details()
            {
                return this.location_details;
            }

            public FMessage.Builder Location_details(String details)
            {
                this.location_details = details;
                return this;
            }

            public String Location_url()
            {
                return this.location_url;
            }

            public FMessage.Builder Location_url(String url)
            {
                this.location_url = url;
                return this;
            }

            public double Longitude()
            {
                return this.longitude;
            }

            public FMessage.Builder Longitude(double longitude)
            {
                this.longitude = longitude;
                return this;
            }

            public int Media_duration_seconds()
            {
                return this.media_duration_seconds;
            }

            public FMessage.Builder Media_duration_seconds(int media_duration_seconds)
            {
                this.media_duration_seconds = media_duration_seconds;
                return this;
            }

            public String Media_name()
            {
                return this.media_name;
            }

            public FMessage.Builder Media_name(String media_name)
            {
                this.media_name = media_name;
                return this;
            }

            public long Media_size()
            {
                return this.media_size;
            }

            public FMessage.Builder Media_size(long media_size)
            {
                this.media_size = media_size;
                return this;
            }

            public String Media_url()
            {
                return this.media_url;
            }

            public FMessage.Builder Media_url(String media_url)
            {
                this.media_url = media_url;
                return this;
            }

            public int Media_wa_type()
            {
                return this.media_wa_type;
            }

            public FMessage.Builder Media_wa_type(int media_wa_type)
            {
                this.media_wa_type = media_wa_type;
                return this;
            }

            public FMessage.Builder NewIncomingInstance()
            {
                if (((this.remote_jid == null) || !this.from_me) || (this.id == null))
                {
                    throw new UnsupportedOperationException(
                        "missing required property before instantiating new incoming message");
                }
                this.message =
                    new FMessage(new FMessage.FMessageIdentifierKey(this.remote_jid, this.from_me, this.id));
                return this;
            }

            public FMessage.Builder NewOutgoingInstance()
            {
                if (((this.remote_jid == null) || (this.data == null)) || (this.thumb_image == null))
                {
                    throw new UnsupportedOperationException(
                        "missing required property before instantiating new outgoing message");
                }
                if ((this.id != null) || (this.from_me && !this.from_me))
                {
                    throw new UnsupportedOperationException("invalid property set before instantiating new outgoing message");
                }
                this.message = new FMessage(this.remote_jid, this.data, this.thumb_image);
                return this;
            }

            public boolean Offline()
            {
                return this.offline;
            }

            public FMessage.Builder Offline(boolean offline)
            {
                this.offline = offline;
                return this;
            }

            public String Remote_jid()
            {
                return this.remote_jid;
            }

            public FMessage.Builder Remote_jid(String remote_jid)
            {
                this.remote_jid = remote_jid;
                return this;
            }

            public String Remote_resource()
            {
                return this.remote_resource;
            }

            public FMessage.Builder Remote_resource(String remote_resource)
            {
                this.remote_resource = remote_resource;
                return this;
            }

            public FMessage.Builder SetInstance(FMessage message)
            {
                this.message = message;
                return this;
            }

            public String Thumb_image()
            {
                return this.thumb_image;
            }

            public FMessage.Builder Thumb_image(String thumb_image)
            {
                this.thumb_image = thumb_image;
                return this;
            }

            public Date Timestamp()
            {
                return this.timestamp;
            }

            public FMessage.Builder Timestamp(Date timestamp)
            {
                this.timestamp = timestamp;
                return this;
            }

            public boolean Wants_receipt()
            {
                return this.wants_receipt;
            }

            public FMessage.Builder Wants_receipt(boolean wants_receipt)
            {
                this.wants_receipt =wants_receipt;
                return this;
            }
        }

        public static class FMessageIdentifierKey
        {
            public boolean from_me;
            public String id;
            public String remote_jid;
            public String serverNickname;

            public FMessageIdentifierKey(String remote_jid, boolean from_me, String id)
            {
                this.remote_jid = remote_jid;
                this.from_me = from_me;
                this.id = id;
            }

            public boolean equals(Object obj)
            {
                if (this != obj)
                {
                    if (obj == null)
                    {
                        return false;
                    }
                    if (this.hashCode()!=obj.hashCode())
                    {
                        return false;
                    }
                    FMessage.FMessageIdentifierKey key = (FMessage.FMessageIdentifierKey)obj;
                    if (this.from_me != key.from_me)
                    {
                        return false;
                    }
                    if (this.id == null)
                    {
                        if (key.id != null)
                        {
                            return false;
                        }
                    }
                    else if (!this.id.equals(key.id))
                    {
                        return false;
                    }
                    if (this.remote_jid == null)
                    {
                        if (key.remote_jid != null)
                        {
                            return false;
                        }
                    }
                    else if (!this.remote_jid.equals(key.remote_jid))
                    {
                        return false;
                    }
                }
                return true;
            }

            public  int hashCode()
            {
                int num = 0x1f;
                int num2 = 1;
                num2 = (0x1f * 1) + (this.from_me ? 0x4cf : 0x4d5);
                num2 = (num * num2) + ((this.id == null) ? 0 : this.id.hashCode());
                return ((num * num2) + ((this.remote_jid == null) ? 0 : this.remote_jid.hashCode()));
            }

            public String ToString()
            {
                return "[Keyid="+this.id+",from_me="+ this.from_me +", remote_jid="+ this.remote_jid+"]";
                                     }
        }

        public interface Status
        {
            int UnsentOld=0;
            int Uploading=1;
            int Uploaded=2;
            int SentByClient=3;
            int ReceivedByServer=4;
            int ReceivedByTarget=5;
            int NeverSend=6;
            int ServerBounce=7;
            int Undefined=8;
            int Unsent=9;
        }

        public interface Type
        {
            int Audio = 2;
            int Contact = 4;
            int Image = 1;
            int Location = 5;
            int System = 7;
            int Undefined = 0;
            int Video = 3;
        }
}
