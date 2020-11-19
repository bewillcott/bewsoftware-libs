/*
 * This file is part of the BEW Files Library (aka: BEWFiles).
 *
 * Copyright (C) 2020 Bradley Willcott
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
package com.bew.fileio.ini;

import com.bew.common.InvalidParameterValueException;
import com.bew.property.IniProperty;
import com.bew.property.MutableIniProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * This class stores the contents of an <u>ini</u> type formatted properties
 * file.
 * <p>
 * It is compliant with the following structures:
 * </p>
 * <pre><code>
 *
 * key=value
 *
 * [section]
 * key=value
 * </code></pre>
 * <p>
 * For more specific file format compatibility information see:
 * {@link IniFile}.
 * </p>
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0
 * @version 1.0.20
 */
public class IniDocument {

    /**
     * Regex pattern for validating a comment.
     *
     * @since 1.0
     */
    public static final String COMMENT_PATTERN;

    /**
     * Regex pattern for parsing an <u>ini</u> format file.
     *
     * @since 1.0
     */
    public static final String INI_PATTERN;

    private static final String[] INI_PATTERNS =
    {
        "^(?:\\[(?<Section>[^\\]]*)\\])",
        "(?<Comment>^[#;][ \\t]+.*)",
        "(?:(?<Key>^[^#;=]+)=)(?:(?<==)(?<Value>.*))",
        "(?<Tail>.*)"
    };

    private static final String INI_COMMENT = INI_PATTERNS[1];
    private static final String INI_PROPERTY = INI_PATTERNS[2];
    private static final String INI_SECTION = INI_PATTERNS[0];
    private static final String INI_TAIL = INI_PATTERNS[3];
    private static final String NULL_KEY_MSG = "A null key is not valid.";

    // Initialise final fields.
    static
    {
        INI_PATTERN = String.join("|", INI_PATTERNS);
        COMMENT_PATTERN = String.join("|", INI_COMMENT, INI_TAIL);
    }

    /**
     * Checks the validity of the {@code comment}.
     *
     * @param comment To be checked.
     *
     * @return {@code true} if validation is successful, {@code false}
     *         otherwise.
     *
     * @since 1.0
     */
    public static boolean validateComment(String comment) {
        if (comment == null)
        {
            return true;
        } else if (comment.isEmpty())
        {
            return false;
        } else
        {
            Pattern p = Pattern.compile(COMMENT_PATTERN);
            Matcher m = p.matcher(comment);
            m.find();
            return (m.group("Comment") != null);
        }
    }

    /**
     * This is the hierarchical storage structure for the properties.
     *
     * @since 1.0
     */
    final ArrayList<MutableIniProperty<ArrayList<MutableIniProperty<Object>>>> entries;

    IniDocument() {
        entries = new ArrayList<>();
        entries.add(new MutableIniProperty<>(null, new ArrayList<>()));
    }

    /**
     * Tests to see whether or not this <b>key</b> exists within this
     * <b>section</b> in the {@link #entries} store.
     *
     * @param section possible section
     * @param key     possible key
     *
     * @return {@code true} if the key exists, {@code false} otherwise.
     *
     * @since 1.0
     */
    public boolean containsKey(String section, String key) {
        int sectionIdx = indexOfSection(section);

        if (sectionIdx > -1)
        {
            ArrayList<MutableIniProperty<Object>> kvlist = entries.get(sectionIdx).value();
            return (indexOfKey(kvlist, key) > -1);
        } else
        {
            return false;
        }

    }

    /**
     * Tests to see whether or not this <b>key</b> exists within the
     * <a >global section</a> in the {@link #entries} store. This is similar to
     * a normal {@link java.util.Properties Properties} file, and therefore
     * means that this is a possible alternative to
     * {@code java.util.Properties}.
     *
     * @param key possible key.
     *
     * @return {@code true} if the key exists, {@code false} otherwise.
     *
     * @since 1.0
     */
    public boolean containsKeyG(String key) {
        return containsKey(null, key);
    }

    /**
     * Tests to see whether or not this <b>section</b> exists within the
     * {@link #entries} store.
     *
     * @param section possible section.
     *
     * @return {@code true} if the section exists, {@code false} otherwise.
     *
     * @since 1.0
     */
    public boolean containsSection(String section) {
        return indexOfSection(section) > -1;
    }

