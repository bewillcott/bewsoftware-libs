/*
 *  File Name:    DisplayDebugLevel.java
 *  Project Name: bewsoftware-utils
 * 
 *  Copyright (c) 2023 Bradley Willcott
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

/**
 * This DisplayDebugLevel enum is used by the {@link Display} interface and its
 * implementing classes to determine which level of messages will be displayed.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 3.0.0
 * @version 3.0.0
 */
public enum DisplayDebugLevel
{
    /**
     * Display/Debug level.
     * <p>
     * Designates fine-grained informational events that
     * are most useful to debug an application.
     */
    DEBUG("DEBUG", 3),
    /**
     * Display/Debug level.
     * <p>
     * Normal text display.
     */
    DEFAULT("", 1),
    /**
     * Display/Debug level.
     * <p>
     * Designates informational messages that highlight the
     * progress of the application at coarse-grained level.
     */
    INFO("INFO ", 2),
    /**
     * Display/Debug level.
     * <p>
     * No text is displayed.
     */
    QUIET("", 0),
    /**
     * Display/Debug level.
     * <p>
     * Designates finer-grained informational events than
     * the DEBUG.
     */
    TRACE("TRACE", 4);

    /**
     * Text string used for display purposes.
     */
    public final String label;

    /**
     * Value used for level comparison purposes.
     */
    public final int value;

    DisplayDebugLevel(final String label, final int value)
    {
        this.label = label;
        this.value = value;
    }
}
