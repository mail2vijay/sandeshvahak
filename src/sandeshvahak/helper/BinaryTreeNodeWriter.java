/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sandeshvahak.helper;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Vijay
 */
public class BinaryTreeNodeWriter {

    private List<Byte> buffer;
    public KeyStream Key;

    public BinaryTreeNodeWriter() {
        buffer = new ArrayList<>();
    }

    public byte[] startStream(String domain, String resource) {
        List<KeyValue> attributes = new ArrayList<>();
        this.buffer = new ArrayList<>();
        attributes.add(new KeyValue("to", domain));
        attributes.add(new KeyValue("resource", resource));
        this.writeListStart(attributes.size() * 2 + 1);
        this.buffer.add((byte) 1);
        this.writeAttributes(attributes.toArray());
        byte[] ret = flushBuffer(true);
        this.buffer.add((byte) 'W');
        this.buffer.add((byte) 'A');
        this.buffer.add((byte) 0x1);
        this.buffer.add((byte) 0x4);
        addRange(ret, buffer);
        ret = toArray(buffer);
        this.buffer = new ArrayList<>();
        return ret;
    }

    private void addRange(byte[] ret, List<Byte> v) {
        for (int i = 0; i < ret.length; i++) {
            v.add(ret[i]);
        }
    }

    private byte[] toArray(List<Byte> v) {
        byte[] toArray = new byte[v.size()];
        for (int i = 0; i < toArray.length; i++) {
            toArray[i] = v.get(i).byteValue();
        }
        return toArray;
    }

    private byte[] copyOfRange(byte[] data, int offset, int length) {
        byte[] da = new byte[length + 3];
        int j = 0;
        for (int i = offset; i < length; i++) {
            da[j++] = data[i];
        }
        return da;
    }

    public byte[] write(ProtocolTreeNode node, boolean encrypt) {

        if (node == null) {
            this.buffer.add((byte) 0);
        } else {
            this.writeInternal(node);
        }
        return this.flushBuffer(encrypt);
    }

    protected byte[] flushBuffer(boolean encrypt) {

        byte[] data = toArray(this.buffer);
        byte[] data2 = new byte[data.length + 4];
        System.arraycopy(data, 0, data2, 0, data.length);
        byte[] size = this.getInt24(data.length);
        if (encrypt && this.Key != null) {

            byte[] paddedData = new byte[data.length];
            System.arraycopy(data, 0, paddedData, 0, data.length);
            this.Key.encodeMessage(paddedData, paddedData.length - 4, 0, paddedData.length - 4);
            data = paddedData;
            //add encryption signature
            int encryptedBit = 0;
            encryptedBit |= 8;
            long dataLength = data.length;
            size[0] = (byte) ((long) ((long) encryptedBit << 4) | (long) ((dataLength & 16711680L) >> 16));
            size[1] = (byte) ((dataLength & 65280L) >> 8);
            size[2] = (byte) (dataLength & 255L);
        }
        byte[] ret = new byte[data.length + 3];
        System.arraycopy(size, 0, ret, 0, 3);
        System.arraycopy(data, 0, ret, 3, data.length);
        this.buffer = new ArrayList<>();
        return ret;
    }

    protected void writeAttributes(Object[] attributes) {

        if (attributes != null) {
            for (Object keyValue : attributes) {
                this.writeString(((KeyValue) keyValue).getKey());
                this.writeString(((KeyValue) keyValue).getValue());
            }
        }
    }

    private byte[] getInt16(int len) {
        byte[] ret = new byte[2];
        ret[0] = (byte) ((len & 0xff00) >> 8);
        ret[1] = (byte) (len & 0x00ff);
        return ret;
    }

    private byte[] getInt24(int len) {
        byte[] ret = new byte[3];
        ret[0] = (byte) ((len & 0xf0000) >> 16);
        ret[1] = (byte) ((len & 0xff00) >> 8);
        ret[2] = (byte) (len & 0xff);
        return ret;
    }

    protected void writeBytes(String bytes) {
        try {
            writeBytes(bytes.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    protected void writeBytes(byte[] bytes) {

        int len = bytes.length;
        if (len >= 0x100) {
            this.buffer.add((byte) 0xfd);
            this.writeInt24(len);
        } else {
            this.buffer.add((byte) (0xfc));
            this.writeInt8(len);
        }
        addRange(bytes, buffer);
    }

    protected void writeInt16(int v) {
        this.buffer.add((byte) ((v & 0xff00) >> 8));
        this.buffer.add((byte) (v & 0x00ff));
    }

    protected void writeInt24(int v) {
        this.buffer.add((byte) ((v & 0xff0000) >> 16));
        this.buffer.add((byte) ((v & 0x00ff00) >> 8));
        this.buffer.add((byte) ((v & 0x0000ff)));
    }

    protected void writeInt8(int v) {

        this.buffer.add((byte) (v & 0xff));
    }

    protected void writeInternal(ProtocolTreeNode node) {
        int len = 1;
        if (node.attributeHash != null) {
            len += node.attributeHash.length * 2;
        }
        if (node.children.length == 1) {
            len += 1;
        }
        if (node.data.length > 0) {
            len += 1;
        }
        this.writeListStart(len);
        this.writeString(node.tag);
        this.writeAttributes(node.attributeHash);
        if (node.data.length > 0) {
            this.writeBytes(node.data);
        }
        if (node.children != null && node.children.length > 0) {
            this.writeListStart(node.children.length);
            for (int i = 0; i < node.children.length; i++) {
                this.writeInternal(node.children[i]);
            }
        }
    }

    protected void writeJid(String user, String server) {
        this.buffer.add((byte) (0xfa));
        if (user.length() > 0) {
            this.writeString(user);
        } else {
            this.writeToken(0);
        }
        this.writeString(server);
    }

    protected void writeListStart(int len) {

        if (len == 0) {

            this.buffer.add((byte) 0x00);
        } else if (len < 256) {
            this.buffer.add((byte) 0xf8);
            this.writeInt8(len);
        } else {
            this.buffer.add((byte) 0xf9);
            this.writeInt16(len);
        }
    }

    protected void writeString(String tag) {
        ByRef<Integer> intValue = new ByRef(-1);
        ByRef<Integer> num = new ByRef(-1);
        if (TokenDictionary.getInstance().tryGetToken(tag, num, intValue)) {
            if (num.get() >= 0) {
                this.writeToken(num.get());
            }
            this.writeToken(intValue.get());
            return;
        }
        int num2 = tag.indexOf('@');
        if (num2 < 1) {
            this.writeBytes(tag);
            return;
        }
        String server = tag.substring(num2 + 1);
        String user = tag.substring(0, num2);
        this.writeJid(user, server);

    }

    protected void writeToken(int token) {

        if (token < 0xf5) {
            this.buffer.add((byte) token);
        } else if (token <= 0x1f4) {
            this.buffer.add((byte) 0xfe);
            this.buffer.add((byte) (token - 0xf5));
        }
    }

    protected void debugPrint(String debugMsg) {
        if (debugMsg.length() > 0) {
            System.out.println(debugMsg);
        }
    }
}
