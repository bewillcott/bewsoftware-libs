/*
 *  File Name:    ConsoleIO.java
 *  Project Name: bewsoftware-utils
 *
 *  Copyright (c) 2021-2023 Bradley Willcott
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
package com.bewsoftware.utils.io;

import com.bewsoftware.annotations.GuardedBy;
import com.bewsoftware.utils.string.Strings;
import java.io.*;
import java.util.*;

import static com.bewsoftware.utils.io.DisplayDebugLevel.DEFAULT;
import static java.lang.String.format;

/**
 * This class implements a console with benefits.
 * <p>
 * Apart from writing to and reading from the console, you can also write to
 * a file.
 *
 * @implNote
 * Once the ConsolIO object is closed, any further calls to any of the methods:
 * append, appendln, print, println, flush, newScanner, readLine, or
 * readPassword, will cause an {@link IOException}.
 *
 * This file has been modified to be compatible with JDK 1.8, by using
 * {@link Strings} methods.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0.7
 * @version 3.0.0
 *
 * @see Strings
 */
public final class ConsoleIO implements Display, Input
{

    private static final String CLOSED = "ConsoleIO is closed.";

    /**
     * Stores the singleton of each {@link Writer} object related to each
     * specific instance of {@link PrintWriter} associated with a
     * {@code filename}.
     */
    @GuardedBy("ConsoleIO.writers")
    private static final List<WriterInstance> writers = Collections.synchronizedList(new ArrayList<>());

    private final boolean blank;

    private final Console console;

    @GuardedBy("this")
    private DisplayDebugLevel debugLevel = DEFAULT;

    @GuardedBy("this")
    private DisplayDebugLevel displayLevel = DEFAULT;

    @GuardedBy("this")
    private Exception exception;

    private PrintWriter file;

    private final String filename;

    private final String linePrefix;

    private Lines lines;

    @GuardedBy("this")
    private boolean open;

    private PrintWriter out;

    private StringBuilder sb;

    /**
     * Instantiates a "blank console".
     * <p>
     * All output is thrown away.
     * <p>
     * For use by factory method.
     */
    private ConsoleIO()
    {
        file = null;
        out = null;
        linePrefix = "";
        open = true;
        blank = true;
        filename = null;
        console = null;
    }

    /**
     * Instantiates a fully configured "console display".
     * <p>
     * All output goes to the system console.
     * <p>
     * For use by factory method.
     *
     * @param linePrefix text to prepend to each line
     */
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    private ConsoleIO(final String linePrefix)
    {
        open = true;
        file = null;
        filename = null;

        console = System.console();

        if (console != null)
        {
            out = console.writer();
        } else
        {
            out = new PrintWriter(System.out);
        }

        this.linePrefix = linePrefix;
        sb = new StringBuilder();
        lines = new Lines();
        blank = false;
    }

    /**
     * Instantiates a type of display based on the parameters: {@code filename}
     * and {@code withConsole}.
     *
     * @implSpec
     * If {@code filename} is not {@code null} and not {@code isBlank()}, then
     * the file will be opened/created. If the file exists, it will be
     * truncated. If successful, a copy of all text will be appended to this
     * file.
     * <p>
     * If {@code withConsole} is {@code true}, then the System console will be
     * sent a copy of all text.
     * <p>
     * For use by factory method.
     *
     * @param linePrefix  text to prepend to each line
     * @param filename    the file to output to
     * @param withConsole whether or not to output to the console, if any
     */
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    private ConsoleIO(final String linePrefix, final String filename, final boolean withConsole)
    {
        open = true;
        boolean lBlank;

        if (withConsole)
        {
            console = System.console();

            if (console != null)
            {
                out = this.console.writer();
            } else
            {
                out = new PrintWriter(System.out);
            }

            this.linePrefix = linePrefix;
            lBlank = false;
        } else
        {
            console = null;
            out = null;
            this.linePrefix = "";
            lBlank = true;
        }

        sb = new StringBuilder();
        lines = new Lines();

        if (filename != null && !filename.isBlank())
        {
            this.filename = filename;

            try
            {
                synchronized (writers)
                {
                    int idx = writers.indexOf(filename);

                    if (idx == -1)
                    {
                        writers.add(new WriterInstance(filename));
                        idx = indexOfWriters(filename);
                    }

                    this.file = writers.get(idx).addUsage();
                    lBlank = false;
                }
            } catch (IOException ex)
            {
                exception = ex;
            }
        } else
        {
            file = null;
            this.filename = null;
        }

        blank = lBlank;
    }

