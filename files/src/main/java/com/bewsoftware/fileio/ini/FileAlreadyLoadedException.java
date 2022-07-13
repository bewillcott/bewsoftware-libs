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
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.bewsoftware.fileio.ini;

import java.io.IOException;

/**
 * The name is very self-explanatory.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0
 * @version 1.0
 */
public class FileAlreadyLoadedException extends IOException
{

    private static final long serialVersionUID = 2345633335946985039L;

    /**
     * Constructs a new exception without a detail message.
     */
    public FileAlreadyLoadedException()
    {
        super();
    }

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message.
     */
    public FileAlreadyLoadedException(final String message)
    {
        super(message);
    }

    /**
     * Constructs a new exception with the specified cause and a detail message
     * of (cause==null ? null : cause.toString()) (which typically contains the
     * class and detail message of cause). This constructor is useful for
     * exceptions that are little more than wrappers for other throwables (for
     * example, PrivilegedActionException).
     *
     * @param cause the cause (which is saved for later retrieval by the
     *              Throwable.getCause() method). (A null value is permitted,
     *              and indicates that the cause is nonexistent or unknown.)
     */
    public FileAlreadyLoadedException(final Throwable cause)
    {
        super(cause);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     * <p>
     * Note that the detail message associated with cause is not automatically
     * incorporated in this exception's detail message.
     *
     * @param message the detail message (which is saved for later retrieval by
     *                the Throwable.getMessage() method).
     * @param cause   the cause (which is saved for later retrieval by the
     *                Throwable.getCause() method). (A null value is permitted,
     *                and indicates that the cause is nonexistent or unknown.)
     */
    public FileAlreadyLoadedException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

}
