/*
 *  File Name:    TestMethod.java
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
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * {@code TestMethod} is an annotation that is used to configure the order in which the
 * <i>test methods</i> of a test class, are executed.
 *
 * @implNote
 * This annotation is repeated using the {@code MethodList} class.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @see MethodList
 * @see MethodListOrder
 *
 * @since 3.1.0
 * @version 3.1.0
 */
@Target(TYPE)
@Retention(RUNTIME)
@Documented
@Repeatable(MethodList.class)
public @interface TestMethod
{
    /**
     * The name of a {@code @Test} method. This is used in conjunction with the annotation
     * {@link MethodListOrder}.
     *
     * @return the name of the method.
     *
     * @since 3.1.0
     */
    String value();
}
