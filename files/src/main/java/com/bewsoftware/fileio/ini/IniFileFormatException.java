/*
 * This file is part of the BEW Files Library (aka: BEWFiles).
 *
 * Copyright (C) 2020, 2021 Bradley Willcott
 *
 * BEWFiles is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BEWFiles is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.bewsoftware.fileio.ini;

/**
 * When the format of the <u>ini</u> file does not conform to what this
 * class expects, this exception will be thrown.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0
 * @version 1.0.5
 */
public class IniFileFormatException extends Exception
{

    private static final long serialVersionUID = -1526284495445937980L;

    /**
     * Path to the <u>ini</u> file.
     */
    public final String filepath;

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param filepath Path to the <u>ini</u> file.
     * @param msg      the detail message.
     */
    public IniFileFormatException(final String filepath, final String msg)
    {
        super(msg);
        this.filepath = filepath;
    }

    @Override
    public String toString()
    {
        StackTraceElement[] stElements = getStackTrace();
        StringBuilder sb = new StringBuilder();

        for (StackTraceElement stElement : stElements)
        {
            sb.append("\t").append(stElement.toString()).append("\n");
        }

        return IniFileFormatException.class.getName() + "\n\nMessage:  " + getMessage()
                + "\n\nIni File: " + filepath + "\n\nStack Trace:\n" + sb.toString();
    }
}