    /**
     * Obtain the <i>value</i> of the <i>key</i> in the form of a
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
    public boolean getBoolean(String section, String key, boolean defaultvalue) {
        String rtn = getValue(section, key);
        return rtn != null ? Boolean.parseBoolean(rtn) : defaultvalue;
    }

    /**
     * Obtain the <i>value</i> of the <i>key</i> from the global section, in the
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
    public boolean getBooleanG(String key, boolean defaultvalue) {
        return getBoolean(null, key, defaultvalue);
    }

    /**
     * Returns the associated {@code comment} for this {@code key} from this
     * {@code section}, if it is set.Otherwise {@code null}.
     *
     * @param section The section in which the key should reside.
     * @param key     The key whose comment we are after.
     *
     * @return The comment or {@code null}.
     *
     * @since 1.0
     */
    public String getComment(String section, String key) {
        int sectionIdx = indexOfSection(section);

        if (sectionIdx > -1)
        {
            ArrayList<MutableIniProperty<Object>> kvlist = entries.get(sectionIdx).value();
            int keyIdx = indexOfKey(kvlist, key);
            return keyIdx > -1 ? kvlist.get(keyIdx).comment() : null;
        } else
        {
            return null;
        }
    }

    /**
     * Returns the associated {@code comment} for this {@code key} from the
     * global section, if iOtherwise {@code null}.tde null}.
     *
     * @param key The key whose comment we are after.
     *
     * @return The comment or {@code null}.
     *
     * @since 1.0
     */
    public String getCommentG(String key) {
        return getComment(null, key);
    }

    /**
     * Obtain the <i>value</i> of the <i>key</i> in the form of a
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
    public double getDouble(String section, String key, double defaultvalue) {
        String rtn = getValue(section, key);
        return rtn != null ? Double.parseDouble(rtn) : defaultvalue;
    }

    /**
     * Obtain the <i>value</i> of the <i>key</i> from the global section, in the
     * form of a <b>double</b>.
     *
     * @param key          The key whose value we are after.
     * @param defaultvalue The value to return if the key does not exist.
     *
     * @return Either the stored value or the defaultValue if key is not found.
     *
     * @since 1.0
     */
    public double getDoubleG(String key, double defaultvalue) {
        return getDouble(null, key, defaultvalue);
    }

    /**
     * Obtain the <i>value</i> of the <i>key</i> in the form of a
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
    public float getFloat(String section, String key, float defaultvalue) {
        String rtn = getValue(section, key);
        return rtn != null ? Float.parseFloat(rtn) : defaultvalue;
    }

    /**
     * Obtain the <i>value</i> of the <i>key</i> from the global section, in the
     * form of a <b>float</b>.
     *
     * @param key          The key whose value we are after.
     * @param defaultvalue The value to return if the key does not exist.
     *
     * @return Either the stored value or the defaultValue if key is not found.
     *
     * @since 1.0
     */
    public float getFloatG(String key, float defaultvalue) {
        return getFloat(null, key, defaultvalue);
    }

    /**
     * Obtain the <i>value</i> of the <i>key</i> in the form of a
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
    public int getInt(String section, String key, int defaultvalue) {
        String rtn = getValue(section, key);
        return rtn != null ? Integer.parseInt(rtn) : defaultvalue;
    }

    /**
     * Obtain the <i>value</i> of the <i>key</i> from the global section, in the
     * form of a <b>int</b>.
     *
     * @param key          The key whose value we are after.
     * @param defaultvalue The value to return if the key does not exist.
     *
     * @return Either the stored value or the defaultValue if key is not found.
     *
     * @since 1.0
     */
    public int getIntG(String key, int defaultvalue) {
        return getInt(null, key, defaultvalue);
    }

    /**
     * Obtain the <i>value</i> of the <i>key</i> in the form of a
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
    public long getLong(String section, String key, long defaultvalue) {
        String rtn = getValue(section, key);
        return rtn != null ? Long.parseLong(rtn) : defaultvalue;
    }

    /**
     * Obtain the <i>value</i> of the <i>key</i> from the global section, in the
     * form of a <b>long</b>.
     *
     * @param key          The key whose value we are after.
     * @param defaultvalue The value to return if the key does not exist.
     *
     * @return Either the stored value or the defaultValue if key is not found.
     *
     * @since 1.0
     */
    public long getLongG(String key, long defaultvalue) {
        return getLong(null, key, defaultvalue);
    }

