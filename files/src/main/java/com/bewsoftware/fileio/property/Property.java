/*
 *  File Name:    Property.java
 *  Project Name: bewsoftware-files
 *
 *  Copyright (c) 2020, 2025 Bradley Willcott
 *
 *  bewsoftware-files is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  bewsoftware-files is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.bewsoftware.fileio.property;

import com.bewsoftware.annotations.jcip.Immutable;
import com.bewsoftware.utils.Observable;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
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
 * <p>
 * If you intend to use the {@link PropertyChangeSupport} for tracking, then
 * you should use one of these constructors:
 * <ul>
 * <li>{@linkplain #Property(int, Property) Property(id, property)}</li>
 * <li>{@linkplain #Property(int, Comparable, Object) Property(id, key, value)}</li>
 * <li>{@linkplain #Property(int, Comparable, Object, String) Property(id, key, value, comment)}</li>
 * </ul>
 * Moved here from BEWSoftware Property Library since it is unlikely to
 * be used, except in conjunction with other classes from this library.
 *
 * @param <K> key type.
 * @param <V> value type.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 3.1.0
 * @version 3.1.0
 */
@Immutable
public sealed class Property<K extends Comparable<K>, V> implements
        Serializable, Comparable<Property<K, V>>, Observable
        permits IniProperty, MutableProperty
{
    public static final String PROP_COMMENT = "comment";

    public static final String PROP_VALUE = "value";

    private static final long serialVersionUID = -3124285320330080298L;

    /**
     * The property's comment field;
     *
     * @since 3.1.0
     */
    protected String comment;

    /**
     * The property's key field.
     *
     * @since 3.1.0
     */
    protected final K key;

    /**
     * Provides the property change support.
     *
     * @since 3.1.0
     */
    protected final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /**
     * The property's value field.
     *
     * @since 3.1.0
     */
    protected V value;

    /**
     * The property's id field.
     * <p>
     * It's default setting is '-1'. However, if you intend to
     * use the {@link PropertyChangeSupport} for tracking, then
     * it should be set to a unique value amongst other Property
     * instances.
     *
     * @since 3.1.0
     */
    private final int id;

    /**
     * Create a new instance of {@code Property} as a copy of an existing
     * instance of {@code Property}, or one of its sub-classes.
     *
     * @param property The instance to copy.
     * @param <T>      the type of the class being copied.
     *
     * @since 3.1.0
     */
    public <T extends Property<K, V>> Property(final T property)
    {
        this(property.key, property.value, property.comment);
    }

    /**
     * Create a new instance of {@code Property} as a copy of an existing
     * instance of {@code Property}, or one of its sub-classes.
     *
     * @param id       The unique id for this Property object.
     * @param property The instance to copy.
     * @param <T>      the type of the class being copied.
     *
     * @since 3.1.0
     */
    public <T extends Property<K, V>> Property(final int id, final T property)
    {
        this(property.key, property.value, property.comment);
    }

    /**
     * Create a new instance of {@code Property} with a {@code null} comment.
     *
     * @param key   The key.
     * @param value The value.
     *
     * @since 3.1.0
     */
    public Property(final K key, final V value)
    {
        this(key, value, null);
    }

    /**
     * Create a new instance of {@code Property} with a {@code null} comment.
     *
     * @param id    The unique id for this Property object.
     * @param key   The key.
     * @param value The value.
     *
     * @since 3.1.0
     */
    public Property(final int id, final K key, final V value)
    {
        this(id, key, value, null);
    }

    /**
     * Create a new instance of {@code Property}.
     *
     * @param key     The key.
     * @param value   The value.
     * @param comment The comment.
     *
     * @since 3.1.0
     */
    public Property(final K key, final V value, final String comment)
    {
        this.key = key;
        this.value = value;
        this.comment = comment;
        id = -1;
    }

    /**
     * Create a new instance of {@code Property}.
     *
     * @param id      The unique id for this Property object.
     * @param key     The key.
     * @param value   The value.
     * @param comment The comment.
     *
     * @since 3.1.0
     */
    public Property(final int id, final K key, final V value, final String comment)
    {
        this.id = id;
        this.key = key;
        this.value = value;
        this.comment = comment;
    }

    @Override
    public void addPropertyChangeListener(final PropertyChangeListener listener)
    {
        pcs.addPropertyChangeListener(listener);
    }

    @Override
    public void addPropertyChangeListener(final String propertyName, PropertyChangeListener listener)
    {
        pcs.addPropertyChangeListener(propertyName, listener);
    }

    /**
     * Get the field: {@code comment}.
     *
     * @return {@code comment} contents.
     *
     * @since 3.1.0
     */
    public String comment()
    {
        return comment;
    }

    @Override
    public int compareTo(final Property<K, V> other)
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
    public boolean equals(final Object obj)
    {
        return (obj instanceof Property<?, ?> other)
                && this.key.equals(other.key)
                && this.value.equals(other.value);
    }

    @Override
    public int getId()
    {
        return id;
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
     *
     * @since 3.1.0
     */
    public K key()
    {
        return key;
    }

    @Override
    public void removePropertyChangeListener(final PropertyChangeListener listener)
    {
        pcs.removePropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(final String propertyName, final PropertyChangeListener listener)
    {
        pcs.removePropertyChangeListener(propertyName, listener);
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
     *
     * @since 3.1.0
     */
    public V value()
    {
        return value;
    }
}
