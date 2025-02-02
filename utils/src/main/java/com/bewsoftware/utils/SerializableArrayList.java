/*
 *  File Name:    SerializableArrayList.java
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

import com.bewsoftware.utils.string.MessageBuilder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * This is a special version of the {@link ArrayList}, because it implements the
 * {@link SerializableList} interface. The purpose of that, is to allow it to be
 * serialized as part of an aggregate class, rather than having to be a transient.
 * <p>
 * Instead of this:
 * <pre><code>
 *     private final transient List&lt;String&gt; names1 = new ArrayList<>();
 * </code></pre>
 * You can now have this:
 * <pre><code>
 *     private final SerializableList&lt;String&gt; names2 = new SerializableArrayList<>();
 * </code></pre>
 * As a transient, you would have to handle the serialization of {@code names1} yourself.
 * However, {@code names2} is automatically serialized along with the rest of the classes
 * non-static members.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 * @param <E> the type of elements in this list
 *
 * @since 3.0.2
 * @version 3.0.2
 */
public class SerializableArrayList<E> extends ArrayList<E> implements SerializableList<E>
{
    private static final long serialVersionUID = 1218746376491915305L;

    /**
     * Constructs an empty list with an initial capacity of ten.
     */
    public SerializableArrayList()
    {
        super();
    }

    /**
     * Constructs an empty list with the specified initial capacity.
     *
     * @param initialCapacity the initial capacity of the list.
     */
    public SerializableArrayList(int initialCapacity)
    {
        super(initialCapacity);
    }

    /**
     * Constructs a list containing the elements of the specified collection, in the order they are returned by the
     * collection's iterator.
     *
     * @param c the collection whose elements are to be placed into this list.
     *
     * @throws NullPointerException if the specified collection is <i>null</i>.
     */
    public SerializableArrayList(Collection<? extends E> c)
    {
        super(c);
    }

    @Override
    public Object clone()
    {
        return super.clone();
    }

    @Override
    public String toString()
    {
        Iterator<E> it = iterator();

        if (!it.hasNext())
        {
            return "[]";
        }

        MessageBuilder mb = new MessageBuilder();
        mb.appendln('[');

        for (;;)
        {
            final E e = it.next();
            mb.append("    ").append(e == this ? "(this Collection)" : e);

            if (!it.hasNext())
            {
                return mb.appendln().append(']').toString();
            }

            mb.appendln(',');
        }
    }
}
