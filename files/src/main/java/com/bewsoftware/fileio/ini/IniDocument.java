/*
 * This file is part of the BEW Files Library (aka: BEWFiles).
 *
 * Copyright (C) 2021 Bradley Willcott
 *
 * BEWFiles is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BEWFiles is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.bewsoftware.fileio.ini;

import com.bewsoftware.common.InvalidParameterValueException;
import com.bewsoftware.property.IniProperty;
import com.bewsoftware.property.MutableIniProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * NewInterface class description.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0
 * @version 1.0
 */
public interface IniDocument {

    /**
     * Test to see whether or not this <b>key</b> exists within this
     * <b>section</b> in the internal store.
     *
     * @param section possible section
     * @param key     possible key
     *
     * @return {@code true} if the key exists, {@code false} otherwise.
     *
     * @since 1.0
     */
    boolean containsKey(final String section, final String key);

    /**
     * Test to see whether or not this <b>section</b> exists within the
     * internal store.
     *
     * @param section possible section.
     *
     * @return {@code true} if the section exists, {@code false} otherwise.
     *
     * @since 1.0
     */
    default boolean containsSection(final String section) {
        return indexOfSection(section) > -1;
    }

    /**
     * Get the <i>value</i> of the <i>key</i> in the form of a
     * <b>boolean</b>.
     *
     * @param section      The section in which the key should reside.
     * @param key          The key whose value we are after.
     * @param defaultvalue The value to return if the key does not exist.
     *
     * @return Either the stored value or the defaultValue if key is not found.
     *
     * @throws NullPointerException if the specified key is {@code null}.
     * @since 1.0
     */
    default boolean getBoolean(final String section, final String key, final boolean defaultvalue) {
        String rtn = getValue(section, key);
        return rtn != null ? Boolean.parseBoolean(rtn) : defaultvalue;
    }

    /**
     * Get the <i>value</i> of the <i>key</i> from the global section, in the
     * form of a <b>boolean</b>.
     *
     * @param key          The key whose value we are after.
     * @param defaultvalue The value to return if the key does not exist.
     *
     * @return Either the stored value or the defaultValue if key is not found.
     *
     * @throws NullPointerException if the specified section is {@code null}.
     * @since 1.0
     */
    default boolean getBooleanG(final String key, final boolean defaultvalue) {
        return getBoolean(null, key, defaultvalue);
    }

    /**
     * Get the associated {@code comment} for this {@code key} from this
     * {@code section}, if it is set.Otherwise {@code null}.
     *
     * @param section The section in which the key should reside.
     * @param key     The key whose comment we are after.
     *
     * @return The comment or {@code null}.
     */
    String getComment(final String section, final String key);

    /**
     * Get the associated {@code comment} for this {@code key} from the
     * global section.
     *
     * @param key The key whose comment we are after.
     *
     * @return the comment or {@code null}
     *
     * @since 1.0
     */
    default String getCommentG(final String key) {
        return getComment(null, key);
    }

    /**
     * Get the <i>value</i> of the <i>key</i> in the form of a
     * <b>double</b>.
     *
     * @param section      The section in which the key should reside.
     * @param key          The key whose value we are after.
     * @param defaultvalue The value to return if the key does not exist.
     *
     * @return Either the stored value or the defaultValue if key is not found.
     *
     * @since 1.0
     */
    default double getDouble(final String section, final String key, final double defaultvalue) {
        String rtn = getValue(section, key);
        return rtn != null ? Double.parseDouble(rtn) : defaultvalue;
    }

    /**
     * Get the <i>value</i> of the <i>key</i> from the global section, in the
     * form of a <b>double</b>.
     *
     * @param key          The key whose value we are after.
     * @param defaultvalue The value to return if the key does not exist.
     *
     * @return Either the stored value or the defaultValue if key is not found.
     *
     * @since 1.0
     */
    default double getDoubleG(final String key, final double defaultvalue) {
        return getDouble(null, key, defaultvalue);
    }

