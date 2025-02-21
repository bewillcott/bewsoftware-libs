/*
 *  File Name:    TriFunction.java
 *  Project Name: bewsoftware-utils
 *
 *  Copyright (c) 2023 Bradley Willcott
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

package com.bewsoftware.utils.function;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Represents a function that accepts three arguments and produces a result.
 * This is the tri-arity specialization of {@link BiFunction}.
 * <p>
 * This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link apply(Object, Object, Object)}.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 * @param <T> the type of the first argument to the function.
 * @param <U> the type of the second argument to the function.
 * @param <V> the type of the third argument to the function.
 * @param <R> the type of the result of the function.
 *
 * @see Function
 * @see BiFunction
 *
 * @since 3.0.0
 * @version 3.1.0
 */
@FunctionalInterface
public interface TriFunction<T, U, V, R>
{
    /**
     * Returns a composed function that first applies this function to
     * its input, and then applies the {@code after} function to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     *
     * @param after the function to apply after this function is applied
     *
     * @param <W>   the type of output of the {@code after} function, and of the
     *              composed function
     *
     * @return a composed function that first applies this function and then
     *         applies the {@code after} function
     *
     * @throws NullPointerException if after is null
     *
     * @since 3.1.0
     */
    default <W> TriFunction<T, U, V, W> andThen(Function<? super R, ? extends W> after)
    {
        Objects.requireNonNull(after);
        return (T t, U u, V v) -> after.apply(apply(t, u, v));
    }

    /**
     * Applies this function to the given arguments.
     *
     * @param t the first function argument.
     * @param u the second function argument.
     * @param v the third function argument.
     *
     * @return the function result.
     *
     * @since 3.0.0
     */
    R apply(T t, U u, V v);
}
