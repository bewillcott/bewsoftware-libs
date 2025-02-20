/*
 *  File Name:    XPath.java
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

import com.bewsoftware.annotations.jcip.Immutable;
import com.bewsoftware.utils.string.MessageBuilder;
import java.util.ArrayDeque;
import java.util.Deque;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

/**
 * XPath class description.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 3.1.0
 * @version 3.1.0
 */
@Immutable
public class XPath
{
    private final String absolutePath;

    private final String[] pathTokens;

    private final String relativePath;

    private final Tag relativeTag;

    private XPath(
            final Tag relativeTag,
            final String relativePath,
            final String[] pathTokens,
            final String absolutePath
    )
    {
        this.relativePath = relativePath;
        this.pathTokens = pathTokens;
        this.relativeTag = relativeTag;
        this.absolutePath = absolutePath;
    }

    /**
     * Create an instance of XPath.
     *
     * @param relativeTag The tag that this xpath is relative to.
     * @param xpath       of this XPath.
     *
     * @return new XPath.
     *
     * @since 3.1.0
     */
    public static XPath of(final Tag relativeTag, final String xpath)
    {
        XPath rtn = null;

        requireNonNull(xpath, "relativePath");

        if (relativeTag == null)
        {
            // Generate the rootTag's XPath.
            if ("/".equals(xpath))
            {
                rtn = new XPath(relativeTag, xpath, new String[]
                {
                    ""
                }, "/");
            } else
            {
                throw new NullPointerException("relativeTag");
            }
        } else
        {
            final String[] pathTokens = xpath.split("/");

            // Is absolute XPath?
            if (xpath.startsWith("/"))
            {
                if (!"/".equals(relativeTag.getName()))
                {
                    final String msg = format(
                            "'xpath' is absolute path (%s), yet 'relativeTag' is not root tag (%s).",
                            xpath, relativeTag
                    );

                    throw new IllegalArgumentException(msg);
                } else
                {
                    // Remove first array element -> "".
                    final String[] tokens = new String[pathTokens.length - 1];

                    for (int i = 1; i < pathTokens.length; i++)
                    {
                        tokens[i - 1] = pathTokens[i];
                    }

                    rtn = new XPath(relativeTag, xpath, tokens, xpath);
                }
            } else
            {
                // Generate absolute path.
                final Deque<Tag> stack = new ArrayDeque<>();
                Tag tag = relativeTag;

                while (tag != null)
                {
                    stack.push(tag);
                    tag = tag.getParent();
                }

                stack.pop(); // the root off.
                final MessageBuilder mb = new MessageBuilder();

                while (!stack.isEmpty())
                {
                    tag = stack.pop();
                    mb.append('/').append(tag.getName());
                }

                mb.append('/').append(xpath);

                rtn = new XPath(relativeTag, xpath, pathTokens, mb.toString());
            }
        }

        return rtn;
    }

    /**
     * Returns the absolute path for this xpath.
     *
     * @return the absolute path for this xpath.
     *
     * @since 3.1.0
     */
    public String getAbsolutePath()
    {
        return absolutePath;
    }

    /**
     * Returns the array of path tokens.
     *
     * @return the array of path tokens.
     *
     * @since 3.1.0
     */
    public String[] getPathTokens()
    {
        return pathTokens;
    }

    /**
     * Returns the relative path initially provided.
     *
     * @return the relative path initially provided.
     *
     * @since 3.1.0
     */
    public String getRelativePath()
    {
        return relativePath;
    }

    /**
     * Returns the Tag initially provided.
     *
     * @return the Tag initially provided.
     *
     * @since 3.1.0
     */
    public Tag getRelativeTag()
    {
        return relativeTag;
    }

}
