/*
 *  File Name:    MutableXmlProperty.java
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
 * <li>{@linkplain #MutableXmlProperty(int, Property) MutableXmlProperty(id, property)}</li>
 * <li>{@linkplain #MutableXmlProperty(int, String, String) MutableXmlProperty(id, key, value)}</li>
 * <li>{@linkplain #MutableXmlProperty(int, String, String, String) MutableXmlProperty(id, key, value, comment)}</li>
 * </ul>
 * Moved here from BEWSoftware Property Library since it is unlikely to
 * be used, except in conjunction with other classes from this
 * library.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 3.1.0
 * @version 3.1.0
 */
public final class MutableXmlProperty extends XmlProperty
{
    private static final long serialVersionUID = 1130990937634774392L;

    /**
     * Create a new instance of {@code MutableIniProperty} as a copy of an existing
     * instance of {@code Property}, or one of its sub-classes.
     *
     * @param <T>      the type of the class being copied.
     * @param property The instance to copy.
     *
     * @since 3.1.0
     */
    public <T extends Property<String, String>> MutableXmlProperty(final T property)
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
    public MutableXmlProperty(final String key, final String value)
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
    public MutableXmlProperty(final String key, final String value, final String comment)
    {
        super(key, value, comment);
    }

    /**
     * Create a new instance of {@code MutableIniProperty} as a copy of an existing
     * instance of {@code Property}, or one of its sub-classes.
     * <p>
     * If you intend to use the {@link PropertyChangeSupport} for tracking, then
     * you should use this version of the constructor.
     *
     * @param id       The unique id for this Property object.
     * @param property The instance to copy.
     * @param <T>      the type of the class being copied.
     *
     * @since 3.1.0
     */
    public <T extends Property<String, String>> MutableXmlProperty(final int id, T property)
    {
        super(id, property);
    }

    /**
     * Create a new instance of {@code MutableIniProperty} with a {@code null} comment.
     * <p>
     * If you intend to use the {@link PropertyChangeSupport} for tracking, then
     * you should use this version of the constructor.
     *
     * @param id    The unique id for this Property object.
     * @param key   The key.
     * @param value The value.
     *
     * @since 3.1.0
     */
    public MutableXmlProperty(final int id, String key, String value)
    {
        super(id, key, value);
    }

    /**
     * Create a new instance of {@code MutableIniProperty}.
     * <p>
     * If you intend to use the {@link PropertyChangeSupport} for tracking, then
     * you should use this version of the constructor.
     *
     * @param id      The unique id for this Property object.
     * @param key     The key.
     * @param value   The value.
     * @param comment The comment.
     *
     * @since 3.1.0
     */
    public MutableXmlProperty(final int id, String key, String value, String comment)
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
    public void value(final String value)
    {
        final String oldValue = this.value;
        this.value = value;
        pcs.firePropertyChange(PROP_VALUE, oldValue, value);
    }
}
