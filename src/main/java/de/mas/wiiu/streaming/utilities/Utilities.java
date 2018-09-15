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

package de.mas.wiiu.streaming.utilities;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class Utilities {
    private Utilities() {
        //
    }

    public static String ByteArrayToString(byte[] ba) {
        if (ba == null)
            return null;
        StringBuilder hex = new StringBuilder(ba.length * 2);
        for (byte b : ba) {
            hex.append(String.format("%02X", b));
        }
        return hex.toString();
    }

    public static BufferedImage byteArrayToImage(byte[] bytes) {
        BufferedImage bufferedImage = null;
        try {
            InputStream inputStream = new ByteArrayInputStream(bytes);
            bufferedImage = ImageIO.read(inputStream);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return bufferedImage;
    }
}
