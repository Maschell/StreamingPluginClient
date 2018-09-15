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

package de.mas.wiiu.streaming;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.zip.CRC32;

import javax.swing.JOptionPane;

import de.mas.wiiu.streaming.gui.IImageProvider;
import de.mas.wiiu.streaming.gui.ImageProvider;
import de.mas.wiiu.streaming.network.TCPClient;
import de.mas.wiiu.streaming.network.UDPClient;
import de.mas.wiiu.streaming.utilities.Utilities;
import lombok.Synchronized;
import lombok.extern.java.Log;

@Log
public class ImageStreamer {

    private final ImageProvider imageProvider = new ImageProvider();
    private final TCPClient tcpClient;
    private final UDPClient udpClient;

    public ImageStreamer(String ip) throws SocketException {
        tcpClient = new TCPClient(ip, 8092, 200);
        udpClient = new UDPClient(9445);
        new Thread(udpClient, "UDPClient").start();
        udpClient.setOnDataCallback(this::udpDataHandler);

        new Thread(() -> {
            while (true) {
                if (!tcpClient.isConnected()) {
                    System.out.print("Connecting..");
                    try {
                        tcpClient.connect();
                        System.out.println("success!");
                    } catch (IllegalArgumentException | UnknownHostException e1) {
                        JOptionPane.showMessageDialog(null, "Make sure to enter a valid ip address.", e1.getClass().getName(), JOptionPane.WARNING_MESSAGE);
                        System.exit(-1);
                    } catch (SocketTimeoutException e) {
                        System.out.println("time out...");
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    sendPing();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            while (true) {
                if (tcpClient.isConnected()) {
                    System.out.println("FPS:" + framesThisSecond);
                    framesThisSecond = 0;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private boolean sendTCP(byte[] rawCommand) {
        boolean result = false;
        try {
            tcpClient.send(rawCommand);
            result = true;
        } catch (Exception e) {
            result = false;
        }

        return result;
    }

    void sendPing() {
        if (sendTCP(new byte[] { 0x15 })) {
            byte pong;
            try {
                pong = tcpClient.recvByte();
                if (pong == 0x16) {
                    // log.info("Ping...Pong!");
                } else {
                    log.info("Got no valid response to a Ping. Disconnecting.");
                    tcpClient.abort();
                }
            } catch (IOException e) {
                log.info("Failed to get PONG. Disconnecting.");
                tcpClient.abort();
            }
        } else {
            log.info("Sending the PING failed");
        }
    }

    private final Object lock = new Object();

    private DataState state = DataState.UNKNOWN;
    private int curcrc32 = 0;
    private int curJPEGSize = 0;
    private byte[] jpegBuffer = {};
    private int curLenPos = 0;

    private int framesThisSecond = 0;

    @Synchronized("lock")
    private void udpDataHandler(byte[] data) {
        if (state == DataState.UNKNOWN) {
            // System.out.println("GET CRC");
            if (data.length == 4) {
                ByteBuffer wrapped = ByteBuffer.wrap(data); // big-endian by default
                curcrc32 = wrapped.getInt(); // 1
                state = DataState.CRC32_RECEIVED;
            } else {

                state = DataState.UNKNOWN;
                return;
            }
        } else if (state == DataState.CRC32_RECEIVED) {
            // System.out.println("GET Size");
            if (data.length == 8) {
                ByteBuffer wrapped = ByteBuffer.wrap(data); // big-endian by default
                curJPEGSize = (int) wrapped.getLong();
                jpegBuffer = new byte[curJPEGSize];
                state = DataState.RECEIVING_IMAGE;
                curLenPos = 0;
            } else {
                // System.out.println("...");
                state = DataState.UNKNOWN;
                return;
            }
        } else if (state == DataState.RECEIVING_IMAGE) {
            // System.out.println("GET IMAGE");
            System.arraycopy(data, 0, jpegBuffer, curLenPos, data.length > curJPEGSize ? curJPEGSize : data.length);

            curJPEGSize -= data.length;
            curLenPos += data.length;
            if (curJPEGSize <= 0) {
                CRC32 crc = new CRC32();
                crc.update(jpegBuffer);
                if ((int) crc.getValue() == curcrc32) {
                    imageProvider.updateImage(Utilities.byteArrayToImage(jpegBuffer));
                    framesThisSecond++;
                } else {
                    System.out.println("Hash mismatch, dropping frame.");
                }
                state = DataState.UNKNOWN;
            }
        }

    }

    public IImageProvider getImageProvider() {
        return imageProvider;
    }

    public enum DataState {
        UNKNOWN, CRC32_RECEIVED, RECEIVING_IMAGE, IMAGE_RECEIVED
    }

}