    /**
     * Get the <i>value</i> of the <i>key</i> in the form of a
     * <b>float</b>.
     *
     * @param section      The section in which the key should reside.
     * @param key          The key whose value we are after.
     * @param defaultvalue The value to return if the key does not exist.
     *
     * @return Either the stored value or the defaultValue if key is not found.
     *
     * @since 1.0
     */
    default float getFloat(final String section, final String key, final float defaultvalue) {
        String rtn = getValue(section, key);
        return rtn != null ? Float.parseFloat(rtn) : defaultvalue;
    }

    /**
     * Get the <i>value</i> of the <i>key</i> from the global section, in the
     * form of a <b>float</b>.
     *
     * @param key          The key whose value we are after.
     * @param defaultvalue The value to return if the key does not exist.
     *
     * @return Either the stored value or the defaultValue if key is not found.
     *
     * @since 1.0
     */
    default float getFloatG(final String key, final float defaultvalue) {
        return getFloat(null, key, defaultvalue);
    }

    /**
     * Get the <i>value</i> of the <i>key</i> in the form of a
     * <b>int</b>.
     *
     * @param section      The section in which the key should reside.
     * @param key          The key whose value we are after.
     * @param defaultvalue The value to return if the key does not exist.
     *
     * @return Either the stored value or the defaultValue if key is not found.
     *
     * @since 1.0
     */
    default int getInt(final String section, final String key, final int defaultvalue) {
        String rtn = getValue(section, key);
        return rtn != null ? Integer.parseInt(rtn) : defaultvalue;
    }

    /**
     * Get the <i>value</i> of the <i>key</i> from the global section, in the
     * form of a <b>int</b>.
     *
     * @param key          The key whose value we are after.
     * @param defaultvalue The value to return if the key does not exist.
     *
     * @return Either the stored value or the defaultValue if key is not found.
     *
     * @since 1.0
     */
    default int getIntG(final String key, final int defaultvalue) {
        return getInt(null, key, defaultvalue);
    }

    /**
     * Get the <i>value</i> of the <i>key</i> in the form of a
     * <b>long</b>.
     *
     * @param section      The section in which the key should reside.
     * @param key          The key whose value we are after.
     * @param defaultvalue The value to return if the key does not exist.
     *
     * @return Either the stored value or the defaultValue if key is not found.
     *
     * @since 1.0
     */
    default long getLong(final String section, final String key, final long defaultvalue) {
        String rtn = getValue(section, key);
        return rtn != null ? Long.parseLong(rtn) : defaultvalue;
    }

    /**
     * Get the <i>value</i> of the <i>key</i> from the global section, in the
     * form of a <b>long</b>.
     *
     * @param key          The key whose value we are after.
     * @param defaultvalue The value to return if the key does not exist.
     *
     * @return Either the stored value or the defaultValue if key is not found.
     *
     * @since 1.0
     */
    default long getLongG(final String key, final long defaultvalue) {
        return getLong(null, key, defaultvalue);
    }

    /**
     * Get the {@code section}'s list of properties.
     * <p>
     * <b>Changes:</b>
     * <dl>
     * <dt>v1.0.6</dt>
     * <dd>Return changed from {@link ArrayList ArrayList&lt;&gt;} to {@link List List&lt;&gt;}</dd>
     * </dl>
     * XXX
     *
     * @param section Name of the section.
     *
     * @return List of properties.
     *
     * @since 1.0
     */
    List<IniProperty<String>> getSection(final String section);

    /**
     * Create an empty section with no comment.
     *
     * @param section New section.
     *
     * @since 1.0
     */
    default void setSection(final String section) {
        setSection(section, null);
    }

    /**
     * Get the {@code comment} for this {@code section}.
     * <p>
     * Will also return {@code null} if there is no section by that name.
     *
     * @param section Name of the section.
     *
     * @return The comment if set, otherwise {@code null}.
     *
     * @since 1.0
     */
    String getSectionComment(final String section);

    /**
     * Get list of all sections.
     *
     * @return list of all sections.
     */
    List<String> getSections();