    /**
     * Returns {@code section}'s list of properties.
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
    public List<IniProperty<Object>> getSection(String section) {
        int sectionIdx = indexOfSection(section);

        if (sectionIdx > -1)
        {
            ArrayList<IniProperty<Object>> rtnList = new ArrayList<>();
            entries.get(sectionIdx).value().forEach(prop
                    -> rtnList.add((IniProperty<Object>) prop));

            return rtnList;
        } else
        {
            return null;
        }
    }

    /**
     * Use to create an empty section with no comment.
     *
     * @param section New section.
     *
     * @since 1.0
     */
    public void setSection(String section) {
        setSection(section, null);
    }

    /**
     * Gets the {@code comment} for this {@code section}.
     * <p>
     * Will also return {@code null} if there is no section by that name.
     *
     * @param section Name of the section.
     *
     * @return The comment if set, otherwise {@code null}.
     *
     * @since 1.0
     */
    public String getSectionComment(String section) {
        int sectionIdx = indexOfSection(section);

        if (sectionIdx > -1)
        {
            return entries.get(sectionIdx).comment();
        } else
        {
            return null;
        }
    }

    /**
     * Get list of all sections.
     *
     * @return list of all sections.
     */
    public List<String> getSections() {
        ArrayList<String> rtnList = new ArrayList<>();

        entries.forEach(section -> rtnList.add(section.key()));

        return rtnList;
    }

    /**
     * Obtain the <i>value</i> of the <i>key</i> in the form of a
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
    public String getString(String section, String key, String defaultvalue) {
        String rtn = getValue(section, key);
        return rtn != null ? rtn : defaultvalue;
    }

    /**
     * Obtain the <i>value</i> of the <i>key</i> from the global section, in the
     * form of a <b>String</b>.
     *
     * @param key          The key whose value we are after.
     * @param defaultvalue The value to return if the key does not exist.
     *
     * @return Either the stored value or the defaultValue if key is not found.
     *
     * @since 1.0
     */
    public String getStringG(String key, String defaultvalue) {
        return getString(null, key, defaultvalue);
    }

    /**
     * Remove a <b>key/value</b> pair, from this <b>section</b>, from the in
     * memory copy of the ini file.
     *
     * @param section The section containing the key/value pair.
     * @param key     The key to remove.
     */
    public void removeKey(String section, String key) {
        int sectionIdx = indexOfSection(section);

        if (sectionIdx > -1)
        {
            ArrayList<MutableIniProperty<Object>> kvlist = entries.get(sectionIdx).value();
            int idx = indexOfKey(kvlist, key);

            if (idx > -1)
            {
                kvlist.remove(idx);
            }
        }
    }

    /**
     * Remove a <b>section</b> from the in memory copy of the ini file.
     *
     * @param section Section to remove.
     */
    public void removeSection(String section) {
        int sectionIdx = indexOfSection(section);

        if (sectionIdx > 0)
        {
            entries.remove(sectionIdx);
        }
    }

