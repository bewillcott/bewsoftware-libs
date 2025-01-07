/*
 *  File Name:    PerThreadPoolExecutor.java
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

import com.bewsoftware.utils.Ref;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.bewsoftware.utils.concurrent.StatusLevel.*;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * A specialized {@code Executor} that maintains a pool of
 * {@linkplain SerialExecutor}s that are individually paired with and dedicated
 * to, an individual thread. Each such thread being one that has called:
 * {@linkplain #execute(java.lang.Runnable) execute(task)}.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 3.0.1
 * @version 3.0.1
 */
public class PerThreadPoolExecutor implements ThreadExecutorService
{
    /**
     * Tracks the number of active tasks.
     */
    private final TaskTracker activeTasks = new TaskTracker();

    /**
     * Key (Long) = threadId.
     */
//    private final ConcurrentMap<Long, ThreadResources> execs = new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, SerialExecutor> execs = new ConcurrentHashMap<>();

    /**
     * Lock held on access to workers set and related bookkeeping.
     * While we could use a concurrent set of some sort, it turns out
     * to be generally preferable to use a lock. Among the reasons is
     * that this serializes interruptIdleWorkers, which avoids
     * unnecessary interrupt storms, especially during shutdown.
     * Otherwise exiting threads would concurrently interrupt those
     * that have not yet interrupted. It also simplifies some of the
     * associated statistics bookkeeping of largestPoolSize etc. We
     * also hold mainLock on shutdown and shutdownNow, for the sake of
     * ensuring workers set is stable while separately checking
     * permission to interrupt and actually interrupting.
     */
    private final ReentrantLock mainLock = new ReentrantLock();

    /**
     * Wait condition to support awaitCompletion.
     */
    private final Condition completion = mainLock.newCondition();

    private volatile StatusLevel status;

    /**
     * Wait condition to support awaitTermination.
     */
    private final Condition termination = mainLock.newCondition();

    /**
     * Default constructor.
     */
    public PerThreadPoolExecutor()
    {
        status = RUNNING;
    }

    private static <T> void cancelAll(final ArrayList<Future<T>> futures)
    {
        cancelAll(futures, 0);
    }

    /**
     * Cancels all futures with index at least j.
     */
    private static <T> void cancelAll(final ArrayList<Future<T>> futures, final int j)
    {
        for (int i = j, size = futures.size(); i < size; i++)
        {
            futures.get(i).cancel(true);
        }
    }

    @Override
    public void awaitCompletion() throws InterruptedException
    {
        if (isCompleted())
        {
            return;
        }

        mainLock.lock();

        try
        {
            while (!isCompleted())
            {
                completion.await();
            }

            System.out.printf("PerThreadPoolExecutor.awaitCompletion(): isCompleted (%b)%n", isCompleted());

        } finally
        {
            mainLock.unlock();
        }
    }

    @Override
    public boolean awaitCompletion(final long timeout, final TimeUnit unit)
            throws InterruptedException
    {
        if (isCompleted())
        {
            return true;
        }

        long nanos = unit.toNanos(timeout);
        mainLock.lock();

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

            System.out.printf("PerThreadPoolExecutor.awaitCompletion(...): isCompleted (%b)%n", isCompleted());
            return true;
        } finally
        {
            mainLock.unlock();
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
    public boolean awaitTermination(final long timeout, final TimeUnit unit)
            throws InterruptedException
    {
        if (isTerminated())
        {
            return true;
        } else if (status != SHUTTING_DOWN)
        {
            return false;
        }

        long nanos = unit.toNanos(timeout);
        mainLock.lock();

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

            return activeTasks.allTasksCompleted();
        } finally
        {
            mainLock.unlock();
        }
    }

    @Override
    public void awaitThreadCompletion(final long threadId) throws InterruptedException
    {
        final SerialExecutor exec = execs.get(threadId);

        if (exec.isCompleted())
        {
            return;
        }

        while (!exec.isCompleted())
        {
            exec.awaitCompletion();
        }
        System.out.printf("PerThreadPoolExecutor.awaitThreadCompletion(): isCompleted (%b)%n", exec.isCompleted());
    }