    /**
     * Get the <i>value</i> of the <i>key</i> in the form of a
     * <b>String</b>.
     *
     * @param section      The section in which the key should reside.
     * @param key          The key whose value we are after.
     * @param defaultvalue The value to return if the key does not exist.
     *
     * @return Either the stored value or the defaultValue if key is not found.
     *
     * @since 1.0
     */
    default String getString(final String section, final String key, final String defaultvalue) {
        String rtn = getValue(section, key);
        return rtn != null ? rtn : defaultvalue;
    }

    /**
     * Get the <i>value</i> of the <i>key</i> from the global section, in the
     * form of a <b>String</b>.
     *
     * @param key          The key whose value we are after.
     * @param defaultvalue The value to return if the key does not exist.
     *
     * @return Either the stored value or the defaultValue if key is not found.
     *
     * @since 1.0
     */
    default String getStringG(final String key, final String defaultvalue) {
        return getString(null, key, defaultvalue);
    }

    /**
     * Get the value to which the specified section/key is mapped, or
     * {@code null} if this map contains no mapping for the section/key.
     * <p>
     * More formally, if this map contains a section with a mapping from a key
     * {@code k} to a value {@code v} such that {@code key.equals(k)}, then this
     * method returns {@code v}; otherwise it returns {@code null}. (There can
     * be at most one such mapping.)
     * <p>
     * Used by the {@code public get*(...)} methods.
     *
     * @param section The section in which the key should reside.
     * @param key     The key whose value we are after.
     *
     * @return the stored value or {@code null}.
     *
     * @since 1.0
     */
    String getValue(final String section, final String key);

    /**
     * Refer to {@link #indexOfSection(java.lang.String)}
     *
     * @param kvlist List to look in.
     * @param key    Key to look for.
     *
     * @return index of 'key', or '-1' if not found.
     */
    default int indexOfKey(final List<MutableIniProperty<String>> kvlist, final String key) {
        for (int i = 0; i < kvlist.size(); i++)
        {
            String lkey = kvlist.get(i).key();

            if (lkey.equals(key))
            {
                return i;
            }
        }

        return -1;
    }

    /**
     * This method replaces the method: {@link ArrayList#indexOf(java.lang.Object) }.
     * <p>
     * The reason being, {@code indexOf} puts the parameter on the left side of the
     * {@code equals()} call. I have developed the {@link MutableIniProperty} class to test
     * equality against a String:
     * <pre><code>
     *
     *    MutableIniProperty&lt;ArrayList&lt;String&gt;&gt; mp = new MutableIniProperty&lt;&gt;();
     *
     *    ...
     *
     *    return mp.equals(obj);
     * </code></pre>
     *
     * @param section searching for.
     *
     * @return its index.
     */
    int indexOfSection(final String section);

    Iterable<MutableIniProperty<List<MutableIniProperty<String>>>> iterable();

    /**
     * Remove a <b>key/value</b> pair, from this <b>section</b>, from the in
     * memory copy of the ini file.
     *
     * @param section The section containing the key/value pair.
     * @param key     The key to remove.
     */
    void removeKey(final String section, final String key);

    /**
     * Remove a <b>section</b> from the in memory copy of the ini file.
     *
     * @param section Section to remove.
     */
    void removeSection(final String section);

    /**
     * Set a property with a value of type: <i>boolean</i>.
     *
     * @param section The ini section to set the key/value pair into to.
     * @param key     The key label. <b>Must not be {@code null}.</b>
     * @param value   The value text to store with the key.
     *
     * @return The previous value of this {@code key},<br>
     * or <i>null</i> if no previous value.
     *
     * @since 1.0
     */
    default Boolean setBoolean(final String section, final String key, final boolean value) {
        String rtn = setString(section, key, Boolean.toString(value));
        return rtn != null ? Boolean.parseBoolean(rtn) : null;
    }

    /**
     * Set a property with a value of type: <i>boolean</i>.
     *
     * @param section The ini section to set the key/value pair into to.
     * @param key     The key label. <b>Must not be {@code null}.</b>
     * @param value   The value text to store with the key.
     * @param comment Associated comment.
     *
     * @return The previous value of this {@code key},<br>
     * or <i>null</i> if no previous value.
     *
     * @throws InvalidParameterValueException If the {@code comment} is not a
     *                                        valid <u>ini</u> file format
     *                                        comment.
     * @since 1.0
     */
    default Boolean setBoolean(final String section, final String key, final boolean value, final String comment)
            throws InvalidParameterValueException {
        String rtn = setString(section, key, Boolean.toString(value), comment);
        return rtn != null ? Boolean.parseBoolean(rtn) : null;
    }

