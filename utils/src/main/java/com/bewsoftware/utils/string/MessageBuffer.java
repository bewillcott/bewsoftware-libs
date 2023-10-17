/*
 *  File Name:    MessageBuffer.java
 *  Project Name: bewsoftware-utils
 *
 *  Copyright (c) 2023 Bradley Willcott
 *
 *  bewsoftware-utils is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  bewsoftware-utils is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.bewsoftware.utils.string;

import com.bewsoftware.annotations.jcip.ThreadSafe;
import java.util.Formatter;

import static java.lang.String.format;

/**
 * The MessageBuffer class is a simplified version of the {@link StringBuffer}
 * class.
 * <p>
 * I wrote this class because I prefer to use a method like {@link #appendln()}
 * in my client code, rather than adding a "\n" to text strings that don't come
 * with it. It is especially appropriate when dealing with objects.
 * <p>
 * Also I like to have the {@link #append(String, Object...)} method for using a
 * format string.
 *
 * @implNote
 * This implementation uses a StringBuffer internally to store the messages.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 3.0.0
 * @version 3.0.0
 */
@ThreadSafe
public class MessageBuffer
{
    /**
     * Internal storage of strings.
     */
    private final StringBuffer sb;

    /**
     * Instantiate default MessageBuilder object.
     */
    public MessageBuffer()
    {
        this.sb = new StringBuffer();
    }

    /**
     * Append the string representation of {@code obj}.
     *
     * @param obj to append
     *
     * @return a reference to this object
     */
    public synchronized MessageBuffer append(final Object obj)
    {
        append(obj.toString());
        return this;
    }

    /**
     * Append the text message.
     *
     * @param message to append
     *
     * @return a reference to this object
     */
    public synchronized MessageBuffer append(final String message)
    {
        sb.append(message);
        return this;
    }

    /**
     * Append this formatted text.
     *
     * @implNote
     * This method is equivalent to appending the result of
     * {@link  String#format(String, Object...) String.format(format, args)}.
     *
     * @param format {@link  Formatter "Format String Syntax"}
     * @param args   Arguments referenced by the format specifiers in the format
     *               string.
     *
     * @return a reference to this object
     */
    public synchronized MessageBuffer append(final String format, final Object... args)
    {
        return append(format(format, args));
    }

    /**
     * Append this formatted text and a line-terminator.
     *
     * @implNote
     * This method is equivalent to appending the result of
     * {@link  String#format(String, Object...) String.format(format, args)}.
     *
     * @param format {@link  Formatter "Format String Syntax"}
     * @param args   Arguments referenced by the format specifiers in the format
     *               string.
     *
     * @return a reference to this object
     */
    public synchronized MessageBuffer appendln(final String format, final Object... args)
    {
        return append(format(format, args)).appendln();
    }

    /**
     * Append a line-terminator.
     *
     * @return a reference to this object
     */
    public synchronized MessageBuffer appendln()
    {
        return append(System.lineSeparator());
    }

    /**
     * Append the text message and a line-terminator.
     *
     * @param message to append
     *
     * @return a reference to this object
     */
    public synchronized MessageBuffer appendln(final String message)
    {
        return append(message).appendln();
    }

    /**
     * Append the string representation of {@code obj} and a line-terminator.
     *
     * @param obj to append
     *
     * @return a reference to this object
     */
    public synchronized MessageBuffer appendln(final Object obj)
    {
        return append(obj).appendln();
    }

    /**
     * Clear out the contents of this MessageBuilder.
     *
     * @return a reference to this object
     */
    public synchronized MessageBuffer clear()
    {
        sb.setLength(0);
        return this;
    }

    /**
     * Returns the length (character count).
     *
     * @implNote
     * This value is supplied directly from the internal StringBuffer object.
     *
     * @return the length of the sequence of characters currently represented by
     *         this object
     */
    public synchronized int length()
    {
        return sb.length();
    }

    /**
     * Returns a string representing the data in this sequence. A new String
     * object is allocated and initialized to contain the character sequence
     * currently represented by this object. This String is then returned.
     * Subsequent changes to this sequence do not affect the contents of the
     * String.
     *
     * @implNote
     * This String is supplied directly from the internal StringBuffer object.
     *
     * @return a string representation of this sequence of characters.
     */
    @Override
    public synchronized String toString()
    {
        return sb.toString();
    }
}
