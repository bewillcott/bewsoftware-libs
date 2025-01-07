/*
 *  File Name:    TaskTracker.java
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

/**
 * Tracks the number of uncompleted tasks.
 * <p>
 * Used by:<br>
 * - {@linkplain PerThreadPoolExecutor}<br>
 * - {@linkplain SerialExecutor}
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 3.0.1
 * @version 3.0.1
 */
final class TaskTracker
{
    private volatile int counter = 0;

    TaskTracker()
    {
    }

    public boolean allTasksCompleted()
    {
        return counter == 0;
    }

    public int decrementAndGet()
    {
        final int rtn = --counter;
        assert rtn >= 0;
        return rtn;
    }

    public int get()
    {
        return counter;
    }

    public int incrementAndGet()
    {
        return ++counter;
    }

    /**
     * Reset internal counter to '0'.
     */
    public void reset()
    {
        counter = 0;
    }

    @Override
    public String toString()
    {
        return "TaskTracker{counter = " + counter + '}';
    }
}
