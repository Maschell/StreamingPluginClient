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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public final class ImagePanel extends JPanel {
    /**
     * 
     */
    private static final long serialVersionUID = -127096088663141229L;
    private Image image;
    private final int preferedWidth;
    private final int preferedHeight;

    public ImagePanel(int width, int height) {
        super(true);
        preferedWidth = width;
        preferedHeight = height;
    }

    public void setImage(Image image) {
        this.image = image;
        repaint();
    }

    public Dimension getPreferredSize() {
        return new Dimension(preferedWidth, preferedHeight);
    }

    public void paint(Graphics g) {
        super.paint(g);
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), 0, 0, image.getWidth(this), image.getHeight(this), this);
        }
    }
}