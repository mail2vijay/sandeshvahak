/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sandeshvahak.helper;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
/*
 *
 * @author Vijay
 */

public class BinaryTreeNodeReader {

    public KeyStream Key;
    private List<Byte> buffer;

    public BinaryTreeNodeReader() {
    }

    public void setKey(byte[] key, byte[] mac) {
        this.Key = new KeyStream(key, mac);
    }

    public ProtocolTreeNode nextTree(byte[] pInput, boolean useDecrypt) {
        if (pInput != null && pInput.length > 0) {
            this.buffer = new ArrayList<>();
            addToList(pInput, this.buffer);
            int stanzaFlag = (this.peekInt8(0) & 0xF0) >> 4;
            int stanzaSize = this.peekInt16(1);
            int flags = stanzaFlag;
            int size = stanzaSize;
            this.readInt24();
            boolean isEncrypted = (stanzaFlag & 8) != 0;
            if (isEncrypted) {
                if (this.Key != null) {
                    int realStanzaSize = stanzaSize - 4;
                    int macOffset = stanzaSize - 4;
                    byte[] treeData = toArray(this.buffer);
                    try {
                        this.Key.decodeMessage(treeData, macOffset, 0, realStanzaSize);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    this.buffer.clear();
                    addRange(treeData, buffer);
                } else {
                    throw new UnsupportedOperationException("Received encrypted message, encryption key not set");
                }
            }

            if (stanzaSize > 0) {
                ProtocolTreeNode node = this.nextTreeInternal();
                if (node != null) {
                    System.out.println("RECVD: " + node);
                }
                return node;
            }
        }
        return null;
    }

    private byte[] toArray(List<Byte> v) {
        byte[] toArray = new byte[v.size()];
        for (int i = 0; i < toArray.length; i++) {
            toArray[i] = v.get(i);
        }
        return toArray;
    }

    private void addRange(byte[] ret, List<Byte> v) {
        for (int i = 0; i < ret.length; i++) {
            v.add(new Byte(ret[i]));
        }
    }

    /**
     * add all elements of array to the list
     *
     * @param token
     * @return
     */
    private void addToList(byte[] byteArray, List<Byte> listReference) {
        for (int i = 0; i < byteArray.length; i++) {
            listReference.add(new Byte(byteArray[i]));
        }
    }

    protected String getToken(int token) {
        ByRef<String> tokenString = new ByRef(null);
        ByRef<Integer> num = new ByRef(-1);
        TokenDictionary.getInstance().getToken(token, num, tokenString);
        if (tokenString.get() == null) {
            token = readInt8();
            TokenDictionary.getInstance().getToken(token, num, tokenString);
        }
        return tokenString.get();
    }

    protected byte[] readBytes(int token) {
        byte[] ret = new byte[0];
        if (token == -1) {
            throw new UnsupportedOperationException("BinTreeNodeReader->readString: Invalid token " + token);
        }
        if ((token > 2) && (token < 245)) {
            try {
                ret = this.getToken(token).getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else if (token == 0) {
            ret = new byte[0];
        } else if (token == 252) {
            int size = this.readInt8();
            ret = this.fillArray(size);
        } else if (token == 253) {
            int size = this.readInt24();
            ret = this.fillArray(size);
        } else if (token == 254) {
            int tmpToken = this.readInt8();
            try {
                ret = this.getToken(tmpToken + 0xf5).getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else if (token == 250) {
            String user = null;
            String server = null;
            try {
                user = new String(this.readBytes(this.readInt8()), "UTF-8");
                server = new String(this.readBytes(this.readInt8()), "UTF-8");
                if ((user.length() > 0) && (server.length() > 0)) {
                    ret = (user + "@" + server).getBytes("UTF-8");
                } else if (server.length() > 0) {
                    ret = server.getBytes("UTF-8");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    protected KeyValue[] readAttributes(int size) {
        List<KeyValue> attributes = new ArrayList<>();
        try {
            int attribCount = (size - 2 + size % 2) / 2;
            for (int i = 0; i < attribCount; i++) {
                byte[] keyB = this.readBytes(this.readInt8());
                byte[] valueB = this.readBytes(this.readInt8());
                String key = new String(keyB, "UTF-8");
                String value = new String(valueB, "UTF-8");
                attributes.add(new KeyValue(key, value));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return keyValueToArray(attributes);
    }

    private KeyValue[] keyValueToArray(List<KeyValue> v) {
        KeyValue[] keyValue = new KeyValue[v.size()];
        for (int i = 0; i < v.size(); i++) {
            keyValue[i] = (KeyValue) v.get(i);
        }
        return keyValue;
    }

    protected ProtocolTreeNode nextTreeInternal() {
        int token1 = this.readInt8();
        int size = this.readListSize(token1);
        int token2 = this.readInt8();
        if (token2 == 1) {
            KeyValue[] attributes = this.readAttributes(size);
            return new ProtocolTreeNode("start", attributes);
        }
        if (token2 == 2) {
            return null;
        }
        String tag = new String(this.readBytes(token2));
        KeyValue[] tmpAttributes = this.readAttributes(size);

        if ((size % 2) == 1) {
            return new ProtocolTreeNode(tag, tmpAttributes);
        }
        int token3 = this.readInt8();
        if (this.isListTag(token3)) {
            return new ProtocolTreeNode(tag, tmpAttributes, this.readList(token3));
        }
        return new ProtocolTreeNode(tag, tmpAttributes, null, this.readBytes(token3));
    }

    protected boolean isListTag(int token) {
        return ((token == 248) || (token == 0) || (token == 249));
    }

    protected ProtocolTreeNode[] readList(int token) {
        ProtocolTreeNode[] ret = null;
        int size = 0;
        try {
            size = this.readListSize(token);
            ret = new ProtocolTreeNode[size];
            for (int i = 0; i < size; i++) {
                ret[i] = (this.nextTreeInternal());
            }
        } catch (Exception e) {
        }
        return ret;
    }

    protected int readListSize(int token) {
        int size = 0;
        if (token == 0) {
            size = 0;
        } else if (token == 0xf8) {
            size = this.readInt8();
        } else if (token == 0xf9) {
            size = this.readInt16();
        } else {
            throw new UnsupportedOperationException("BinTreeNodeReader->readListSize: Invalid token " + token);
        }
        return size;
    }

    protected int peekInt8(int offset) {
        int ret = 0;
        if (offset < 0) {
            offset = 0;
        }
        if (this.buffer.size() >= offset + 1) {
            ret = this.buffer.get(offset) & 255;
        }

        return ret;
    }

    protected int peekInt24(int offset) {
        int ret = 0;
        if (offset < 0) {
            offset = 0;
        }
        if (this.buffer.size() >= 3 + offset) {
            ret = ((this.buffer.get(0 + offset) & 255) << 16) + ((this.buffer.get(1 + offset) & 255) << 8) + (this.buffer.get(2 + offset) & 255);
        }
        return ret;
    }

    protected int readInt24() {
        int ret = 0;
        if (this.buffer.size() >= 3) {
            ret = (this.buffer.get(0) & 255) << 16;
            ret |= (this.buffer.get(1) & 255) << 8;
            ret |= (this.buffer.get(2) & 255);
            removeElements(0, 3);
        }
        return ret;
    }

    /**
     * remove elements from list
     *
     * @param offset
     * @return
     */
    private void removeElements(int startIndex, int endIndex) {
        for (int i = startIndex; i < endIndex; i++) {
            this.buffer.remove(startIndex);
        }
    }

    protected int peekInt16(int offset) {
        int ret = 0;
        if (offset < 0) {
            offset = 0;
        }
        if (this.buffer.size() >= offset + 2) {
            ret = (this.buffer.get(0 + offset) & 255) << 8;
            ret |= (this.buffer.get(1 + offset) & 255) << 0;
        }
        return ret;
    }

    protected int readInt16() {
        int ret = 0;

        if (this.buffer.size() >= 2) {
            ret = (this.buffer.get(0) & 255) << 8;
            ret |= (this.buffer.get(1) & 255);
            removeElements(0, 2);
        }
        return ret;
    }

    protected int readInt8() {
        int ret = 0;
        if (this.buffer.size() >= 1) {
            ret = (this.buffer.get(0) & 255);
            this.buffer.remove(0);
        }
        return ret;
    }

    protected byte[] fillArray(int len) {
      byte[] ret = new byte[len];
        if (this.buffer.size() >= len) {
            ret = copyOfRange(toArray(this.buffer), 0, len);
            removeElements(0, len);
        } else {
            throw new UnsupportedOperationException();
        }
        return ret;
    }

    protected void debugPrint(String debugMsg) {

        System.out.println(debugMsg);

    }

    private byte[] copyOfRange(byte[] data, int offset, int length) {
        byte[] da = new byte[length - offset];
        int j = 0;
        for (int i = offset; i < length; i++) {
            da[j++] = data[i];
        }
        return da;
    }
}
