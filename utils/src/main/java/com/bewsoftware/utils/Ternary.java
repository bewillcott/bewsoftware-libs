/*
 *  File Name:    Ternary.java
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

/**
 * This enum provides the opportunity to represent a third possible
 * state for a variable or return value for a function call.
 * <p>
 * Sometimes with only a boolean value available, showing an <i>undecided</i>
 * status, or <i>not yet set</i> status, is not possible. Further, many times
 * a function will throw a NullPointerException, because neither <i>true</i>
 * nor <i>false</i> would be appropriate.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 3.1.0
 * @version 3.1.0
 */
public enum Ternary
{
    /**
     * This is equivalent to <i><b>false</b></i>.
     *
     * @since 3.1.0
     */
    False,
    /**
     * This is equivalent to <i><b>true</b></i>.
     *
     * @since 3.1.0
     */
    True,
    /**
     * This is the undecided/undecidable state.
     * This can also mean, that a value has yet to be set.
     * As such, it can be treated in the same manner as <i><b>null</b></i>
     * in relation to {@code Object} variables.
     *
     * @since 3.1.0
     */
    Null
}
