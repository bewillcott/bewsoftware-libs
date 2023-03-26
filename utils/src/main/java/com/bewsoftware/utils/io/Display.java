/*
 *  File Name:    Display.java
 *  Project Name: bewsoftware-utils
 *
 *  Copyright (c) 2021-2023 Bradley Willcott
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
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.bewsoftware.utils.io;

import java.io.Closeable;

/**
 * This interface provides a means of displaying output in an abstract manner.
 * <p>
 * By using this interface, it is possible to have multiple implementations,
 * each of which provides a different type or level of output. For instance,
 * the {@link ConsoleIO} class provides console level output.
 * <p>
 * Also, by implementing most of the methods in the interface
 * (<i>default</i> and <i>static</i>), it will make it even simpler to develop
 * additional implementations.
 *
 * @implNote
 * The ideas for the various methods, and even some of the implementation code
 * for the <i>default</i> methods was derived from the
 * {@link java.io.PrintStream} and {@link java.lang.StringBuilder} classes from
 * JDK 15.
 * <p>
 * The reason for this, was to make it as close as possible to being a
 * minimalist drop-in replacement for {@link java.lang.System#out}. Further
 * more, by including methods from both classes, I believe it makes this class
 * even more useful.
 * <p>
 * In addition to the methods derived from the above mentioned classes, I have
 * included a few extra helper methods:
 * <ul>
 * <li>{@link #appendln(java.lang.String)}</li>
 * <li>{@link #appendln(int)}</li>
 * </ul>
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0.7
 * @version 3.0.0
 */
public interface Display extends Closeable, Exceptions
{
    /**
     * Display/Debug level.
     * <p>
     * Designates fine-grained informational events that
     * are most useful to debug an application.
     */
    public static int DEBUG = 2;

    /**
     * Display/Debug level.
     * <p>
     * Normal text display.
     */
    public static int DEFAULT = 0;

    /**
     * Display/Debug level.
     * <p>
     * Designates informational messages that highlight the
     * progress of the application at coarse-grained level.
     */
    public static int INFO = 1;

    /**
     * Display/Debug level.
     * <p>
     * Designates finer-grained informational events than
     * the DEBUG.
     */
    public static int TRACE = 3;

    /**
     * Adds the text to the internal buffer, much like with
     * {@code StringBuilder}.
     *
     * @param text to be added
     *
     * @return this Display for chaining purposes
     */
    public Display append(final String text);

    /**
     * Empties the internal buffer of all unflushed output.
     */
    public void clear();

    /**
     * Get the debug level for this run.
     *
     * @return Debug level currently set
     */
    public int debugLevel();

    /**
     * Set the debug level for this run.
     *
     * @param level Debug level to use
     */
    public void debugLevel(int level);

    /**
     * Flushes all output from the internal buffer to the output destination(s).
     */
    public void flush();

    /**
     * Adds a formatted string to the internal buffer using the specified
     * format string and arguments.
     *
     * @param format The syntax of this string is implementation specific.
     * @param args   Arguments referenced by the format specifiers in the format
     *               string.
     *
     * @return this Display for chaining purposes
     */
    public Display format(String format, Object... args);

    /**
     * Display all following text if the {@linkplain #debugLevel(int) }
     * is greater than or equal to the {@code level}.
     *
     * @param level The debug level at which to display the following text.
     *
     * @return this Display for chaining purposes
     */
    public Display level(int level);

    /**
     * Adds the text equivalent of the value to the internal buffer.
     *
     * @implNote
     * Uses {@code value ? "true" : "false"} to convert value to String.
     *
     * @param value to print
     *
     * @return this Display for chaining purposes
     *
     * @see #append(java.lang.String)
     */
    default Display append(final boolean value)
    {
        return append(value ? "true" : "false");
    }

    /**
     * Adds the number to the internal buffer.
     *
     * @implNote
     * Uses {@code Integer.toString(number)} to convert int to String.
     *
     * @param number to print
     *
     * @return this Display for chaining purposes
     *
     * @see #append(java.lang.String)
     */
    default Display append(final int number)
    {
        return append(Integer.toString(number));
    }

    /**
     * Adds the obj to the internal buffer.
     *
     * @implNote
     * Uses {@code obj != null ? obj.toString() : "null"} to convert obj to
     * String.
     *
     * @param obj to print
     *
     * @return this Display for chaining purposes
     *
     * @see #append(java.lang.String)
     */
    default Display append(final Object obj)
    {
        return append(obj != null ? obj.toString() : "null");
    }

    /**
     * Add the System line separator to the internal buffer.
     *
     * @return this Display for chaining purposes
     *
     * @see #append(java.lang.String)
     */
    default Display appendln()
    {
        return append(System.lineSeparator());
    }