    /**
     * Set a property with a value of type: <i>boolean</i>, in the
     * global section.
     *
     * @param key   The key label. <b>Must not be {@code null}.</b>
     * @param value The value text to store with the key.
     *
     * @return The previous value of this {@code key},<br>
     * or <i>null</i> if no previous value.
     *
     *
     * @since 1.0
     */
    default Boolean setBooleanG(final String key, final boolean value) {
        String rtn = setStringG(key, Boolean.toString(value));
        return rtn != null ? Boolean.parseBoolean(rtn) : null;
    }

    /**
     * Set a property with a value of type: <i>boolean</i>, in the
     * global section.
     *
     * @param key     The key label. <b>Must not be {@code null}.</b>
     * @param value   The value text to store with the key.
     * @param comment Associated comment.
     *
     * @return The previous value of this {@code key},<br>
     * or <i>null</i> if no previous value.
     *
     *
     * @throws InvalidParameterValueException If the {@code comment} is not a
     *                                        valid <u>ini</u> file format
     *                                        comment.
     * @since 1.0
     */
    default Boolean setBooleanG(final String key, final boolean value, final String comment)
            throws InvalidParameterValueException {
        String rtn = setStringG(key, Boolean.toString(value), comment);
        return rtn != null ? Boolean.parseBoolean(rtn) : null;
    }

    /**
     * Set the {@code comment} for this {@code key} in this {@code section}.
     *
     * @param section The ini section to store the key and comment into. Use
     *                {@code null} to access the <b>global section</b>.
     * @param key     The key label. <b>Must not be {@code null}.</b>
     * @param comment Associated comment.
     *
     * @return The previous comment of this {@code key},<br>
     * or <i>null</i> if no previous value.
     *
     * @throws InvalidParameterValueException If the {@code comment} is not a
     *                                        valid <u>ini</u> file format
     *                                        comment.
     * @throws NullPointerException           If {@code key} is {@code null}.
     * @since 1.0
     */
    String setComment(final String section, final String key, final String comment) throws InvalidParameterValueException;

    /**
     * Set the {@code comment} for this {@code key} in the global section.
     *
     * @param key     The key label. <b>Must not be {@code null}.</b>
     * @param comment Associated comment.
     *
     * @return The previous comment of this {@code key},<br>
     * or <i>null</i> if no previous value.
     *
     *
     * @throws InvalidParameterValueException If the {@code comment} is not a
     *                                        valid <u>ini</u> file format
     *                                        comment.
     * @since 1.0
     */
    default String setCommentG(final String key, final String comment)
            throws InvalidParameterValueException {
        return setComment(null, key, comment);
    }

    /**
     * Set a property with a value of type: <i>double</i>.
     *
     * @param section The ini section to set the key/value pair into to.
     * @param key     The key label. <b>Must not be {@code null}.</b>
     * @param value   The value text to store with the key.
     *
     * @return The previous value of this {@code key},<br>
     * or <i>null</i> if no previous value.
     *
     * @since 1.0
     */
    default Double setDouble(final String section, final String key, final double value) {
        String rtn = setString(section, key, Double.toString(value));
        return rtn != null ? Double.parseDouble(rtn) : null;
    }

    /**
     * Set a property with a value of type: <i>double</i>.
     *
     * @param section The ini section to set the key/value pair into to.
     * @param key     The key label. <b>Must not be {@code null}.</b>
     * @param value   The value text to store with the key.
     * @param comment Associated comment.
     *
     * @return The previous value of this {@code key},<br>
     * or <i>null</i> if no previous value.
     *
     *
     * @throws InvalidParameterValueException If the {@code comment} is not a
     *                                        valid <u>ini</u> file format
     *                                        comment.
     * @since 1.0
     */
    default Double setDouble(final String section, final String key, final double value, final String comment)
            throws InvalidParameterValueException {
        String rtn = setString(section, key, Double.toString(value), comment);
        return rtn != null ? Double.parseDouble(rtn) : null;
    }

