/*
 *  File Name:    ThreadExecutorService.java
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

import java.util.concurrent.TimeUnit;

/**
 * Extends {@code ExtendedExecutorService} adding methods related to task
 * completion.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 3.0.1
 * @version 3.0.1
 */
public interface ThreadExecutorService extends ExtendedExecutorService
{
    /**
     * Blocks until all tasks (submitted by this Thread) have been completed, or
     * the current thread is interrupted, whichever happens first.If there are
     * no tasks on entry, returns immediately.<p>
     * BLOCK-UNTIL: isCompleted(), or interrupted.
     *
     * @param threadId id of thread to wait on.
     *
     * @throws InterruptedException if interrupted while waiting.
     */
    void awaitThreadCompletion(long threadId) throws InterruptedException;

    /**
     * Blocks until all tasks (submitted by this Thread) have been completed, or
     * the timeout occurs, or the current thread is interrupted, whichever
     * happens first. If there are no tasks on entry, returns immediately.
     * <p>
     * BLOCK-UNTIL: isCompleted(), timeout elapses, or interrupted.
     *
     * @param threadId id of thread to wait on.
     * @param timeout  the maximum time to wait.
     * @param unit     the time unit of the timeout argument.
     *
     * @return {@code true} if tasks completed, {@code false} if timed-out.
     *
     * @throws InterruptedException if interrupted while waiting.
     */
    boolean awaitThreadCompletion(long threadId, long timeout, TimeUnit unit) throws InterruptedException;

    /**
     * Initiates an orderly shutdown in which previously submitted tasks are
     * executed, but no new tasks will be accepted. This method waits until all
     * tasks have completed execution and the executor has terminated.
     * <p>
     * If interrupted while waiting, this method stops all executing tasks as
     * if by invoking {@link #shutdownNow()}. It then continues to wait until
     * all actively executing tasks have completed. Tasks that were awaiting
     * execution are not executed. The interrupt status will be re-asserted
     * before this method returns.
     * <p>
     * If already terminated, invoking this method has no effect.
     *
     * @implSpec
     * The default implementation invokes {@code shutdown()} and waits for tasks
     * to complete execution with {@code awaitTermination}.
     *
     * @throws SecurityException if a security manager exists and
     *                           shutting down this ExecutorService may manipulate
     *                           threads that the caller is not permitted to modify
     *                           because it does not hold {@link
     *         java.lang.RuntimePermission}{@code ("modifyThread")},
     *                           or the security manager's {@code checkAccess} method
     *                           denies access.
     * @since 19
     */
    @Override
    default void close()
    {
        boolean terminated = isTerminated();

        if (!terminated)
        {
            shutdown();
            boolean interrupted = false;

            while (!terminated)
            {
                try
                {
                    terminated = awaitTermination(1L, TimeUnit.DAYS);
                } catch (InterruptedException e)
                {
                    if (!interrupted)
                    {
                        shutdownNow();
                        interrupted = true;
                    }
                }
            }
            
            if (interrupted)
            {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * All tasks (submitted by this Thread) are completed.
     *
     * @return {@code true} if all tasks are completed.
     */
    boolean isThreadCompleted();
}
