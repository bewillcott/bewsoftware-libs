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

import com.bewsoftware.utils.concurrent.PerThreadPoolExecutor;
import com.bewsoftware.utils.concurrent.ThreadExecutorService;
import com.bewsoftware.utils.string.Strings;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.bewsoftware.utils.io.ConsoleIO.Status.CLOSED;
import static com.bewsoftware.utils.io.ConsoleIO.Status.CLOSING;
import static com.bewsoftware.utils.io.ConsoleIO.Status.OPEN;
import static com.bewsoftware.utils.io.DisplayDebugLevel.DEFAULT;
import static java.util.Objects.requireNonNull;

/**
 * This class implements a console with benefits.
 * <p>
 * Apart from writing to and reading from the console, you can also write to
 * a file.
 *
 * @apiNote
 * When using this in a multi-threaded application, it is highly recommended
 * that the first instantiation be made within the {@code main(..)} class.
 *
 * @implNote
 * Once the ConsolIO object is closed, any further calls to any of the methods:
 * append, appendln, print, println, flush, newScanner, readLine, or
 * readPassword, will cause an {@link IOException}.
 *
 * This file has been modified:
 * - to be compatible with JDK 1.8, by using {@link Strings} methods.
 * - to be @ThreadSafe.
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

    private static final String CLOSED_MSG = "ConsoleIO is closed.";

    private static final int FLUSH_ALL = -1;

    /**
     * This is to be used to determine if a thread is the parent one, when doing
     * a 'flush(threadId)'.
     */
    private static final long PARENT_ID = Thread.currentThread().threadId();

    private static final ThreadExecutorService exec = new PerThreadPoolExecutor();

    private static final AtomicReference<PrintWriter> file = new AtomicReference<>();

    private static final AtomicReference<String> filename = new AtomicReference<>();

    private static final AtomicReference<String> linePrefix = new AtomicReference<>("");

    private static final AtomicReference<Lines> lines = new AtomicReference<>();

    private static final AtomicReference<PrintWriter> out = new AtomicReference<>();

    /**
     * Stores the singleton of each {@link Writer} object related to each
     * specific instance of {@link PrintWriter} associated with a
     * {@code filename}.
     */
    private static final ConcurrentMap<String, WriterInstance> writers = new ConcurrentHashMap<>();

    private final boolean blank;

    private final AtomicReference<DisplayDebugLevel> debugLevel = new AtomicReference<>(DEFAULT);

    private final AtomicReference<Exception> exception = new AtomicReference<>();// ???

    private final ReentrantLock lock = new ReentrantLock(false);

    /**
     * This is the current 'state' of this ConsoleIO.
     */
    private final AtomicReference<Status> status = new AtomicReference<>(CLOSED);

    /**
     * Instantiates a "blank console".
     * <p>
     * All output is thrown away.
     * <p>
     * For use by factory method.
     */
    private ConsoleIO()
    {
        status.set(OPEN);
        blank = true;
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
        out.set(new PrintWriter(System.out));
        ConsoleIO.linePrefix.set(linePrefix != null ? linePrefix : "");
        lines.set(new Lines());
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
            out.set(new PrintWriter(System.out));
            ConsoleIO.linePrefix.set(linePrefix != null ? linePrefix : "");
            lBlank = false;
        } else
        {
            lBlank = true;
        }

        if (filename != null && !filename.isBlank())
        {
            fname = filename;

            try
            {
                if (!writers.containsKey(filename))
                {
                    writers.put(filename, new WriterInstance(filename));
                }

                file.set(writers.get(filename).addUsage());
                lBlank = false;

            } catch (NullPointerException | IllegalArgumentException | IOException ex)
            {
                exception.compareAndSet(null, ex);
            }
        }

        blank = lBlank;
        ConsoleIO.filename.set(fname);
        lines.set(new Lines());
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
     *
     * @since 3.0.1
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
            out.set(new PrintWriter(System.out));
            ConsoleIO.linePrefix.set(linePrefix != null ? linePrefix : "");
            lBlank = false;
        } else
        {
            lBlank = true;
        }

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

                    file.set(writers.get(fname).addUsage());
                    lBlank = false;

                } else
                {
                    throw new NullPointerException("ident - must not be 'null' or blank.");
                }

            } catch (IOException | NullPointerException | IllegalArgumentException ex)
            {
                exception.compareAndSet(null, ex);
            }

        }

        blank = lBlank;
        ConsoleIO.filename.set(fname);
        lines.set(new Lines());
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
     * @param ident      String to identify this Writer in internal data store.
     * @param writer     where to write output
     *
     * @return new Display
     *
     * @since 3.0.1
     */
    public static Display consoleWriterDisplay(
            final String linePrefix,
            final String ident,
            final Writer writer
    )
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
     * @param ident      String to identify this Writer in internal data store.
     * @param writer     where to write output
     *
     * @return new Display
     */
    public static Display writerDisplay(
            final String linePrefix,
            final String ident,
            final Writer writer
    )
    {

        return new ConsoleIO(linePrefix, ident, writer, false);
    }

    @Override
    public Display append(final boolean flush, final DisplayDebugLevel level, final String text)
    {
        if (isOpen())
        {
            if (!blank && displayOK(level))
            {
                submitTask(flush, level, text);
            }
        } else
        {
            exception.compareAndSet(null, new IOException(CLOSED_MSG));
        }

        return this;
    }

    @Override
    public void await(final long timeout, final TimeUnit unit) throws InterruptedException
    {
        if (isOpen() || isClosing())
        {
            if (timeout <= 0)
            {
                exec.awaitCompletion();
            } else
            {
                exec.awaitCompletion(timeout, Objects.requireNonNull(unit, "'unit' is null"));
            }
        }
    }

    @Override
    public void awaitThread(long timeout, TimeUnit unit) throws InterruptedException
    {
        if (isOpen() || isClosing())
        {
            final long threadId = Thread.currentThread().threadId();

            if (timeout <= 0)
            {
                exec.awaitThreadCompletion(threadId);
            } else
            {
                exec.awaitThreadCompletion(threadId, timeout, Objects.requireNonNull(unit, "'unit' is null"));
            }
        }
    }

    @Override
    public Display clear()
    {
        lock.lock();

        try
        {
            if (isOpen() || isClosing())
            {
                if (!blank)
                {
                    lines.get().clear();
                }
            } else
            {
                lines.set(null);
            }
        } finally
        {
            lock.unlock();
        }

        return this;
    }

    @Override
    public void clearExceptions()
    {
        exception.set(null);
    }

    @Override
    @SuppressWarnings("ConvertToTryWithResources")
    public void close() throws IOException
    {
        if (status.compareAndSet(OPEN, CLOSING))
        {
            try
            {
                await();
                flush(FLUSH_ALL);

                if (out != null)
                {
                    out.get().close();
                    out.set(null);
                }

                if (file.get() != null)
                {
                    writers.get(filename.get()).removeUsage();
                    file.set(null);
                }

                assert status.compareAndSet(CLOSING, CLOSED);
                exec.close();
                clear();
            } catch (InterruptedException ex)
            {
                throw new IOException("exec.awaitCompletion()", ex);
            }
        }
    }

    @Override
    public void debugLevel(final DisplayDebugLevel level
    )
    {
        debugLevel.set(level);
    }

    @Override
    public DisplayDebugLevel debugLevel()
    {
        return debugLevel.get();
    }

    /**
     * Determine if the current text will be displayed, by comparing the
     * current
     * debug level to the supplied display level.
     *
     * @param level The display level to compare with.
     *
     * @return {@code true} if it will be, {@code false} otherwise.
     */
    @Override
    public boolean displayOK(final DisplayDebugLevel level
    )
    {
        return debugLevel.get().value >= level.value;
    }

    @Override
    public void flush()
    {
        flush(Thread.currentThread().threadId());
    }

    @Override
    public boolean isException()
    {
        return exception.get() != null;
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
    public String readLine(String fmt, Object... args
    )
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
    public char[] readPassword(String fmt, Object... args
    )
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

    private void flush(final long threadId)
    {
        if (isOpen() || isClosing())
        {
            lock.lock();

            try
            {
                final StringBuilder sb = new StringBuilder();

                if (lines.get() != null && (out != null || file.get() != null))
                {
                    if (!lines.get().isEmpty())
                    {
                        if (threadId == FLUSH_ALL)
                        {
                            lines.get().getThreadIds().forEach((tId) -> flushThreadId(tId, sb));
                        } else
                        {
                            flushThreadId(threadId, sb);
                        }
                    }

                    if (out != null)
                    {
                        out.get().print(sb);
                        out.get().flush();
                    }

                    if (file.get() != null)
                    {
                        file.get().print(sb);
                        file.get().flush();
                    }

                    clear();
                }
            } finally
            {
                lock.unlock();
            }

        } else
        {
            exception.compareAndSet(null, new IOException(CLOSED_MSG));
        }
    }

    private void flushThreadId(final long threadId, final StringBuilder sb)
    {
        lock.lock();

        try
        {
            final String sThreadId = (threadId != PARENT_ID)
                    ? "[" + threadId + "] " : "";

            if (linePrefix != null && !linePrefix.get().isBlank())
            {
                for (Line line : lines.get().getLines(threadId))
                {
                    sb.append(sThreadId).append(linePrefix);

                    final DisplayDebugLevel level = line.getLevel();

                    if (level.value > DEFAULT.value)
                    {
                        sb.append("(").append(level.label).append(") ");
                    }

                    sb.append(line.getText());

                }
            } else
            {
                sb.append(sThreadId).append(lines.get().getText(threadId));
            }
        } finally
        {
            lock.unlock();
        }
    }

    private boolean isClosing()
    {
        return status.get() == CLOSING;
    }

    private boolean isOpen()
    {
        return status.get() == OPEN;
    }

    /**
     * Submit a new task to process the supplied text.
     *
     * @param flush the all text to outputs.
     * @param level The debug level in which to display this text.
     * @param text  to be processed.
     */
    private void submitTask(final boolean flush, final DisplayDebugLevel level, final String text)
    {
        lock.lock();

        try
        {
            final long threadId = Thread.currentThread().threadId();

            final Runnable task = () ->
            {
                lines.get().append(threadId, level, text);

                if (flush)
                {
                    flush(threadId);
                }

            };

            exec.execute(task);
        } finally
        {
            lock.unlock();
        }
    }

    private void waitCompletion(final long threadId) throws InterruptedException
    {
        if (threadId == FLUSH_ALL)
        {
            exec.awaitCompletion();
        } else
        {
            exec.awaitThreadCompletion(threadId);
        }
    }

    /**
     * The options for the current status of this ConsoleIO.
     */
    @SuppressWarnings("PackageVisibleInnerClass")
    static enum Status
    {
        OPEN, CLOSING, CLOSED
    }

    /**
     * Used to store a single appended text entry.
     */
    private static final class Line
    {
        private final DisplayDebugLevel level;

        private boolean terminated;

        private final StringBuilder text;

        /**
         * Only accessible by the enclosing class.
         *
         * @param level Debug level of text.
         * @param text  to store
         */
        private Line(final DisplayDebugLevel level, final String text)
        {
            this.level = level;
            this.text = new StringBuilder();
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
        private final ConcurrentMap<Long, Line> lastUnterminated;

        private final Lock lock = new ReentrantLock(false);

        private final ConcurrentMap<Long, Queue<Line>> queues;

        /**
         * Only accessible by the enclosing class.
         */
        private Lines()
        {
            queues = new ConcurrentHashMap<>();
            lastUnterminated = new ConcurrentHashMap<>();
        }

        /**
         * Append the text by either adding a new line, or appending it to a
         * previously added, unterminated line of the same 'level'.
         *
         * @param threadID Id of the thread associated with this {@code text}.
         * @param level    debug display level.
         * @param text     to be stored.
         *
         * @return {@code true} if successful, {@code false} otherwise.
         */
        public boolean append(final long threadID, final DisplayDebugLevel level, final String text)
        {
            boolean rtn = false;
            boolean done = false;

            lock.lock();

            try
            {
                if (level != null && text != null)
                {
                    final Line lu;

                    if ((lu = lastUnterminated.get(threadID)) != null)
                    {
                        if (lu.getLevel() == level)
                        {
                            rtn = lu.append(text);

                            if (lu.isTerminated())
                            {
                                lastUnterminated.remove(threadID);
                            }

                            done = true;
                        } else
                        {
                            rtn = lu.append("\n");
                            lastUnterminated.remove(threadID);
                        }
                    }

                    if (!done)
                    {
                        final Line line = new Line(level, text);
                        rtn = add(threadID, line);

                        if (!line.isTerminated())
                        {
                            lastUnterminated.put(threadID, line);
                        }
                    }
                }
            } finally
            {
                lock.unlock();
            }

            return rtn;
        }

        /**
         * Removes all of the lines currently being stored.
         */
        public void clear()
        {
            queues.clear();
        }

        /**
         * Get all of the lines currently being stored for the requested
         * {@code thread}.
         * <p>
         * If there is an unterminated line, it will be terminate, and cleared.
         *
         * @param threadID Id of the thread.
         *
         * @return a new list of terminated lines.
         */
        public List<Line> getLines(final long threadID)
        {
            final List<Line> rtn = new ArrayList<>();
            final Line lu;

            lock.lock();

            try
            {
                if ((lu = lastUnterminated.get(threadID)) != null)
                {
                    lu.append("\n");
                    lastUnterminated.remove(threadID);
                }

                final Queue<Line> queue;

                if ((queue = queues.get(threadID)) != null)
                {
                    queue.forEach(line ->
                    {
                        String[] textArr = line.getText().replace("\n", " \n").split("\n");

                        if (textArr.length > 1)
                        {
                            for (String text : textArr)
                            {
                                final Line line2 = new Line(line.getLevel(), text.stripTrailing());
                                line2.append("\n");
                                rtn.add(line2);
                            }
                        } else
                        {
                            rtn.add(line);
                        }
                    });
                }
            } finally
            {
                lock.unlock();
            }

            return Collections.unmodifiableList(rtn);
        }

        /**
         * Returns a text string representation of all the lines currently being
         * stored for the requested {@code thread}.
         * <p>
         * If there is an unterminated line, it will be terminate, and
         * cleared.
         *
         * @param threadId Id of the thread.
         *
         * @return a text string representation of all the lines currently
         *         stored.
         */
        public String getText(final long threadId)
        {
            String rtn = "";

            lock.lock();

            try
            {
                if (!queues.isEmpty())
                {
                    final StringBuilder sb = new StringBuilder();
                    final Line lu;

                    if ((lu = lastUnterminated.get(threadId)) != null)
                    {
                        lu.append("\n");
                        lastUnterminated.remove(threadId);
                    }

                    final Queue<Line> queue;

                    if ((queue = queues.get(threadId)) != null)
                    {
                        queue.forEach(line -> sb.append(line.getText()));
                    }

                    rtn = sb.toString();
                }
            } finally
            {
                lock.unlock();
            }

            return rtn;
        }

        /**
         * Provide unmodifiable set of threadIds.
         *
         * @return set of threadIds.
         */
        public Set<Long> getThreadIds()
        {
            return Collections.unmodifiableSet(queues.keySet());
        }

        /**
         * @return {@code true} if no lines are being stored.
         */
        public boolean isEmpty()
        {
            return queues.isEmpty();
        }

        /**
         * @param threadId Id of the thread.
         *
         * @return {@code true} if no lines are being stored.
         */
        public boolean isEmpty(final long threadId)
        {
            return queues.get(threadId).isEmpty();
        }

        /**
         * @return the number of lines being stored.
         */
        public int size()
        {
            return queues.size();
        }

        /**
         * @param threadId Id of the thread.
         *
         * @return the number of lines being stored.
         */
        public int size(final long threadId)
        {
            return queues.get(threadId).size();
        }

        @Override
        public String toString()
        {
            return "Lines{" + "lines=" + queues + '}';
        }

        /**
         * Inserts the new Line into the thread's queue, returning {@code true}
         * upon success.
         *
         * @param threadId id of thread.
         * @param line     to be added.
         *
         * @return {@code true} (as specified by {@link Collection#add})
         *
         * @throws NullPointerException if the specified {@code line} is null.
         */
        private boolean add(final long threadId, final Line line)
        {
            Queue<Line> queue;
            Objects.requireNonNull(line, "'line' is null");

            lock.lock();

            try
            {
                if ((queue = queues.get(threadId)) == null)
                {
                    queue = new ConcurrentLinkedQueue<>();
                    queues.put(threadId, queue);
                }

                return queue.add(line);
            } finally
            {
                lock.unlock();
            }
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
         * @throws FileNotFoundException    If the given string does not denote
         *                                  an
         *                                  existing, writable regular file and a
         *                                  new regular file of that name cannot be
         *                                  created, or if some other error occurs
         *                                  while opening or creating the file.
         * @throws NullPointerException     if {@code filename} is
         *                                  {@code null}.
         * @throws IllegalArgumentException if {@code filename}
         *                                  {@link String#isBlank() isBlank()}.
         */
        private WriterInstance(final String filename)
                throws FileNotFoundException, NullPointerException, IllegalArgumentException
        {
            if (requireNonNull(filename).isBlank())
            {
                throw new IllegalArgumentException(filename);
            }

            this.filename = filename;
            printWriter = new PrintWriter(filename);
            status.set(OPEN);
        }

        /**
         * Will only be instantiated by parent class.
         *
         * @param ident  String to identify this Writer in internal data store.
         * @param writer where to write output
         *
         * @throws NullPointerException     if either {@code indent} or
         *                                  {@code writer} are {@code null}.
         * @throws IllegalArgumentException if {@code indent}
         *                                  {@linkplain String#isBlank() isBlank()}.
         */
        private WriterInstance(final String ident, final Writer writer)
                throws NullPointerException, IllegalArgumentException
        {
            if (requireNonNull(ident).isBlank())
            {
                throw new IllegalArgumentException(ident);
            }

            filename = ident;
            printWriter = new PrintWriter(requireNonNull(writer));
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
