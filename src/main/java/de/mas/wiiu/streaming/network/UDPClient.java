/*******************************************************************************
 * Copyright (c) 2018 Maschell
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
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.function.Consumer;

import lombok.extern.java.Log;

@Log
public final class UDPClient implements Runnable {
    private final DatagramSocket sock;

    public UDPClient(int port) throws SocketException {
        sock = new DatagramSocket(port);
    }

    private Consumer<byte[]> onDataCallback = null;

    public void setOnDataCallback(Consumer<byte[]> function) {
        onDataCallback = function;
    }

    @Override
    public void run() {
        log.info("UDPClient running.");
        byte[] receiveData = new byte[1400];
        while (true) {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            try {
                sock.receive(receivePacket);
            } catch (IOException e) {
                continue;
            }
            byte[] data = Arrays.copyOf(receivePacket.getData(), receivePacket.getLength());
            if (onDataCallback != null) {
                onDataCallback.accept(data);
            }
        }

    }
}
