/*
 * This file is part of the BEWSoftware Utils Library.
 *
 * Copyright (C) 2020, 2021 Bradley Willcott
 *
 * BEWSoftware Utils is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BEWSoftware Utils is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.bewsoftware.utils.graphics;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Objects;
import javax.swing.ImageIcon;

/**
 * This class contains various static helper methods related to the
 * manipulation of graphic images.
 *
 * @Note
 * The code or ideas for these methods came mostly from the effort/work
 * of others on the Internet. Recognition of their individual effort is
 * primarily given in the JavaDoc comments attached to each method.
 * This is usually in the form a link to the website page that was the
 * source of the code/information.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0.0
 * @version 1.0.0
 */
public final class ImageTools
{

    /**
     * This class in not meant to be instantiated.
     */
    private ImageTools()
    {
    }

    /**
     * Returns an ImageIcon, or {@code null} if the path was invalid.
     * <p>
     * Example:<br>
     * <pre><code>
     *     ImageIcon icon = createImageIcon(MyClass.class, "images/MyIcon.png", "My favourite picture");
     * </code></pre>
     * Method code sourced from :
     * <a href="https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/components/IconDemoProject/src/components/IconDemoApp.java"
     * target="_blank">Java Tutorials Code Sample – IconDemoApp.java</a>
     * <p>
     * Modified the code to add the new first parameter: {@code t}. This allows
     * the code to be
     * further modified, making it into a {@code static} method of an external
     * library class.
     *
     * @param <T>         class type of the calling instance.
     * @param t           Usually should be the calling class, such as:
     *                    {@code MyClass.class}.
     * @param path        Image file path.
     * @param description of icon.
     *
     * @return new icon, or {@code null} if not found.
     */
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public static <T> ImageIcon createImageIcon(Class<T> t, String path, String description)
    {
        URL imgURL = t.getResource(path);

        if (imgURL != null)
        {
            return new ImageIcon(imgURL, description);
        } else
        {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    /**
     * The fastest way to scale an image in java without losing image quality is
     * to use Bilinear scaling. Bilinear is only good if you scale the image by
     * 50% at a time because of the way it works. The following code is from
     * "Filthy rich clients" by Chet Haase. He explains multiple techniques in
     * the book, but this one has the highest performance to quality trade-off.
     * <p>
     * It supports all types of BufferedImages so don't worry about
     * compatibility.
     * It also lets java2D hardware accelerate your image because the
     * calculations
     * are done by Java2D. Don't worry if you don't understand that last part.
     * The most important thing is that this is the fastest way to do it.
     * <p>
     * Method code and the above comments were sourced from:
     * <a href="https://stackoverflow.com/questions/3967731/how-to-improve-the-performance-of-g-drawimage-method-for-resizing-images/11371387#11371387"
     * target="_blank">
     * Stack Overflow - How to improve the performance of g.drawImage() method
     * for
     * resizing images</a>
     *
     * @param img                 Source image.
     * @param targetWidth         Target width.
     * @param targetHeight        Target height.
     * @param progressiveBilinear Use Progressive Bilinear?
     *
     * @return the scaled image.
     */
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public static BufferedImage getFasterScaledInstance(BufferedImage img, int targetWidth, int targetHeight, boolean progressiveBilinear)
    {
        int type = (img.getTransparency() == Transparency.OPAQUE)
                ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage ret = img;
        BufferedImage scratchImage = null;
        Graphics2D g2 = null;
        int w;
        int h;
        int prevW = ret.getWidth();
        int prevH = ret.getHeight();

        if (progressiveBilinear)
        {
            w = img.getWidth();
            h = img.getHeight();
        } else
        {
            w = targetWidth;
            h = targetHeight;
        }

        do
        {
            if (progressiveBilinear && w > targetWidth)
            {
                w /= 2;
                if (w < targetWidth)
                {
                    w = targetWidth;
                }
            }

            if (progressiveBilinear && h > targetHeight)
            {
                h /= 2;
                if (h < targetHeight)
                {
                    h = targetHeight;
                }
            }

            if (scratchImage == null)
            {
                scratchImage = new BufferedImage(w, h, type);
                g2 = scratchImage.createGraphics();
            }

            Objects.requireNonNull(g2).
                    setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(ret, 0, 0, w, h, 0, 0, prevW, prevH, null);
            prevW = w;
            prevH = h;
            ret = scratchImage;
        } while (w != targetWidth || h != targetHeight);

        g2.dispose();

        if (targetWidth != ret.getWidth() || targetHeight != ret.getHeight())
        {
            scratchImage = new BufferedImage(targetWidth, targetHeight, type);
            g2 = scratchImage.createGraphics();
            g2.drawImage(ret, 0, 0, null);
            g2.dispose();
            ret = scratchImage;
        }

        System.out.println("ret is " + ret);
        return ret;
    }

    /**
     * Resizes an image using a Graphics2D object backed by a BufferedImage.
     * <p>
     * Method code sourced from:
     * <a href="https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/components/IconDemoProject/src/components/IconDemoApp.java"
     * target="_blank">Java Tutorials Code Sample – IconDemoApp.java</a>
     * <p>
     * This code has been modified to incorporate code from:
     * <a href="https://exceptionshub.com/java-image-resize-maintain-aspect-ratio.html"
     * target="_blank">ExceptionsHub.com - Java image resize, maintain aspect
     * ratio</a><br>
     * Added preservation of aspect ratio, and changed the parameter names for
     * {@code w}
     * and {@code h}.
     * <p>
     * Further modification ideas came from:
     * <a href="https://stackoverflow.com/questions/244164/how-can-i-resize-an-image-using-java"
     * target="_blank">How can I resize an image using Java?</a> and
     * <a href="https://mkyong.com/java/how-to-resize-an-image-in-java/" target="_blank">
     * How to resize an image in Java ?</a><br>
     * Added Alpha preservation and additional {@code RenderingHints}.
     *
     * @param srcImg        source image to scale
     * @param scaledWidth   desired width
     * @param scaledHeight  desired height
     * @param preserveAlpha Preserve Alpha - image with 8-bit RGBA color
     *                      components
     * @param preserveRatio Preserve aspect ratio?
     *
     * @return - the new resized image
     */
    @SuppressWarnings("AssignmentToMethodParameter")
    public static Image getScaledImage(Image srcImg, int scaledWidth, int scaledHeight, boolean preserveAlpha, boolean preserveRatio)
    {
        if (preserveRatio)
        {
            double imageHeight = srcImg.getHeight(null);
            double imageWidth = srcImg.getWidth(null);

            if (imageHeight / scaledHeight > imageWidth / scaledWidth)
            {
                scaledWidth = (int) (scaledHeight * imageWidth / imageHeight);
            } else
            {
                scaledHeight = (int) (scaledWidth * imageHeight / imageWidth);
            }
        }

        BufferedImage resizedImg = new BufferedImage(scaledWidth, scaledHeight,
                preserveAlpha ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);

        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawImage(srcImg, 0, 0, scaledWidth, scaledHeight, null);
        g2.dispose();

        return resizedImg;
    }
}
