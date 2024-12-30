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

import com.bewsoftware.utils.string.Strings;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

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
 * @version 3.0.1
 *
 * @see Strings
 */
public final class ConsoleIO implements Display, Input
{
    private static final int CLOSED = 0;

    private static final String CLOSED_MSG = "ConsoleIO is closed.";

    private static final int OPEN = 1;

    /**
     * Stores the singleton of each {@link Writer} object related to each
     * specific instance of {@link PrintWriter} associated with a
     * {@code filename}.
     */
    private static final ConcurrentMap<String, WriterInstance> writers = new ConcurrentHashMap<>();

    private final boolean blank;

    private final AtomicReference<DisplayDebugLevel> debugLevel = new AtomicReference<>(DEFAULT);

//    private final AtomicReference<DisplayDebugLevel> displayLevel = new AtomicReference<>(DEFAULT);
    private final AtomicReference<Exception> exception = new AtomicReference<>();

    private final AtomicReference<PrintWriter> file = new AtomicReference<>();

    private final String filename;

    private final String linePrefix;

    private Lines lines;

    private PrintWriter out;

    private StringBuffer sb;

    /**
     * state variable : true if this instance is open.
     */
    private final AtomicInteger status = new AtomicInteger(CLOSED);

    /**
     * Instantiates a "blank console".
     * <p>
     * All output is thrown away.
     * <p>
     * For use by factory method.
     */
    private ConsoleIO()
    {
        out = null;
        linePrefix = "";
        status.set(OPEN);
        blank = true;
        filename = null;
        lines = null;
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
        status.set(OPEN);
        filename = null;
        out = new PrintWriter(System.out);
        this.linePrefix = linePrefix;
        sb = new StringBuffer();
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
        status.set(OPEN);
        boolean lBlank;
        String fname = null;

        if (withConsole)
        {
            out = new PrintWriter(System.out);
            this.linePrefix = linePrefix;
            lBlank = false;
        } else
        {
            out = null;
            this.linePrefix = "";
            lBlank = true;
        }

        sb = new StringBuffer();

        if (filename != null && !filename.isBlank())
        {
            fname = filename;

            try
            {
                if (!writers.containsKey(filename))
                {
                    writers.put(filename, new WriterInstance(filename));
                }

                file.compareAndSet(null, writers.get(filename).addUsage());
                lBlank = false;

            } catch (IOException ex)
            {
                exception.compareAndSet(null, ex);
            }
        }

        blank = lBlank;
        this.filename = fname;
        lines = new Lines();
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
     * @param ident       String to identify this Writer in internal data store.
     * @param writer      Where to send the output.
     * @param withConsole whether or not to output to the console, if any.
     */
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    private ConsoleIO(
            final String linePrefix,
            final String ident,
            final Writer writer,
            final boolean withConsole)
    {
        status.set(OPEN);
        boolean lBlank;
        String fname = null;

        if (withConsole)
        {
            out = new PrintWriter(System.out);
            this.linePrefix = linePrefix;
            lBlank = false;
        } else
        {
            out = null;
            this.linePrefix = "";
            lBlank = true;
        }

        sb = new StringBuffer();

        if (writer != null)
        {
            try
            {
                if (ident != null && !ident.isBlank())
                {
                    fname = ident;

                    if (!writers.containsKey(fname))
                    {
                        writers.put(fname, new WriterInstance(fname, writer));
                    }

                    file.compareAndSet(null, writers.get(fname).addUsage());
                    lBlank = false;

                } else
                {
                    throw new NullPointerException("ident - must not be 'null' or blank.");
                }

            } catch (IOException | NullPointerException ex)
            {
                exception.compareAndSet(null, ex);
            }
        }

        blank = lBlank;
        filename = fname;
        lines = new Lines();
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

        return new ConsoleIO(linePrefix, ident, writer, true);
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

        return new ConsoleIO(linePrefix, ident, writer, false);
    }

    @Override
    public Display append(final DisplayDebugLevel level, final String text)
    {
        if (isOpen())
        {
            if (!blank && displayOK(level))
            {
                lines.append(level, text);
            }
        } else
        {
            exception.compareAndSet(null, new IOException(CLOSED_MSG));
        }

        return this;
    }

    /**
     * @param level  {@inheritDoc}
     * @param format This implementation uses
     *               {@link Formatter#format(java.lang.String, java.lang.Object...)
     *               Formatter.format(String format, Object... args)}
     *               as the work-horse.
     *
     * @see java.util.Formatter - Format String Syntax
     */
    @Override
    public Display append(final DisplayDebugLevel level, String format, Object... args)
    {
        if (isOpen())
        {
            if (!blank && displayOK(level))
            {
                lines.append(level, format(format, args));
            }
        } else
        {
            exception.compareAndSet(null, new IOException(CLOSED_MSG));
        }

        return this;
    }

    @Override
    public Display clear()
    {
        if (isOpen())
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
        @SuppressWarnings("ThrowableResultIgnored")
        Exception ignored = exception.getAndSet(null);
    }

    @Override
    public void close() throws IOException
    {
        flush();

        if (out != null)
        {
            out.close();
            out = null;
        }

        if (file.get() != null)
        {
            writers.get(filename).removeUsage();
            file.set(null);
        }

        status.set(CLOSED);
        clear();
    }

    @Override
    public void debugLevel(final DisplayDebugLevel level)
    {
        debugLevel.set(level);
    }

    @Override
    public DisplayDebugLevel debugLevel()
    {
        return debugLevel.get();
    }

    @Override
    @Deprecated
    public boolean displayOK()
    {
        throw new UnsupportedOperationException("Deprecated.");
    }

    /**
     * Determine if the current text will be displayed, by comparing the current
     * debug level to the supplied display level.
     *
     * @param level The display level to compare with.
     *
     * @return {@code true} if it will be, {@code false} otherwise.
     */
    @Override
    public boolean displayOK(final DisplayDebugLevel level)
    {
        return debugLevel.get().value >= level.value;
    }

    @Override
    public void flush()
    {
        if (isOpen())
        {
            if (out != null || file.get() != null)
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

                if (file.get() != null)
                {
                    file.get().print(sb);
                    file.get().flush();
                }

                clear();
            }
        } else
        {
            exception.compareAndSet(null, new IOException(CLOSED_MSG));
        }
    }

