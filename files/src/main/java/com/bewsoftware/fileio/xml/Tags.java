/*
 *  File Name:    Tags.java
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

/**
 * This is a helper interface, containing various static methods.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 3.1.0
 * @version 3.1.0
 */
public interface Tags
{
    /**
     * Get the tag at the end of the {@code xpath}.
     * <p>
     * If there are multiple tags of the same name and path,
     * then this method will return the first one found. Each time
     * this method is called, it replaces any previous search list with
     * a new one. You need to use the {@link #nextTag(Tag) nextTag(tag)}
     * method to continue processing any additional tags from the
     * current search.
     *
     * @param relativeTag The tag on to which the {@code xpath} is built.
     * @param xpath       to use.
     *
     * @return either the first tag found, or <i>null</i> if none found.
     *
     * @see #nextTag(Tag)
     *
     * @since 3.1.0
     */
    public static Tag getTag(final Tag relativeTag, final String xpath)
    {
        final XPath xPath = XPath.of(relativeTag, xpath);
        Tag tag = relativeTag;

        for (final String pt : xPath.getPathTokens())
        {
            tag = tag.getChild(pt);
        }

        return tag;
    }

    /**
     * Returns <i>true</i> if the search results have not all been returned
     * via {@link #nextTag(Tag) nextTag(tag)}, <i>false</i> otherwise.
     *
     * @param tag returned by {@link #getTag(Tag, String) getTag(relativeTag, xpath)},
     *            or a previous call to this method.
     *
     * @return if <i>true</i> a tag is available for {@link #nextTag(Tag) nextTag(tag)},
     * <i>false</i> otherwise.
     *
     * @see #nextTag(Tag)
     *
     * @since 3.1.0
     */
    public static boolean hasNextTag(final Tag tag)
    {
        return tag.getParent().hasChild();
    }

    /**
     * Returns the next available {@code Tag}, from the search list generated
     * by {@link #getTag(Tag, String) getTag(relativeTag, xpath)}.
     *
     * @param tag returned by {@link #getTag(Tag, String) getTag(relativeTag, xpath)},
     *            or a previous call to this method.
     *
     * @return the next available {@code Tag}, or <i>null</i>.
     *
     * @see #getTag(Tag, String)
     *
     * @since 3.1.0
     */
    public static Tag nextTag(final Tag tag)
    {
        return tag.getParent().nextChild();
    }
}
