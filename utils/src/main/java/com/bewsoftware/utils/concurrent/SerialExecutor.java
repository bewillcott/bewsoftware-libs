/*
 *  File Name:    SerialExecutor.java
 *  Project Name: bewsoftware-utils
 *
 *  Copyright (c) 2025 Bradley Willcott
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

package com.bewsoftware.utils.concurrent;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static com.bewsoftware.utils.concurrent.StatusLevel.*;

/**
 * An {@code Executor} that processes tasks one-at-a-time, in the order that
 * they are submitted.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 3.0.1
 * @version 3.0.1
 */
public class SerialExecutor implements ExtendedExecutorService
{
    private final AtomicReference<Runnable> active = new AtomicReference<>();

    /**
     * Tracks the number of active tasks.
     */
    private final TaskTracker activeTasks = new TaskTracker();

    private final Executor executor = new ThreadPoolExecutor(
            1,
            1,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(),
            (final Runnable command) ->
    {
        return new Thread(command);
    });

    private final ReentrantLock serialLock = new ReentrantLock();

    /**
     * Wait condition to support awaitCompletion.
     */
    private final Condition completion = serialLock.newCondition();

    private volatile StatusLevel status;

    private final Queue<Runnable> tasks = new ArrayDeque<>();

    /**
     * Wait condition to support awaitTermination.
     */
    private final Condition termination = serialLock.newCondition();

    SerialExecutor()
    {
        status = RUNNING;
    }

    @Override
    public void awaitCompletion() throws InterruptedException
    {
        if (isCompleted())
        {
            return;
        }

        serialLock.lock();

        try
        {
            while (!isCompleted())
            {
                completion.await();
            }
        } finally
        {
            serialLock.unlock();
        }
    }

    @Override
    public boolean awaitCompletion(final long timeout, final TimeUnit unit) throws InterruptedException
    {
        if (isCompleted())
        {
            return true;
        }

        long nanos = unit.toNanos(timeout);
        serialLock.lock();

        try
        {
            while (!isCompleted())
            {
                if (nanos <= 0L)
                {
                    return false;
                }

                nanos = completion.awaitNanos(nanos);
            }

            return true;
        } finally
        {
            serialLock.unlock();
        }
    }

    /**
     * Blocks until all tasks have completed execution after a shutdown
     * request, or the timeout occurs, or the current thread is
     * interrupted, whichever happens first.
     *
     * @param timeout the maximum time to wait
     * @param unit    the time unit of the timeout argument
     *
     * @return {@code true} if this executor terminated and
     *         {@code false} if the timeout elapsed before termination
     *
     * @throws InterruptedException if interrupted while waiting
     */
    @Override
    public boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException
    {
        if (isTerminated())
        {
            return true;
        } else if (status != SHUTTING_DOWN)
        {
            return false;
        }

        long nanos = unit.toNanos(timeout);
        serialLock.lock();

        try
        {
            while (!isShutdown())
            {
                if (nanos <= 0L)
                {
                    return false;
                }

                nanos = termination.awaitNanos(nanos);
            }

            return isCompleted();
        } finally
        {
            serialLock.unlock();
        }
    }

