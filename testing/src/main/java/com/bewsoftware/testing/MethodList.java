/*
 *  File Name:    MethodList.java
 *  Project Name: bewsoftware-testing
 *
 *  Copyright (c) 2025 Bradley Willcott
 *
 *  JCodes is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  JCodes is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.bewsoftware.testing;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * {@code MethodList} is an annotation that is used internally by
 * the {@code @TestMethod} annotation and the {@code MethodListOrder}
 * class.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @see TestMethod
 * @see MethodListOrder
 *
 * @since 3.1.0
 * @version 3.1.0
 */
@Target(TYPE)
@Retention(RUNTIME)
@Documented
public @interface MethodList
{
    /**
     * List of {@link TestMethod test methods} in the order in which they are to be executed.
     * This list is to be used by the annotation: {@link MethodListOrder}.
     *
     * @return an array of method names.
     *
     * @since 3.1.0
     */
    TestMethod[] value();// default
//    {
//    };
}
