/*
 *  File Name:    Ref.java
 *  Project Name: bewsoftware-utils
 *
 *  Copyright (c) 2021-2023 Bradley Willcott
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

import java.util.Optional;
import java.util.function.Consumer;

/**
 * This class provides a way to get a value of type &lt;T&gt; into
 * and out of either a Lambda expression or a method through a parameter.
 * <p>
 * <b>Example:</b>
 * <hr><pre><code> ...
 *     Ref&lt;Integer&gt; iRtn = Ref.val();
 *
 *     if(add(2, 3, iRtn)){
 *         System.out.println("2 + 3 = " + iRtn.val);
 *     }
 * ...
 *
 * public boolean add(final int a, final int b, final Ref&lt;Integer&gt; iRtn){
 *     iRtn.val = a + b;
 *
 *     return iRtn.val &gt; 0;  // just something to use up return
 * }</code></pre><hr>
 *
 * @Note
 * This file was copied from one of my personal libraries.
 * <p>
 * Some might think that the class: {@link Optional} would be just as good to perform
 * the same tasks, I disagree. As is mentioned in the Javadoc for {@code Optional} :-
 * <hr>
 * <b>API Note:</b><br>
 * <i>
 * Optional is primarily intended for use as a method return type where there is a clear need to represent "no result,"
 * and where using null is likely to cause errors. [...]
 * </i>
 * <hr>
 * Technically, you probably could use it, but I think that its use in the context of {@code Ref}'s usage, would be more
 * confusing than helpful.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 * @param <T> type of Object
 *
 * @since 3.0.0
 * @version 3.0.2
 */
@SuppressWarnings("PublicField")
public final class Ref<T>
{
    /**
     * The object being held.
     *
     * @Note
     * This field is intentionally accessible publicly, to improve the convenience
     * of its use. As this class is a temporary holder of an object, rather than
     * something more permanent.
     */
    public T val;

    /**
     * This constructor is <b>private</b> to prevent instantiation by external clients.
     * <p>
     * This class should <i>only</i> be instantiated through one of the factory methods:
     * <ul>
     * <li>{@link  #val()}</li>
     * <li>{@link  #val(Object) val(T val)}</li>
     * </ul>
     */
    private Ref()
    {
        val = null;
    }

    /**
     * This factory method instantiates an empty Ref object.
     *
     * @param <T> type of object.
     *
     * @return a new instance of the Ref class.
     */
    public static <T> Ref<T> val()
    {
        return new Ref<>();
    }

    /**
     * This factory method instantiates a Ref object containing {@code val}.
     *
     * @param <T> type of object.
     * @param val the object to be held
     *
     * @return a new instance of the Ref class, initialized with the 'val'
     *         object.
     */
    public static <T> Ref<T> val(final T val)
    {
        Ref<T> rtn = new Ref<>();
        rtn.val = val;
        return rtn;
    }

    /**
     * Reset 'val' to {@code null}.
     */
    public void clear()
    {
        val = null;
    }

    /**
     * If a value is present, performs the given action with the value,
     * otherwise does nothing.
     *
     * @param action to be performed, if a value is present.
     *
     * @throws NullPointerException if value is present and the given action is
     *                              {@code null}
     * @since 3.0.2
     */
    public void ifPresent(Consumer<? super T> action)
    {
        if (isPresent())
        {
            action.accept(val);
        }
    }

    /**
     * Check to see if this Ref object has not yet been set to a value.
     *
     * @return {@code true} if it hasn't been set to a value, {@code false}
     *         otherwise.
     */
    public boolean isEmpty()
    {
        return val == null;
    }

    /**
     * Check to see if this Ref object has been set to a value.
     *
     * @return {@code true} if it has been set to a value, {@code false} otherwise.
     */
    public boolean isPresent()
    {
        return val != null;
    }

    /**
     * Returns a string representation of the object.
     *
     * @implSpec
     * This implementation returns a string consisting of the default conversion
     * to a string, of the object held in {@code val}. This is achieved by
     * calling its {@code toString()} method. If {@code val} is empty, then the
     * empty string is returned: "".
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString()
    {
        return "" + val;
    }
}
