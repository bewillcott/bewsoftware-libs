/*
 *  File Name:    MutableProperty.java
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

import java.beans.PropertyChangeSupport;

/**
 * This is a mutator sub-class of {@code Property}.
 * <p>
 * It provides mutator methods that can be used to change both:
 * {@code value} and {@code comment}.
 * <p>
 * If you intend to use the {@link PropertyChangeSupport} for tracking, then
 * you should use one of these constructors:
 * <ul>
 * <li>{@linkplain #MutableProperty(int, Property) MutableProperty(id, property)}</li>
 * <li>{@linkplain #MutableProperty(int, Comparable, Object) MutableProperty(id, key, value)}</li>
 * <li>{@linkplain #MutableProperty(int, Comparable, Object, String) MutableProperty(id, key, value, comment)}</li>
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
public final class MutableProperty<K extends Comparable<K>, V> extends Property<K, V>
{
    private static final long serialVersionUID = 4834750125364741184L;

    /**
     * Create a new instance of {@code MutableProperty} as a copy of an existing
     * instance of {@code Property}, or one of its sub-classes.
     *
     * @param property The instance to copy.
     * @param <T>      the type of the class being copied.
     *
     * @since 3.1.0
     */
    public <T extends Property<K, V>> MutableProperty(final T property)
    {
        super(property);
    }

    /**
     * Create a new instance of {@code MutableProperty} as a copy of an existing
     * instance of {@code Property}, or one of its sub-classes.
     * <p>
     * If you intend to use the {@link PropertyChangeSupport} for tracking, then
     * you should use this version of the constructor, instead of
     * {@link #MutableProperty(Property) }.
     *
     * @param id       The unique id for this Property object.
     * @param property The instance to copy.
     * @param <T>      the type of the class being copied.
     *
     * @since 3.1.0
     */
    public <T extends Property<K, V>> MutableProperty(final int id, final T property)
    {
        super(id, property);
    }

    /**
     * Create a new instance of {@code MutableProperty} with a {@code null} comment.
     * <p>
     * If you intend to use the {@link PropertyChangeSupport} for tracking, then
     * you should use this version of the constructor, instead of
     * {@link #MutableProperty(Comparable, Object)}.
     *
     * @param id    The unique id for this Property object.
     * @param key   The key.
     * @param value The value.
     *
     * @since 3.1.0
     */
    public MutableProperty(final int id, final K key, final V value)
    {
        super(id, key, value);
    }

    /**
     * Create a new instance of {@code MutableProperty}.
     * <p>
     * If you intend to use the {@link PropertyChangeSupport} for tracking, then
     * you should use this version of the constructor, instead of
     * {@link #MutableProperty(Comparable, Object, String)}.
     *
     * @param id      The unique id for this Property object.
     * @param key     The key.
     * @param value   The value.
     * @param comment The comment.
     *
     * @since 3.1.0
     */
    public MutableProperty(final int id, final K key, final V value, final String comment)
    {
        super(id, key, value, comment);
    }

    /**
     * Create a new instance of {@code MutableProperty} with a {@code null} comment.
     *
     * @param key   The key.
     * @param value The value.
     *
     * @since 3.1.0
     */
    public MutableProperty(final K key, final V value)
    {
        super(key, value);
    }

    /**
     * Create a new instance of {@code MutableProperty}.
     *
     * @param key     The key.
     * @param value   The value.
     * @param comment The comment.
     *
     * @since 3.1.0
     */
    public MutableProperty(final K key, final V value, final String comment)
    {
        super(key, value, comment);
    }

    /**
     * Set the field: {@code comment}.
     *
     * @param comment to be set.
     *
     * @since 3.1.0
     */
    public void comment(final String comment)
    {
        final String oldValue = this.comment;
        this.comment = comment;
        pcs.firePropertyChange(PROP_COMMENT, oldValue, comment);
    }

    /**
     * Set the field: {@code value}.
     *
     * @param value to be set.
     *
     * @since 3.1.0
     */
    public void value(final V value)
    {
        final V oldValue = this.value;
        this.value = value;
        pcs.firePropertyChange(PROP_VALUE, oldValue, value);
    }
}