    /**
     * Used to set a property with a value of type: <i>boolean</i>.
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
    public Boolean setBoolean(String section, String key, boolean value) {
        String rtn = setString(section, key, Boolean.toString(value));
        return rtn != null ? Boolean.parseBoolean(rtn) : null;
    }

    /**
     * Used to set a property with a value of type: <i>boolean</i>.
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
    public Boolean setBoolean(String section, String key, boolean value, String comment)
            throws InvalidParameterValueException {
        String rtn = setString(section, key, Boolean.toString(value), comment);
        return rtn != null ? Boolean.parseBoolean(rtn) : null;
    }

    /**
     * Used to set a property with a value of type: <i>boolean</i>, in the
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
    public Boolean setBooleanG(String key, boolean value) {
        String rtn = setStringG(key, Boolean.toString(value));
        return rtn != null ? Boolean.parseBoolean(rtn) : null;
    }

    /**
     * Used to set a property with a value of type: <i>boolean</i>, in the
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
    public Boolean setBooleanG(String key, boolean value, String comment)
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
    public String setComment(String section, String key, String comment)
            throws InvalidParameterValueException {

        Objects.requireNonNull(key, NULL_KEY_MSG);

        if (!validateComment(comment))
        {
            throw new InvalidParameterValueException(
                    "section=" + section + "key=" + key + "\ncomment=" + comment
                    + "\nThe comment text is not a valid 'ini' file format comment.");
        }

        int sectionIdx = indexOfSection(section);

        if (sectionIdx
            == -1)
        {
            entries.add(new MutableIniProperty<>(section, new ArrayList<>()));
            sectionIdx = indexOfSection(section);
        }

        ArrayList<MutableIniProperty<Object>> kvlist = entries.get(sectionIdx).value();
        int keyIdx = indexOfKey(kvlist, key);

        if (keyIdx == -1)
        {
            kvlist.add(new MutableIniProperty<>(key, null, comment));
            return null;
        } else
        {
            MutableIniProperty<Object> kv = kvlist.get(keyIdx);
            String rtn = kv.comment();
            kv.comment(comment);
            return rtn;
        }
    }

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
    public String setCommentG(String key, String comment)
            throws InvalidParameterValueException {
        return setComment(null, key, comment);
    }

    /**
     * Used to set a property with a value of type: <i>double</i>.
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
    public Double setDouble(String section, String key, double value)
            throws NumberFormatException {
        String rtn = setString(section, key, Double.toString(value));
        return rtn != null ? Double.parseDouble(rtn) : null;
    }

    /**
     * Used to set a property with a value of type: <i>double</i>.
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
    public Double setDouble(String section, String key, double value, String comment)
            throws NumberFormatException, InvalidParameterValueException {
        String rtn = setString(section, key, Double.toString(value), comment);
        return rtn != null ? Double.parseDouble(rtn) : null;
    }

    /**
     * Used to set a property with a value of type: <i>double</i>, in the global
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
    public Double setDoubleG(String key, double value)
            throws NumberFormatException {
        String rtn = setStringG(key, Double.toString(value));
        return rtn != null ? Double.parseDouble(rtn) : null;
    }

    /**
     * Used to set a property with a value of type: <i>double</i>, in the global
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
    public Double setDoubleG(String key, double value, String comment)
            throws NumberFormatException, InvalidParameterValueException {
        String rtn = setStringG(key, Double.toString(value), comment);
        return rtn != null ? Double.parseDouble(rtn) : null;
    }

    /**
     * Used to set a property with a value of type: <i>float</i>.
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
    public Float setFloat(String section, String key, float value)
            throws NumberFormatException {
        String rtn = setString(section, key, Float.toString(value));
        return rtn != null ? Float.parseFloat(rtn) : null;
    }

    /**
     * Used to set a property with a value of type: <i>float</i>.
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
    public Float setFloat(String section, String key, float value, String comment)
            throws NumberFormatException, InvalidParameterValueException {
        String rtn = setString(section, key, Float.toString(value), comment);
        return rtn != null ? Float.parseFloat(rtn) : null;
    }

    /**
     * Used to set a property with a value of type: <i>float</i>, in the global
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
    public Float setFloatG(String key, float value)
            throws NumberFormatException {
        String rtn = setStringG(key, Float.toString(value));
        return rtn != null ? Float.parseFloat(rtn) : null;
    }

    /**
     * Used to set a property with a value of type: <i>Float</i>, in the global
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
    public Float setFloatG(String key, Float value, String comment)
            throws NumberFormatException, InvalidParameterValueException {
        String rtn = setStringG(key, Float.toString(value), comment);
        return rtn != null ? Float.parseFloat(rtn) : null;
    }

    /**
     * Used to set a property with a value of type: <i>int</i>.
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
    public Integer setInt(String section, String key, int value)
            throws NumberFormatException {
        String rtn = setString(section, key, Integer.toString(value));
        return rtn != null ? Integer.parseInt(rtn) : null;
    }

    /**
     * Used to set a property with a value of type: <i>int</i>.
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
    public Integer setInt(String section, String key, int value, String comment)
            throws NumberFormatException, InvalidParameterValueException {
        String rtn = setString(section, key, Integer.toString(value), comment);
        return rtn != null ? Integer.parseInt(rtn) : null;
    }

    /**
     * Used to set a property with a value of type: <i>int</i>, in the global
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
    public Integer setIntG(String key, int value)
            throws NumberFormatException {
        String rtn = setStringG(key, Integer.toString(value));
        return rtn != null ? Integer.parseInt(rtn) : null;
    }

    /**
     * Used to set a property with a value of type: <i>int</i>, in the global
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
    public Integer setIntG(String key, int value, String comment)
            throws NumberFormatException, InvalidParameterValueException {
        String rtn = setStringG(key, Integer.toString(value), comment);
        return rtn != null ? Integer.parseInt(rtn) : null;
    }

    /**
     * Used to set a property with a value of type: <i>long</i>.
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
    public Long setLong(String section, String key, long value)
            throws NumberFormatException {
        String rtn = setString(section, key, Long.toString(value));
        return rtn != null ? Long.parseLong(rtn) : null;
    }

    /**
     * Used to set a property with a value of type: <i>long</i>.
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
    public Long setLong(String section, String key, long value, String comment)
            throws NumberFormatException, InvalidParameterValueException {
        String rtn = setString(section, key, Long.toString(value), comment);
        return rtn != null ? Long.parseLong(rtn) : null;
    }

    /**
     * Used to set a property with a value of type: <i>long</i>, in the global
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
    public Long setLongG(String key, long value)
            throws NumberFormatException {
        String rtn = setStringG(key, Long.toString(value));
        return rtn != null ? Long.parseLong(rtn) : null;
    }

    /**
     * Used to set a property with a value of type: <i>long</i>, in the global
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
    public Long setLongG(String key, long value, String comment)
            throws NumberFormatException, InvalidParameterValueException {
        String rtn = setStringG(key, Long.toString(value), comment);
        return rtn != null ? Long.parseLong(rtn) : null;
    }

    /**
     * Use to create an empty section with a comment.
     *
     * @param section New section.
     * @param comment New comment.
     *
     * @since 1.0
     */
    public void setSection(String section, String comment) {
        int sectionIdx = indexOfSection(section);

        if (sectionIdx == -1)
        {
            entries.add(new MutableIniProperty<>(section, new ArrayList<>(), comment));
            sectionIdx = indexOfSection(section);
        }

        entries.get(sectionIdx).comment(comment);
    }

