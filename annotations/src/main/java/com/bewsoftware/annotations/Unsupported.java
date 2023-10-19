/*
 *  File Name:    Unsupported.java
 *  Project Name: bewsoftware-annotations
 *
 *  Copyright (c) 2023 Bradley Willcott
 *
 *  bewsoftware-annotations is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  bewsoftware-annotations is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.bewsoftware.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * The methods to which this annotation is applied are Unsupported.
 * <p>
 * This annotation has a string-valued argument. The value of this
 * argument is the <i>reason</i> that the method is unsupported.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 3.0.0
 * @version 3.0.0
 */
@Documented
@Target(METHOD)
@Retention(CLASS)
public @interface Unsupported
{

    /**
     * Returns the text explaining the reason for the lack of support.
     *
     * @return the reason.
     */
    String value();
}
