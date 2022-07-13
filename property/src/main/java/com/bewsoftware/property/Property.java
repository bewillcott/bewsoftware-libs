/*
 * This file is part of the BEW Property Library (aka: BEWProperty).
 *
 * Copyright (C) 2020 Bradley Willcott
 *
 * BEWProperty is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BEWProperty is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.bewsoftware.property;

import java.io.Serializable;
import java.util.Objects;

/**
 * This is the parent class for all of the classes in this package.
 * <p>
 * This class is an immutable class that contains all the fields
 * and the methods to read them.
 * <dl>
 * <dt>The fields:</dt>
 * <dd>
 * <ul>
 * <li>K key: is {@code protected} and {@code final}.</li>
 * <li>V value: is {@code protected}</li>
 * <li>String comment: is {@code protected}</li>
 * </ul>
 * </dd>
 * </dl>
 * The {@code key} field is immutable, and therefore
 * can only be set by one of the constructors of this class.
 * <p>
 * It is possible for a sub-class to be a mutator class, having methods that
 * can modify either/or both of the fields: {@code value} and {@code comment}.
 *
 * @param <K> key type.
 * @param <V> value type.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0
 * @version 1.0
 */
public class Property<K extends Comparable<K>, V> implements
        Serializable,
        Comparable<Property<K, V>>
{

    /**
     * @serial serial
     */
    private static final long serialVersionUID = -3045318643922146363L;

    /**
     * The property's comment field;
     */
    protected String comment;

    /**
     * The property's key field.
     */
    protected transient final K key;

    /**
     * The property's value field.
     */
    protected transient V value;

    /**
     * Create a new instance of {@code Property} as a copy of an existing
     * instance
     * of {@code Property}, or one of its sub-classes.
     *
     * @param <T>      the type of the class being copied.
     * @param property The instance to copy.
     */
    public <T extends Property<K, V>> Property(T property)
    {
        this(property.key, property.value, property.comment);
    }

    /**
     * Create a new instance of {@code Property} with a {@code null} comment.
     *
     * @param key   The key.
     * @param value The value.
     */
    public Property(K key, V value)
    {
        this(key, value, null);
    }

    /**
     * Create a new instance of {@code Property}.
     *
     * @param key     The key.
     * @param value   The value.
     * @param comment The comment.
     */
    public Property(K key, V value, String comment)
    {
        this.key = key;
        this.value = value;
        this.comment = comment;
    }

    /**
     * Get the field: {@code comment}.
     *
     * @return {@code comment} contents.
     */
    public String comment()
    {
        return comment;
    }

    @Override
    public int compareTo(Property<K, V> other)
    {
        int rtn = 0;

        if (!equals(Objects.requireNonNull(other)))
        {
            if (this.getClass() != other.getClass())
            {
                throw new ClassCastException("Must be the same sub-class of Property<K, V>.");
            }

            if (this.key == null)
            {
                if (other.key != null)
                {
                    rtn = -1;
                }
            } else
            {
                if (other.key == null)
                {
                    rtn = 1;
                } else
                {
                    rtn = this.key.compareTo(other.key);
                }
            }
        }

        return rtn;
    }

    @Override
    public boolean equals(Object obj)
    {
        boolean rtn = false;

        if (this == obj)
        {
            rtn = true;
        } else if (obj instanceof Property<?, ?>)
        {
            Property<?, ?> other = (Property<?, ?>) obj;

            if (this.getClass() == other.getClass()
                    && this.value.getClass() == other.value.getClass()
                    && this.key.equals(other.key))
            {
                rtn = true;
            }
        }

        return rtn;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.key);
        return hash;
    }

    /**
     * Get the field: {@code key}.
     *
     * @return {@code key} contents.
     */
    public K key()
    {
        return key;
    }

    @Override
    public String toString()
    {
        return "{ key = " + key + ", value = " + value + ", comment = " + comment + " }";
    }

    /**
     * Get the field: {@code value}.
     *
     * @return {@code value} contents.
     */
    public V value()
    {
        return value;
    }

}
