/*
 *  File Name:    Ref.java
 *  Project Name: bewsoftware-utils
 *
 *  Copyright (c) 2021-2022 Bradley Willcott
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
 * This class provides a way to get a value of type &lt;T&gt; into
 * and out of either a Lambda expression or a method through a parameter.
 * <p>
 * <b>Example:</b>
 * {@snippet internal:
 * ...
 *     Ref<Integer>; iRtn = new Ref<>();
 *
 *     if(add(2, 3, iRtn)){
 *         System.out.println("2 + 3 = " + iRtn.val);
 *     }
 * ...
 *
 * public boolean add(final int a, final int b, final Ref<Integer> iRtn){
 *     iRtn.val = a + b;
 *
 *     return iRtn.val > 0; // just something to use up return
 * }
 * }
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 * @param <T> type of Object
 *
 * @since 3.0.0
 * @version 3.0.0
 */
@SuppressWarnings("PublicField")
public final class Ref<T>
{
    /**
     * The object being held.
     */
    public T val;

    /**
     * This constructor is <b>private</b> to prevent instantiation by external
     * clients.
     * <p>
     * This class should <i>only</i> be instantiated through one of the factory
     * methods:
     * <ul>
     * <li>{@link #val()}</li>
     * <li>{@link #val(java.lang.Object) val(T val)}</li>
     * </ul>
     */
    private Ref()
    {
        this.val = null;
    }

    /**
     * Alternative to using the 'new' directive to instantiate the class.
     *
     * @param <T> type of Object
     *
     * @return a new instance of the Ref class.
     */
    public static <T> Ref<T> val()
    {
        return new Ref<>();
    }

    /**
     * Alternative to using the 'new' directive to instantiate the class.
     *
     * @param <T> type of Object
     * @param val the object to be held
     *
     * @return a new instance of the Ref class, initialized with the 'val'
     *         object.
     */
    public static <T> Ref<T> val(T val)
    {
        Ref<T> rtn = new Ref<>();
        rtn.val = val;
        return rtn;
    }

    /**
     * Reset 'val' to null.
     */
    public void clear()
    {
        this.val = null;
    }

    /**
     * Check to see if this {@linkplain Ref} object has yet been set to a
     * value.
     *
     * @return {@code true} if it hasn't been set to a value, {@code false}
     *         otherwise.
     */
    public boolean isEmpty()
    {
        return this.val == null;
    }

    /**
     * Check to see if this {@linkplain Ref} object has been set to a value.
     *
     * @return {@code true} if it has been set to a value, {@code false}
     *         otherwise.
     */
    public boolean isPresent()
    {
        return this.val != null;
    }

    /**
     * Returns a string representation of the object.
     *
     * @implSpec
     * This implementation returns a string consisting of the default conversion
     * to a string, of the object held in {@code val}. This is achieved by calling
     * its {@code toString()} method. If {@code val} is empty, then the empty string
     * is returned: "".
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString()
    {
        return isPresent() ? this.val.toString() : "";
    }
}
