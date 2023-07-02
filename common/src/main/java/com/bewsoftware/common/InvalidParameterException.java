/*
 *  File Name:    InvalidParameterException.java
 *  Project Name: bewsoftware-common
 *
 *  Copyright (c) 2023 Bradley Willcott
 *
 *  bewsoftware-common is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  bewsoftware-common is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.bewsoftware.common;

/**
 * The InvalidParameterException is thrown when one or more parameters of a
 * method have been passed invalid values.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 3.0.0
 * @version 3.0.0
 */
public class InvalidParameterException extends Exception
{
    /**
     * Serial version ID number.
     */
    private static final long serialVersionUID = -4021420668313274385L;

    /**
     * Constructs a new exception without a detail message.
     */
    public InvalidParameterException()
    {
        super();
    }

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidParameterException(String msg)
    {
        super(msg);
    }

    /**
     * Constructs a new exception with the specified cause and a detail message
     * of {@code (cause==null ? null : cause.toString())} (which typically
     * contains the class and detail message of cause). This constructor is
     * useful for exceptions that are little more than wrappers for other
     * throwables (for example, PrivilegedActionException).
     *
     * @param cause The cause (which is saved for later retrieval by the
     *              {@link Throwable#getCause()} method). (A null value is
     *              permitted, and indicates that the cause is nonexistent or
     *              unknown.)
     */
    public InvalidParameterException(Throwable cause)
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
     *                the {@link Throwable#getMessage()} method).
     * @param cause   The cause (which is saved for later retrieval by the
     *                {@link Throwable#getCause()} method). (A null value is
     *                permitted, and indicates that the cause is nonexistent or
     *                unknown.)
     */
    public InvalidParameterException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