    /**
     * Set a property with a value of type: <i>double</i>, in the global
     * section.
     *
     * @param key   The key label. <b>Must not be {@code null}.</b>
     * @param value The value text to store with the key.
     *
     * @return The previous value of this {@code key},<br>
     * or <i>null</i> if no previous value.
     *
     *
     * @since 1.0
     */
    default Double setDoubleG(final String key, final double value) {
        String rtn = setStringG(key, Double.toString(value));
        return rtn != null ? Double.parseDouble(rtn) : null;
    }

    /**
     * Set a property with a value of type: <i>double</i>, in the global
     * section.
     *
     * @param key     The key label. <b>Must not be {@code null}.</b>
     * @param value   The value text to store with the key.
     * @param comment Associated comment.
     *
     * @return The previous value of this {@code key},<br>
     * or <i>null</i> if no previous value.
     *
     *
     * @throws InvalidParameterValueException If the {@code comment} is not a
     *                                        valid <u>ini</u> file format
     *                                        comment.
     * @since 1.0
     */
    default Double setDoubleG(final String key, final double value, final String comment)
            throws InvalidParameterValueException {
        String rtn = setStringG(key, Double.toString(value), comment);
        return rtn != null ? Double.parseDouble(rtn) : null;
    }

    /**
     * Set a property with a value of type: <i>float</i>.
     *
     * @param section The ini section to set the key/value pair into to.
     * @param key     The key label. <b>Must not be {@code null}.</b>
     * @param value   The value text to store with the key.
     *
     * @return The previous value of this {@code key},<br>
     * or <i>null</i> if no previous value.
     *
     *
     * @since 1.0
     */
    default Float setFloat(final String section, final String key, final float value) {
        String rtn = setString(section, key, Float.toString(value));
        return rtn != null ? Float.parseFloat(rtn) : null;
    }

    /**
     * Set a property with a value of type: <i>float</i>.
     *
     * @param section The ini section to set the key/value pair into to.
     * @param key     The key label. <b>Must not be {@code null}.</b>
     * @param value   The value text to store with the key.
     * @param comment Associated comment.
     *
     * @return The previous value of this {@code key},<br>
     * or <i>null</i> if no previous value.
     *
     *
     * @throws InvalidParameterValueException If the {@code comment} is not a
     *                                        valid <u>ini</u> file format
     *                                        comment.
     * @since 1.0
     */
    default Float setFloat(final String section, final String key, final float value, final String comment)
            throws InvalidParameterValueException {
        String rtn = setString(section, key, Float.toString(value), comment);
        return rtn != null ? Float.parseFloat(rtn) : null;
    }

    /**
     * Set a property with a value of type: <i>float</i>, in the global
     * section.
     *
     * @param key   The key label. <b>Must not be {@code null}.</b>
     * @param value The value text to store with the key.
     *
     * @return The previous value of this {@code key},<br>
     * or <i>null</i> if no previous value.
     *
     *
     * @since 1.0
     */
    default Float setFloatG(final String key, final float value) {
        String rtn = setStringG(key, Float.toString(value));
        return rtn != null ? Float.parseFloat(rtn) : null;
    }

    /**
     * Set a property with a value of type: <i>Float</i>, in the global
     * section.
     *
     * @param key     The key label. <b>Must not be {@code null}.</b>
     * @param value   The value text to store with the key.
     * @param comment Associated comment.
     *
     * @return The previous value of this {@code key},<br>
     * or <i>null</i> if no previous value.
     *
     *
     * <u>key</u>.
     *
     * @throws InvalidParameterValueException If the {@code comment} is not a
     *                                        valid <u>ini</u> file format
     *                                        comment.
     * @since 1.0
     */
    default Float setFloatG(final String key, final Float value, final String comment)
            throws InvalidParameterValueException {
        String rtn = setStringG(key, Float.toString(value), comment);
        return rtn != null ? Float.parseFloat(rtn) : null;
    }