    /**
     * Executes the given command at some time in the future. The command
     * may execute in a new thread, in a pooled thread, or in the calling
     * thread, at the discretion of the {@code Executor} implementation.
     *
     * @param command the runnable task
     *
     * @throws RejectedExecutionException if this task cannot be
     *                                    accepted for execution
     * @throws NullPointerException       if command is null
     */
    @Override
    public void execute(final Runnable command)
    {
        final long threadId = Thread.currentThread().threadId();
        System.out.printf("SerialExecutor.execute(): threadId (%d), status (%s)%n", threadId, status);

        if (!(status == RUNNING || status == SHUTTING_DOWN))
        {
            return;
        }

        Objects.requireNonNull(command, "command is null.");
        activeTasks.incrementAndGet();

        tasks.add(() ->
        {
            System.out.printf("SerialExecutor.task: threadId (%d)%n", threadId);

            try
            {
                command.run();
            } finally
            {
                activeTasks.decrementAndGet();
                scheduleNext();
            }

            System.out.println("SerialExecutor.task: completed.");
        });

        if (active.get() == null)
        {
            scheduleNext();
        }
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException Not supported.
     */
    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
            throws InterruptedException
    {
        throw new UnsupportedOperationException("Not supported.");
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException Not supported.
     */
    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> clctn, long l, TimeUnit tu)
            throws InterruptedException
    {
        throw new UnsupportedOperationException("Not supported.");
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException Not supported.
     */
    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks)
            throws InterruptedException, ExecutionException
    {
        throw new UnsupportedOperationException("Not supported.");
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException Not supported.
     */
    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> clctn, long l, TimeUnit tu)
            throws InterruptedException, ExecutionException, TimeoutException
    {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean isCompleted()
    {
        return activeTasks.allTasksCompleted();
    }

    /**
     * Returns {@code true} if this executor has been shut down.
     *
     * @return {@code true} if this executor has been shut down
     */
    @Override
    public boolean isShutdown()
    {
        return status == SHUTDOWN || status == FORCED_SHUTDOWN;
    }

    /**
     * Returns {@code true} if all tasks have completed following shut down.
     * Note that {@code isTerminated} is never {@code true} unless
     * either {@code shutdown} or {@code shutdownNow} was called first.
     *
     * @return {@code true} if all tasks have completed following shut down
     */
    @Override
    public boolean isTerminated()
    {
        return isShutdown() && isCompleted();
    }

    /**
     * Initiates an orderly shutdown in which previously submitted
     * tasks are executed, but no new tasks will be accepted.
     * Invocation has no additional effect if already shut down.
     * <p>
     * This method does not wait for previously submitted tasks to
     * complete execution. Use {@link #awaitTermination awaitTermination}
     * to do that.
     *
     * @throws SecurityException if a security manager exists and
     *                           shutting down this ExecutorService may manipulate
     *                           threads that the caller is not permitted to modify
     *                           because it does not hold {@link
     *         java.lang.RuntimePermission}{@code ("modifyThread")},
     *                           or the security manager's {@code checkAccess} method
     *                           denies access.
     */
    @Override
    public void shutdown()
    {
        final long threadId = Thread.currentThread().threadId();
        System.out.printf("SerialExecutor.shutdown(): threadId (%d)%n", threadId);

        if (status == RUNNING)
        {
            System.out.println("SerialExecutor.shutdown()");
            serialLock.lock();

            try
            {
                status = SHUTTING_DOWN;

                if (isCompleted())
                {
                    System.out.println("SerialExecutor.shutdown(): isComplete()");
                    status = SHUTDOWN;
                    termination.signalAll();
                }
            } finally
            {
                serialLock.unlock();
            }
            System.out.println("SerialExecutor.shutdown(): status = " + status);
        }
    }

    /**
     * Attempts to stop all actively executing tasks, halts the
     * processing of waiting tasks, and returns a list of the tasks
     * that were awaiting execution.
     * <p>
     * This method does not wait for actively executing tasks to
     * terminate. Use {@link #awaitTermination awaitTermination} to
     * do that.
     * <p>
     * There are no guarantees beyond best-effort attempts to stop
     * processing actively executing tasks. For example, typical
     * implementations will cancel via {@link Thread#interrupt}, so any
     * task that fails to respond to interrupts may never terminate.
     *
     * @return list of tasks that never commenced execution
     *
     * @throws SecurityException if a security manager exists and
     *                           shutting down this ExecutorService may manipulate
     *                           threads that the caller is not permitted to modify
     *                           because it does not hold {@link
     *         java.lang.RuntimePermission}{@code ("modifyThread")},
     *                           or the security manager's {@code checkAccess} method
     *                           denies access.
     */
    @Override
    public List<Runnable> shutdownNow()
    {
        List<Runnable> rtnList = null;
        if (status == RUNNING)
        {
            serialLock.lock();

            try
            {
                status = FORCING_SHUTDOWN;
                rtnList = new ArrayList<>(0);

                if (!tasks.isEmpty())
                {
                    rtnList.addAll(tasks);
                    tasks.clear();
                }

                status = FORCED_SHUTDOWN;
                termination.signalAll();
            } finally
            {
                serialLock.unlock();
            }
        }
        return rtnList;
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException Not supported.
     */
    @Override
    public <T> Future<T> submit(Callable<T> clbl)
    {
        throw new UnsupportedOperationException("Not supported.");
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException Not supported.
     */
    @Override
    public <T> Future<T> submit(Runnable r, T t)
    {
        throw new UnsupportedOperationException("Not supported.");
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException Not supported.
     */
    @Override
    public Future<?> submit(Runnable r)
    {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public String toString()
    {
        return new StringBuffer().append("SerialExecutor{\n").append("    status = ").append(status).append('\n').append("    tasks  =\n").append(tasks).append('\n').append("}\n").toString();
    }

    /**
     * Returns a {@code RunnableFuture} for the given runnable and default
     * value.
     *
     * @param runnable the runnable task being wrapped
     * @param value    the default value for the returned future
     * @param <T>      the type of the given value
     *
     * @return a {@code RunnableFuture} which, when run, will run the
     *         underlying runnable and which, as a {@code Future}, will yield
     *         the given value as its result and provide for cancellation of
     *         the underlying task
     *
     * @since 1.6
     */
    protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value)
    {
        return new FutureTask<>(runnable, value);
    }

    /**
     * Returns a {@code RunnableFuture} for the given callable task.
     *
     * @param callable the callable task being wrapped
     * @param <T>      the type of the callable's result
     *
     * @return a {@code RunnableFuture} which, when run, will call the
     *         underlying callable and which, as a {@code Future}, will yield
     *         the callable's result as its result and provide for
     *         cancellation of the underlying task
     *
     * @since 1.6
     */
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable)
    {
        return new FutureTask<>(callable);
    }

    protected void scheduleNext()
    {
        final long threadId = Thread.currentThread().threadId();
        System.out.printf("SerialExecutor.scheduleNext(): status (%s), threadId (%d)%n", status, threadId);

        if (status == RUNNING || status == SHUTTING_DOWN)
        {
            serialLock.lock();

            try
            {
                final Runnable task = tasks.poll();
                active.set(task);

                System.out.printf("SerialExecutor.scheduleNext(): task == null (%b)%n", (task == null));
                if (task != null)
                {
                    executor.execute(task);
                } else if (status == SHUTTING_DOWN)
                {
                    status = SHUTDOWN;
                    termination.signalAll();
                } else
                {
                    completion.signalAll();
                }
            } finally
            {
                serialLock.unlock();
            }
        } else
        {
            active.set(null);
        }

        System.out.println("SerialExecutor.scheduleNext(): completed.");
    }

}
