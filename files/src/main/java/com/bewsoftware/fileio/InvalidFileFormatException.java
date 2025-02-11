/*
 *  File Name:    InvalidFileFormatException.java
 *  Project Name: bewsoftware-files
 *
 *  Copyright (c) 2025 Bradley Willcott
 *
 *  bewsoftware-files is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  bewsoftware-files is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.bewsoftware.fileio;

import java.io.IOException;

/**
 * InvalidFileFormatException class description.<br>
 * If a file being read is not what was expected...
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 3.0.2
 * @version 3.0.2
 */
public class InvalidFileFormatException extends IOException
{
    private static final long serialVersionUID = 5256008284023206732L;

    public InvalidFileFormatException()
    {
        super();
    }

    public InvalidFileFormatException(String message)
    {
        super(message);
    }

    public InvalidFileFormatException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public InvalidFileFormatException(Throwable cause)
    {
        super(cause);
    }
}
