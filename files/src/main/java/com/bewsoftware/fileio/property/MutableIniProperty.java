/*
 *  File Name:    MutableIniProperty.java
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
 * This is a mutator sub-class of {@code IniProperty}. As with that class,
 * this one is designed to simplify working with <i>ini</i> files.
 * <p>
 * It provides mutator methods that can be used to change both:
 * {@code value} and {@code comment}.
 * <p>
 * If you intend to use the {@link PropertyChangeSupport} for tracking, then
 * you should use one of these constructors:
 * <ul>
 * <li>{@linkplain #MutableIniProperty(int, Property) MutableIniProperty(id, property)}</li>
 * <li>{@linkplain #MutableIniProperty(int, String, Object) MutableIniProperty(id, key, value)}</li>
 * <li>{@linkplain #MutableIniProperty(int, String, Object, String) MutableIniProperty(id, key, value, comment)}</li>
 * </ul>
 * Moved here from BEWSoftware Property Library since it is unlikely to
 * be used, except in conjunction with other classes from this
 * library.
 *
 * @param <V> value type.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 3.1.0
 * @version 3.1.0
 */
public final class MutableIniProperty<V> extends IniProperty<V>
{
    private static final long serialVersionUID = 4053168366346430630L;

    /**
     * Create a new instance of {@code MutableIniProperty} as a copy of an existing
     * instance of {@code Property}, or one of its sub-classes.
     *
     * @param <T>      the type of the class being copied.
     * @param property The instance to copy.
     *
     * @since 3.1.0
     */
    public <T extends Property<String, V>> MutableIniProperty(final T property)
    {
        super(property);
    }

    /**
     * Create a new instance of {@code MutableIniProperty} with a {@code null} comment.
     *
     * @param key   The key.
     * @param value The value.
     *
     * @since 3.1.0
     */
    public MutableIniProperty(final String key, final V value)
    {
        super(key, value);
    }

    /**
     * Create a new instance of {@code MutableIniProperty}.
     *
     * @param key     The key.
     * @param value   The value.
     * @param comment The comment.
     *
     * @since 3.1.0
     */
    public MutableIniProperty(final String key, final V value, final String comment)
    {
        super(key, value, comment);
    }

    /**
     * Create a new instance of {@code MutableIniProperty} as a copy of an existing
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
    public <T extends Property<String, V>> MutableIniProperty(final int id, T property)
    {
        super(id, property);
    }

    /**
     * Create a new instance of {@code MutableIniProperty} with a {@code null} comment.
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
    public MutableIniProperty(final int id, String key, V value)
    {
        super(id, key, value);
    }

    /**
     * Create a new instance of {@code MutableIniProperty}.
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
    public MutableIniProperty(final int id, String key, V value, String comment)
    {
        super(id, key, value, comment);
    }

    /**
     * Set the field: {@code comment}.
     *
     * @param comment to be set.
     *
     * @since 3.1.0
     */
    public void comment(String comment)
    {
        this.comment = comment;
    }

    /**
     * Set the field: {@code value}.
     *
     * @param value to be set.
     *
     * @since 3.1.0
     */
    public void value(V value)
    {
        this.value = value;
    }
}
