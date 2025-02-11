/*
 *  File Name:    Clazz.java
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

package com.bewsoftware.utils;

import java.net.URL;

import static java.lang.Class.forName;

/**
 * Clazz provides helper methods related to class and resource access.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 3.0.2
 * @version 3.0.2
 */
public interface Clazz
{
    /**
     * Get the class of the method that called the method that called
     * this method.
     *
     * @return the class of the calling method.
     *
     * @throws ClassNotFoundException if unable to find the class.
     * @since 3.0.2
     */
    public static Class<?> getCallingClass() throws ClassNotFoundException
    {
        return getCallingClass(1);
    }

    /**
     * Get the class of the method that called the method that called
     * this method.
     *
     * @param level How far down the calling stack to go.<br>
     * - 0: immediate caller,<br>
     * - 1: their immediate caller.
     *
     * @return the class of the calling method.
     *
     * @throws ClassNotFoundException if unable to find the class.
     * @since 3.0.2
     */
    public static Class<?> getCallingClass(final int level) throws ClassNotFoundException
    {
        return forName(Thread.currentThread().getStackTrace()[level + 3].getClassName());
    }

    /**
     * Get the name class of the method that called the method that called
     * this method.
     *
     * @return the class name.
     *
     * @since 3.0.2
     */
    public static String getCallingClassName()
    {
        return getCallingClassName(1);
    }

    /**
     * Get the name class of the method that called the method that called
     * this method.
     *
     * @param level How far down the calling stack to go.<br>
     * - 0: immediate caller,<br>
     * - 1: their immediate caller.
     *
     * @return the class name.
     *
     * @since 3.0.2
     */
    public static String getCallingClassName(final int level)
    {
        final String fullClassName = Thread.currentThread().getStackTrace()[level + 3].getClassName();
        return fullClassName.substring(fullClassName.lastIndexOf('.') + 1);
    }

    /**
     * Obtain the resource's {@code URL}. The location is related to the class
     * of the method that called the method that called this method.
     *
     * @implSpec
     * If your project uses modules, then the package containing the
     * required resource <i>must</i> be atleast <i><b>open</b></i> to this library.
     *
     * @param name of the resource.
     *
     * @return the resource's {@code URL}.
     *
     * @throws ClassNotFoundException if unable to find the class.
     *
     * @see Class#getResource(String)
     *
     * @since 3.0.2
     */
    public static URL getResource(final String name) throws ClassNotFoundException
    {
        return getCallingClass(1).getResource(name);
    }

    /**
     * Returns an array of stack trace elements representing the stack dump of this thread.
     *
     * @return an array of {@link StackTraceElement}, each represents one stack frame.
     *
     * @see Thread#getStackTrace()
     *
     * @since 3.0.2
     */
    public static StackTraceElement[] getStack()
    {
        return Thread.currentThread().getStackTrace();
    }
}