    /**
     * Adds the text equivalent of the value to the internal buffer.
     *
     * @implNote
     * Uses {@code value ? "true" : "false"} to convert value to String.
     *
     * @param value to print
     *
     * @return this Display for chaining purposes
     *
     * @see #appendln(java.lang.String)
     */
    default Display appendln(final boolean value)
    {
        return appendln(value ? "true" : "false");
    }

    /**
     * Adds the number to the internal buffer, followed by the System line
     * separator.
     *
     * @implNote
     * Uses {@code Integer.toString(number)} to convert int to String.
     *
     * @param number to print
     *
     * @return this Display for chaining purposes
     *
     * @see #appendln(java.lang.String)
     * @see java.lang.System#lineSeparator()
     */
    default Display appendln(final int number)
    {
        return appendln(Integer.toString(number));
    }

    /**
     * Adds the obj to the internal buffer, followed by the System line
     * separator.
     *
     * @implNote
     * Uses {@code obj != null ? obj.toString() : "null"} to convert obj to
     * String.
     *
     * @param obj to print
     *
     * @return this Display for chaining purposes
     *
     * @see #appendln(java.lang.String)
     */
    default Display appendln(final Object obj)
    {
        return appendln(obj != null ? obj.toString() : "null");
    }

    /**
     * Adds the text to the internal buffer, followed by the System line
     * separator.
     *
     * @param text to be added
     *
     * @return this Display for chaining purposes
     *
     * @see #append(java.lang.String)
     * @see #appendln()
     */
    default Display appendln(final String text)
    {
        return append(text).appendln();
    }

    /**
     * Obtain the text label for the current debug level.
     *
     * @return text label
     */
    default String debugLevelStr()
    {
        String rtn = "";

        switch (debugLevel())
        {
            case DEFAULT -> rtn = "DEFAULT";

            case INFO -> rtn = "INFO";

            case DEBUG -> rtn = "DEBUG";

            case TRACE -> rtn = "TRACE";

            default -> throw new AssertionError();
        }

        return rtn;
    }

    /**
     * Prints the text equivalent of the value to the Display.
     *
     * @implNote
     * Uses {@code value ? "true" : "false"} to convert boolean to String.
     *
     * @param value to print
     *
     * @see #print(java.lang.String)
     */
    default void print(final boolean value)
    {
        print(value ? "true" : "false");
    }

    /**
     * Prints the number.
     *
     * @implNote
     * Uses {@code Integer.toString(number)} to convert int to String.
     *
     * @param number to print
     *
     * @see #print(java.lang.String)
     */
    default void print(final int number)
    {
        print(Integer.toString(number));
    }

    /**
     * Prints the object.
     *
     * @implNote
     * Uses {@code obj != null ? obj.toString() : "null"} to convert the Object
     * to String.
     *
     * @param obj to print
     *
     * @see #print(java.lang.String)
     */
    default void print(final Object obj)
    {
        print(obj != null ? obj.toString() : "null");
    }

    /**
     * Prints the text.
     *
     * @param text to print
     *
     * @see #append(java.lang.String)
     * @see #flush()
     */
    default void print(final String text)
    {
        append(text).flush();
    }

    /**
     * Prints a formatted string using the specified format string and
     * arguments.
     *
     * @param format The syntax of this string is implementation specific.
     * @param args   Arguments referenced by the format specifiers in the format
     *               string.
     */
    default void printf(String format, Object... args)
    {
        format(format, args).flush();
    }

    /**
     * Prints a line terminator.
     */
    default void println()
    {
        appendln().flush();
    }

    /**
     * Prints the text equivalent of the value.
     *
     * @implNote
     * Uses {@code value ? "true" : "false"} to convert boolean to String.
     *
     * @param value to print
     *
     * @see #println(java.lang.String)
     */
    default void println(final boolean value)
    {
        println(value ? "true" : "false");
    }

    /**
     * Prints the number, followed by the System line separator.
     *
     * @implNote
     * Uses {@code Integer.toString(number)} to convert int to String.
     *
     * @param number to print
     *
     * @see #println(java.lang.String)
     */
    default void println(final int number)
    {
        println(Integer.toString(number));
    }

    /**
     * Prints the object.
     *
     * @implNote
     * Uses {@code obj != null ? obj.toString() : "null"} to convert the Object
     * to String.
     *
     * @param obj to print
     *
     * @see #println(java.lang.String)
     */
    default void println(final Object obj)
    {
        println(obj != null ? obj.toString() : "null");
    }

    /**
     * Prints the text, followed by the System line separator.
     *
     * @param text to print
     *
     * @see #appendln(java.lang.String)
     * @see #flush()
     */
    default void println(final String text)
    {
        appendln(text).flush();
    }
}