    @Override
    public boolean awaitThreadCompletion(final long threadId, long timeout, TimeUnit unit) throws InterruptedException
    {
        final SerialExecutor exec = execs.get(threadId);
        boolean rtn = false;

        if (exec.isCompleted())
        {
            return true;
        }

        while (!exec.isCompleted())
        {
            rtn = exec.awaitCompletion(timeout, unit);
        }

        System.out.printf("PerThreadPoolExecutor.awaitThreadCompletion(...): isCompleted (%b)%n", exec.isCompleted());
        return rtn;
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
        System.out.printf("PerThreadPoolExecutor.execute(): status (%s)%n", status);

        if (status != RUNNING)
        {
            return;
        }

        Objects.requireNonNull(command, "command is null.");
        final long threadId = Thread.currentThread().threadId();
        System.out.printf("PerThreadPoolExecutor.execute(): threadId (%d)%n", threadId);
        SerialExecutor exec;

        if ((exec = execs.get(threadId)) == null)
        {
            exec = new SerialExecutor();
            execs.put(threadId, exec);
        }

        activeTasks.incrementAndGet();
        System.out.printf("PerThreadPoolExecutor.execute(): activeTasks (%d)%n", activeTasks.get());

        exec.execute(() ->
        {
            final long taskThreadId = Thread.currentThread().threadId();
            System.out.printf("PerThreadPoolExecutor.exec.execute(task): taskThreadId (%d)%n", taskThreadId);

            try
            {
                command.run();
            } finally
            {
                taskCompleted();
            }

            System.out.println("PerThreadPoolExecutor.exec.execute(task): completed.");
        });
    }