    /**
     * Instantiates a type of display based on the parameters: {@code writer}
     * and {@code withConsole}.
     *
     * @implSpec
     * If {@code writer} is not {@code null}, then it will be opened/created. If
     * successful, a copy of all text will be appended to it.
     * <p>
     * If {@code withConsole} is {@code true}, then the System console will be
     * sent a copy of all text.
     * <p>
     * For use by factory method.
     *
     * @param linePrefix  text to prepend to each line.
     * @param writer      Where to send the output.
     * @param ident       String to identify this Writer in internal data store.
     * @param withConsole whether or not to output to the console, if any.
     */
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    private ConsoleIO(
            final String linePrefix,
            final Writer writer,
            final String ident,
            final boolean withConsole)
    {
        open = true;
        boolean lBlank;

        if (withConsole)
        {
            console = System.console();

            if (console != null)
            {
                out = this.console.writer();
            } else
            {
                out = new PrintWriter(System.out);
            }

            this.linePrefix = linePrefix;
            lBlank = false;
        } else
        {
            console = null;
            out = null;
            this.linePrefix = "";
            lBlank = true;
        }

        sb = new StringBuilder();
        lines = new Lines();

        if (writer != null)
        {
            filename = ident != null && !ident.isBlank()
                    ? ident
                    : writer.getClass().getName();

            try
            {
                synchronized (writers)
                {
                    int idx = writers.indexOf(filename);

                    if (idx == -1)
                    {
                        writers.add(new WriterInstance(writer, filename));
                        idx = indexOfWriters(filename);
                    }

                    this.file = writers.get(idx).addUsage();
                    lBlank = false;
                }
            } catch (IOException ex)
            {
                exception = ex;
            }
        } else
        {
            file = null;
            this.filename = null;
        }

        blank = lBlank;
    }

    /**
     * Provides a version of the ConsolIO that displays nothing.
     *
     * @return new blank Display
     */
    public static Display blankDisplay()
    {
        return new ConsoleIO();
    }

    /**
     * Provides a fully configured console display.
     *
     * @param linePrefix text to prepend to each line
     *
     * @return new Display
     */
    public static Display consoleDisplay(final String linePrefix)
    {
        return new ConsoleIO(linePrefix);
    }

    /**
     * Provides a version of the ConsoleIO that outputs to both the console and
     * the designated file.
     *
     * @param linePrefix text to prepend to each line
     * @param filename   the file to output to
     *
     * @return new Display
     */
    public static Display consoleFileDisplay(
            final String linePrefix,
            final String filename)
    {

        return new ConsoleIO(linePrefix, filename, true);
    }

    /**
     * Provides a version of the ConsoleIO that outputs to both the console and
     * the designated file.
     *
     * @param linePrefix text to prepend to each line
     * @param writer     where to write output
     * @param ident      String to identify this Writer in internal data store.
     *
     * @return new Display
     */
    public static Display consoleWriterDisplay(
            final String linePrefix,
            final Writer writer,
            final String ident)
    {

        return new ConsoleIO(linePrefix, writer, ident, true);
    }

    /**
     * Provides a version of the ConsoleIO that outputs only to the designated
     * file.
     *
     * @param linePrefix text to prepend to each line
     * @param filename   the file to output to
     *
     * @return new Display
     */
    public static Display fileDisplay(
            final String linePrefix,
            final String filename)
    {

        return new ConsoleIO(linePrefix, filename, false);
    }

    /**
     * Provides a version of the ConsoleIO that outputs only to the designated
     * {@code writer}.
     *
     * @param linePrefix text to prepend to each line
     * @param writer     where to write output
     * @param ident      String to identify this Writer in internal data store.
     *
     * @return new Display
     */
    public static Display writerDisplay(
            final String linePrefix,
            final Writer writer,
            final String ident)
    {

        return new ConsoleIO(linePrefix, writer, ident, false);
    }

    @Override
    @GuardedBy("this")
    public synchronized Display append(final String text)
    {
        if (open)
        {
            if (!blank && displayOK())
            {
                lines.append(displayLevel, text);
            }
        } else
        {
            exception = new IOException(CLOSED);
        }

        return this;
    }

