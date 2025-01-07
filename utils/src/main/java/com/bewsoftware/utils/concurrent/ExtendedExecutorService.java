/*
 *  File Name:    ExtendedExecutorService.java
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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Extends {@code ExecutorService} adding methods related to task completion.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 3.0.1
 * @version 3.0.1
 */
public interface ExtendedExecutorService extends ExecutorService
{
    /**
     * Blocks until all tasks have been completed, or the current thread is
     * interrupted, whichever happens first. If there are no tasks on entry,
     * returns immediately.
     * <p>
     * BLOCK-UNTIL: isCompleted(), or interrupted.
     *
     * @throws InterruptedException if interrupted while waiting.
     */
    void awaitCompletion() throws InterruptedException;

    /**
     * Blocks until all tasks have been completed, or the timeout occurs, or the
     * current thread is interrupted, whichever happens first. If there are no
     * tasks on entry, returns immediately.
     * <p>
     * BLOCK-UNTIL: isCompleted(), timeout elapses, or interrupted.
     *
     * @param timeout the maximum time to wait.
     * @param unit    the time unit of the timeout argument.
     *
     * @return {@code true} if tasks completed, {@code false} if timed-out.
     *
     * @throws InterruptedException if interrupted while waiting.
     */
    boolean awaitCompletion(long timeout, TimeUnit unit) throws InterruptedException;

    /**
     * All tasks are completed.
     *
     * @return {@code true} if all tasks are completed.
     */
    boolean isCompleted();
}
