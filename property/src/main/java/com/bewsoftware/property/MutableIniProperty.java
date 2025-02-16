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

/**
 * This is a mutator sub-class of {@code IniProperty}. As with that class,
 * this one is designed to simplify working with <i>ini</i> files.
 * <p>
 * It provides mutator methods that can be used to change both:
 * {@code value} and {@code comment}.
 * <p>
 * Moved here from BEWCommons to make it easier to handle version updates.
 * This is a copy of v1.0.21, now reset to v1.0 for this project.
 *
 * @deprecated Moved to {@code bewsoftware.files/com.bewsoftware.fileio.property.MutableIniProperty}.
 *
 * @param <V> value type.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0
 * @version 3.1.0
 */
@Deprecated(forRemoval = true, since = "3.1.0")
public final class MutableIniProperty<V> extends IniProperty<V>
{

    /**
     * @serial serial
     */
    private static final long serialVersionUID = 7000108685520570407L;

    /**
     * Create a new instance of {@code MutableIniProperty} as a copy of an existing
     * instance of {@code Property}, or one of its sub-classes.
     *
     * @param <T>      the type of the class being copied.
     * @param property The instance to copy.
     */
    public <T extends Property<String, V>> MutableIniProperty(T property)
    {
        super(property.key, property.value, property.comment);
    }

    /**
     * Create a new instance of {@code MutableIniProperty} with a {@code null} comment.
     *
     * @param key   The key.
     * @param value The value.
     */
    public MutableIniProperty(String key, V value)
    {
        super(key, value);
    }

    /**
     * Create a new instance of {@code MutableIniProperty}.
     *
     * @param key     The key.
     * @param value   The value.
     * @param comment The comment.
     */
    public MutableIniProperty(String key, V value, String comment)
    {
        super(key, value, comment);
    }

    /**
     * Set the field: {@code comment}.
     *
     * @param comment to be set.
     */
    public void comment(String comment)
    {
        this.comment = comment;
    }

    /**
     * Set the field: {@code value}.
     *
     * @param value to be set.
     */
    public void value(V value)
    {
        this.value = value;
    }
}