    /**
     * @param format This implementation uses
     *               {@link Formatter#format(java.lang.String, java.lang.Object...)
     *               Formatter.format(String format, Object... args)}
     *               as the work-horse.
     *
     * @see java.util.Formatter - Format String Syntax
     */
    @Override
    @GuardedBy("this")
    public synchronized Display append(String format, Object... args)
    {
        if (open)
        {
            if (!blank && displayOK())
            {
                lines.append(displayLevel, format(format, args));
            }
        } else
        {
            exception = new IOException(CLOSED);
        }

        return this;
    }

    @Override
    public Display clear()
    {
        if (open)
        {
            if (!blank)
            {
                sb.setLength(0);
                lines.clear();
            }
        } else
        {
            sb = null;
            lines = null;
        }

        return this;
    }

    @Override
    public void clearExceptions()
    {
        exception = null;
    }

    @GuardedBy("ConsoleIO.writers")
    @Override
    public void close() throws IOException
    {
        flush();

        if (out != null)
        {
            out.close();
            out = null;
        }

        if (file != null)
        {
            synchronized (writers)
            {
                writers.get(writers.indexOf(filename)).removeUsage();
            }

            file = null;
        }

        open = false;
        clear();
    }

    @Override
    @GuardedBy("this")
    public synchronized void debugLevel(DisplayDebugLevel level)
    {
        debugLevel = level;
    }

    @Override
    @GuardedBy("this")
    public synchronized DisplayDebugLevel debugLevel()
    {
        return debugLevel;
    }

    @GuardedBy("this")
    @Override
    public synchronized boolean displayOK()
    {
        return debugLevel.value >= displayLevel.value;
    }

    @Override
    @GuardedBy("this")
    public synchronized void flush()
    {
        if (open)
        {
            if (displayOK())
            {
                if (out != null || file != null)
                {
                    if (linePrefix != null && linePrefix.length() > 0 && !lines.isEmpty())
                    {
                        sb.setLength(0);

                        for (Line line : lines.getLines())
                        {
                            sb.append(linePrefix);

                            if (line.getLevel().value > DEFAULT.value)
                            {
                                sb.append("[").append(line.getLevel().label).append("] ");
                            }

                            sb.append(line.getText());

                        }
                    } else if (!lines.isEmpty())
                    {
                        sb.append(lines.getText());
                    }

                    if (out != null)
                    {
                        out.print(sb);
                        out.flush();
                    }

                    if (file != null)
                    {
                        file.print(sb);
                        file.flush();
                    }

                    clear();
                }
            }
        } else
        {
            exception = new IOException(CLOSED);
        }
    }

    @Override
    public boolean isException()
    {
        return exception != null;
    }

    @Override
    @GuardedBy("this")
    public synchronized Display level(DisplayDebugLevel level)
    {
        displayLevel = level;
        return this;
    }

    @Override
    public Scanner newScanner()
    {
        Scanner rtn = null;

        if (open)
        {
            if (console != null)
            {
                rtn = new Scanner(console.reader());
            } else
            {
                rtn = new Scanner(System.in);
            }
        } else
        {
            exception = new IOException(CLOSED);
        }

        return rtn;
    }

    @Override
    public Exception popException()
    {
        Exception rtn = exception;
        clearExceptions();
        return rtn;
    }

    @Override
    public String readLine()
    {
        String rtn = null;

        if (open)
        {
            if (console != null)
            {
                rtn = console.readLine();
            } else
            {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(System.in));
                try
                {
                    rtn = reader.readLine();
                } catch (IOException ex)
                {
                    exception = ex;
                }
            }
        } else
        {
            exception = new IOException(CLOSED);
        }

