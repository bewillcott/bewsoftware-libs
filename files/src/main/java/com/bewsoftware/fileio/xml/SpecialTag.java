/*
 *  File Name:    SpecialTag.java
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
import com.bewsoftware.utils.string.MessageBuilder;
import java.util.function.IntSupplier;

import static com.bewsoftware.fileio.xml.XmlDocumentImpl.COMMENT_NAME;

/**
 * This is a special version of the {@link Tag} class.
 * <p>
 * It is used to store either the first line of an <u>xml</u> file:
 * "{@code <?xml version="1.0" encoding="UTF-8"?>}",
 * or any comment tags: "{@code <!-- This is a comment -->}".
 * <br>
 * <dl class="notes">
 * <dt><b>Parameters:</b></dt>
 * <dd><code>group</code> - The tag being checked.</dd>
 * <dd><code>text</code> - The tag being checked.</dd>
 * </dl>
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 3.1.0
 * @version 3.1.0
 */
public class SpecialTag extends Tag
{
    private static final String COMMENT_CLOSING = " -->";

    private static final String COMMENT_OPENING = "<!-- ";

    private static final String XML_CLOSING = "?>";

    private static final String XML_OPENING = "<?";

    private final String xml;

    private SpecialTag(
            final Tag parent,
            final IntSupplier idSupplier,
            final String name,
            final String xml
    )
    {
        super(parent, idSupplier, name);
        this.xml = xml;
    }

    /**
     * Create named special tag.
     * <p>
     * The new SpecialTag is added to the parent's children.
     *
     * @param parent     of this tag. Can be <i>null</i>.
     * @param idSupplier <i>id</i> number supplier.
     * @param name       of tag.
     * @param xml        the <u>xml</u> text.
     *
     * @return a new SpecialTag instance.
     *
     * @since 3.1.0
     */
    @SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
    public static SpecialTag get(
            final Tag parent,
            final IntSupplier idSupplier,
            final String name,
            final String xml
    )
    {
        final SpecialTag tag = new SpecialTag(parent, idSupplier, name, xml);

        if (parent != null)
        {
            parent.getChildren().add(tag);
        }

        return tag;
    }

    /**
     * Returns the stored <u>xml</u> text.
     *
     * @return the stored <u>xml</u> text.
     *
     * @since 3.1.0
     */
    public String getXml()
    {
        return xml;
    }

    @Override
    public String toString()
    {
        final MessageBuilder mb = new MessageBuilder();

        if (COMMENT_NAME.equals(getName()))
        {
            mb.append(COMMENT_OPENING)
                    .append(getText())
                    .append(COMMENT_CLOSING);

        } else
        {
            mb.append(XML_OPENING)
                    .append(getName());

            getAttributes().forEach((final XmlProperty p) ->
            {
                mb.append(' ')
                        .append(p.key())
                        .append("=\"")
                        .append(p.value())
                        .append('\"');
            });

            mb.append(XML_CLOSING);
        }

        return mb.toString();
    }
}
