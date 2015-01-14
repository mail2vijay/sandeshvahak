/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sandeshvahak.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Vijay
 */
public class ProtocolTreeNode {

    public String tag;
    public KeyValue[] attributeHash;
    public ProtocolTreeNode[] children;
    public byte[] data;

    public ProtocolTreeNode(String tag, KeyValue[] attributeHash, ProtocolTreeNode[] children, byte[] data) {
        this.tag = tag == null ? "" : tag;
        this.attributeHash = (attributeHash == null ? new KeyValue[0] : attributeHash);
        this.children = children == null ? new ProtocolTreeNode[0]:children;
        this.data = new byte[0];
        if (data != null) {
            this.data = data;
        }
    }

    public ProtocolTreeNode(String tag, KeyValue[] attributeHash, ProtocolTreeNode[] children) {
        this.tag = tag == null ? "" : tag;
        this.attributeHash = (attributeHash == null ? new KeyValue[0] : attributeHash);
        this.children = children == null ? new ProtocolTreeNode[0] : children;
        this.data = new byte[0];
    }

    public ProtocolTreeNode(String tag, KeyValue[] attributeHash, byte[] data) {
        this(tag, attributeHash, new ProtocolTreeNode[0], data);
    }

    public ProtocolTreeNode(String tag, KeyValue[] attributeHash) {
        this(tag, attributeHash, new ProtocolTreeNode[0], null);
    }

    public String nodeString(String indent) {
        String ret = "\n" + indent + "<" + this.tag;
        if (this.attributeHash != null) {
            for (int i = 0; i < this.attributeHash.length; i++) {
                ret += ((KeyValue) attributeHash[i]).getKey() + "=\"" + ((KeyValue) attributeHash[i]).getValue() + "\"";
            }
        }
        ret += ">";
        if (this.data.length > 0) {
            if (this.data.length <= 1024) {
                ret += new String(data);
            } else {
                ret += "--" + this.data.length + " byte--";
            }
        }

        if (this.children != null) {
            for (int i = 0; i < this.children.length; i++) {
                ret += this.children[i].nodeString(indent + "  ");
            }
            ret += "\n" + indent;
        }
        ret += "</" + this.tag + ">";
        return ret;
    }

    public String getAttribute(String attribute) {
        KeyValue ret = null;
        for (int i = 0; i < this.attributeHash.length; i++) {
            if (this.attributeHash[i].getKey().equalsIgnoreCase(attribute)) {
                ret = this.attributeHash[i];
                break;
            }
        }
        return (ret == null) ? null : ret.getValue();
    }

    public ProtocolTreeNode getChild(String tag) {
        if (this.children != null) {
            for (int i = 0; i < this.children.length; i++) {
                if (ProtocolTreeNode.tagEquals(this.children[i], tag)) {
                    return this.children[i];
                }
                ProtocolTreeNode ret = this.children[i].getChild(tag);
                if (ret != null) {
                    return ret;
                }
            }
        }
        return null;
    }

    public ProtocolTreeNode[] getAllChildren(String tag) {
        List<ProtocolTreeNode> tmpReturn = new ArrayList<>();
        if (this.children != null) {
            for (int i = 0; i < this.children.length; i++) {
                if (tag.equalsIgnoreCase(this.children[i].tag)) {
                    tmpReturn.add(this.children[i]);
                }
                addRange(this.children[i].getAllChildren(tag), tmpReturn);
            }
        }
        return (ProtocolTreeNode[]) tmpReturn.toArray();
    }

    public void addRange(ProtocolTreeNode[] tree, List<ProtocolTreeNode> listReference) {
        if (tree != null) {
            listReference.addAll(Arrays.asList(tree));
        }

    }

    public ProtocolTreeNode[] getAllChildren() {

        return this.children;
    }

    public byte[] getData() {
        return this.data;
    }

    public static boolean tagEquals(ProtocolTreeNode node, String _string) {
        return (((node != null) && (node.tag != null)) && node.tag.equalsIgnoreCase(_string));
    }
}
