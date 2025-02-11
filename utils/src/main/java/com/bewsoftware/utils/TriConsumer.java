/*
 *  File Name:    TriConsumer.java
 *  Project Name: bewsoftware-utils
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

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Represents an operation that accepts three input arguments and returns no
 * result. This is the three-arity specialization of {@link Consumer}. Unlike
 * most other functional interfaces, {@code TriConsumer} is expected
 * to operate via side-effects.
 * <p>
 * This is a {@link FunctionalInterface functional interface}
 * whose functional method is {@link #accept(Object, Object, Object)}.
 *
 * @apiNote
 * The text and code for this interface were originally copied from the JDK
 * 16 {@link BiConsumer} interface, and modified by me to provide the third
 * parameter.
 *
 * @deprecated Class moved to: {@linkplain com.bewsoftware.utils.function.TriConsumer}.
 *
 * @param <T> the type of the first argument to the operation
 * @param <U> the type of the second argument to the operation
 * @param <V> the type of the third argument to the operation
 *
 * @see Consumer
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0.9
 * @version 1.0.9
 */
@Deprecated(since = "3.0.1", forRemoval = true)
@FunctionalInterface
public interface TriConsumer<T, U, V>
{

    /**
     * Performs this operation on the given arguments.
     *
     * @param t the first input argument
     * @param u the second input argument
     * @param v the third input argument
     */
    void accept(T t, U u, V v);

    /**
     * Returns a composed {@code TriConsumer} that performs, in sequence, this
     * operation followed by the {@code after} operation. If performing either
     * operation throws an exception, it is relayed to the caller of the
     * composed operation. If performing this operation throws an exception,
     * the {@code after} operation will not be performed.
     *
     * @param after the operation to perform after this operation
     *
     * @return a composed {@code TriConsumer} that performs in sequence this
     *         operation followed by the {@code after} operation
     *
     * @throws NullPointerException if {@code after} is null
     */
    default TriConsumer<T, U, V> andThen(TriConsumer<? super T, ? super U, ? super V> after)
    {
        Objects.requireNonNull(after);

        return (t, u, v) ->
        {
            accept(t, u, v);
            after.accept(t, u, v);
        };
    }
}
