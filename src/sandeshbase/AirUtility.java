/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sandeshbase;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Low level Network handler
 *
 * @author Vijay
 *
 */
public class AirUtility {

    private final int receiveTimeOut = 200;
    private final String whatsHost = "c3.whatsapp.net";
    private final int whatsPort = 443;
    private Socket socket;
    private InputStream reader;
    private OutputStream writer;
    private static AirUtility airUtility;

    public static AirUtility getInstance() {
        if (airUtility == null) {
            airUtility = new AirUtility();
        }
        return airUtility;

    }

    private AirUtility() {
    }

    public void connectToServer() {
        try {
            SocketAddress endPoint = new InetSocketAddress(whatsHost, whatsPort);
            this.socket = new Socket();
            this.socket.setSoTimeout(receiveTimeOut);
            this.socket.setKeepAlive(false);
            this.socket.connect(endPoint);
            this.reader = this.socket.getInputStream();
            this.writer = this.socket.getOutputStream();

        } catch (IOException e) {
        }
    }

    public void disconnectFromServer() {
        if (this.socket.isConnected()) {
            try {
                this.socket.close();
            } catch (IOException e) {
            }
        }
    }

    public void sendData(byte[] data) {
        try {
            if (this.socket.isConnected() && !this.socket.isOutputShutdown()) {
                writer.write(data);
            }
        } catch (IOException e) {
        }
    }

    public void sendData(String stringData) {

        try {
            sendData(stringData.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(AirUtility.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public byte[] readData(int length) {
        return socketRead(length);
    }

    private byte[] socketRead(int length) {
        int receiveLength = 0;
        byte[] buff = new byte[length];
        do {
            try {
                receiveLength = reader.read(buff, 0, length);
            } catch (IOException e) {
            }
            if (receiveLength <= 0) {
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                }
            }
        } while (receiveLength <= 0);
        byte[] tmpArray = new byte[receiveLength];
        if (receiveLength > 0) {
            System.arraycopy(buff, 0, tmpArray, 0, receiveLength);
        }
        return tmpArray;
    }

    public byte[] readNextNode() throws Exception {
        /*
        byte[] nodeHeader = this.ReadData(3);

            if (nodeHeader == null || nodeHeader.Length == 0)
            {
                //empty response
                return null;
            }

            if (nodeHeader.Length != 3)
            {
                throw new Exception("Failed to read node header");
            }
            int nodeLength = 0;
            nodeLength = (int)nodeHeader[1] << 8;
            nodeLength |= (int)nodeHeader[2] << 0;

            //buffered read
            int toRead = nodeLength;
            List<byte> nodeData = new List<byte>();
            do
            {
                byte[] nodeBuff = this.ReadData(toRead);
                nodeData.AddRange(nodeBuff);
                toRead -= nodeBuff.Length;
            } while (toRead > 0);

            if (nodeData.Count != nodeLength)
            {
                throw new Exception("Read Next Tree error");
            }

            List<byte> buff = new List<byte>();
            buff.AddRange(nodeHeader);
            buff.AddRange(nodeData.ToArray());
            return buff.ToArray();
        */
        
        
        byte[] nodeHeader = this.readData(3);
        if (nodeHeader == null || nodeHeader.length == 0) {
            return null;
        }
        if (nodeHeader.length != 3) {
            throw new Exception("Failed to read node header");
        }
        int nodeLength = 0;
        nodeLength = nodeHeader[1]<<8;
        nodeLength |= nodeHeader[2]<<0;
        //buffered read
        int toRead = nodeLength;
        List<Byte> nodeData = new ArrayList<>();
        do {
            byte[] nodeBuff = this.readData(toRead);
            for (int i = 0; i < nodeBuff.length; i++) {
                nodeData.add(i, nodeBuff[i]);
            }

            toRead -= nodeBuff.length;
        } while (toRead > 0);

        if (nodeData.size() != nodeLength) {
            throw new Exception("Read Next Tree error");
        }

        List<Byte> buff = new ArrayList<>();
        for (int i = 0; i < nodeHeader.length; i++) {
            buff.add(nodeHeader[i]);
        }
        int size = nodeData.size();
        for (int i = 0; i < size; i++) {
            buff.add(nodeData.get(i));
        }

        return toArray(buff);
    }

    private byte[] toArray(List<Byte> data) {
        int length = data.size();
        byte array[] = new byte[length];
        for (int i = 0; i < length; i++) {
            array[i] = data.get(i);
        }
        return array;
    }

    public boolean socketStatus() {
        return this.socket.isConnected();
    }

}
