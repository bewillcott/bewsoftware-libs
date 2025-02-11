/*
 * This file is part of the BEW Common Library (aka: BEWCommon).
 *
 * Copyright (C) 2020 Bradley Willcott
 *
 * BEWCommon is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BEWCommon is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.bewsoftware.common;

import static java.lang.String.format;

/**
 * Thrown to indicate that a method has been passed a parameter with an invalid
 * or inappropriate value.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @deprecated Use {@link IllegalArgumentException} instead. Will be removed
 * in later version.
 *
 * @since 2.1.0
 * @version 3.0.0
 */
@Deprecated(forRemoval = true, since = "3.0.0")
public class InvalidParameterValueException extends RuntimeException
{

    private static final long serialVersionUID = 947167888439966970L;

    /**
     * Constructs a new exception without a detail message.
     */
    public InvalidParameterValueException()
    {
        super();
    }

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message The detail message.
     */
    public InvalidParameterValueException(String message)
    {
        super(message);
    }

    /**
     * Constructs a new exception with the constructed message.
     *
     * @param format the format string.
     * @param args   the arguments for the format string.
     */
    public InvalidParameterValueException(String format, Object... args)
    {
        super(format(format, args));
    }

    /**
     * Constructs a new exception with the specified cause and a detail message
     * of (cause == null ? null : cause.toString()) (which typically contains
     * the
     * class and detail message of cause).
     * <p>
     * This constructor is useful for exceptions that are little more than
     * wrappers
     * for other throwables (for example, PrivilegedActionException).
     *
     * @param cause The cause (which is saved for later retrieval by the
     *              Throwable.getCause() method). (A null value is permitted,
     *              and indicates that the cause is nonexistent or unknown.)
     */
    public InvalidParameterValueException(Throwable cause)
    {
        super(cause);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     * <p>
     * Note that the detail message associated with cause is not automatically
     * incorporated in this exception's detail message.
     *
     * @param message The detail message (which is saved for later retrieval by
     *                the Throwable.getMessage() method).
     * @param cause   The cause (which is saved for later retrieval by the
     *                Throwable.getCause() method). (A null value is permitted,
     *                and indicates that the cause is nonexistent or unknown.)
     */
    public InvalidParameterValueException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
