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

import java.awt.GraphicsEnvironment;
import java.net.BindException;
import java.net.SocketException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import de.mas.wiiu.streaming.gui.StreamWindow;

public class Main {

    public static void main(String[] args) throws Exception {
        if(GraphicsEnvironment.isHeadless()) {
            System.out.println("This program does not support running in a headless environment!");
            System.exit(2);
        }else {
            String ip = null;
            if(args.length != 0) {
                CommandLineParser parser = new DefaultParser();

                Options options = new Options();
                options.addOption("ip", "ip", true, "IP address of your Wii U Console.");

                CommandLine line = parser.parse(options, args, true);


                if (line.hasOption("ip")) {
                    ip = line.getOptionValue("ip");
                }else {
                    HelpFormatter formatter = new HelpFormatter();
                    formatter.printHelp("streamingTool", options);
                    System.exit(2);
                }
            } else{




                //Create a JFrame to show the icon in the taskbar
                final JFrame frame = new JFrame("Wii U Streaming Client - Enter IP...");
                frame.setUndecorated( true );
                frame.setVisible( true );
                frame.setLocationRelativeTo( null );

                //Display the IP Dialog
                ip = JOptionPane.showInputDialog(frame, "Please enter the local IP address of your Wii U", "Wii U streaming client", JOptionPane.PLAIN_MESSAGE);

                //Check if user clicked "Cancel"
                if(ip == null) {
                    System.out.println("Cancelled. Exiting program");
                    frame.dispose();
                    System.exit(0);
                }
                //Close the JFrame again
                frame.dispose();

            }

            try {
                new Main(ip);
            } catch (BindException e) {
                //Create a JFrame to show the icon in the taskbar
                final JFrame frame = new JFrame("Wii U Streaming Client - "+e.getClass().getName());
                frame.setUndecorated( true );
                frame.setVisible( true );
                frame.setLocationRelativeTo( null );

                //Display the Message
                JOptionPane.showMessageDialog(frame, "Can't bind socket. The client is probably already running.", e.getClass().getName(),
                        JOptionPane.WARNING_MESSAGE);
                //Close the JFrame again
                frame.dispose();

                System.exit(-1);
            }
        }
    }

    public Main(String ip) throws SocketException {
        ImageStreamer imageStreamer = new ImageStreamer(ip);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new StreamWindow(imageStreamer.getImageProvider());
            }
        });
    }
}