        return rtn;
    }

    @Override
    public String readLine(String fmt, Object... args)
    {
        String rtn = null;

        if (open)
        {
            if (console != null)
            {
                rtn = console.readLine(fmt, args);
            } else
            {
                println(String.format(fmt, args));
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(System.in));
                try
                {
                    rtn = reader.readLine();
                } catch (IOException ex)
                {
                    exception = ex;
                }
            }
        } else
        {
            exception = new IOException(CLOSED);
        }

        return rtn;
    }

    @Override
    public char[] readPassword()
    {
        char[] rtn = null;

        if (open)
        {
            if (console != null)
            {
                rtn = console.readPassword();
            } else
            {
                String input = readLine();
                rtn = input != null ? input.toCharArray() : null;
            }
        } else
        {
            exception = new IOException(CLOSED);
        }

        return rtn;
    }

    @Override
    public char[] readPassword(String fmt, Object... args)
    {
        char[] rtn = null;

        if (open)
        {
            if (console != null)
            {
                rtn = console.readPassword(fmt, args);
            } else
            {
                String input = readLine(fmt, args);
                rtn = input != null ? input.toCharArray() : null;
            }
        } else
        {
            exception = new IOException(CLOSED);
        }

        return rtn;

    }

    /**
     * Find the index of the required WriterInstance.
     *
     * @param filename to find
     *
     * @return index of WriterInstance
     */
    @GuardedBy("ConsoleIO.writers")
    @SuppressWarnings("IncompatibleEquals")
    private int indexOfWriters(final String filename)
    {
        int rtn = -1;

        synchronized (writers)
        {
            if (!writers.isEmpty() && filename != null && !filename.isBlank())
            {
                for (int i = 0; i < writers.size(); i++)
                {
                    WriterInstance wi = writers.get(i);

                    if (wi.equals(filename))
                    {
                        rtn = i;
                        break;
                    }
                }
            }
        }

        return rtn;
    }

    /**
     * Used to store a single appended text entry.
     */
    private static final class Line
    {
        private final DisplayDebugLevel level;

        private boolean terminated;

        private String text = "";

        /**
         * Only accessible by the enclosing class.
         *
         * @param level Debug level of text.
         * @param text  to store
         */
        private Line(final DisplayDebugLevel level, final String text)
        {
            this.level = level;
            append(text);
        }

        /**
         * @return the level
         */
        public DisplayDebugLevel getLevel()
        {
            return level;
        }

        /**
         * @return the text
         */
        public String getText()
        {
            return text;
        }

        /**
         * @return the terminated status
         */
        public boolean isTerminated()
        {
            return terminated;
        }

        @Override
        public String toString()
        {
            return "Line{" + "level=" + level + ", terminated=" + terminated + ", text=" + text + '}';
        }

        /**
         * Append the new 'text' to the existing stored 'text'.
         * <p>
         * Updates 'terminated' status.
         *
         * @param text to append
         *
         * @return {@code true} if successful, {@code false} otherwise.
         */
        public boolean append(final String text)
        {
            boolean rtn = false;

            if (text != null)
            {
                this.text += text;
                this.terminated = text.endsWith("\n");
                rtn = true;
            }

            return rtn;
        }
    }

    /**
     * Used to store all of the text entries prior to printing.
     */
    private static final class Lines
    {
        private Line lastUnterminated = null;

        private final List<Line> lines;

        /**
         * Only accessible by the enclosing class.
         */
        private Lines()
        {
            lines = Collections.synchronizedList(new ArrayList<>());
        }

        /**
         * Append the text by either adding a new line, or appending it to a
         * previously added, unterminated line of the same 'level'.
         *
         * @param level debug display level.
         * @param text  to be stored.
         *
         * @return {@code true} if successful, {@code false} otherwise.
         */
        public boolean append(final DisplayDebugLevel level, final String text)
        {
            boolean rtn = false;
            boolean done = false;

            if (level != null && text != null)
            {
                if (lastUnterminated != null)
                {
                    if (lastUnterminated.getLevel() == level)
                    {
                        rtn = lastUnterminated.append(text);

                        if (lastUnterminated.isTerminated())
                        {
                            lastUnterminated = null;
                        }

                        done = true;
                    } else
                    {
                        rtn = lastUnterminated.append("\n");
                        lastUnterminated = null;
                    }
                }

                if (!done)
                {
                    Line line = new Line(level, text);
                    rtn = lines.add(line);

                    if (!line.isTerminated())
                    {
                        lastUnterminated = line;
                    }
                }
            }

            return rtn;
        }

        /**
         * Removes all of the lines currently being stored.
         */
        public void clear()
        {
            lines.clear();
        }

        /**
         * Get all of the lines currently being stored.
         * <p>
         * If there is an unterminated line, it will be terminate, and cleared.
         *
         * @return a new list of terminated lines.
         */
        public List<Line> getLines()
        {
            List<Line> list = new ArrayList<>();

            int len1 = lines.size();

            for (int i = 0; i < len1; i++)
            {
                Line line = lines.get(i);

                String[] textArr = line.getText().replace("\n", " \n").split("\n");

                if (textArr.length > 1)
                {
                    int len2 = textArr.length;

                    for (int j = 0; j < len2; j++)
                    {
                        Line line2 = new Line(line.getLevel(), Strings.rTrim(textArr[j]));

                        if (lastUnterminated == null)
                        {
                            line2.append("\n");
                        } else if (i == len1 - 1 && j == len2 - 1)
                        {
                            lastUnterminated = null;
                        } else
                        {
                            line2.append("\n");
                        }

                        list.add(line2);

                    }
                } else
                {
                    list.add(line);
                }
            }

            return list;
        }

        /**
         * Returns a text string representation of all the lines being stored.
         * <p>
         * If there is an unterminated line, it will be terminate, and cleared.
         *
         * @return a text string representation of all the lines being stored.
         */
        public String getText()
        {
            StringBuilder sb = new StringBuilder();

            if (!lines.isEmpty())
            {
                if (lastUnterminated != null)
                {
                    lastUnterminated.append("\n");
                    lastUnterminated = null;
                }

                lines.forEach(line -> sb.append(line.getText()));
            }

            return sb.toString();
        }

        /**
         * @return {@code true} if no lines are being stored.
         */
        public boolean isEmpty()
        {
            return lines.isEmpty();
        }

        /**
         * @return the number of lines being stored.
         */
        public int size()
        {
            return lines.size();
        }

        @Override
        public String toString()
        {
            return "Lines{" + "lines=" + lines + '}';
        }
    }

    /**
     * Contains a single instance of the {@link PrintWriter} object associated
     * with the file: {@code filename}.
     *
     * @since 1.0.7
     * @version 1.0.7
     */
    private static final class WriterInstance implements Closeable
    {

        /**
         * Number of references of this instance of {@link PrintWriter} that are
         * still active.
         */
        private int count;

        /**
         * The filename associated with the {@link #printWriter}.
         */
        private final String filename;

        /**
         * The single instance of this {@link PrintWriter}.
         */
        private PrintWriter printWriter;

        /**
         * Will only be instantiated by parent class.
         *
         * @param filename name of file to create/open
         *
         * @throws FileNotFoundException If the given string does not denote an
         *                               existing, writable regular file and a
         *                               new regular file of that name cannot be
         *                               created, or if some other error occurs
         *                               while opening or creating the file
         *
         */
        private WriterInstance(final String filename) throws FileNotFoundException
        {
            this.filename = filename;
            printWriter = new PrintWriter(filename);
            count = 1;
        }

        /**
         * Will only be instantiated by parent class.
         *
         * @param writer where to write output
         * @param ident  String to identify this Writer in internal data store.
         */
        private WriterInstance(final Writer writer, final String ident)
        {
            Objects.requireNonNull(writer);
            printWriter = new PrintWriter(writer);
            this.filename = ident;
            count = 1;
        }

        /**
         * Increments the usage counter and provides a reference to the single
         * instance of {@link PrintWriter} stored internally.
         *
         * @return reference to {@link PrintWriter}
         *
         * @throws IOException if any
         */
        public PrintWriter addUsage() throws IOException
        {
            if (isOpen())
            {
                count++;
                return printWriter;
            } else
            {
                throw new IOException("PrintWriter closed.");
            }
        }

        @Override
        public void close()
        {
            count = 0;
            printWriter.flush();
            printWriter.close();
            printWriter = null;
        }

        /**
         * Number of references of this instance of {@link PrintWriter} that are
         * still active.
         *
         * @return count
         */
        public int count()
        {
            return count;
        }

        @Override
        @SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
        public boolean equals(Object obj)
        {
            if (this == obj)
            {
                return true;
            }

            if (obj == null)
            {
                return false;
            }

            if (getClass() != obj.getClass())
            {
                return this.filename.equals(obj);
            }

            final WriterInstance other = (WriterInstance) obj;
            return Objects.equals(this.filename, other.filename);
        }

        /**
         * The filename associated with the {@link #printWriter}.
         *
         * @return filename
         */
        public String filename()
        {
            return filename;
        }

        @Override
        public int hashCode()
        {
            return Objects.hashCode(this.filename);
        }

        /**
         *
         * @return {@code true} if open, {@code false} otherwise.
         */
        public boolean isOpen()
        {
            return printWriter != null;
        }

        /**
         * Decrements the usage counter.
         *
         * @implSpec
         * When the counter reaches zero, then the console is closed.
         */
        public void removeUsage()
        {
            if (isOpen())
            {
                --count;

                if (count == 0)
                {
                    close();
                }
            }
        }
    }
}