    @Override
    public boolean isException()
    {
        return exception != null;
    }

    @Override
    @Deprecated
    public Display level(final DisplayDebugLevel level)
    {
        throw new UnsupportedOperationException("Deprecated.");
    }

    @Override
    public Scanner newScanner()
    {
        Scanner rtn = null;

        if (isOpen())
        {
            rtn = new Scanner(System.in);
        } else
        {
            exception.compareAndSet(null, new IOException(CLOSED_MSG));
        }

        return rtn;
    }

    @Override
    public Exception popException()
    {
        final Exception rtn = exception.get();
        clearExceptions();
        return rtn;
    }

    @Override
    public String readLine()
    {
        String rtn = null;

        if (isOpen())
        {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(System.in)))
            {
                rtn = reader.readLine();
            } catch (IOException ex)
            {
                exception.compareAndSet(null, ex);
            }
        } else
        {
            exception.compareAndSet(null, new IOException(CLOSED_MSG));
        }

        return rtn;
    }

    @Override
    public String readLine(String fmt, Object... args)
    {
        String rtn = null;

        if (isOpen())
        {
            println(String.format(fmt, args));

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(System.in)))
            {
                rtn = reader.readLine();
            } catch (IOException ex)
            {
                exception.compareAndSet(null, ex);
            }
        } else
        {
            exception.compareAndSet(null, new IOException(CLOSED_MSG));
        }

        return rtn;
    }

    @Override
    public char[] readPassword()
    {
        char[] rtn = null;

        if (isOpen())
        {
            String input = readLine();
            rtn = input != null ? input.toCharArray() : null;
        } else
        {
            exception.compareAndSet(null, new IOException(CLOSED_MSG));
        }

        return rtn;
    }

    @Override
    public char[] readPassword(String fmt, Object... args)
    {
        char[] rtn = null;

        if (isOpen())
        {
            String input = readLine(fmt, args);
            rtn = input != null ? input.toCharArray() : null;
        } else
        {
            exception.compareAndSet(null, new IOException(CLOSED_MSG));
        }

        return rtn;

    }

    private boolean isOpen()
    {
        return status.get() == OPEN;
    }

    /**
     * Used to store a single appended text entry.
     */
    private static final class Line
    {
        private final DisplayDebugLevel level;

        private boolean terminated;

        private final StringBuffer text;

        /**
         * Only accessible by the enclosing class.
         *
         * @param level Debug level of text.
         * @param text  to store
         */
        private Line(final DisplayDebugLevel level, final String text)
        {
            this.level = level;
            this.text = new StringBuffer();
            this.append(text);
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
            return text.toString();
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
                this.text.append(text);
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
        private final AtomicReference<Line> lastUnterminated = new AtomicReference<>();

        private final Queue<Line> lines;

        /**
         * Only accessible by the enclosing class.
         */
        private Lines()
        {
            lines = new ConcurrentLinkedQueue<>();
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
                final Line lu;

                if ((lu = lastUnterminated.get()) != null)
                {
                    if (lu.getLevel() == level)
                    {
                        rtn = lu.append(text);

                        if (lu.isTerminated())
                        {
                            lastUnterminated.compareAndSet(lu, null);
                        }

                        done = true;
                    } else
                    {
                        rtn = lu.append("\n");
                        lastUnterminated.compareAndSet(lu, null);
                    }
                }

                if (!done)
                {
                    final Line line = new Line(level, text);
                    rtn = lines.add(line);

                    if (!line.isTerminated())
                    {
                        lastUnterminated.compareAndSet(null, line);
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
            final List<Line> list = new ArrayList<>();
            final Line lu;

            if ((lu = lastUnterminated.get()) != null
                    && lastUnterminated.compareAndSet(lu, null))
            {
                lu.append("\n");
            }

            for (Line line : lines)
            {
                String[] textArr = line.getText().replace("\n", " \n").split("\n");

                if (textArr.length > 1)
                {
                    for (String text : textArr)
                    {
                        final Line line2 = new Line(line.getLevel(), Strings.rTrim(text));
                        line2.append("\n");
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
         * Returns a text string representation of all the lines being
         * stored.
         * <p>
         * If there is an unterminated line, it will be terminate, and
         * cleared.
         *
         * @return a text string representation of all the lines currently
         *         stored.
         */
        public String getText()
        {
            final StringBuffer sb;
            final Line lu;
            String rtn = "";

            if (!lines.isEmpty())
            {
                sb = new StringBuffer();

                if ((lu = lastUnterminated.get()) != null
                        && lastUnterminated.compareAndSet(lu, null))
                {
                    lu.append("\n");
                }

                lines.forEach(line -> sb.append(line.getText()));
                rtn = sb.toString();
            }

            return rtn;
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
        private static final int CLOSED = 0;

        private static final int OPEN = 1;

        /**
         * Number of references of this instance of {@link PrintWriter} that are
         * still active.
         */
        private final AtomicInteger count = new AtomicInteger(1);

        /**
         * The filename associated with the {@link #printWriter}.
         */
        private final String filename;

        /**
         * The single instance of this {@link PrintWriter}.
         */
        private final PrintWriter printWriter;

        /**
         * state variable : true if this instance is open.
         */
        private final AtomicInteger status = new AtomicInteger(CLOSED);

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
         */
        private WriterInstance(final String filename) throws FileNotFoundException
        {
            this.filename = filename;
            printWriter = new PrintWriter(filename);
            status.set(OPEN);
        }

        /**
         * Will only be instantiated by parent class.
         *
         * @param ident  String to identify this Writer in internal data store.
         * @param writer where to write output
         */
        private WriterInstance(final String ident, final Writer writer)
        {
            Objects.requireNonNull(writer);
            printWriter = new PrintWriter(writer);
            this.filename = ident;
            status.set(OPEN);
        }

        @Override
        public void close()
        {
            try (printWriter)
            {
                if (status.compareAndSet(OPEN, CLOSED))
                {
                    count.set(0);
                    printWriter.flush();
                }
            }
        }

        /**
         * Number of references of this instance of {@link PrintWriter} that are
         * still active.
         *
         * @return count
         */
        public int count()
        {
            return count.get();
        }

        @Override
        @SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
        public boolean equals(final Object obj)
        {
            return (obj instanceof WriterInstance other)
                    && (Objects.equals(this.filename, other.filename));
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
            int hash = 7;
            hash = 59 * hash + Objects.hashCode(this.filename);
            return hash;
        }

        /**
         *
         * @return {@code true} if open, {@code false} otherwise.
         */
        public boolean isOpen()
        {
            return status.get() == OPEN;
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
                count.addAndGet(-1);

                if (count.get() == 0)
                {
                    close();
                }
            }
        }

        /**
         * Increments the usage counter and provides a reference to the single
         * instance of {@link PrintWriter} stored internally.
         *
         * @return reference to {@link PrintWriter}
         *
         * @throws IOException if any
         */
        PrintWriter addUsage() throws IOException
        {
            if (isOpen())
            {
                count.addAndGet(1);
                return printWriter;
            } else
            {
                throw new IOException("PrintWriter closed.");
            }
        }
    }
}
