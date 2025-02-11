/*
 *  File Name:    MessageBuilderProperty.java
 *  Project Name: bewsoftware-jfx
 *
 *  Copyright (c) 2023 Bradley Willcott
 *
 *  bewsoftware-jfx is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  bewsoftware-jfx is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.bewsoftware.jfx.beans.property;

import com.bewsoftware.annotations.jcip.GuardedBy;
import com.bewsoftware.annotations.jcip.ThreadSafe;
import com.bewsoftware.utils.string.MessageBuilder;
import java.util.Formatter;
import javafx.beans.property.SimpleStringProperty;

import static java.lang.String.format;

/**
 * The MessageBuilderProperty class is a simplified version of the
 * {@link StringBuilder} class with the added functionality of being a
 * {@link SimpleStringProperty} as well.
 * <p>
 * I wrote this class because I prefer to use a method like {@link #appendln()}
 * in my client code, rather than adding a "\n" to text strings that don't come
 * with it. It is especially appropriate when dealing with objects.
 * <p>
 * Also I like to have the {@link #append(String, Object...)} method for using a
 * format string.
 *
 * @implNote
 * This implementation uses a MessageBuilder internally to store the messages.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 3.0.0
 * @version 3.0.2
 */
@ThreadSafe
public final class MessageBuilderProperty extends SimpleStringProperty
{
    /**
     * Internal storage of strings.
     */
    @GuardedBy("mb")
    private final MessageBuilder mb;

    /**
     * Instantiate default MessageBuilder object.
     */
    public MessageBuilderProperty()
    {
        this.mb = new MessageBuilder();
    }

    public MessageBuilderProperty(final String initialValue)
    {
        super(initialValue);
        this.mb = new MessageBuilder().append(initialValue);
    }

    public MessageBuilderProperty(final Object bean,final String name)
    {
        super(bean, name);
        this.mb = new MessageBuilder();
    }

    public MessageBuilderProperty(final Object bean, final String name,final String initialValue)
    {
        super(bean, name, initialValue);
        this.mb = new MessageBuilder().append(initialValue);
    }

    /**
     * Append the string representation of {@code obj}.
     *
     * @param obj to append.
     *
     * @return a reference to this object.
     */
    public MessageBuilderProperty append(final Object obj)
    {
        append(obj.toString());
        _update();

        return this;
    }

    /**
     * Append the text message.
     *
     * @param message to append.
     *
     * @return a reference to this object.
     */
    @GuardedBy("mb")
    public MessageBuilderProperty append(final String message)
    {
        synchronized (mb)
        {
            mb.append(message);
        }

        _update();

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
     * @return a reference to this object.
     */
    public MessageBuilderProperty append(final String format, final Object... args)
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
    public MessageBuilderProperty appendln(final String format, final Object... args)
    {
        return append(format(format, args)).appendln();
    }

    /**
     * Append a line-terminator.
     *
     * @return a reference to this object.
     */
    public MessageBuilderProperty appendln()
    {
        return append(System.lineSeparator());
    }

    /**
     * Append the text message and a line-terminator.
     *
     * @param message to append.
     *
     * @return a reference to this object.
     */
    public MessageBuilderProperty appendln(final String message)
    {
        return append(message).appendln();
    }

    /**
     * Append the string representation of {@code obj} and a line-terminator.
     *
     * @param obj to append.
     *
     * @return a reference to this object.
     */
    public MessageBuilderProperty appendln(final Object obj)
    {
        return append(obj).appendln();
    }

    /**
     * Clear out all existing text from this property.
     *
     * @return a reference to this object.
     */
    @GuardedBy("mb")
    public MessageBuilderProperty clear()
    {
        synchronized (mb)
        {
            mb.clear();
        }

        _update();

        return this;
    }

    /**
     * Returns a string representing the data in this sequence. A new String
     * object is allocated and initialized to contain the character sequence
     * currently represented by this object. This String is then returned.
     * Subsequent changes to this sequence do not affect the contents of the
     * String.
     *
     * @implNote
     * This String is supplied directly from the internal StringBuilder object.
     *
     * @return a string representation of this sequence of characters.
     */
    @Override
    @GuardedBy("mb")
    public String toString()
    {
        String rtn = "";

        synchronized (mb)
        {
            rtn = mb.toString();
        }

        return rtn;
    }

    /**
     * Update the underlying {@link SimpleStringProperty#value}
     */
    @GuardedBy("mb")
    private void _update()
    {
        synchronized (mb)
        {
            set(mb.toString());
        }
    }
}