    /**
     * Set a property with a value of type: <i>int</i>.
     *
     * @param section The ini section to set the key/value pair into to.
     * @param key     The key label. <b>Must not be {@code null}.</b>
     * @param value   The value text to store with the key.
     *
     * @return The previous value of this {@code key},<br>
     * or <i>null</i> if no previous value.
     *
     *
     * @since 1.0
     */
    default Integer setInt(final String section, final String key, final int value) {
        String rtn = setString(section, key, Integer.toString(value));
        return rtn != null ? Integer.parseInt(rtn) : null;
    }

    /**
     * Set a property with a value of type: <i>int</i>.
     *
     * @param section The ini section to set the key/value pair into to.
     * @param key     The key label. <b>Must not be {@code null}.</b>
     * @param value   The value text to store with the key.
     * @param comment Associated comment.
     *
     * @return The previous value of this {@code key},<br>
     * or <i>null</i> if no previous value.
     *
     *
     * <u>key</u>.
     *
     * @throws InvalidParameterValueException If the {@code comment} is not a
     *                                        valid <u>ini</u> file format
     *                                        comment.
     * @since 1.0
     */
    default Integer setInt(final String section, final String key, final int value, final String comment)
            throws InvalidParameterValueException {
        String rtn = setString(section, key, Integer.toString(value), comment);
        return rtn != null ? Integer.parseInt(rtn) : null;
    }

    /**
     * Set a property with a value of type: <i>int</i>, in the global
     * section.
     *
     * @param key   The key label. <b>Must not be {@code null}.</b>
     * @param value The value text to store with the key.
     *
     * @return The previous value of this {@code key},<br>
     * or <i>null</i> if no previous value.
     *
     *
     * @since 1.0
     */
    default Integer setIntG(final String key, final int value) {
        String rtn = setStringG(key, Integer.toString(value));
        return rtn != null ? Integer.parseInt(rtn) : null;
    }

    /**
     * Set a property with a value of type: <i>int</i>, in the global
     * section.
     *
     * @param key     The key label. <b>Must not be {@code null}.</b>
     * @param value   The value text to store with the key.
     * @param comment Associated comment.
     *
     * @return The previous value of this {@code key},<br>
     * or <i>null</i> if no previous value.
     *
     *
     * <u>key</u>.
     *
     * @throws InvalidParameterValueException If the {@code comment} is not a
     *                                        valid <u>ini</u> file format
     *                                        comment.
     * @since 1.0
     */
    default Integer setIntG(final String key, final int value, final String comment)
            throws InvalidParameterValueException {
        String rtn = setStringG(key, Integer.toString(value), comment);
        return rtn != null ? Integer.parseInt(rtn) : null;
    }

    /**
     * Set a property with a value of type: <i>long</i>.
     *
     * @param section The ini section to set the key/value pair into to.
     * @param key     The key label. <b>Must not be {@code null}.</b>
     * @param value   The value text to store with the key.
     *
     * @return The previous value of this {@code key},<br>
     * or <i>null</i> if no previous value.
     *
     *
     * @since 1.0
     */
    default Long setLong(final String section, final String key, final long value) {
        String rtn = setString(section, key, Long.toString(value));
        return rtn != null ? Long.parseLong(rtn) : null;
    }

    /**
     * Set a property with a value of type: <i>long</i>.
     *
     * @param section The ini section to set the key/value pair into to.
     * @param key     The key label. <b>Must not be {@code null}.</b>
     * @param value   The value text to store with the key.
     * @param comment Associated comment.
     *
     * @return The previous value of this {@code key},<br>
     * or <i>null</i> if no previous value.
     *
     *
     * <u>key</u>.
     *
     * @throws InvalidParameterValueException If the {@code comment} is not a
     *                                        valid <u>ini</u> file format
     *                                        comment.
     * @since 1.0
     */
    default Long setLong(final String section, final String key, final long value, final String comment)
            throws InvalidParameterValueException {
        String rtn = setString(section, key, Long.toString(value), comment);
        return rtn != null ? Long.parseLong(rtn) : null;
    }

