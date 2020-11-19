/*
 * This file is part of the BEW Utils Library (aka: BEWUtils).
 *
 * Copyright (C) 2020 Bradley Willcott
 *
 * BEWUtils is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BEWUtils is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.bew.utils.string;

import java.util.Objects;

/**
 * Strings class description.
 * Class contains some helper methods.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0.6
 * @version 1.0.7
 */
public class Strings {

    /**
     * This class is not meant to be instantiated.
     */
    private Strings() {
    }

    /**
     * Checks that the specified string isn't <i>blank</i>.
     * <dl>
     * <dt>blank:</dt>
     * <dd>The string is either <i>empty</i> or contains only white space code-points.</dd>
     * <dt>empty:</dt>
     * <dd>The string's {@code length} is {@code 0}.</dd>
     * </dl>
     *
     * @param str The string to check for blankness.
     *
     * @return {@code str} if not <i>blank</i>.
     *
     * @throws NullPointerException     if {@code str} is <i>null</i>.
     * @throws IllegalArgumentException if {@code str} is <i>blank</i>.
     *
     * @see #requireNonEmpty(java.lang.String)
     */
    public static String requireNonBlank(String str) {
        if (Objects.requireNonNull(str).isBlank())
        {
            throw new IllegalArgumentException("isBlank.");
        }

        return str;
    }

    /**
     * Checks that the specified string isn't <i>blank</i>.
     * <dl>
     * <dt>blank:</dt>
     * <dd>The string is either <i>empty</i> or contains only white space code-points.</dd>
     * <dt>empty:</dt>
     * <dd>The string's {@code length} is {@code 0}.</dd>
     * </dl>
     *
     * @param str     The string to check for blankness.
     * @param message Detail message to be used in the event that an exception is thrown.
     *
     * @return {@code str} if not <i>blank</i>.
     *
     * @throws NullPointerException     if {@code str} is <i>null</i>.
     * @throws IllegalArgumentException if {@code str} is <i>blank</i>.
     *
     * @see #requireNonEmpty(java.lang.String, java.lang.String)
     */
    public static String requireNonBlank(String str, String message) {
        if (Objects.requireNonNull(str, message).isBlank())
        {
            throw new IllegalArgumentException("isBlank: " + message);
        }

        return str;
    }

    /**
     * Checks that the specified string isn't <i>empty</i>.
     * <dl>
     * <dt>empty:</dt>
     * <dd>The string's {@code length} is {@code 0}.</dd>
     * </dl>
     *
     * @param str The string to check for emptiness.
     *
     * @return {@code str} if not <i>empty</i>.
     *
     * @throws NullPointerException     if {@code str} is <i>null</i>.
     * @throws IllegalArgumentException if {@code str} is <i>empty</i>.
     */
    public static String requireNonEmpty(String str) {
        if (Objects.requireNonNull(str).isEmpty())
        {
            throw new IllegalArgumentException("isEmpty.");
        }

        return str;
    }

    /**
     * Checks that the specified string isn't <i>empty</i>.
     * <dl>
     * <dt>empty:</dt>
     * <dd>The string's {@code length} is {@code 0}.</dd>
     * </dl>
     *
     * @param str     The string to check for emptiness.
     * @param message Detail message to be used in the event that an exception is thrown.
     *
     * @return {@code str} if not <i>empty</i>.
     *
     * @throws NullPointerException     if {@code str} is <i>null</i>.
     * @throws IllegalArgumentException if {@code str} is <i>empty</i>.
     */
    public static String requireNonEmpty(String str, String message) {
        if (Objects.requireNonNull(str, message).isEmpty())
        {
            throw new IllegalArgumentException("isEmpty: " + message);
        }

        return str;
    }
}
