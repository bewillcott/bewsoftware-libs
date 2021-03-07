/*
 *  File Name:    Strings.java
 *  Project Name: bewsoftware-utils
 *
 *  Copyright (c) 2021 Bradley Willcott
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.bewsoftware.utils.string;

import java.util.Objects;

/**
 * This class contains some helper methods.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0.6
 * @version 1.0.7
 */
public class Strings {

    /**
     * Centre fills the {@code number} within a text string of {@code width}
     * length.
     *
     * @param number to wrap
     * @param width  of required text
     *
     * @return the formatted text
     */
    public static String centreFill(final int number, final int width) {
        return centreFill(Integer.toString(number), width);
    }

    /**
     * Formats the {@code text} to be centred within a text string of {@code width}
     * length.
     *
     * @param text  to wrap
     * @param width of required text
     *
     * @return the formatted text
     */
    public static String centreFill(final String text, final int width) {
        String rtn = "";
        int length = text.length();

        // Process only if necessary
        if (length < width)
        {
            StringBuilder sb = new StringBuilder();
            int preLen = (width - text.length()) / 2;
            int postLen = width - text.length() - preLen;
            sb.append(" ".repeat(preLen)).append(text).append(" ".repeat(postLen));
            rtn = sb.toString();

        } else
        {
            rtn = text;
        }

        return rtn;
    }

    /**
     * Fills out a new String with the <i>text</i> repeated <i>count</i> times.
     *
     * @implNote
     * Uses {@code text.repeat(count)} to fill out String.
     *
     * @param text  to repeat
     * @param count number of repeats
     *
     * @return new String
     *
     * @see java.lang.String#repeat(int)
     */
    public static String fill(final String text, final int count) {
        return text.repeat(count);
    }

    /**
     * Formats the {@code number} to be left justified within a text string of
     * {@code width} length.
     *
     * @param number to wrap
     * @param width  of required text
     *
     * @return the formatted text
     */
    public static String leftFill(final int number, final int width) {
        return leftFill(Integer.toString(number), width);
    }

    /**
     * Formats the {@code text} to be left justified within a text string of
     * {@code width} length.
     *
     * @param text  to wrap
     * @param width of required text
     *
     * @return the formatted text
     */
    public static String leftFill(final String text, final int width) {
        String rtn = text;
        int length = text.length();

        if (length < width)
        {
            rtn = text + " ".repeat(width - length);
        }

        return rtn;
    }

    /**
     * Checks that the specified string isn't <i>blank</i>.
     * <dl>
     * <dt>blank:</dt>
     * <dd>The string is either <i>empty</i> or contains only white space code-points.</dd>
     * <dt>empty:</dt>
     * <dd>The string's {@code length} is {@code 0}.</dd>
     * </dl>
     *
     * @param str The string to check for blankness.
     *
     * @return {@code str} if not <i>blank</i>.
     *
     * @throws NullPointerException     if {@code str} is <i>null</i>.
     * @throws IllegalArgumentException if {@code str} is <i>blank</i>.
     *
     * @see #requireNonEmpty(java.lang.String)
     */
    public static String requireNonBlank(String str) {
        if (Objects.requireNonNull(str).isBlank())
        {
            throw new IllegalArgumentException("isBlank.");
        }

        return str;
    }

    /**
     * Checks that the specified string isn't <i>blank</i>.
     * <dl>
     * <dt>blank:</dt>
     * <dd>The string is either <i>empty</i> or contains only white space code-points.</dd>
     * <dt>empty:</dt>
     * <dd>The string's {@code length} is {@code 0}.</dd>
     * </dl>
     *
     * @param str     The string to check for blankness.
     * @param message Detail message to be used in the event that an exception is thrown.
     *
     * @return {@code str} if not <i>blank</i>.
     *
     * @throws NullPointerException     if {@code str} is <i>null</i>.
     * @throws IllegalArgumentException if {@code str} is <i>blank</i>.
     *
     * @see #requireNonEmpty(java.lang.String, java.lang.String)
     */
    public static String requireNonBlank(String str, String message) {
        if (Objects.requireNonNull(str, message).isBlank())
        {
            throw new IllegalArgumentException("isBlank: " + message);
        }

        return str;
    }

    /**
     * Checks that the specified string isn't <i>empty</i>.
     * <dl>
     * <dt>empty:</dt>
     * <dd>The string's {@code length} is {@code 0}.</dd>
     * </dl>
     *
     * @param str The string to check for emptiness.
     *
     * @return {@code str} if not <i>empty</i>.
     *
     * @throws NullPointerException     if {@code str} is <i>null</i>.
     * @throws IllegalArgumentException if {@code str} is <i>empty</i>.
     */
    public static String requireNonEmpty(String str) {
        if (Objects.requireNonNull(str).isEmpty())
        {
            throw new IllegalArgumentException("isEmpty.");
        }

        return str;
    }

    /**
     * Checks that the specified string isn't <i>empty</i>.
     * <dl>
     * <dt>empty:</dt>
     * <dd>The string's {@code length} is {@code 0}.</dd>
     * </dl>
     *
     * @param str     The string to check for emptiness.
     * @param message Detail message to be used in the event that an exception is thrown.
     *
     * @return {@code str} if not <i>empty</i>.
     *
     * @throws NullPointerException     if {@code str} is <i>null</i>.
     * @throws IllegalArgumentException if {@code str} is <i>empty</i>.
     */
    public static String requireNonEmpty(String str, String message) {
        if (Objects.requireNonNull(str, message).isEmpty())
        {
            throw new IllegalArgumentException("isEmpty: " + message);
        }

        return str;
    }

    /**
     * This class is not meant to be instantiated.
     */
    private Strings() {
    }

}
