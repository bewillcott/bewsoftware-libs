/*
 *  File Name:    ReindexFailedException.java
 *  Project Name: Java3AT2-Two
 *
 *  Copyright (c) 2021 Bradley Willcott
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.bewsoftware.utils;

/**
 * This would be used when ever an indexation process has failed.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0.9
 * @version 1.0.9
 */
public class ReindexFailedException extends RuntimeException
{

    /**
     * For serialization.
     */
    private static final long serialVersionUID = 3209096141532094223L;

    /**
     * Initializes a new instance of the {@linkplain ReindexFailedException} class.
     */
    public ReindexFailedException()
    {
    }

    /**
     * Initializes a new instance of the {@linkplain ReindexFailedException} class.
     *
     * @param message the message that describes the error
     */
    public ReindexFailedException(String message)
    {
        super(message);
    }

    /**
     * Initializes a new instance of the {@linkplain ReindexFailedException} class.
     *
     * @param message the message that describes the error
     * @param cause   the exception that is the cause of the current exception, or
     *                a {@code null} reference if no inner exception is specified
     */
    public ReindexFailedException(String message, Throwable cause)
    {
        super(message, cause);
    }

    /**
     * Initializes a new instance of the {@linkplain ReindexFailedException} class.
     *
     * @param cause the exception that is the cause of the current exception, or
     *              a {@code null} reference if no inner exception is specified
     */
    public ReindexFailedException(Throwable cause)
    {
        super(cause);
    }

}
