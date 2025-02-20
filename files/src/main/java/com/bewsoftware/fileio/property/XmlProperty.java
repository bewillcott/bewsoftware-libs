/*
 *  File Name:    XmlProperty.java
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
 * This is a more specialized sub-class of {@code Property}, in
 * that it simplifies working with <i>ini</i> files.
 * <p>
 * Primarily it sets up the {@code <K>} type of
 * {@code Property<K extends Comparable<K>, V>} as {@link String}, thus
 * leaving only the {@code <V>} type for the {@code value}, to track.
 * <p>
 * If you intend to use the {@link PropertyChangeSupport} for tracking, then
 * you should use one of these constructors:
 * <ul>
 * <li>{@linkplain #XmlProperty(int, Property) XmlProperty(id, property)}</li>
 * <li>{@linkplain #XmlProperty(int, String, String) XmlProperty(id, key, value)}</li>
 * <li>{@linkplain #XmlProperty(int, String, String, String) XmlProperty(id, key, value, comment)}</li>
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
public sealed class XmlProperty extends IniProperty<String> permits MutableXmlProperty
{
    private static final long serialVersionUID = 5380468967540265450L;

    /**
     * Create a new instance of {@code IniProperty} as a copy of an existing
     * instance of {@code Property}, or one of its sub-classes.
     *
     * @param property The instance to copy.
     * @param <T>      the type of the class being copied.
     *
     * @since 3.1.0
     */
    public <T extends Property<String, String>> XmlProperty(final T property)
    {
        super(property);
    }

    /**
     * Create a new instance of {@code IniProperty} with a {@code null} comment.
     *
     * @param key   The key.
     * @param value The value.
     *
     * @since 3.1.0
     */
    public XmlProperty(final String key, final String value)
    {
        super(key, value);
    }

    /**
     * Create a new instance of {@code IniProperty}.
     *
     * @param key     The key.
     * @param value   The value.
     * @param comment The comment.
     *
     * @since 3.1.0
     */
    public XmlProperty(final String key, final String value, final String comment)
    {
        super(key, value, comment);
    }

    /**
     * Create a new instance of {@code IniProperty} as a copy of an existing
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
    public <T extends Property<String, String>> XmlProperty(final int id, final T property)
    {
        super(id, property);
    }

    /**
     * Create a new instance of {@code IniProperty} with a {@code null} comment.
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
    public XmlProperty(final int id, final String key, final String value)
    {
        super(id, key, value);
    }

    /**
     * Create a new instance of {@code IniProperty}.
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
    public XmlProperty(final int id, final String key, final String value, final String comment)
    {
        super(id, key, value, comment);
    }
}
