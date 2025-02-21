/*
 *  File Name:    XmlDocument.java
 *  Project Name: bewsoftware-files
 *
 *  Copyright (c) 2025 Bradley Willcott
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

package com.bewsoftware.fileio.xml;

import com.bewsoftware.fileio.property.XmlProperty;

/**
 * This interface provides methods to access and modify the in-memory contents
 * of an <u>xml</u> file.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 3.1.0
 * @version 3.1.0
 */
public interface XmlDocument
{
    /**
     * Returns the root tag.
     *
     * @return the root tag.
     *
     * @since 3.1.0
     */
    public Tag getRootTag();

    /**
     * Returns the tag
     *
     * @param context    The context the XPath expression will be evaluated in.
     * @param expression The XPath expression.
     *
     * @return
     *
     * @since 3.1.0
     */
    public Tag getTag(Tag context, String expression);

    /**
     * Create a new XmlProperty.
     *
     * @param key   Property label.
     * @param value Property setting.
     *
     * @return new XmlProperty.
     *
     * @since 3.1.0
     */
    public XmlProperty newProperty(final String key, final String value);

    /**
     * Create named tag.
     * <p>
     * The new Tag is added to the parent's children.
     *
     * @param parent of this tag. Can be <i>null</i>.
     * @param name   of tag.
     * @param xml    the <u>xml</u> text.
     *
     * @return a new Tag instance.
     *
     * @since 3.1.0
     */
    public SpecialTag newSpecialTag(final Tag parent, final String name, final String xml);

    /**
     * Create named tag.
     * <p>
     * The new Tag is added to the parent's children.
     *
     * @param parent of this tag. Can be <i>null</i>.
     * @param name   of tag.
     *
     * @return a new Tag instance.
     *
     * @since 3.1.0
     */
    public Tag newTag(final Tag parent, final String name);

}