    /**
     * Used to set a property with a value of type: <i>String</i>.
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
    public String setString(String section, String key, String value) {
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
     * Used to set a property with a value of type: <i>String</i>.
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
    public String setString(String section, String key, String value, String comment)
            throws InvalidParameterValueException {

        Objects.requireNonNull(key, NULL_KEY_MSG);

        if (!validateComment(comment))
        {
            throw new InvalidParameterValueException(
                    "section=" + section + "key=" + key + "\ncomment=" + comment
                    + "\nThe comment text is not a valid 'ini' file format comment.");
        }

        int sectionIdx = indexOfSection(section);

        if (sectionIdx
            == -1)
        {
            entries.add(new MutableIniProperty<>(section, new ArrayList<>()));
            sectionIdx = indexOfSection(section);
        }

        ArrayList<MutableIniProperty<Object>> kvlist = entries.get(sectionIdx).value();
        int keyIdx = indexOfKey(kvlist, key);

        if (keyIdx
            == -1)
        {
            kvlist.add(new MutableIniProperty<>(key, value, comment));
            return null;
        } else
        {
            MutableIniProperty<Object> kv = kvlist.get(keyIdx);
            String rtn = (String) kv.value();
            kv.value(value);
            kv.comment(comment);
            return rtn;
        }
    }

    /**
     * Used to set a property with a value of type: <i>String</i>.The key is
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
    public String setStringG(String key, String value) {
        return setString(null, key, value);
    }

    /**
     * Used to set a property with a value of type: <i>String</i>.The key is
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
    public String setStringG(String key, String value, String comment)
            throws InvalidParameterValueException {
        return setString(null, key, value, comment);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(IniDocument.class
                .getName()).append("\n");
        sb.append(entries);

        return sb.toString();
    }

    /**
     * Returns the value to which the specified section/key is mapped, or
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
    private String getValue(String section, String key) {
        int sectionIdx = indexOfSection(section);

        if (sectionIdx > -1)
        {
            ArrayList<MutableIniProperty<Object>> kvlist = entries.get(sectionIdx).value();
            int keyIdx = indexOfKey(kvlist, key);

            return keyIdx > -1 ? (String) kvlist.get(keyIdx).value() : null;
        }

        return null;
    }

    /**
     * Refer to {@link #indexOfSection(java.lang.String)
     */
    private int indexOfKey(ArrayList<MutableIniProperty<Object>> kvlist, String key) {
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
    private int indexOfSection(String section) {
        if (section != null)
        {
            for (int i = 1; i < entries.size(); i++)
            {
                if (entries.get(i).key().equals(section))
                {
                    return i;
                }
            }

            return -1;
        }

        return 0;

    }
}
