/*
 *  File Name:    TermIO.java
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

import com.bewsoftware.utils.string.Strings;
import java.util.Formatter;

import static java.lang.String.format;

/**
 * This helper class contains some static methods that help format the output
 * being displayed on the terminal.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 3.0.0
 * @version 3.0.0
 */
public class TermIO
{
    /**
     * The length of the various lines used in titles, etc.
     */
    public static final int LINE_LENGTH = 40;

    /**
     * The length of the various extended lines used around tables, etc.
     */
    public static final int EXT_LINE_LENGTH = LINE_LENGTH * 2;

    /**
     * A line of asterisks (*).
     */
    public static final String DIV_LINE3 = "*".repeat(EXT_LINE_LENGTH);

    /**
     * A line of equals (=).
     */
    public static final String DIV_LINE2 = "=".repeat(EXT_LINE_LENGTH);

    /**
     * A line of hyphens (-).
     */
    public static final String DIV_LINE1 = "-".repeat(EXT_LINE_LENGTH);

    /**
     * A line of asterisks (*).
     */
    public static final String STAR_LINE = "*".repeat(LINE_LENGTH);

    /**
     * A line of hyphens (-).
     */
    public static final String SUBTITLE_LINE = "-".repeat(LINE_LENGTH);

    /**
     * A line of equals (=).
     */
    public static final String TITLE_LINE = "=".repeat(LINE_LENGTH);

    /**
     * Not meant to be instantiated.
     */
    private TermIO()
    {
    }

    /**
     * Generate a method line with '-' characters.
     *
     * @param methodName to wrap
     *
     * @return new titled line
     */
    public static String methodLine(final String methodName)
    {
        String rtn = "";

        if (methodName != null && !methodName.isEmpty())
        {
            rtn = Strings.centreFill(" " + methodName + " ", EXT_LINE_LENGTH, "-");
        }

        return rtn;
    }

    /**
     * Generate a method line with '-' characters.
     *
     * @param format A format string. Refer to {@link Formatter}: Format String
     *               Syntax.
     * @param args   Arguments referenced by the format specifiers in the format
     *               string.
     *
     * @return new titled line
     */
    public static String methodLine(final String format, final Object... args)
    {
        return methodLine(format(format, args));
    }

}
