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

package de.mas.wiiu.streaming.gui;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.WindowConstants;

public class StreamWindow {
	private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private final ImagePanel image = new ImagePanel(screenSize.width-15, screenSize.height-100);

    public StreamWindow(IImageProvider imageProvider) {
    	
        JFrame editorFrame = new JFrame("Stream");

        editorFrame.setMaximumSize(screenSize);
        editorFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        imageProvider.setOnImageChange((bi) -> image.setImage(bi));
        editorFrame.getContentPane().add(image);
        
        JMenuBar menuBar = new JMenuBar();
        editorFrame.getContentPane().add(menuBar, BorderLayout.NORTH);
        
        JMenu mnSettings = new JMenu("Settings");
        menuBar.add(mnSettings);
        
        
        JMenuItem mntmNewMenuItem = new JMenuItem("Config (Not implemented!)");
        mntmNewMenuItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		//TODO Add program config
        	}
        });
        JMenuItem mntmExit = new JMenuItem("Exit");
        mntmExit.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		System.exit(0);
        	}
        });
        
        mntmNewMenuItem.setEnabled(false);
        mnSettings.add(mntmNewMenuItem);
        mnSettings.add(mntmExit);

        editorFrame.pack();
        editorFrame.setLocationRelativeTo(null);
        editorFrame.setVisible(true);
    }

}
