/*
 *  File Name:    JfxGlobals.java
 *  Project Name: bewsoftware-jfx
 *
 *  Copyright (c) 2023 Bradley Willcott
 *
 *  bewsoftware-jfx is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  bewsoftware-jfx is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.bewsoftware.jfx;

import com.bewsoftware.annotations.jcip.GuardedBy;
import com.bewsoftware.utils.io.DisplayDebugLevel;

import static com.bewsoftware.utils.io.DisplayDebugLevel.DEFAULT;

/**
 * This class contains global parameters.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 3.0.0
 * @version 3.0.0
 */
public class JfxGlobals
{
    @GuardedBy("JfxGlobals")
    private static volatile DisplayDebugLevel debugLevel = DEFAULT; // Do NOT edit this line!
    // Overide value in your main(...) method.

    /**
     * Not meant to be instantiated.
     */
    private JfxGlobals()
    {
    }

    /**
     * Used to obtain the current setting of the global DISPLAY debug level.
     *
     * @return current setting
     *
     * @see #setDebugLevel(DisplayDebugLevel)
     */
    @GuardedBy("JfxGlobals")
    public static synchronized DisplayDebugLevel getDebugLevel()
    {
        return JfxGlobals.debugLevel;
    }

    /**
     * Used to set the debug level of the DISPLAY instances throughout the
     * project.
     * <p>
     * Debug level usage:
     * <ul>
     * <li><b>0</b>: QUIET - display nothing.</li>
     * <li><b>1</b>: DEFAULT - Normal text display.</li>
     * <li><b>2</b>: INFO - designates informational messages that highlight the
     * progress of the application at coarse-grained level.</li>
     * <li><b>3</b>: DEBUG - designates fine-grained informational events that
     * are most useful to debug an application.</li>
     * <li><b>4</b>: TRACE - designates finer-grained informational events than
     * the DEBUG.</li>
     * </ul>
     * <p>
     * Example use in your 'main()' method:
     * <p>
     * {@code Globals.setDebugLevel(DEBUG);}
     *
     * @param level new level to set
     */
    @GuardedBy("JfxGlobals")
    public static synchronized void setDebugLevel(final DisplayDebugLevel level)
    {
        JfxGlobals.debugLevel = level;
    }

}
