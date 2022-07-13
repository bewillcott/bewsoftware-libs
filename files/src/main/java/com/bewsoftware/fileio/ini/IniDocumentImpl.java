/*
 * This file is part of the BEW Files Library (aka: BEWFiles).
 *
 * Copyright (C) 2020, 2021 Bradley Willcott
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
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.bewsoftware.fileio.ini;

import com.bewsoftware.common.InvalidParameterValueException;
import com.bewsoftware.property.IniProperty;
import com.bewsoftware.property.MutableIniProperty;
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
 * @version 1.0.12
 */
public class IniDocumentImpl implements IniDocument
{

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

    /**
     * This is the hierarchical storage structure for the properties.
     *
     * @since 1.0
     */
    private final List<MutableIniProperty<List<MutableIniProperty<String>>>> entries;

    // Initialise final fields.
    static
    {
        INI_PATTERN = String.join("|", INI_PATTERNS);
        COMMENT_PATTERN = String.join("|", INI_COMMENT, INI_TAIL);
    }

    /**
     * Can only be instantiated by another class in this package.
     */
    IniDocumentImpl()
    {
        entries = new ArrayList<>();
        entries.add(new MutableIniProperty<>(null, new ArrayList<>()));
    }

    @Override
    public boolean containsKey(final String section, final String key)
    {
        boolean rtn = false;
        int sectionIdx = indexOfSection(section);

        if (sectionIdx > -1)
        {
            List<MutableIniProperty<String>> kvlist = entries.get(sectionIdx).value();
            rtn = (indexOfKey(kvlist, key) > -1);
        }

        return rtn;
    }

    /**
     *
     * @since 1.0
     */
    @Override
    public String getComment(final String section, final String key)
    {
        String rtn = null;
        int sectionIdx = indexOfSection(section);

        if (sectionIdx > -1)
        {
            List<MutableIniProperty<String>> kvlist = entries.get(sectionIdx).value();
            int keyIdx = indexOfKey(kvlist, key);
            rtn = keyIdx > -1 ? kvlist.get(keyIdx).comment() : null;
        }

        return rtn;
    }

    /**
     * Returns {@code section}'s list of properties.
     * <p>
     * <b>Changes:</b>
     * <dl>
     * <dt>v1.0.6</dt>
     * <dd>Return changed from {@link ArrayList ArrayList&lt;&gt;} to
     * {@link List List&lt;&gt;}</dd>
     * </dl>
     * XXX
     *
     * @param section Name of the section.
     *
     * @return List of properties, {@code null} if section doesn't exist.
     *
     * @since 1.0
     */
    @Override
    public List<IniProperty<String>> getSection(final String section)
    {
        List<IniProperty<String>> rtnList = null;
        int sectionIdx = indexOfSection(section);

        if (sectionIdx > -1)
        {
            rtnList = new ArrayList<>();
            entries.get(sectionIdx).value().forEach(rtnList::add);
        }

        return rtnList;
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
    @Override
    public String getSectionComment(final String section)
    {
        int sectionIdx = indexOfSection(section);
        return sectionIdx > -1 ? entries.get(sectionIdx).comment() : null;
    }

    /**
     * Get list of all sections.
     *
     * @return list of all sections.
     */
    @Override
    public List<String> getSections()
    {
        List<String> rtnList = new ArrayList<>();
        entries.forEach(section -> rtnList.add(section.key()));
        return rtnList;
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
    @Override
    public String getValue(final String section, final String key)
    {
        String rtn = null;
        int sectionIdx = indexOfSection(section);

        if (sectionIdx > -1)
        {
            List<MutableIniProperty<String>> kvlist = entries.get(sectionIdx).value();
            int keyIdx = indexOfKey(kvlist, key);
            rtn = keyIdx > -1 ? kvlist.get(keyIdx).value() : null;
        }

        return rtn;
    }

    /**
     * This method replaces the method: {@link ArrayList#indexOf(java.lang.Object)
     * }.
     * <p>
     * The reason being, {@code indexOf} puts the parameter on the left side of
     * the
     * {@code equals()} call. I have developed the {@link MutableIniProperty}
     * class to test
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
    @Override
    public int indexOfSection(final String section)
    {
        int rtn = 0;

        if (section != null)
        {
            rtn = -1;

            for (int i = 1; i < entries.size() && rtn == -1; i++)
            {
                if (entries.get(i).key().equals(section))
                {
                    rtn = i;
                }
            }
        }

        return rtn;
    }

    @Override
    public Iterable<MutableIniProperty<List<MutableIniProperty<String>>>> iterable()
    {
        return entries;
    }

    /**
     * Remove a <b>key/value</b> pair, from this <b>section</b>, from the in
     * memory copy of the ini file.
     *
     * @param section The section containing the key/value pair.
     * @param key     The key to remove.
     */
    @Override
    public void removeKey(final String section, final String key)
    {
        int sectionIdx = indexOfSection(section);

        if (sectionIdx > -1)
        {
            List<MutableIniProperty<String>> kvlist = entries.get(sectionIdx).value();
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
    @Override
    public void removeSection(final String section)
    {
        int sectionIdx = indexOfSection(section);

        if (sectionIdx > 0)
        {
            entries.remove(sectionIdx);
        }
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
    @Override
    public String setComment(final String section, final String key, final String comment)
            throws InvalidParameterValueException
    {
        String rtn = null;
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

        List<MutableIniProperty<String>> kvlist = entries.get(sectionIdx).value();
        int keyIdx = indexOfKey(kvlist, key);

        if (keyIdx == -1)
        {
            kvlist.add(new MutableIniProperty<>(key, null, comment));
        } else
        {
            MutableIniProperty<String> kv = kvlist.get(keyIdx);
            rtn = kv.comment();
            kv.comment(comment);
        }

        return rtn;

    }

    /**
     * Use to create an empty section with a comment.
     *
     * @param section New section.
     * @param comment New comment.
     *
     * @since 1.0
     */
    @Override
    public void setSection(final String section, final String comment)
    {
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
    @Override
    public String setString(final String section, final String key, final String value,
            final String comment) throws InvalidParameterValueException
    {
        String rtn = null;
        Objects.requireNonNull(key, NULL_KEY_MSG);

        if (!validateComment(comment))
        {
            throw new InvalidParameterValueException(
                    "section=" + section + "key=" + key + "\ncomment=" + comment
                    + "\nThe comment text is not a valid 'ini' file format comment.");
        }

        int sectionIdx = indexOfSection(section);

        if (sectionIdx == -1)
        {
            entries.add(new MutableIniProperty<>(section, new ArrayList<>()));
            sectionIdx = indexOfSection(section);
        }

        List<MutableIniProperty<String>> kvlist = entries.get(sectionIdx).value();
        int keyIdx = indexOfKey(kvlist, key);

        if (keyIdx == -1)
        {
            kvlist.add(new MutableIniProperty<>(key, value, comment));
        } else
        {
            MutableIniProperty<String> kv = kvlist.get(keyIdx);
            rtn = kv.value();
            kv.value(value);
            kv.comment(comment);
        }

        return rtn;

    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append(IniDocument.class
                .getName()).append("\n");
        sb.append(entries);

        return sb.toString();
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
    @Override
    public boolean validateComment(final String comment)
    {
        boolean rtn = false;

        if (comment == null)
        {
            rtn = true;
        } else if (!comment.isEmpty())
        {
            Pattern p = Pattern.compile(COMMENT_PATTERN);
            Matcher m = p.matcher(comment);
            m.find();
            rtn = (m.group("Comment") != null);
        }

        return rtn;
    }
}
