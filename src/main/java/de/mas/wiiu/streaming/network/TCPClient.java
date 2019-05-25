/*******************************************************************************
 * Copyright (c) 2017,2018 Ash (QuarkTheAwesome) & Maschell
 * Taken from the HID to VPAD Networkclient. Modified for the  StreamingPluginClient.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *******************************************************************************/
package de.mas.wiiu.streaming.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

import lombok.Synchronized;
import lombok.extern.java.Log;

@Log
final public class TCPClient {
    private final Object lock = new Object();

    private Socket sock;
    private DataInputStream in;
    private DataOutputStream out;

    private final String ip;
    private final int port;
    private final int timeout;

    public TCPClient(String ip, int port, int timeout) {
        this.ip = ip;
        this.port = port;
        this.timeout = timeout;
    }

    @Synchronized("lock")
    public void connect() throws IOException {
        sock = new Socket();
        sock.connect(new InetSocketAddress(ip, port), timeout);
        in = new DataInputStream(sock.getInputStream());
        out = new DataOutputStream(sock.getOutputStream());
    }

    @Synchronized("lock")
    public boolean abort() {
        try {
            sock.close();
            log.info("TCP client closed");
        } catch (IOException e) {
            log.info(e.getMessage()); // TODO: handle
            return false;
        }
        return true;
    }

    @Synchronized("lock")
    public void send(byte[] rawCommand) throws IOException {
        try {
            out.write(rawCommand);
            out.flush();
        } catch (IOException e) {
            throw e;
        }
    }

    void send(int value) throws IOException {
        send(ByteBuffer.allocate(4).putInt(value).array());
    }

    public void send(byte _byte) throws IOException {
        send(ByteBuffer.allocate(1).put(_byte).array());
    }

    @Synchronized("lock")
    public byte recvByte() throws IOException {
        return in.readByte();
    }

    @Synchronized("lock")
    short recvShort() throws IOException {
        try {
            return in.readShort();
        } catch (IOException e) {
            log.info(e.getMessage());
            throw e;
        }
    }

    @Synchronized("lock")
    int recvInt() throws IOException {
        try {
            return in.readInt();
        } catch (IOException e) {
            log.info(e.getMessage());
            throw e;
        }
    }

    @Synchronized("lock")
    public boolean isConnected() {
        return (sock != null && sock.isConnected() && !sock.isClosed());
    }
}