    /**
     * Set a property with a value of type: <i>long</i>, in the global
     * section.
     *
     * @param key   The key label. <b>Must not be {@code null}.</b>
     * @param value The value text to store with the key.
     *
     * @return The previous value of this {@code key},<br>
     * or <i>null</i> if no previous value.
     *
     *
     * @since 1.0
     */
    default Long setLongG(final String key, final long value) {
        String rtn = setStringG(key, Long.toString(value));
        return rtn != null ? Long.parseLong(rtn) : null;
    }

    /**
     * Set a property with a value of type: <i>long</i>, in the global
     * section.
     *
     * @param key     The key label. <b>Must not be {@code null}.</b>
     * @param value   The value text to store with the key.
     * @param comment Associated comment.
     *
     * @return The previous value of this {@code key},<br>
     * or <i>null</i> if no previous value.
     *
     *
     * <u>key</u>.
     *
     * @throws InvalidParameterValueException If the {@code comment} is not a
     *                                        valid <u>ini</u> file format
     *                                        comment.
     * @since 1.0
     */
    default Long setLongG(final String key, final long value, final String comment)
            throws InvalidParameterValueException {
        String rtn = setStringG(key, Long.toString(value), comment);
        return rtn != null ? Long.parseLong(rtn) : null;
    }

    /**
     * Create an empty section with a comment.
     *
     * @param section New section.
     * @param comment New comment.
     *
     * @since 1.0
     */
    void setSection(final String section, final String comment);

    /**
     * Set a property with a value of type: <i>String</i>.
     *
     * @param section The ini section to store the key/value pair into. Use
     *                {@code null} to access the <b>global section</b>.
     * @param key     The key label. <b>Must not be {@code null}.</b>
     * @param value   The value text to store with the key.
     *
     * @return The previous value of this {@code key},<br>
     * or <i>null</i> if no previous value.
     *
     * <u>section</u> or <u>key</u>.
     *
     * @since 1.0
     */
    default String setString(final String section, final String key, final String value) {
        String rtn = null;

        try
        {
            return setString(section, key, value, null);
        } catch (InvalidParameterValueException ex)
        {
            // Ignore exception, as it won't ever be thrown.
        }

        return rtn;
    }

    /**
     * Set a property with a value of type: <i>String</i>.
     *
     * @param section The ini section to store the key/value pair into. Use
     *                {@code null} to access the <b>global section</b>.
     * @param key     The key label. <b>Must not be {@code null}.</b>
     * @param value   The value text to store with the key.
     * @param comment Associated comment.
     *
     * @return The previous value of this {@code key},<br>
     * or <i>null</i> if no previous value.
     *
     * @throws InvalidParameterValueException If the {@code comment} is not a
     *                                        valid <u>ini</u> file format
     *                                        comment.
     * @since 1.0
     */
    String setString(final String section, final String key, final String value, final String comment) throws InvalidParameterValueException;

    /**
     * Set a property with a value of type: <i>String</i>.The key is
     * stored in the global section.
     *
     * @param key   The key label. <b>Must not be {@code null}.</b>
     * @param value The value text to store with the key.
     *
     * @return The previous value of this {@code key},<br>
     * or <i>null</i> if no previous value.
     *
     *
     * @since 1.0
     */
    default String setStringG(final String key, final String value) {
        return setString(null, key, value);
    }

    /**
     * Set a property with a value of type: <i>String</i>.The key is
     * stored in the global section.
     *
     * @param key     The key label. <b>Must not be {@code null}.</b>
     * @param value   The value text to store with the key.
     * @param comment Associated comment.
     *
     * @return The previous value of this {@code key},<br>
     * or <i>null</i> if no previous value.
     *
     *
     * @throws InvalidParameterValueException If the {@code comment} is not a
     *                                        valid <u>ini</u> file format
     *                                        comment.
     * @since 1.0
     */
    default String setStringG(final String key, final String value, final String comment)
            throws InvalidParameterValueException {
        return setString(null, key, value, comment);
    }

    /**
     * Check the validity of the {@code comment}.
     *
     * @param comment To be checked.
     *
     * @return {@code true} if validation is successful, {@code false}
     *         otherwise.
     *
     * @since 1.0
     */
    boolean validateComment(final String comment);
}