    /**
     * Executes the given tasks, returning a list of Futures holding
     * their status and results when all complete.
     * {@link Future#isDone} is {@code true} for each
     * element of the returned list.
     * Note that a <em>completed</em> task could have
     * terminated either normally or by throwing an exception.
     * The results of this method are undefined if the given
     * collection is modified while this operation is in progress.
     *
     * @param tasks the collection of tasks
     * @param <T>   the type of the values returned from the tasks
     *
     * @return a list of Futures representing the tasks, in the same
     *         sequential order as produced by the iterator for the
     *         given task list, each of which has completed
     *
     * @throws InterruptedException       if interrupted while waiting, in
     *                                    which case unfinished tasks are cancelled
     * @throws NullPointerException       if tasks or any of its elements are
     *                                    {@code null}
     * @throws RejectedExecutionException if any task cannot be
     *                                    scheduled for execution
     */
    @Override
    public <T> List<Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks) throws InterruptedException
    {
        final ArrayList<Future<T>> futures = new ArrayList<>(
                Objects.requireNonNull(tasks, "'tasks' is null.").size()
        );

        try
        {
            tasks.stream().map(task -> newTaskFor(task)).map(future ->
            {
                futures.add(future);
                return future;
            }).forEachOrdered(future ->
            {
                execute(future);
            });

            for (int i = 0, size = futures.size(); i < size; i++)
            {
                final Future<T> f = futures.get(i);

                if (!f.isDone())
                {
                    try
                    {
                        f.get();
                    } catch (CancellationException | ExecutionException ignore)
                    {
                    }
                }
            }

            return futures;
        } catch (InterruptedException ex)
        {
            cancelAll(futures);
            throw ex;
        }
    }

    /**
     * Executes the given tasks, returning a list of Futures holding
     * their status and results
     * when all complete or the timeout expires, whichever happens first.
     * {@link Future#isDone} is {@code true} for each
     * element of the returned list.
     * Upon return, tasks that have not completed are cancelled.
     * Note that a <em>completed</em> task could have
     * terminated either normally or by throwing an exception.
     * The results of this method are undefined if the given
     * collection is modified while this operation is in progress.
     *
     * @param tasks   the collection of tasks
     * @param timeout the maximum time to wait
     * @param unit    the time unit of the timeout argument
     * @param <T>     the type of the values returned from the tasks
     *
     * @return a list of Futures representing the tasks, in the same
     *         sequential order as produced by the iterator for the
     *         given task list. If the operation did not time out,
     *         each task will have completed. If it did time out, some
     *         of these tasks will not have completed.
     *
     * @throws InterruptedException       if interrupted while waiting, in
     *                                    which case unfinished tasks are cancelled
     * @throws NullPointerException       if tasks, any of its elements, or
     *                                    unit are {@code null}
     * @throws RejectedExecutionException if any task cannot be scheduled
     *                                    for execution
     */
    @Override
    public <T> List<Future<T>> invokeAll(
            final Collection<? extends Callable<T>> tasks,
            final long timeout,
            final TimeUnit unit
    ) throws InterruptedException
    {
        final long nanos = unit.toNanos(timeout);
        final long deadline = System.nanoTime() + nanos;

        final ArrayList<Future<T>> futures = new ArrayList<>(
                Objects.requireNonNull(tasks, "'tasks' is null.").size()
        );

        int j = 0;

        timedOut:
        try
        {
            tasks.forEach(task ->
            {
                futures.add(newTaskFor(task));
            });

            final int size = futures.size();

            // Interleave time checks and calls to execute in case
            // executor doesn't have any/much parallelism.
            for (int i = 0; i < size; i++)
            {
                if (((i == 0) ? nanos : deadline - System.nanoTime()) <= 0L)
                {
                    break timedOut;
                }

                execute((Runnable) futures.get(i));
            }

            for (; j < size; j++)
            {
                Future<T> future = futures.get(j);

                if (!future.isDone())
                {
                    try
                    {
                        future.get(deadline - System.nanoTime(), NANOSECONDS);
                    } catch (CancellationException | ExecutionException ignore)
                    {
                    } catch (TimeoutException timedOut)
                    {
                        break timedOut;
                    }
                }
            }

            return futures;
        } catch (InterruptedException ex)
        {
            cancelAll(futures);
            throw ex;
        }
        // Timed out before all the tasks could be completed; cancel remaining
        cancelAll(futures, j);

        return futures;
    }

    /**
     * Executes the given tasks, returning the result
     * of one that has completed successfully (i.e., without throwing
     * an exception), if any do. Upon normal or exceptional return,
     * tasks that have not completed are cancelled.
     * The results of this method are undefined if the given
     * collection is modified while this operation is in progress.
     *
     * @param tasks the collection of tasks
     * @param <T>   the type of the values returned from the tasks
     *
     * @return the result returned by one of the tasks
     *
     * @throws InterruptedException       if interrupted while waiting
     * @throws NullPointerException       if tasks or any element task
     *                                    subject to execution is {@code null}
     * @throws IllegalArgumentException   if tasks is empty
     * @throws ExecutionException         if no task successfully completes
     * @throws RejectedExecutionException if tasks cannot be scheduled
     *                                    for execution
     */
    @Override
    public <T> T invokeAny(final Collection<? extends Callable<T>> tasks)
            throws InterruptedException, ExecutionException
    {
        try
        {
            return doInvokeAny(tasks, false, 0);
        } catch (TimeoutException cannotHappen)
        {
            assert false;
            return null;
        }
    }

    /**
     * Executes the given tasks, returning the result
     * of one that has completed successfully (i.e., without throwing
     * an exception), if any do before the given timeout elapses.
     * Upon normal or exceptional return, tasks that have not
     * completed are cancelled.
     * The results of this method are undefined if the given
     * collection is modified while this operation is in progress.
     *
     * @param tasks   the collection of tasks
     * @param timeout the maximum time to wait
     * @param unit    the time unit of the timeout argument
     * @param <T>     the type of the values returned from the tasks
     *
     * @return the result returned by one of the tasks
     *
     * @throws InterruptedException       if interrupted while waiting
     * @throws NullPointerException       if tasks, or unit, or any element
     *                                    task subject to execution is {@code null}
     * @throws TimeoutException           if the given timeout elapses before
     *                                    any task successfully completes
     * @throws ExecutionException         if no task successfully completes
     * @throws RejectedExecutionException if tasks cannot be scheduled
     *                                    for execution
     */
    @Override
    public <T> T invokeAny(
            final Collection<? extends Callable<T>> tasks,
            final long timeout,
            final TimeUnit unit
    ) throws InterruptedException, ExecutionException, TimeoutException
    {
        return doInvokeAny(tasks, true, unit.toNanos(timeout));
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

    @Override
    public boolean isThreadCompleted()
    {
        final long threadId = Thread.currentThread().threadId();
        final SerialExecutor exec = execs.get(threadId);

        return exec.isCompleted();
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
        if (status == RUNNING)
        {
            mainLock.lock();

            try
            {
                status = SHUTTING_DOWN;
                final Ref<RuntimeException> ref = Ref.val();

                final Thread thread = new Thread(() ->
                {
                    execs.values().stream().takeWhile((value) -> ref.isEmpty()).forEachOrdered((exec) ->
                    {
                        if (!exec.isShutdown())
                        {
                            exec.shutdown();

                            while (!exec.isShutdown())
                            {
                                try
                                {
                                    //                                termination.await();
                                    System.out.println("PerThreadPoolExecutor.shutdown(): exec.awaitTermination(1, SECONDS); >>");
                                    boolean awaitTermination = exec.awaitTermination(1, SECONDS);
                                    System.out.println("<< PerThreadPoolExecutor.shutdown(): exec.awaitTermination(1, SECONDS);");
//                                    System.out.println("PerThreadPoolExecutor.shutdown(): exec.awaitCompletion(1, SECONDS); >>");
//                                    boolean awaitTermination = exec.awaitCompletion(1, SECONDS);
//                                    System.out.println("<< PerThreadPoolExecutor.shutdown(): exec.awaitCompletion(1, SECONDS);");
                                } catch (InterruptedException ex)
                                {
                                    ref.val = new RuntimeException("exec.awaitTermination(...)", ex);
                                }
                            }
                        }
                    });

                    if (ref.isPresent())
                    {
                        throw ref.val;
                    }
                });

                thread.start();
                thread.join(SECONDS.toMillis(5));

                try
                {
                    thread.join(SECONDS.toMillis(1));
                } catch (InterruptedException ex)
                {
                    Logger.getLogger(PerThreadPoolExecutor.class.getName()).log(Level.SEVERE, null, ex);
                }
//                awaitCompletion();
                System.out.printf("PerThreadPoolExecutor.shutdown(): assert activeTasks.allTasksCompleted() == %b%n",
                        activeTasks.allTasksCompleted());

                assert activeTasks.allTasksCompleted();

                execs.clear();
                status = SHUTDOWN;
                termination.signalAll();
            } catch (InterruptedException ignore)
            {

            } finally
            {
                mainLock.unlock();
            }
        }

        System.out.println("PerThreadPoolExecutor.shutdown(): exiting");
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
    public synchronized List<Runnable> shutdownNow()
    {
        final Ref<List<Runnable>> rtnList = Ref.val();

        if (status == RUNNING)
        {
            mainLock.lock();

            try
            {
                status = FORCING_SHUTDOWN;
                rtnList.val = new ArrayList<>(0);

                execs.values().forEach((exec) ->
                {
                    if (!exec.isShutdown())
                    {
                        List<Runnable> remaining;

                        if ((remaining = exec.shutdownNow()) != null && !remaining.isEmpty())
                        {
                            rtnList.val.addAll(remaining);
                        }
                    }
                });

                assert activeTasks.allTasksCompleted() == rtnList.val.isEmpty();

                execs.clear();
                status = FORCED_SHUTDOWN;
                termination.signalAll();
            } finally
            {
                mainLock.unlock();
            }
        }

        return rtnList.val;
    }

    /**
     * Submits a value-returning task for execution and returns a
     * Future representing the pending results of the task. The
     * Future's {@code get} method will return the task's result upon
     * successful completion.
     * <p>
     * If you would like to immediately block waiting
     * for a task, you can use constructions of the form
     * {@code result = exec.submit(aCallable).get();}
     * <p>
     * Note: The {@link Executors} class includes a set of methods
     * that can convert some other common closure-like objects,
     * for example, {@link java.security.PrivilegedAction} to
     * {@link Callable} form so they can be submitted.
     *
     * @param task the task to submit
     * @param <T>  the type of the task's result
     *
     * @return a Future representing pending completion of the task
     *
     * @throws RejectedExecutionException if the task cannot be
     *                                    scheduled for execution
     * @throws NullPointerException       if the task is null
     */
    @Override
    public <T> Future<T> submit(final Callable<T> task)
    {
        final RunnableFuture<T> ftask = newTaskFor(
                Objects.requireNonNull(task, "'task' is null.")
        );

        execute(ftask);

        return ftask;
    }

    /**
     * Submits a Runnable task for execution and returns a Future
     * representing that task. The Future's {@code get} method will
     * return the given result upon successful completion.
     *
     * @param task   the task to submit
     * @param result the result to return
     * @param <T>    the type of the result
     *
     * @return a Future representing pending completion of the task
     *
     * @throws RejectedExecutionException if the task cannot be
     *                                    scheduled for execution
     * @throws NullPointerException       if the task is null
     */
    @Override
    public <T> Future<T> submit(final Runnable task, final T result)
    {
        final RunnableFuture<T> ftask = newTaskFor(
                Objects.requireNonNull(task, "'task' is null."),
                result
        );

        execute(ftask);

        return ftask;
    }

    /**
     * Submits a Runnable task for execution and returns a Future
     * representing that task. The Future's {@code get} method will
     * return {@code null} upon <em>successful</em> completion.
     *
     * @param task the task to submit
     *
     * @return a Future representing pending completion of the task
     *
     * @throws RejectedExecutionException if the task cannot be
     *                                    scheduled for execution
     * @throws NullPointerException       if the task is null
     */
    @Override
    public Future<?> submit(final Runnable task)
    {
        final RunnableFuture<Void> ftask = newTaskFor(
                Objects.requireNonNull(task, "'task' is null."),
                null
        );

        execute(ftask);

        return ftask;
    }

    @Override
    public synchronized String toString()
    {
        return new StringBuffer()
                .append("PerThreadPoolExecutor{\n")
                .append("    status = ").append(status).append('\n')
                .append("    execs  =\n").append(execs).append('\n')
                .append("}\n")
                .toString();
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
    protected <T> RunnableFuture<T> newTaskFor(final Runnable runnable, final T value)
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
    protected <T> RunnableFuture<T> newTaskFor(final Callable<T> callable)
    {
        return new FutureTask<>(callable);
    }

    /**
     * The main mechanics of invokeAny.
     *
     * @Note
     * Code copied from {@linkplain AbstractExecutorService}.
     */
    private <T> T doInvokeAny(
            final Collection<? extends Callable<T>> tasks,
            final boolean timed,
            final long nanos
    ) throws InterruptedException, ExecutionException, TimeoutException
    {
        int ntasks = Objects.requireNonNull(tasks, "'tasks' is null.").size();
        long nanosLeft = nanos;

        if (ntasks == 0)
        {
            throw new IllegalArgumentException();
        }

        final ArrayList<Future<T>> futures = new ArrayList<>(ntasks);
        final ExecutorCompletionService<T> ecs = new ExecutorCompletionService<>(this);

        // For efficiency, especially in executors with limited
        // parallelism, check to see if previously submitted tasks are
        // done before submitting more of them. This interleaving
        // plus the exception mechanics account for messiness of main
        // loop.
        try
        {
            // Record exceptions so that if we fail to obtain any
            // result, we can throw the last exception we got.
            ExecutionException ee = null;
            final long deadline = timed ? System.nanoTime() + nanosLeft : 0L;
            Iterator<? extends Callable<T>> it = tasks.iterator();

            // Start one task for sure; the rest incrementally
            futures.add(ecs.submit(it.next()));
            --ntasks;
            int active = 1;

            for (;;)
            {
                Future<T> future = ecs.poll();

                if (future == null)
                {
                    if (ntasks > 0)
                    {
                        --ntasks;
                        futures.add(ecs.submit(it.next()));
                        ++active;
                    } else if (active == 0)
                    {
                        break;
                    } else if (timed)
                    {
                        future = ecs.poll(nanosLeft, NANOSECONDS);

                        if (future == null)
                        {
                            throw new TimeoutException();
                        }

                        nanosLeft = deadline - System.nanoTime();
                    } else
                    {
                        future = ecs.take();
                    }
                }

                if (future != null)
                {
                    --active;

                    try
                    {
                        return future.get();
                    } catch (ExecutionException eex)
                    {
                        ee = eex;
                    } catch (RuntimeException rex)
                    {
                        ee = new ExecutionException(rex);
                    }
                }
            }

            if (ee == null)
            {
                ee = new ExecutionException(null);
            }

            throw ee;

        } finally
        {
            cancelAll(futures);
        }
    }

    /**
     * Should the the last call in finally block of a task.
     */
    private void taskCompleted()
    {
        System.out.println("PerThreadPoolExecutor.taskCompleted()");
        mainLock.lock();

        try
        {
            activeTasks.decrementAndGet();

            if (activeTasks.allTasksCompleted())
            {
                completion.signalAll();
            }
        } finally
        {
            mainLock.unlock();
        }

        System.out.println("PerThreadPoolExecutor.taskCompleted(): completed.");
    }
}
