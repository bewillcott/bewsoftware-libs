/*
 *  File Name:    StatusLevel.java
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
 * StatusLevel is used by both {@linkplain PerThreadPoolExecutor} and
 * {@linkplain SerialExecutor}.
 *
 * @apiNote
 * The transition sequence should be as follows:
 * <p>
 * <b>A)</b>
 * {@linkplain #RUNNING} -> {@linkplain #SHUTTING_DOWN} -> {@linkplain #SHUTDOWN}
 * (eg: {@code shutdown()}), or<br>
 * <b>B)</b>
 * {@linkplain #RUNNING} -> {@linkplain #FORCING_SHUTDOWN} -> {@linkplain #FORCED_SHUTDOWN}
 * (eg: {@code shutdownNow()}.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 3.0.1
 * @version 3.0.1
 */
enum StatusLevel
{
    RUNNING, SHUTTING_DOWN, SHUTDOWN, FORCING_SHUTDOWN, FORCED_SHUTDOWN
}
