/*
 *  File Name:    TriPredicate.java
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

package com.bewsoftware.utils.function;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * Represents a predicate (boolean-valued function) of three arguments.
 * This is the tri-arity specialization of {@link Predicate}.
 * <p>
 * This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #test(Object, Object, Object)}.
 *
 * @apiNote
 * The text and code for this interface were originally copied from the JDK
 * 23 {@link BiPredicate} interface, and modified by me to provide the third
 * parameter.
 *
 * @param <T> the type of the first argument to the operation
 * @param <U> the type of the second argument to the operation
 * @param <V> the type of the third argument to the operation
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @see Predicate
 * @see BiPredicate
 *
 * @since 3.1.0
 * @version 3.1.0
 */
public interface TriPredicate<T, U, V>
{
    /**
     * Returns a composed predicate that represents a short-circuiting logical
     * AND of this predicate and another. When evaluating the composed
     * predicate, if this predicate is {@code false}, then the {@code other}
     * predicate is not evaluated.
     * <p>
     * Any exceptions thrown during evaluation of either predicate are relayed
     * to the caller; if evaluation of this predicate throws an exception, the
     * {@code other} predicate will not be evaluated.
     *
     * @param other a predicate that will be logically-ANDed with this
     *              predicate
     *
     * @return a composed predicate that represents the short-circuiting logical
     *         AND of this predicate and the {@code other} predicate
     *
     * @throws NullPointerException if other is null
     *
     * @since 3.1.0
     */
    default TriPredicate<T, U, V> and(TriPredicate<? super T, ? super U, ? super V> other)
    {
        Objects.requireNonNull(other);
        return (T t, U u, V v) -> test(t, u, v) && other.test(t, u, v);
    }

    /**
     * Returns a predicate that represents the logical negation of this
     * predicate.
     *
     * @return a predicate that represents the logical negation of this
     *         predicate
     */
    default TriPredicate<T, U, V> negate()
    {
        return (T t, U u, V v) -> !test(t, u, v);
    }

    /**
     * Returns a composed predicate that represents a short-circuiting logical
     * OR of this predicate and another. When evaluating the composed
     * predicate, if this predicate is {@code true}, then the {@code other}
     * predicate is not evaluated.
     * <p>
     * <p>
     * Any exceptions thrown during evaluation of either predicate are relayed
     * to the caller; if evaluation of this predicate throws an exception, the
     * {@code other} predicate will not be evaluated.
     *
     * @param other a predicate that will be logically-ORed with this
     *              predicate
     *
     * @return a composed predicate that represents the short-circuiting logical
     *         OR of this predicate and the {@code other} predicate
     *
     * @throws NullPointerException if other is null
     *
     * @since 3.1.0
     */
    default TriPredicate<T, U, V> or(TriPredicate<? super T, ? super U, ? super V> other)
    {
        Objects.requireNonNull(other);
        return (T t, U u, V v) -> test(t, u, v) || other.test(t, u, v);
    }

    /**
     * Evaluates this predicate on the given arguments.
     *
     * @param t the first input argument
     * @param u the second input argument
     * @param v the third input argument
     *
     * @return {@code true} if the input arguments match the predicate,
     *         otherwise {@code false}
     *
     * @since 3.1.0
     */
    boolean test(T t, U u, V v);
}
